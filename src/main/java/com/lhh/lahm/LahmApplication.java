package com.lhh.lahm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;





//排除数据源自动配
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
//@ComponentScan(basePackages = {"com.lhh.lahm.cache","com.lhh.lahm.controller","com.lhh.lahm.dao","com.lhh.lahm.service"})
public class LahmApplication {

    public static void main(String[] args) {
        SpringApplication.run(LahmApplication.class, args);
    }
}
