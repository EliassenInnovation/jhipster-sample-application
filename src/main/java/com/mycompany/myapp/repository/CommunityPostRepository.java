package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CommunityPost;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CommunityPost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {}
