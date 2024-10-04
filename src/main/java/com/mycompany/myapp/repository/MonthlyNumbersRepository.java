package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MonthlyNumbers;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MonthlyNumbers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonthlyNumbersRepository extends JpaRepository<MonthlyNumbers, Long> {}
