package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductH7;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductH7 entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductH7Repository extends JpaRepository<ProductH7, Long> {}
