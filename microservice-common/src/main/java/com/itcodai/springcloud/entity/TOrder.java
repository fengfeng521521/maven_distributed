package com.itcodai.springcloud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单实体类
 * @author shengwu ni
 * @date 2018/07/08
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TOrder implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品价格
     */
    private Double price;

    /**
     * 所存的数据库名称
     */
    private String dbSource;


}
