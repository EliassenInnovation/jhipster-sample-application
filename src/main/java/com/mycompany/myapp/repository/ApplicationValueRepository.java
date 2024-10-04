package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ApplicationValue;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ApplicationValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationValueRepository extends JpaRepository<ApplicationValue, Long> {}
