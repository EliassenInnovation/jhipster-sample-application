package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ReplacedProducts;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReplacedProducts entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReplacedProductsRepository extends JpaRepository<ReplacedProducts, Long> {}
