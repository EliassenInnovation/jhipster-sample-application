package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CommunityLikeMst;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CommunityLikeMst entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommunityLikeMstRepository extends JpaRepository<CommunityLikeMst, Long> {}
