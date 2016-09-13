package com.planning.budget.repository;

import com.planning.budget.domain.ManagerAccount;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ManagerAccount entity.
 */
@SuppressWarnings("unused")
public interface ManagerAccountRepository extends JpaRepository<ManagerAccount,Long> {

}
