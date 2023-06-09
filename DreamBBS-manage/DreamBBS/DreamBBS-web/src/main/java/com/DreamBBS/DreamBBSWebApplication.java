package com.DreamBBS;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"com.DreamBBS"})
@MapperScan(basePackages = {"com.DreamBBS.mappers"})
@EnableTransactionManagement
@EnableScheduling
public class DreamBBSWebApplication {
    public static void main(String[] args){
        SpringApplication.run(DreamBBSWebApplication.class,args);


    }
}
