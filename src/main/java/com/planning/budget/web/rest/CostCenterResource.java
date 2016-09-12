package com.planning.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.planning.budget.domain.CostCenter;

import com.planning.budget.repository.CostCenterRepository;
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
 * REST controller for managing CostCenter.
 */
@RestController
@RequestMapping("/api")
public class CostCenterResource {

    private final Logger log = LoggerFactory.getLogger(CostCenterResource.class);
        
    @Inject
    private CostCenterRepository costCenterRepository;

    /**
     * POST  /cost-centers : Create a new costCenter.
     *
     * @param costCenter the costCenter to create
     * @return the ResponseEntity with status 201 (Created) and with body the new costCenter, or with status 400 (Bad Request) if the costCenter has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cost-centers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CostCenter> createCostCenter(@Valid @RequestBody CostCenter costCenter) throws URISyntaxException {
        log.debug("REST request to save CostCenter : {}", costCenter);
        if (costCenter.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("costCenter", "idexists", "A new costCenter cannot already have an ID")).body(null);
        }
        CostCenter result = costCenterRepository.save(costCenter);
        return ResponseEntity.created(new URI("/api/cost-centers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("costCenter", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cost-centers : Updates an existing costCenter.
     *
     * @param costCenter the costCenter to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated costCenter,
     * or with status 400 (Bad Request) if the costCenter is not valid,
     * or with status 500 (Internal Server Error) if the costCenter couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/cost-centers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CostCenter> updateCostCenter(@Valid @RequestBody CostCenter costCenter) throws URISyntaxException {
        log.debug("REST request to update CostCenter : {}", costCenter);
        if (costCenter.getId() == null) {
            return createCostCenter(costCenter);
        }
        CostCenter result = costCenterRepository.save(costCenter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("costCenter", costCenter.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cost-centers : get all the costCenters.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of costCenters in body
     */
    @RequestMapping(value = "/cost-centers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<CostCenter> getAllCostCenters() {
        log.debug("REST request to get all CostCenters");
        List<CostCenter> costCenters = costCenterRepository.findAll();
        return costCenters;
    }

    /**
     * GET  /cost-centers/:id : get the "id" costCenter.
     *
     * @param id the id of the costCenter to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the costCenter, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/cost-centers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CostCenter> getCostCenter(@PathVariable Long id) {
        log.debug("REST request to get CostCenter : {}", id);
        CostCenter costCenter = costCenterRepository.findOne(id);
        return Optional.ofNullable(costCenter)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cost-centers/:id : delete the "id" costCenter.
     *
     * @param id the id of the costCenter to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/cost-centers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCostCenter(@PathVariable Long id) {
        log.debug("REST request to delete CostCenter : {}", id);
        costCenterRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("costCenter", id.toString())).build();
    }

}
