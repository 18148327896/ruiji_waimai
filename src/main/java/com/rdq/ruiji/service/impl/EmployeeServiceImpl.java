package com.rdq.ruiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rdq.ruiji.entity.Employee;
import com.rdq.ruiji.mapper.EmployeeMapper;
import com.rdq.ruiji.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeService{

}
