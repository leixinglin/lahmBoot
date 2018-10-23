package com.lhh.lahm.controller.async;

import com.lhh.lahm.common.LogBase;
import com.lhh.lahm.controller.BaseController;
import com.lhh.lahm.entity.Car;
import com.lhh.lahm.rabbitmq.producer.CarSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

//异步执行类
@Component
public class CarAsync extends LogBase {

    @Autowired
    private CarSender carSender;

    @Async
    public void sendCar(Car car){
        long curr=System.currentTimeMillis();
        log.info("添加汽车操作。。。");
        car.setMessageId(System.currentTimeMillis()+"$"+ UUID.randomUUID());
        carSender.sendCar(car,true);
        log.info("耗时"+String.valueOf(System.currentTimeMillis()-curr));
        //int num=carService.saveCar(car);
        //log.info("添加ok,放回："+num);
    }
}
