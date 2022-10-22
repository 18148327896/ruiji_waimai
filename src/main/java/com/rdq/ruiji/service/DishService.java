package com.rdq.ruiji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rdq.ruiji.dto.DishDto;
import com.rdq.ruiji.entity.Dish;

public interface DishService extends IService<Dish> {

    //同时插入两张表，菜品信息和口味信息
        public void saveWithFlavors(DishDto dishDto);
        //根据id查询对应的菜品信息以及口味信息
        public DishDto getByIdWithFlavors(Long id);
        //更新菜品信息同时新增口味信息
        public void updateWithFlavors(DishDto dishDto);
}
