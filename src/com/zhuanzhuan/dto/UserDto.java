package com.zhuanzhuan.dto;

import java.util.List;

/**
 * @Description:
 * @author: xiexing01
 * @date: 2021/11/28 14:30
 */
public class UserDto<S> {

    private String name;

    private int age;

    private Integer integerAge;

    private long phone;

    private S father;

    private List<S> dataList;

    private S[] relations;
}
