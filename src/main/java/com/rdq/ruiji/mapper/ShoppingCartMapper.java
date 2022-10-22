package com.rdq.ruiji.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rdq.ruiji.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.ManagedBean;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
