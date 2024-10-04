package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductH7Old;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductH7Old entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductH7OldRepository extends JpaRepository<ProductH7Old, Long> {}
