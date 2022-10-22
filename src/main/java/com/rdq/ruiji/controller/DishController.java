package com.rdq.ruiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rdq.ruiji.common.R;
import com.rdq.ruiji.dto.DishDto;
import com.rdq.ruiji.entity.Category;
import com.rdq.ruiji.entity.Dish;
import com.rdq.ruiji.entity.DishFlavor;
import com.rdq.ruiji.service.CategoryService;
import com.rdq.ruiji.service.DishFlavorService;
import com.rdq.ruiji.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/dish")
@RestController
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishFlavorService dishFlavorService;

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize, String name) {
        log.info("页数{}，页大小是，{},名字是{}",page,pageSize,name);
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish>  queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Dish :: getName,name);
        queryWrapper.orderByDesc(Dish::getSort);
        dishService.page(pageInfo,queryWrapper);


        //对象copy
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list =  records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象

            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    //新增菜品
    @PostMapping
    public R<String> saveDish(@RequestBody  DishDto dishDto){
        log.info("菜品信息为{}",dishDto.toString());
        dishService.saveWithFlavors(dishDto);
        return R.success("菜品信息添加成功");
    }

    /*回显数据
    * 根据id查询对应的菜品信息和口味信息
    * */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable("id") Long id){
        DishDto dishDto = dishService.getByIdWithFlavors(id);

        return R.success(dishDto);
    }

    /*
    * 修改菜品信息
    * */
    @PutMapping
    public R<String> updateDish(@RequestBody  DishDto dishDto){
        log.info("菜品信息为{}",dishDto.toString());
        dishService.updateWithFlavors(dishDto);
        return R.success("菜品信息修改成功");
    }

    /*
    * 回显套餐数据
    * */
/*    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){

        //构造查询条件对象
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish :: getCategoryId,dish.getCategoryId());
        queryWrapper.orderByAsc(Dish ::getSort);
        queryWrapper.orderByDesc(Dish ::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }*/
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        //构造查询条件对象
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish :: getCategoryId,dish.getCategoryId());
        queryWrapper.orderByAsc(Dish ::getSort);
        queryWrapper.orderByDesc(Dish ::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dishDtoList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象

            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            Long dishId = item.getId();//当前菜品的id
            //根据id查询口味信息
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());;
        return R.success(dishDtoList);
    }


    //批量停售或起售商品
    @PostMapping("/status/{status}")
    public R<String> statusUpdate(@PathVariable("status") Integer status,@RequestParam("ids") Long[] ids){
        for (int i = 0; i < ids.length; i++) {
           Dish dish = dishService.getById(ids[i]);
            dish.setStatus(status);
            dishService.updateById(dish);

        }
        return R.success("状态修改成功，亲");
    }


    //批量删除菜品信息
    @DeleteMapping
    public R<String> deleteDishes(@RequestParam("ids") Long[] ids){
        for (int i = 0; i < ids.length; i++) {
            Dish dish = dishService.getById(ids[i]);
            dishService.removeById(dish);
        }

        return R.success("菜品删除成功");
    }

}
