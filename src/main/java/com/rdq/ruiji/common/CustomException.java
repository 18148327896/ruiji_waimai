package com.rdq.ruiji.common;

import org.springframework.stereotype.Component;

/*
* 自定义业务异常
* */
//运行时异常
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
