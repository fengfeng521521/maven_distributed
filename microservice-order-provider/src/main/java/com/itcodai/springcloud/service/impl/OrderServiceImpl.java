package com.itcodai.springcloud.service.impl;

import com.itcodai.springcloud.entity.TOrder;
import com.itcodai.springcloud.dao.OrderMapper;

import com.itcodai.springcloud.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Override
    public TOrder findById(Long id) {
        return orderMapper.findById(id);
    }

    @Override
    public List<TOrder> findAll() {
        return orderMapper.findAll();
    }
}
