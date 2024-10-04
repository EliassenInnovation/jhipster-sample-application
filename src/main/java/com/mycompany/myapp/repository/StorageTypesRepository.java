package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.StorageTypes;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StorageTypes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StorageTypesRepository extends JpaRepository<StorageTypes, Long> {}
