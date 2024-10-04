package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductDistrictAllocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductDistrictAllocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductDistrictAllocationRepository extends JpaRepository<ProductDistrictAllocation, Long> {}
