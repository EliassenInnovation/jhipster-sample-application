package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.StateInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StateInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StateInfoRepository extends JpaRepository<StateInfo, Long> {}
