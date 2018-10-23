package com.lhh.lahm;

import com.lhh.lahm.entity.Car;
import com.lhh.lahm.rabbitmq.producer.CarSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LahmApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Autowired
    private CarSender carSender;

    @Test
    public void send1() {
            Car car=new Car();

            car.setName("dodo");
            car.setMessageId(System.currentTimeMillis() +"$"+ UUID.randomUUID());
        for (int i = 0; i <20 ; i++) {
            car.setId(i);
            carSender.sendCar(car,true);
        }

        System.out.println("ok");
    }

    @Test
    public void sendZyk() {

        for (int i = 0; i <20 ; i++) {

            carSender.sendZyk("消息id:"+String.valueOf(i));
        }

        System.out.println("ok");
    }
}
