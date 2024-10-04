package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AllergenMst;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AllergenMst entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AllergenMstRepository extends JpaRepository<AllergenMst, Long> {}
