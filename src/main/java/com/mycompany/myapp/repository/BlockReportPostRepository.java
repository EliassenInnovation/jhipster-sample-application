package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.BlockReportPost;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BlockReportPost entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlockReportPostRepository extends JpaRepository<BlockReportPost, Long> {}
