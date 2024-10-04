package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductImageBeforeApprove;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductImageBeforeApprove entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductImageBeforeApproveRepository extends JpaRepository<ProductImageBeforeApprove, Long> {}
