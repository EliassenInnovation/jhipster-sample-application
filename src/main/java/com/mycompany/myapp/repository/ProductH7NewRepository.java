package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductH7New;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductH7New entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductH7NewRepository extends JpaRepository<ProductH7New, Long> {}
