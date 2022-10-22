package com.rdq.ruiji.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.sql.SQLIntegrityConstraintViolationException;

/*全局异常处理*/
/*拦截指定控制器的异常进行捕获*/
@ControllerAdvice(annotations = {RestController.class, Controller.class})
//返回json数据
@Slf4j
@ResponseBody
public class GlobalExactionHandler {
    /*
    * 处理SQLIntegrityConstraintViolationException这个异常
    * */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> ExactionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        //异常信息如果是Duplicate entry报异常
        if (ex.getMessage().contains("Duplicate entry")){
             String[] split = ex.getMessage().split(" ");
             String msg =  split[2] + "已存在";
             return R.error(msg);

        }

        return R.error("未知错误");
    }



    //全局处理自己定义的异常
    @ExceptionHandler( CustomException.class)
    public R<String> ExactionHandler(CustomException ce){
        return R.error(ce.getMessage());
    }

}
