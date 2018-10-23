package com.lhh.lahm.dao;

import com.lhh.lahm.entity.Car;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface CarMapper {


    List<Car> findAll(Map map);
    Car findById(int id);
    int saveCar(Car car);
    int updateCar(Car car);
    int deleteById(int id);
}