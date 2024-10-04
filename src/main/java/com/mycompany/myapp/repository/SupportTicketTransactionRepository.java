package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SupportTicketTransaction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SupportTicketTransaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupportTicketTransactionRepository extends JpaRepository<SupportTicketTransaction, Long> {}
