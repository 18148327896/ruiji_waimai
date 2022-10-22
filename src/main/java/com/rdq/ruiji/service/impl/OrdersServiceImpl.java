package com.rdq.ruiji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rdq.ruiji.common.BaseContext;
import com.rdq.ruiji.entity.*;
import com.rdq.ruiji.mapper.OrdersMapper;
import com.rdq.ruiji.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private OrderDetailService orderDetailService;
    /*
    * 用户下单
    * */
    @Transactional
    @Override
    public void submit(Orders orders) {
        //获得当前用户的id
        Long UserId = BaseContext.getCurrentId();
        //查询当前用户的订单信息
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart ::getUserId,UserId);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        if (list==null || list.size() == 0){
            throw new RuntimeException("购物车为空，不能下单");
        }
        //查询用户数据
         User user = userService.getById(UserId);
        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressService.getById(addressBookId);
        if (addressBook == null) {
            throw new RuntimeException("用户地址信息有误，不能下单");
        }

        //订单号
        long orderId = IdWorker.getId();
        AtomicInteger amount = new AtomicInteger(0);


        List<OrderDetail> orderDetails = list.stream().map((item)->{
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        //向订单表插入信息，一条数据
        orders.setNumber(String.valueOf(orderId));
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(UserId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));

        //向订单表插入一条数据
        this.save(orders);
        //向订单明细表插入多条数据
        orderDetailService.saveBatch(orderDetails);

        //清空购物车，
        shoppingCartService.remove(queryWrapper);
    }

    @Override
    public List<OrderDetail> getOrderDetailByOrderId(Long id) {
        LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        orderDetailLambdaQueryWrapper.eq(OrderDetail ::getOrderId,id);
        List<OrderDetail> list = orderDetailService.list(orderDetailLambdaQueryWrapper);
        return list;
    }
}
