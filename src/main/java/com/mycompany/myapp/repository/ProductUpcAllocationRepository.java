package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductUpcAllocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductUpcAllocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductUpcAllocationRepository extends JpaRepository<ProductUpcAllocation, Long> {}
