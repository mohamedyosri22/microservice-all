package com.spring.orderservice.service;

import com.spring.orderservice.dto.InvetoryResponse;
import com.spring.orderservice.dto.OrderLineItemsDto;
import com.spring.orderservice.dto.OrderRequest;
import com.spring.orderservice.event.OrderPlacedEvent;
import com.spring.orderservice.model.Order;
import com.spring.orderservice.model.OrderLineItems;
import com.spring.orderservice.repository.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepo orderRepo;
    private final WebClient.Builder webClient;
    private final KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtos()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItems(orderLineItems);

       List<String> skuCodes = order.getOrderLineItems()
               .stream()
               .map(OrderLineItems::getSkuCode)
                .toList();

        //  Call Inventory-service and place order is product is in
        //  stock

        InvetoryResponse[] response =  webClient.build().get()
                .uri("http://inventory-service/api/inventory"
                        ,uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes)
                                .build())
                .retrieve()
                .bodyToMono(InvetoryResponse[].class)
                .block();

        assert response != null;
        boolean allProductsIsInStock = Arrays
                .stream(response)
                .allMatch(InvetoryResponse::isInStock);


        if(allProductsIsInStock){
            orderRepo.save(order);
            kafkaTemplate.send("notification-topic",new OrderPlacedEvent(order.getOrderNumber()));//topic,data
            return "Order placed Successfully";
        }
        else{
            throw new IllegalArgumentException("product is not in stock");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();

        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

        return orderLineItems;
    }
}
