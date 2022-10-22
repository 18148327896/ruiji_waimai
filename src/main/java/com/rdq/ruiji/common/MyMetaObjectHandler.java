package com.rdq.ruiji.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/*
* 源数据处理器
* */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
    log.info("数据yuan处理器是{}",metaObject.toString());
    log.info("公共字段填充Insert");
    metaObject.setValue("updateTime", LocalDateTime.now());
    metaObject.setValue("createTime", LocalDateTime.now());
    metaObject.setValue("createUser",BaseContext.getCurrentId());
    metaObject.setValue("updateUser",BaseContext.getCurrentId());


    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("数据yuan处理器是{}",metaObject.toString());
        log.info("公共字段填充Insert——Update");
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
