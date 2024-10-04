package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ProductChangeHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ProductChangeHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductChangeHistoryRepository extends JpaRepository<ProductChangeHistory, Long> {}
