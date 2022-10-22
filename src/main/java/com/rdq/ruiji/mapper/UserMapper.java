package com.rdq.ruiji.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rdq.ruiji.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
