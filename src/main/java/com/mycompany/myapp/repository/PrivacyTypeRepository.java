package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PrivacyType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PrivacyType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrivacyTypeRepository extends JpaRepository<PrivacyType, Long> {}
