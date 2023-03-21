package com.spring.orderservice.controller;

import com.spring.orderservice.dto.OrderRequest;
import com.spring.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.auth.AuthStateCacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="mySettings",fallbackMethod = "fallBack")
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        orderService.placeOrder(orderRequest);
        return "Order placed successfully";
    }


    public String fallBack(OrderRequest orderRequest,RuntimeException e){
        return "Oops !! Something went wrong";
    }
}
