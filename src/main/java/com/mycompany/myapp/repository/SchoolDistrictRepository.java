package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SchoolDistrict;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SchoolDistrict entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SchoolDistrictRepository extends JpaRepository<SchoolDistrict, Long> {}
