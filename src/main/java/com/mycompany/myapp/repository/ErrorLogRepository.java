package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ErrorLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ErrorLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {}
