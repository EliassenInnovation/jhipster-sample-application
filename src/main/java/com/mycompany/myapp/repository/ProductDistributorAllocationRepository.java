package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductDistributorAllocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductDistributorAllocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductDistributorAllocationRepository extends JpaRepository<ProductDistributorAllocation, Long> {}
