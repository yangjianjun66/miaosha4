package cn.wolfcode.shop.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SeckillServer {
    public static void main(String[] args) {
        SpringApplication.run(SeckillServer.class,args);
    }
}
