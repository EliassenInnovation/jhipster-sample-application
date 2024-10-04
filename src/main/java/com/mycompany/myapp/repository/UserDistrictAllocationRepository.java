package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserDistrictAllocation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserDistrictAllocation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserDistrictAllocationRepository extends JpaRepository<UserDistrictAllocation, Long> {}
