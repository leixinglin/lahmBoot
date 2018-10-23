package com.lhh.lahm.rabbitmq.consume;

import com.lhh.lahm.entity.Car;
import com.lhh.lahm.service.CarService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CarConsume {

    @Autowired
    private CarService carService;
    /**
     *  @RabbitListener注解还支持自动创建
     *
     */
    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "car-queue",durable = "true"),
            exchange = @Exchange(name = "car-exchanges",durable = "true",type = "topic"),
            key = "order.*"
    ))
    public void onCarMessage(@Payload Car car, @Headers Map<String,Object> headers, Channel channel) throws Exception{

        //消费者操作
        System.out.println("收到消息！！！");
        System.out.println("宠物messageId:"+car.getMessageId());
        carService.saveCar(car);
        Long deliveryTag=(long)headers.get(AmqpHeaders.DELIVERY_TAG);
        //ACK 手工签收
        channel.basicAck(deliveryTag,false);
    }
}
