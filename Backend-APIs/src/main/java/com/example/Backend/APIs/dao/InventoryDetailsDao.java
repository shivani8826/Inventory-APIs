package com.example.Backend.APIs.dao;

import com.example.Backend.APIs.entity.InventoryDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryDetailsDao extends JpaRepository<InventoryDetails, Integer> {

    @Query(nativeQuery = true, value = "select * from inventory_details where code=?1 or supplier=?2")
    public Page<InventoryDetails> findBySupplierIdOrName(String supplierId, String supplierName, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from inventory_details where name=?1")
    public Page<InventoryDetails> findByProductName(String name, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from inventory_details where supplier IN (?1)")
    public Page<InventoryDetails> findBySupplierList(String supplierList, Pageable pageable);


}
