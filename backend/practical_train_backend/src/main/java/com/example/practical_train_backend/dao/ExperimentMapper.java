package com.example.practical_train_backend.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.practical_train_backend.entity.Experiment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExperimentMapper extends BaseMapper<Experiment>{
    @Select("SELECT * FROM experiment")
    List<Experiment> getAll();
    @Insert("INSERT INTO experiment(username, experiment_name, smoothness, periodicity, correlation, metric_num, time_series_data) " +
            "VALUES(#{username}, #{experimentName}, #{smoothness}, #{periodicity}, #{correlation}, #{metricNum}, #{timeSeriesData})")
    int insertExperiment(Experiment experiment);

    @Select("SELECT * FROM experiment WHERE username = #{username} AND experiment_name = #{experimentName}")
    Experiment getByUsernameAndExperimentName(@Param("username") String username, @Param("experimentName") String experimentName);


    @Select("SELECT COUNT(*) > 0 FROM experiment WHERE username = #{username} AND experiment_name = #{experimentName}")
    boolean ifExists(@Param("username") String username, @Param("experimentName") String experimentName);
}
