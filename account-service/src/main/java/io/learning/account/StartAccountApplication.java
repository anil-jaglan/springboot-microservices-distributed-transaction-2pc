package io.learning.account;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableDiscoveryClient
@SpringBootApplication
@EnableAsync
@EnableRabbit
public class StartAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(StartAccountApplication.class, args);
    }
}
