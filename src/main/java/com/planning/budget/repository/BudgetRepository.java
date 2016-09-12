package com.planning.budget.repository;

import com.planning.budget.domain.Budget;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Budget entity.
 */
@SuppressWarnings("unused")
public interface BudgetRepository extends JpaRepository<Budget,Long> {

}
