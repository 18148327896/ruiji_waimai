package com.rdq.ruiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rdq.ruiji.common.R;
import com.rdq.ruiji.dto.SetmealDto;
import com.rdq.ruiji.entity.Category;
import com.rdq.ruiji.entity.Setmeal;
import com.rdq.ruiji.service.CategoryService;
import com.rdq.ruiji.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetMealController {
    @Autowired
    private SetMealService setMealService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize,String name){
        log.info("页数是：{}，页大小是{},名字是{}",page,pageSize,name);
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> DtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal :: getStatus);
        setMealService.page(pageInfo,queryWrapper);

        //对象copy
        BeanUtils.copyProperties(pageInfo,DtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象

            Category category =  categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);

            }
            return setmealDto;
        }).collect(Collectors.toList());
        DtoPage.setRecords(list);
        return R.success(DtoPage);
    }

    @PostMapping
    public R<String> save(@RequestBody   SetmealDto setmealDto){
        log.info("套餐信息{}",setmealDto);

        //保存数据到对应表
        setMealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /*
    * 删除套餐
    * */

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setMealService.removeWithDish(ids);
        return R.success("套餐数据删除成功，哦亲");
    }

    //批量删除，启用
    @PostMapping("/status/{status}")
   public R<String> statusUpdate(@PathVariable("status") Integer status,@RequestParam("ids") Long[] ids){
        for (int i = 0; i < ids.length; i++){
            Setmeal setmeal = setMealService.getById(ids[i]);
            setmeal.setStatus(status);
            setMealService.updateById(setmeal);
        }
        return R.success("状态修改成功哦亲");
    }

    /*根据条件查询套餐数据*/
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal ::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setMealService.list(queryWrapper);
        return R.success(list);
    }
}
