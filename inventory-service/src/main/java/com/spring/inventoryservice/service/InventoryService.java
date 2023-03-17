package com.spring.inventoryservice.service;

import com.spring.inventoryservice.dto.InvetoryResponse;
import com.spring.inventoryservice.model.Inventory;
import com.spring.inventoryservice.repository.InventoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepo inventoryRepo;
    @Transactional(readOnly = true)
    public List<InvetoryResponse> isInStock(List<String> skuCode){
        return inventoryRepo.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                    InvetoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity() > 0)
                            .build()
                ).toList();
    }
}
