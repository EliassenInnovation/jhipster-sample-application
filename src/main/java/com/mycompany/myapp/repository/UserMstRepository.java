package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserMst;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserMst entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserMstRepository extends JpaRepository<UserMst, Long> {}
