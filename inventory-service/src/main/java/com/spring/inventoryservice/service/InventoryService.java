package com.spring.inventoryservice.service;

import com.spring.inventoryservice.dto.InvetoryResponse;
import com.spring.inventoryservice.repository.InventoryRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private final InventoryRepo inventoryRepo;

    @Value("${server.port}")
    private String postNumber;

    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InvetoryResponse> isInStock(List<String> skuCode) {
        log.info("wait started");
        Thread.sleep(10000);
        log.info("wait ended");
        System.out.println(postNumber);
        return inventoryRepo.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                    InvetoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity() > 0)
                            .build()
                ).toList();
    }
}
