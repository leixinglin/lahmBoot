package com.lhh.lahm.service;

import com.lhh.lahm.entity.Car;

import java.util.List;
import java.util.Map;

public interface CarService {
    List<Car> findAll(Map map);
    int saveCar(Car car);
    int updateCar(Car car);
    int deleteById(int id);
    Car findById(int id);
}
