package com.spring.orderservice.controller;

import com.spring.orderservice.dto.OrderRequest;
import com.spring.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.auth.AuthStateCacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="mySettings",fallbackMethod = "fallBack")
    //@TimeLimiter(name="mySettings")
    @Retry(name="mySettings")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){

        return CompletableFuture
                .supplyAsync(()->orderService.placeOrder(orderRequest));
    }


    public CompletableFuture<String> fallBack(OrderRequest orderRequest,RuntimeException e){
        return CompletableFuture.supplyAsync(()->"Oops !! Something went wrong");
    }
}
