package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.IocCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the IocCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IocCategoryRepository extends JpaRepository<IocCategory, Long> {}
