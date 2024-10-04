package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductBeforeApprove;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductBeforeApprove entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductBeforeApproveRepository extends JpaRepository<ProductBeforeApprove, Long> {}
