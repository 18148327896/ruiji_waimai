package com.rdq.ruiji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rdq.ruiji.dto.DishDto;
import com.rdq.ruiji.entity.Dish;
import com.rdq.ruiji.entity.DishFlavor;
import com.rdq.ruiji.mapper.DishMapper;
import com.rdq.ruiji.service.DishFlavorService;
import com.rdq.ruiji.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    /*
    * 新增菜品同时保存口味信息
    * */

    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    @Override
    public void saveWithFlavors(DishDto dishDto) {
        //保存菜品信息
        this.save(dishDto);
        final Long dishID = dishDto.getId();

        //保存菜品口味信息到dishflavor
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishID);
            return item;
        }).collect(Collectors.toList());

    }


    //根据id查询对应的菜品信息以及口味信息

    @Override
    public DishDto getByIdWithFlavors(Long id) {
        //查询菜品基本信息
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        //查询菜品口味信息
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor :: getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }
    //更新菜品信息和口味信息
    @Transactional
    @Override
    public void updateWithFlavors(DishDto dishDto) {
    //更新dish表基本信息2更新口味表基本信息
        this.updateById(dishDto);
        //清理当前表的基本信息，再次添加
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        //添加当前提交过来的基本信息
        dishFlavorService.remove(queryWrapper);
        //添加提交过来的口味信息
         List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);

    }
}