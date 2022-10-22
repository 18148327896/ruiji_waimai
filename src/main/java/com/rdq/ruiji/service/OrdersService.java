package com.rdq.ruiji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rdq.ruiji.entity.OrderDetail;
import com.rdq.ruiji.entity.Orders;

import java.util.List;

public interface OrdersService extends IService<Orders> {

    //用户下单
    public void submit(Orders orders);

    //通过订单id茶渣订单的相信详细信息
    public List<OrderDetail> getOrderDetailByOrderId(Long id);
}
