package com.example.practical_train_backend.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.practical_train_backend.entity.Experiment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExperimentMapper extends BaseMapper<Experiment>{
    @Select("SELECT * FROM experiment")
    List<Experiment> getAll();
}
