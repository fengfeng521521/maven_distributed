package com.itcodai.springcloud;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.itcodai.springcloud.dao")
public class OrderProvider1 {

    public static void main(String[] args) {
        SpringApplication.run(OrderProvider1.class, args);
    }
}
