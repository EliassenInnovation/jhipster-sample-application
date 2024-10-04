package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductsToUpdate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductsToUpdate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductsToUpdateRepository extends JpaRepository<ProductsToUpdate, Long> {}
