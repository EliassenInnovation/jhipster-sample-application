package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.H7KeywordMst;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the H7KeywordMst entity.
 */
@SuppressWarnings("unused")
@Repository
public interface H7KeywordMstRepository extends JpaRepository<H7KeywordMst, Long> {}
