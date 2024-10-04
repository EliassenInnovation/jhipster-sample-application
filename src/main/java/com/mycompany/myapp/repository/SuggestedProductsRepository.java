package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SuggestedProducts;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SuggestedProducts entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuggestedProductsRepository extends JpaRepository<SuggestedProducts, Long> {}
