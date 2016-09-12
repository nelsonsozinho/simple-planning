package com.planning.budget.repository;

import com.planning.budget.domain.CostCenter;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CostCenter entity.
 */
@SuppressWarnings("unused")
public interface CostCenterRepository extends JpaRepository<CostCenter,Long> {

}
