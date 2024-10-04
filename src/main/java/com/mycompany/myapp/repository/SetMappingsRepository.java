package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SetMappings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SetMappings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SetMappingsRepository extends JpaRepository<SetMappings, Long> {}
