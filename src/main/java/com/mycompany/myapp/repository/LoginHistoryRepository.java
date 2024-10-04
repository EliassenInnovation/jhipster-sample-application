package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.LoginHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LoginHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {}
