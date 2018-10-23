package com.lhh.lahm.schedule;

import com.alibaba.fastjson.JSON;
import com.lhh.lahm.common.LogBase;
import com.lhh.lahm.controller.BaseController;
import com.lhh.lahm.dao.CarMessageLogMapper;
import com.lhh.lahm.entity.Car;
import com.lhh.lahm.entity.CarMessageLog;
import com.lhh.lahm.rabbitmq.producer.CarSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import java.util.List;

@Component
public class CarConsumeTask extends LogBase {
    @Autowired
    private CarMessageLogMapper carMessageLogMapper;

    @Autowired
    private CarSender carSender;
    @Scheduled(cron = "0 0/1 * * * ?")
    public void findFailMessage() {
        List<CarMessageLog> carMessageLogList=carMessageLogMapper.findFailMessage();
        log.info("findFailMessage-Size:"+carMessageLogList.size());
        carMessageLogList.forEach(item->{
            Car car= JSON.parseObject(item.getMessage(), Car.class);
            carSender.sendCar(car,false);
        });
    }
}
