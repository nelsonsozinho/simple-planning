package com.planning.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.planning.budget.domain.ManagerAccount;

import com.planning.budget.repository.ManagerAccountRepository;
import com.planning.budget.web.rest.util.HeaderUtil;
import com.planning.budget.web.rest.util.PaginationUtil;
import com.planning.budget.service.dto.ManagerAccountDTO;
import com.planning.budget.service.mapper.ManagerAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing ManagerAccount.
 */
@RestController
@RequestMapping("/api")
public class ManagerAccountResource {

    private final Logger log = LoggerFactory.getLogger(ManagerAccountResource.class);
        
    @Inject
    private ManagerAccountRepository managerAccountRepository;

    @Inject
    private ManagerAccountMapper managerAccountMapper;

    /**
     * POST  /manager-accounts : Create a new managerAccount.
     *
     * @param managerAccountDTO the managerAccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new managerAccountDTO, or with status 400 (Bad Request) if the managerAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/manager-accounts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagerAccountDTO> createManagerAccount(@Valid @RequestBody ManagerAccountDTO managerAccountDTO) throws URISyntaxException {
        log.debug("REST request to save ManagerAccount : {}", managerAccountDTO);
        if (managerAccountDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("managerAccount", "idexists", "A new managerAccount cannot already have an ID")).body(null);
        }
        ManagerAccount managerAccount = managerAccountMapper.managerAccountDTOToManagerAccount(managerAccountDTO);
        managerAccount = managerAccountRepository.save(managerAccount);
        ManagerAccountDTO result = managerAccountMapper.managerAccountToManagerAccountDTO(managerAccount);
        return ResponseEntity.created(new URI("/api/manager-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("managerAccount", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /manager-accounts : Updates an existing managerAccount.
     *
     * @param managerAccountDTO the managerAccountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated managerAccountDTO,
     * or with status 400 (Bad Request) if the managerAccountDTO is not valid,
     * or with status 500 (Internal Server Error) if the managerAccountDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/manager-accounts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagerAccountDTO> updateManagerAccount(@Valid @RequestBody ManagerAccountDTO managerAccountDTO) throws URISyntaxException {
        log.debug("REST request to update ManagerAccount : {}", managerAccountDTO);
        if (managerAccountDTO.getId() == null) {
            return createManagerAccount(managerAccountDTO);
        }
        ManagerAccount managerAccount = managerAccountMapper.managerAccountDTOToManagerAccount(managerAccountDTO);
        managerAccount = managerAccountRepository.save(managerAccount);
        ManagerAccountDTO result = managerAccountMapper.managerAccountToManagerAccountDTO(managerAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("managerAccount", managerAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /manager-accounts : get all the managerAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of managerAccounts in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/manager-accounts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ManagerAccountDTO>> getAllManagerAccounts(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ManagerAccounts");
        Page<ManagerAccount> page = managerAccountRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/manager-accounts");
        return new ResponseEntity<>(managerAccountMapper.managerAccountsToManagerAccountDTOs(page.getContent()), headers, HttpStatus.OK);
    }

    /**
     * GET  /manager-accounts/:id : get the "id" managerAccount.
     *
     * @param id the id of the managerAccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the managerAccountDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/manager-accounts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ManagerAccountDTO> getManagerAccount(@PathVariable Long id) {
        log.debug("REST request to get ManagerAccount : {}", id);
        ManagerAccount managerAccount = managerAccountRepository.findOne(id);
        ManagerAccountDTO managerAccountDTO = managerAccountMapper.managerAccountToManagerAccountDTO(managerAccount);
        return Optional.ofNullable(managerAccountDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /manager-accounts/:id : delete the "id" managerAccount.
     *
     * @param id the id of the managerAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/manager-accounts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteManagerAccount(@PathVariable Long id) {
        log.debug("REST request to delete ManagerAccount : {}", id);
        managerAccountRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("managerAccount", id.toString())).build();
    }

}
