package com.rdq.ruiji.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rdq.ruiji.entity.AddressBook;
import com.rdq.ruiji.mapper.AddressBookMapper;
import com.rdq.ruiji.service.AddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressService {
}
