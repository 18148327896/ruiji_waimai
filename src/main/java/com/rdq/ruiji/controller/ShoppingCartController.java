package com.rdq.ruiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rdq.ruiji.common.BaseContext;
import com.rdq.ruiji.common.R;
import com.rdq.ruiji.entity.ShoppingCart;
import com.rdq.ruiji.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    /*
    * 添加购物车
    * 增加菜品数量
    * */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("需要添加到购物车的信息是{}",shoppingCart);

        //设置用户id指定是那个用户的购物车、
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //判断当前菜品是否在购物车中，如果才存在数据+1如果不存在添加，默认为1
        Long dishid = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart ::getUserId,currentId);
        if (dishid != null) {
            //添加到购物车的是菜品
            wrapper.eq(ShoppingCart :: getDishId,dishid);

        }else {
            //添加到购物车的的是套餐
            wrapper.eq(ShoppingCart ::getSetmealId,shoppingCart.getSetmealId() );
        }
        ShoppingCart cart = shoppingCartService.getOne(wrapper);


        if (cart != null) {
            int num = cart.getNumber();
            cart.setNumber(num+1);
            shoppingCartService.updateById(cart);

        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cart=shoppingCart;
        }
        return R.success(cart);
    }

    /*
    * 减少菜品数量
    * */
    @PostMapping("/sub")
    public R<ShoppingCart> subZone(@RequestBody ShoppingCart shoppingCart){
        log.info("需要添加到购物车的信息是{}",shoppingCart);

        //设置用户id指定是那个用户的购物车、
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //判断当前菜品是否在购物车中，如果才存在数据+1如果不存在添加，默认为1
        Long dishid = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart ::getUserId,currentId);
        if (dishid != null) {
            //添加到购物车的是菜品
            wrapper.eq(ShoppingCart :: getDishId,dishid);

        }else {
            //添加到购物车的的是套餐
            wrapper.eq(ShoppingCart ::getSetmealId,shoppingCart.getSetmealId() );
        }
        ShoppingCart cart = shoppingCartService.getOne(wrapper);
        if (cart != null) {
            if (cart.getNumber()>=2){
                cart.setNumber(cart.getNumber()-1);
                shoppingCartService.updateById(cart);
            }

        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            shoppingCart=cart;
        }
        return R.success(shoppingCart);
    }

    //查看购物车
    @GetMapping("/list")
    public R<List<ShoppingCart>> shoppingCartList(){
    log.info("查看购物车");
    LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ShoppingCart ::getUserId,BaseContext.getCurrentId());
    queryWrapper.orderByAsc(ShoppingCart :: getCreateTime);
    List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
    return R.success(list);
    }
}
