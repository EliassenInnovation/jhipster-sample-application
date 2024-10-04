package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MonthMst;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MonthMst entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonthMstRepository extends JpaRepository<MonthMst, Long> {}
