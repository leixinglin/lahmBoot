package com.lhh.lahm.dao;

import com.lhh.lahm.entity.CarMessageLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface CarMessageLogMapper {


    int insert(CarMessageLog record);

    int update(CarMessageLog record);

    List<CarMessageLog> findFailMessage();
}