package com.lhh.lahm.service.impl;

import com.github.pagehelper.PageHelper;
import com.lhh.lahm.dao.CarMapper;
import com.lhh.lahm.dataSource.DS;
import com.lhh.lahm.entity.Car;
import com.lhh.lahm.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarMapper carMapper;

    @Override
    @Cacheable(value = "findCache", key = "T(String).valueOf(#map.get('pageNum')).concat('-').concat(T(String).valueOf(#map.get('name')))+#map.get('date') ", sync = true)
    //@Cacheable(value = "findCache#${select.cache.timeout:1800}#${select.cache.refresh:600}", key = "#page", sync = true)
    public List<Car> findAll(Map map) {
        PageHelper.startPage(Integer.valueOf(map.get("pageNum").toString()),5);
        System.out.println("为id、key为:" +map.get("pageNum")+"-"+map.get("name")+map.get("date")+ "数据做了缓存");

        return carMapper.findAll(map);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @DS
    @CacheEvict(value="findCache", allEntries=true)
    public int saveCar(Car car) {
        return carMapper.saveCar(car);
    }

    @Override
    @DS
    @CacheEvict(value="findCache", allEntries=true)
    public int updateCar(Car car) {
        return carMapper.updateCar(car);
    }

    @Override
    @DS
    @CacheEvict(value="findCache", allEntries=true)
    public int deleteById(int id) {
        return carMapper.deleteById(id);
    }

    @Override
    public Car findById(int id) {
        return (Car) carMapper.findById(id);
    }
}
