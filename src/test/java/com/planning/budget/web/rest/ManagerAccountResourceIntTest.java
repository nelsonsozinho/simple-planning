package com.planning.budget.web.rest;

import com.planning.budget.PlanningApp;
import com.planning.budget.domain.ManagerAccount;
import com.planning.budget.repository.ManagerAccountRepository;
import com.planning.budget.service.dto.ManagerAccountDTO;
import com.planning.budget.service.mapper.ManagerAccountMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ManagerAccountResource REST controller.
 *
 * @see ManagerAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlanningApp.class)
public class ManagerAccountResourceIntTest {

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final LocalDate DEFAULT_MATURITY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MATURITY = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_IS_PAID = false;
    private static final Boolean UPDATED_IS_PAID = true;
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private ManagerAccountRepository managerAccountRepository;

    @Inject
    private ManagerAccountMapper managerAccountMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restManagerAccountMockMvc;

    private ManagerAccount managerAccount;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ManagerAccountResource managerAccountResource = new ManagerAccountResource();
        ReflectionTestUtils.setField(managerAccountResource, "managerAccountRepository", managerAccountRepository);
        ReflectionTestUtils.setField(managerAccountResource, "managerAccountMapper", managerAccountMapper);
        this.restManagerAccountMockMvc = MockMvcBuilders.standaloneSetup(managerAccountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManagerAccount createEntity(EntityManager em) {
        ManagerAccount managerAccount = new ManagerAccount();
        managerAccount = new ManagerAccount()
                .value(DEFAULT_VALUE)
                .description(DEFAULT_DESCRIPTION)
                .maturity(DEFAULT_MATURITY)
                .isPaid(DEFAULT_IS_PAID)
                .name(DEFAULT_NAME);
        return managerAccount;
    }

    @Before
    public void initTest() {
        managerAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createManagerAccount() throws Exception {
        int databaseSizeBeforeCreate = managerAccountRepository.findAll().size();

        // Create the ManagerAccount
        ManagerAccountDTO managerAccountDTO = managerAccountMapper.managerAccountToManagerAccountDTO(managerAccount);

        restManagerAccountMockMvc.perform(post("/api/manager-accounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(managerAccountDTO)))
                .andExpect(status().isCreated());

        // Validate the ManagerAccount in the database
        List<ManagerAccount> managerAccounts = managerAccountRepository.findAll();
        assertThat(managerAccounts).hasSize(databaseSizeBeforeCreate + 1);
        ManagerAccount testManagerAccount = managerAccounts.get(managerAccounts.size() - 1);
        assertThat(testManagerAccount.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testManagerAccount.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testManagerAccount.getMaturity()).isEqualTo(DEFAULT_MATURITY);
        assertThat(testManagerAccount.isIsPaid()).isEqualTo(DEFAULT_IS_PAID);
        assertThat(testManagerAccount.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = managerAccountRepository.findAll().size();
        // set the field null
        managerAccount.setValue(null);

        // Create the ManagerAccount, which fails.
        ManagerAccountDTO managerAccountDTO = managerAccountMapper.managerAccountToManagerAccountDTO(managerAccount);

        restManagerAccountMockMvc.perform(post("/api/manager-accounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(managerAccountDTO)))
                .andExpect(status().isBadRequest());

        List<ManagerAccount> managerAccounts = managerAccountRepository.findAll();
        assertThat(managerAccounts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = managerAccountRepository.findAll().size();
        // set the field null
        managerAccount.setDescription(null);

        // Create the ManagerAccount, which fails.
        ManagerAccountDTO managerAccountDTO = managerAccountMapper.managerAccountToManagerAccountDTO(managerAccount);

        restManagerAccountMockMvc.perform(post("/api/manager-accounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(managerAccountDTO)))
                .andExpect(status().isBadRequest());

        List<ManagerAccount> managerAccounts = managerAccountRepository.findAll();
        assertThat(managerAccounts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllManagerAccounts() throws Exception {
        // Initialize the database
        managerAccountRepository.saveAndFlush(managerAccount);

        // Get all the managerAccounts
        restManagerAccountMockMvc.perform(get("/api/manager-accounts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(managerAccount.getId().intValue())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].maturity").value(hasItem(DEFAULT_MATURITY.toString())))
                .andExpect(jsonPath("$.[*].isPaid").value(hasItem(DEFAULT_IS_PAID.booleanValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getManagerAccount() throws Exception {
        // Initialize the database
        managerAccountRepository.saveAndFlush(managerAccount);

        // Get the managerAccount
        restManagerAccountMockMvc.perform(get("/api/manager-accounts/{id}", managerAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(managerAccount.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.maturity").value(DEFAULT_MATURITY.toString()))
            .andExpect(jsonPath("$.isPaid").value(DEFAULT_IS_PAID.booleanValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingManagerAccount() throws Exception {
        // Get the managerAccount
        restManagerAccountMockMvc.perform(get("/api/manager-accounts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateManagerAccount() throws Exception {
        // Initialize the database
        managerAccountRepository.saveAndFlush(managerAccount);
        int databaseSizeBeforeUpdate = managerAccountRepository.findAll().size();

        // Update the managerAccount
        ManagerAccount updatedManagerAccount = managerAccountRepository.findOne(managerAccount.getId());
        updatedManagerAccount
                .value(UPDATED_VALUE)
                .description(UPDATED_DESCRIPTION)
                .maturity(UPDATED_MATURITY)
                .isPaid(UPDATED_IS_PAID)
                .name(UPDATED_NAME);
        ManagerAccountDTO managerAccountDTO = managerAccountMapper.managerAccountToManagerAccountDTO(updatedManagerAccount);

        restManagerAccountMockMvc.perform(put("/api/manager-accounts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(managerAccountDTO)))
                .andExpect(status().isOk());

        // Validate the ManagerAccount in the database
        List<ManagerAccount> managerAccounts = managerAccountRepository.findAll();
        assertThat(managerAccounts).hasSize(databaseSizeBeforeUpdate);
        ManagerAccount testManagerAccount = managerAccounts.get(managerAccounts.size() - 1);
        assertThat(testManagerAccount.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testManagerAccount.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testManagerAccount.getMaturity()).isEqualTo(UPDATED_MATURITY);
        assertThat(testManagerAccount.isIsPaid()).isEqualTo(UPDATED_IS_PAID);
        assertThat(testManagerAccount.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteManagerAccount() throws Exception {
        // Initialize the database
        managerAccountRepository.saveAndFlush(managerAccount);
        int databaseSizeBeforeDelete = managerAccountRepository.findAll().size();

        // Get the managerAccount
        restManagerAccountMockMvc.perform(delete("/api/manager-accounts/{id}", managerAccount.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ManagerAccount> managerAccounts = managerAccountRepository.findAll();
        assertThat(managerAccounts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
