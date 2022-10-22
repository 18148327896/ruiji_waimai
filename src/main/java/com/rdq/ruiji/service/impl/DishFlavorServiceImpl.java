package com.rdq.ruiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rdq.ruiji.entity.DishFlavor;
import com.rdq.ruiji.mapper.DishFlavorMapper;
import com.rdq.ruiji.mapper.DishMapper;
import com.rdq.ruiji.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
