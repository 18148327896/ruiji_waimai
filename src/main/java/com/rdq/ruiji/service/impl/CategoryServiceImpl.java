package com.rdq.ruiji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rdq.ruiji.common.CustomException;
import com.rdq.ruiji.entity.Category;
import com.rdq.ruiji.entity.Dish;
import com.rdq.ruiji.entity.Setmeal;
import com.rdq.ruiji.mapper.CategoryMapper;
import com.rdq.ruiji.service.CategoryService;
import com.rdq.ruiji.service.DishService;
import com.rdq.ruiji.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetMealService setMealService;
    /*
    * 根据id删除分类，删除之前进行判断是否关联了菜品或者菜单
    * */

    @Override
    public void remove(Long ids) {
        //查询菜品是否关联了菜单或者套餐如果关联则抛出异常
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        queryWrapper.eq(Dish ::getCategoryId,ids);
        //进行查询
        int count = dishService.count(queryWrapper);
        if (count > 0) {
            //已经关联菜品抛出异常
            throw new CustomException("当前分类下关联了部分菜品不能删除");
        }

        LambdaQueryWrapper<Setmeal> MealQueryWrapper = new LambdaQueryWrapper<>();
        MealQueryWrapper.eq(Setmeal ::getCategoryId,ids);
        int count1 = setMealService.count(MealQueryWrapper);
        if (count1 > 0) {
            //已经关联菜单抛出异常
            throw new CustomException("当前分类下关联了部分菜品不能删除");

        }
        //无异常则删除
        super.removeById(ids);

    }
}
