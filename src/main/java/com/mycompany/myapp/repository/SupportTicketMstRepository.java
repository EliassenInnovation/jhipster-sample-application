package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SupportTicketMst;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SupportTicketMst entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupportTicketMstRepository extends JpaRepository<SupportTicketMst, Long> {}
