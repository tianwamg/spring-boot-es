package com.es.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class UserInfo {

    private String name;
    private Integer age;
    private Float salary;
    private String address;
    private String remark;
    private Date createTime;
    private String birthDate;
}
