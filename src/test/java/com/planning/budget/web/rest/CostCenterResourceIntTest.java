package com.planning.budget.web.rest;

import com.planning.budget.PlanningApp;
import com.planning.budget.domain.CostCenter;
import com.planning.budget.repository.CostCenterRepository;

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
 * Test class for the CostCenterResource REST controller.
 *
 * @see CostCenterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PlanningApp.class)
public class CostCenterResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Boolean DEFAULT_IS_GENERATE_REVENUE = false;
    private static final Boolean UPDATED_IS_GENERATE_REVENUE = true;

    @Inject
    private CostCenterRepository costCenterRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCostCenterMockMvc;

    private CostCenter costCenter;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CostCenterResource costCenterResource = new CostCenterResource();
        ReflectionTestUtils.setField(costCenterResource, "costCenterRepository", costCenterRepository);
        this.restCostCenterMockMvc = MockMvcBuilders.standaloneSetup(costCenterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CostCenter createEntity(EntityManager em) {
        CostCenter costCenter = new CostCenter();
        costCenter = new CostCenter()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .isGenerateRevenue(DEFAULT_IS_GENERATE_REVENUE);
        return costCenter;
    }

    @Before
    public void initTest() {
        costCenter = createEntity(em);
    }

    @Test
    @Transactional
    public void createCostCenter() throws Exception {
        int databaseSizeBeforeCreate = costCenterRepository.findAll().size();

        // Create the CostCenter

        restCostCenterMockMvc.perform(post("/api/cost-centers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(costCenter)))
                .andExpect(status().isCreated());

        // Validate the CostCenter in the database
        List<CostCenter> costCenters = costCenterRepository.findAll();
        assertThat(costCenters).hasSize(databaseSizeBeforeCreate + 1);
        CostCenter testCostCenter = costCenters.get(costCenters.size() - 1);
        assertThat(testCostCenter.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCostCenter.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCostCenter.isIsGenerateRevenue()).isEqualTo(DEFAULT_IS_GENERATE_REVENUE);
    }

    @Test
    @Transactional
    public void checkIsGenerateRevenueIsRequired() throws Exception {
        int databaseSizeBeforeTest = costCenterRepository.findAll().size();
        // set the field null
        costCenter.setIsGenerateRevenue(null);

        // Create the CostCenter, which fails.

        restCostCenterMockMvc.perform(post("/api/cost-centers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(costCenter)))
                .andExpect(status().isBadRequest());

        List<CostCenter> costCenters = costCenterRepository.findAll();
        assertThat(costCenters).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCostCenters() throws Exception {
        // Initialize the database
        costCenterRepository.saveAndFlush(costCenter);

        // Get all the costCenters
        restCostCenterMockMvc.perform(get("/api/cost-centers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(costCenter.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].isGenerateRevenue").value(hasItem(DEFAULT_IS_GENERATE_REVENUE.booleanValue())));
    }

    @Test
    @Transactional
    public void getCostCenter() throws Exception {
        // Initialize the database
        costCenterRepository.saveAndFlush(costCenter);

        // Get the costCenter
        restCostCenterMockMvc.perform(get("/api/cost-centers/{id}", costCenter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(costCenter.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.isGenerateRevenue").value(DEFAULT_IS_GENERATE_REVENUE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCostCenter() throws Exception {
        // Get the costCenter
        restCostCenterMockMvc.perform(get("/api/cost-centers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCostCenter() throws Exception {
        // Initialize the database
        costCenterRepository.saveAndFlush(costCenter);
        int databaseSizeBeforeUpdate = costCenterRepository.findAll().size();

        // Update the costCenter
        CostCenter updatedCostCenter = costCenterRepository.findOne(costCenter.getId());
        updatedCostCenter
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .isGenerateRevenue(UPDATED_IS_GENERATE_REVENUE);

        restCostCenterMockMvc.perform(put("/api/cost-centers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCostCenter)))
                .andExpect(status().isOk());

        // Validate the CostCenter in the database
        List<CostCenter> costCenters = costCenterRepository.findAll();
        assertThat(costCenters).hasSize(databaseSizeBeforeUpdate);
        CostCenter testCostCenter = costCenters.get(costCenters.size() - 1);
        assertThat(testCostCenter.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCostCenter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCostCenter.isIsGenerateRevenue()).isEqualTo(UPDATED_IS_GENERATE_REVENUE);
    }

    @Test
    @Transactional
    public void deleteCostCenter() throws Exception {
        // Initialize the database
        costCenterRepository.saveAndFlush(costCenter);
        int databaseSizeBeforeDelete = costCenterRepository.findAll().size();

        // Get the costCenter
        restCostCenterMockMvc.perform(delete("/api/cost-centers/{id}", costCenter.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CostCenter> costCenters = costCenterRepository.findAll();
        assertThat(costCenters).hasSize(databaseSizeBeforeDelete - 1);
    }
}
