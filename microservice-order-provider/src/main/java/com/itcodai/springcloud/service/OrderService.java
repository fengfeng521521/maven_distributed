package com.itcodai.springcloud.service;


import com.itcodai.springcloud.entity.TOrder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface OrderService {

    TOrder findById(Long id);

    List<TOrder> findAll();
}
