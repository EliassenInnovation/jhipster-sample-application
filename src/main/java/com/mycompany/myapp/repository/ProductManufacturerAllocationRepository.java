package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductManufacturerAllocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductManufacturerAllocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductManufacturerAllocationRepository extends JpaRepository<ProductManufacturerAllocation, Long> {}
