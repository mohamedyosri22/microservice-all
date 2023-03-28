package com.spring.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;

@SpringBootApplication
@Slf4j
@EnableDiscoveryClient
public class NotificationServiceApplication {
    public static void main(String[] args){
        SpringApplication.run(NotificationServiceApplication.class,args);
    }

    @KafkaListener(topics="notification-topic")
    public void handelNotification(OrderPlacedEvent orderPlacedEvent){
        //send out an email notification
        log.info("Received Notification for order - {}",orderPlacedEvent.getOrderNumber());
        //String.format("Order number %s",orderPlacedEvent)
    }
}
