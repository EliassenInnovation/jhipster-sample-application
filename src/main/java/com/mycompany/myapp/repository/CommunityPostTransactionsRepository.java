package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CommunityPostTransactions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CommunityPostTransactions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommunityPostTransactionsRepository extends JpaRepository<CommunityPostTransactions, Long> {}
