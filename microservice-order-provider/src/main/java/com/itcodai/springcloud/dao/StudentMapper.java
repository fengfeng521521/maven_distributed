package com.itcodai.springcloud.dao;

import com.itcodai.springcloud.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentMapper {



    @Select("select * from Student where id=#{id}")
    public Student showStudent();
    /**
     * @param
     * @date 2020-06-1
     * @return 所有
     *
     * */
    @Select("select * from Student")
    public List<Student> showAllStudent();

}
