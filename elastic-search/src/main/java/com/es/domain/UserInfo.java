package com.es.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private String name;
    private Integer age;
    private Float salary;
    private String address;
    private String remark;
    private Date createTime;
    private String birthDate;
}
