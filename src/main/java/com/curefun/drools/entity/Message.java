package com.curefun.drools.entity;

import lombok.Data;

/**
 * Message 用于测试 hello-world规则
 * Message Test Drl Hello World
 */
@Data
public class Message {

    public static final Integer HELLO = 0;
    public static final Integer GOODBYE = 1;

    private String message;
    private Integer status;
}
