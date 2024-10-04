package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.USDAUpdateMst;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the USDAUpdateMst entity.
 */
@SuppressWarnings("unused")
@Repository
public interface USDAUpdateMstRepository extends JpaRepository<USDAUpdateMst, Long> {}
