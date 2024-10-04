package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductActivityHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductActivityHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductActivityHistoryRepository extends JpaRepository<ProductActivityHistory, Long> {}
