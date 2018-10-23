package com.lhh.lahm.rabbitmq.producer;

import com.alibaba.fastjson.JSONArray;
import com.lhh.lahm.dao.CarMessageLogMapper;
import com.lhh.lahm.entity.Car;
import com.lhh.lahm.entity.CarMessageLog;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class CarSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CarMessageLogMapper carMessageLogMapper;

    final RabbitTemplate.ConfirmCallback confirmCallback=new RabbitTemplate.ConfirmCallback(){
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String s) {
            System.out.println("回调correlationData:"+correlationData.getId());
            String messageId=correlationData.getId();
            if(ack){
                CarMessageLog carMessageLog=new CarMessageLog(messageId,"1",new Date());
                carMessageLogMapper.update(carMessageLog);
            }else {
                System.out.println("异常处理！");
            }
        }
    };

    public void sendCar(Car car,boolean again){
        rabbitTemplate.setConfirmCallback(confirmCallback);

        CorrelationData correlationData=new CorrelationData();
        correlationData.setId(car.getMessageId());

        if(again){
            CarMessageLog carMessageLog=new CarMessageLog(car.getMessageId(), JSONArray.toJSONString(car));
            carMessageLogMapper.insert(carMessageLog);
        }


        rabbitTemplate.convertAndSend("car-exchanges","car.abc",car,correlationData);
    }


    public void sendZyk(String message){
        rabbitTemplate.convertAndSend("test_simple_queue",message);
    }

}
