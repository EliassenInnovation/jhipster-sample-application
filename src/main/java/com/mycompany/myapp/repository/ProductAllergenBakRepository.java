package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductAllergenBak;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductAllergenBak entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductAllergenBakRepository extends JpaRepository<ProductAllergenBak, Long> {}
