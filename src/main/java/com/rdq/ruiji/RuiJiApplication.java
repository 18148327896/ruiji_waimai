package com.rdq.ruiji;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
//使过滤器生效开启组件扫描
@ServletComponentScan
//开启事务支持注解
@EnableTransactionManagement
public class RuiJiApplication {
    public static void main(String[] args) {
        SpringApplication.run(RuiJiApplication.class,args);
        log.info("瑞吉外卖已编译完成");

    }
}
