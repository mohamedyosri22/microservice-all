package com.spring.inventoryservice.repository;

import com.spring.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepo extends JpaRepository<Inventory,Long> {

    public Optional<Inventory> findBySkuCode(String skuCode);

}
