package com.rdq.ruiji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rdq.ruiji.dto.SetmealDto;
import com.rdq.ruiji.entity.Setmeal;

import java.util.List;

public interface SetMealService extends IService<Setmeal> {
    /*
    * 新增套餐同时保存菜品的关联关系
    * */
    public void saveWithDish(SetmealDto setmealDto);

    /*
    * 删除套餐同时将菜品删除
    * */
    public  void removeWithDish(List<Long>ids);
}
