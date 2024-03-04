package com.ecnu.vphbackend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @program: vph-backend
 * @author: astarforbae
 * @create: 2024-03-04 21:27
 **/

@Mapper
public interface TestMapper {
    @Select("SELECT COUNT(*) AS total_students FROM student;")
    public Integer getCount();
}
