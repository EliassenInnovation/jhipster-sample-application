package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductAllergen;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductAllergen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductAllergenRepository extends JpaRepository<ProductAllergen, Long> {}
