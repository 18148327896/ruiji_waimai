package com.rdq.ruiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rdq.ruiji.common.R;
import com.rdq.ruiji.entity.Category;
import com.rdq.ruiji.service.CategoryService;
import com.rdq.ruiji.service.DishService;
import com.rdq.ruiji.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;
    @Autowired
    private SetMealService setMealService;

    /*
    * 新增菜品分类
    * */

    @PostMapping
    public R<String> addCategory(@RequestBody Category category){
        log.info(category.toString());
        categoryService.save(category);
        return R.success("新增分类成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        Page<Category> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        /*根据排序条件进行排序*/
        queryWrapper.orderByAsc(Category :: getSort);
        /*进行查询*/
        categoryService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }
    /*
    * 删除分类
    * */

    @DeleteMapping
    public R<String> deleteCategory(Long ids ){
        log.info("删除的id是{}",ids);

        //判断该菜品是否关联了菜单或者菜品信息
/*
        categoryService.removeById(id);
*/
        categoryService.remove(ids);
        return R.success("删除成功了，亲");
    }

    //根据id修改分类信息
    @PutMapping
    public R<String> updateCategory(@RequestBody Category category){
        log.info("修改的分类信息是{}",category.toString());
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /*
     * 根据条件查询分类数据
     * */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category ::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category ::getSort);
        queryWrapper.orderByDesc(Category ::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }


}
