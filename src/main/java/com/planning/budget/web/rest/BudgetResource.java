package com.planning.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.planning.budget.domain.Budget;

import com.planning.budget.repository.BudgetRepository;
import com.planning.budget.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Budget.
 */
@RestController
@RequestMapping("/api")
public class BudgetResource {

    private final Logger log = LoggerFactory.getLogger(BudgetResource.class);
        
    @Inject
    private BudgetRepository budgetRepository;

    /**
     * POST  /budgets : Create a new budget.
     *
     * @param budget the budget to create
     * @return the ResponseEntity with status 201 (Created) and with body the new budget, or with status 400 (Bad Request) if the budget has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/budgets",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Budget> createBudget(@Valid @RequestBody Budget budget) throws URISyntaxException {
        log.debug("REST request to save Budget : {}", budget);
        if (budget.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("budget", "idexists", "A new budget cannot already have an ID")).body(null);
        }
        Budget result = budgetRepository.save(budget);
        return ResponseEntity.created(new URI("/api/budgets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("budget", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /budgets : Updates an existing budget.
     *
     * @param budget the budget to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated budget,
     * or with status 400 (Bad Request) if the budget is not valid,
     * or with status 500 (Internal Server Error) if the budget couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/budgets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Budget> updateBudget(@Valid @RequestBody Budget budget) throws URISyntaxException {
        log.debug("REST request to update Budget : {}", budget);
        if (budget.getId() == null) {
            return createBudget(budget);
        }
        Budget result = budgetRepository.save(budget);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("budget", budget.getId().toString()))
            .body(result);
    }

    /**
     * GET  /budgets : get all the budgets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of budgets in body
     */
    @RequestMapping(value = "/budgets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Budget> getAllBudgets() {
        log.debug("REST request to get all Budgets");
        List<Budget> budgets = budgetRepository.findAll();
        return budgets;
    }

    /**
     * GET  /budgets/:id : get the "id" budget.
     *
     * @param id the id of the budget to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the budget, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/budgets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Budget> getBudget(@PathVariable Long id) {
        log.debug("REST request to get Budget : {}", id);
        Budget budget = budgetRepository.findOne(id);
        return Optional.ofNullable(budget)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /budgets/:id : delete the "id" budget.
     *
     * @param id the id of the budget to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/budgets/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        log.debug("REST request to delete Budget : {}", id);
        budgetRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("budget", id.toString())).build();
    }

}
