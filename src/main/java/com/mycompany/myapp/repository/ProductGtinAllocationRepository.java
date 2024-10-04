package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductGtinAllocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductGtinAllocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductGtinAllocationRepository extends JpaRepository<ProductGtinAllocation, Long> {}
