package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.OneWorldSyncProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OneWorldSyncProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OneWorldSyncProductRepository extends JpaRepository<OneWorldSyncProduct, Long> {}
