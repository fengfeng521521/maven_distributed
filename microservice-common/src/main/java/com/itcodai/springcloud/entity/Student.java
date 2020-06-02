package com.itcodai.springcloud.entity;

import com.sun.xml.internal.stream.StaxErrorReporter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Student {
    private int id;
    private  String name;
    private String tel;


}
