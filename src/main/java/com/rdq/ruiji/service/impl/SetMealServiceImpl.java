package com.rdq.ruiji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rdq.ruiji.common.CustomException;
import com.rdq.ruiji.dto.SetmealDto;
import com.rdq.ruiji.entity.Setmeal;
import com.rdq.ruiji.entity.SetmealDish;
import com.rdq.ruiji.mapper.SetmealMapper;
import com.rdq.ruiji.service.SetMealDishService;
import com.rdq.ruiji.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetMealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetMealService {
    //新增菜品同时保存菜品的关联关系
    @Autowired
    private SetMealDishService setMealDishService;
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存菜品基本信息操作setmeal，执行insert操作
        this.save(setmealDto);
        //保存套餐和菜品的关联信息，操作setmealdish执行insert操作
         List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
         setmealDishes.stream().map((item)->{
             item.setSetmealId(setmealDto.getId());
             return item;
         }).collect(Collectors.toList());
        setMealDishService.saveBatch(setmealDishes);



    }
    /*
     * 删除套餐同时将菜品删除
     * */
    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {

        //查询套餐数据查询是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal ::getId,ids);
        queryWrapper.eq(Setmeal ::getStatus,1);
        int count = this.count(queryWrapper);
        //如果不能删除抛出业务异常信息
        if (count > 0) {
            throw new CustomException("当前套餐包括其他正在出售的套餐，不能删除");
        }
        //如果可以删除先删除套餐中的数据
        this.removeByIds(ids);

        //删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish :: getSetmealId,ids);
        setMealDishService.remove(lambdaQueryWrapper);

    }


}
