package com.itcodai.springcloud.dao;



import com.itcodai.springcloud.entity.TOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface OrderMapper {

    @Select("select * from Torder where id=#{id}")
    TOrder findById(Long id);

    @Select("select * from Torder")
    List<TOrder> findAll();
}
