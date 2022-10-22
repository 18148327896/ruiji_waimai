package com.rdq.ruiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rdq.ruiji.entity.User;
import com.rdq.ruiji.mapper.UserMapper;
import com.rdq.ruiji.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
