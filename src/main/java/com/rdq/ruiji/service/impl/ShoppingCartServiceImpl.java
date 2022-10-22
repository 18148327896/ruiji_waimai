package com.rdq.ruiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rdq.ruiji.entity.ShoppingCart;
import com.rdq.ruiji.mapper.ShoppingCartMapper;
import com.rdq.ruiji.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
