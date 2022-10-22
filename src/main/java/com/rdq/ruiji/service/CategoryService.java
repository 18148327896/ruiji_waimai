package com.rdq.ruiji.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rdq.ruiji.entity.Category;
import org.springframework.stereotype.Service;


public interface CategoryService extends IService<Category> {

    public void remove(Long ids);

}
