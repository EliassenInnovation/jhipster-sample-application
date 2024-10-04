package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CommunityComment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CommunityComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {}
