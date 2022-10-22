package com.rdq.ruiji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rdq.ruiji.common.BaseContext;
import com.rdq.ruiji.common.R;
import com.rdq.ruiji.dto.OrdersDto;
import com.rdq.ruiji.entity.OrderDetail;
import com.rdq.ruiji.entity.Orders;
import com.rdq.ruiji.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    @Autowired
    private OrdersService orderService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单信息为{}",orders);
        orderService.submit(orders);
        return R.success("即将进入支付页面");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String number,String beginTime,String endTime){
        Page<Orders> pageInfo = new Page<Orders>(page, pageSize);
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.like(number!=null,Orders::getNumber,number)
                .gt(StringUtils.isNotEmpty(beginTime),Orders ::getOrderTime,beginTime)
                .lt(StringUtils.isNotEmpty(endTime),Orders ::getOrderTime,endTime);

        orderService.page(pageInfo,ordersLambdaQueryWrapper);

        return R.success(pageInfo);

    }

    @GetMapping("/userPage")
    public R<Page> userPage(int page,int pageSize){
        Long uid = BaseContext.getCurrentId();
        Page<Orders> orders = new Page<>(page,pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();
        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.eq(Orders ::getUserId,uid);
        ordersLambdaQueryWrapper.orderByDesc(Orders ::getOrderTime).orderByAsc(Orders ::getStatus);
        orderService.page(orders,ordersLambdaQueryWrapper);
        LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Orders> records = orders.getRecords();
        List<OrdersDto> ordersDtoList = records.stream().map((item)->{
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item,ordersDto);
            //获取当前订单的id
            Long id =item.getId();
            List<OrderDetail> orderDetailByOrderId = orderService.getOrderDetailByOrderId(id);
            BeanUtils.copyProperties(item,orderDetailByOrderId);
            ordersDto.setOrderDetails(orderDetailByOrderId);
            return ordersDto;



        }).collect(Collectors.toList());


        BeanUtils.copyProperties(orders,ordersDtoPage,"records");
       ordersDtoPage.setRecords(ordersDtoList);

        return R.success(ordersDtoPage);
    }
}
