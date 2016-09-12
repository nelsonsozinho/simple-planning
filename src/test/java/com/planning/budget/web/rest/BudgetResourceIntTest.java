package com.planning.budget.web.rest;

import com.planning.budget.PlanningApp;
import com.planning.budget.domain.Budget;
import com.planning.budget.repository.BudgetRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BudgetResource REST controller.
 *
 * @see BudgetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlanningApp.class)
public class BudgetResourceIntTest {
    private static final String DEFAULT_YEAR = "AAAAA";
    private static final String UPDATED_YEAR = "BBBBB";

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    @Inject
    private BudgetRepository budgetRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBudgetMockMvc;

    private Budget budget;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BudgetResource budgetResource = new BudgetResource();
        ReflectionTestUtils.setField(budgetResource, "budgetRepository", budgetRepository);
        this.restBudgetMockMvc = MockMvcBuilders.standaloneSetup(budgetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Budget createEntity(EntityManager em) {
        Budget budget = new Budget();
        budget = new Budget()
                .year(DEFAULT_YEAR)
                .version(DEFAULT_VERSION);
        return budget;
    }

    @Before
    public void initTest() {
        budget = createEntity(em);
    }

    @Test
    @Transactional
    public void createBudget() throws Exception {
        int databaseSizeBeforeCreate = budgetRepository.findAll().size();

        // Create the Budget

        restBudgetMockMvc.perform(post("/api/budgets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(budget)))
                .andExpect(status().isCreated());

        // Validate the Budget in the database
        List<Budget> budgets = budgetRepository.findAll();
        assertThat(budgets).hasSize(databaseSizeBeforeCreate + 1);
        Budget testBudget = budgets.get(budgets.size() - 1);
        assertThat(testBudget.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testBudget.getVersion()).isEqualTo(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = budgetRepository.findAll().size();
        // set the field null
        budget.setYear(null);

        // Create the Budget, which fails.

        restBudgetMockMvc.perform(post("/api/budgets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(budget)))
                .andExpect(status().isBadRequest());

        List<Budget> budgets = budgetRepository.findAll();
        assertThat(budgets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = budgetRepository.findAll().size();
        // set the field null
        budget.setVersion(null);

        // Create the Budget, which fails.

        restBudgetMockMvc.perform(post("/api/budgets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(budget)))
                .andExpect(status().isBadRequest());

        List<Budget> budgets = budgetRepository.findAll();
        assertThat(budgets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBudgets() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);

        // Get all the budgets
        restBudgetMockMvc.perform(get("/api/budgets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(budget.getId().intValue())))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.toString())))
                .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)));
    }

    @Test
    @Transactional
    public void getBudget() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);

        // Get the budget
        restBudgetMockMvc.perform(get("/api/budgets/{id}", budget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(budget.getId().intValue()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION));
    }

    @Test
    @Transactional
    public void getNonExistingBudget() throws Exception {
        // Get the budget
        restBudgetMockMvc.perform(get("/api/budgets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBudget() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);
        int databaseSizeBeforeUpdate = budgetRepository.findAll().size();

        // Update the budget
        Budget updatedBudget = budgetRepository.findOne(budget.getId());
        updatedBudget
                .year(UPDATED_YEAR)
                .version(UPDATED_VERSION);

        restBudgetMockMvc.perform(put("/api/budgets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBudget)))
                .andExpect(status().isOk());

        // Validate the Budget in the database
        List<Budget> budgets = budgetRepository.findAll();
        assertThat(budgets).hasSize(databaseSizeBeforeUpdate);
        Budget testBudget = budgets.get(budgets.size() - 1);
        assertThat(testBudget.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testBudget.getVersion()).isEqualTo(UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void deleteBudget() throws Exception {
        // Initialize the database
        budgetRepository.saveAndFlush(budget);
        int databaseSizeBeforeDelete = budgetRepository.findAll().size();

        // Get the budget
        restBudgetMockMvc.perform(delete("/api/budgets/{id}", budget.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Budget> budgets = budgetRepository.findAll();
        assertThat(budgets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
