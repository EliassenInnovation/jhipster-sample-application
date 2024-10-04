package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SupportCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SupportCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupportCategoryRepository extends JpaRepository<SupportCategory, Long> {}
