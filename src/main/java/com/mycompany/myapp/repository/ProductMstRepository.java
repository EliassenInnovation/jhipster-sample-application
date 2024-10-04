package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductMst;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductMst entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductMstRepository extends JpaRepository<ProductMst, Long> {}
