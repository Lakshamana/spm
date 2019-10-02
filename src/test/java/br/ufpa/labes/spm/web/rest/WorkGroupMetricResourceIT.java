package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.WorkGroupMetric;
import br.ufpa.labes.spm.repository.WorkGroupMetricRepository;
import br.ufpa.labes.spm.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static br.ufpa.labes.spm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link WorkGroupMetricResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class WorkGroupMetricResourceIT {

    @Autowired
    private WorkGroupMetricRepository workGroupMetricRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restWorkGroupMetricMockMvc;

    private WorkGroupMetric workGroupMetric;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WorkGroupMetricResource workGroupMetricResource = new WorkGroupMetricResource(workGroupMetricRepository);
        this.restWorkGroupMetricMockMvc = MockMvcBuilders.standaloneSetup(workGroupMetricResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkGroupMetric createEntity(EntityManager em) {
        WorkGroupMetric workGroupMetric = new WorkGroupMetric();
        return workGroupMetric;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkGroupMetric createUpdatedEntity(EntityManager em) {
        WorkGroupMetric workGroupMetric = new WorkGroupMetric();
        return workGroupMetric;
    }

    @BeforeEach
    public void initTest() {
        workGroupMetric = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkGroupMetric() throws Exception {
        int databaseSizeBeforeCreate = workGroupMetricRepository.findAll().size();

        // Create the WorkGroupMetric
        restWorkGroupMetricMockMvc.perform(post("/api/work-group-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workGroupMetric)))
            .andExpect(status().isCreated());

        // Validate the WorkGroupMetric in the database
        List<WorkGroupMetric> workGroupMetricList = workGroupMetricRepository.findAll();
        assertThat(workGroupMetricList).hasSize(databaseSizeBeforeCreate + 1);
        WorkGroupMetric testWorkGroupMetric = workGroupMetricList.get(workGroupMetricList.size() - 1);
    }

    @Test
    @Transactional
    public void createWorkGroupMetricWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workGroupMetricRepository.findAll().size();

        // Create the WorkGroupMetric with an existing ID
        workGroupMetric.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkGroupMetricMockMvc.perform(post("/api/work-group-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workGroupMetric)))
            .andExpect(status().isBadRequest());

        // Validate the WorkGroupMetric in the database
        List<WorkGroupMetric> workGroupMetricList = workGroupMetricRepository.findAll();
        assertThat(workGroupMetricList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllWorkGroupMetrics() throws Exception {
        // Initialize the database
        workGroupMetricRepository.saveAndFlush(workGroupMetric);

        // Get all the workGroupMetricList
        restWorkGroupMetricMockMvc.perform(get("/api/work-group-metrics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workGroupMetric.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getWorkGroupMetric() throws Exception {
        // Initialize the database
        workGroupMetricRepository.saveAndFlush(workGroupMetric);

        // Get the workGroupMetric
        restWorkGroupMetricMockMvc.perform(get("/api/work-group-metrics/{id}", workGroupMetric.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workGroupMetric.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkGroupMetric() throws Exception {
        // Get the workGroupMetric
        restWorkGroupMetricMockMvc.perform(get("/api/work-group-metrics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkGroupMetric() throws Exception {
        // Initialize the database
        workGroupMetricRepository.saveAndFlush(workGroupMetric);

        int databaseSizeBeforeUpdate = workGroupMetricRepository.findAll().size();

        // Update the workGroupMetric
        WorkGroupMetric updatedWorkGroupMetric = workGroupMetricRepository.findById(workGroupMetric.getId()).get();
        // Disconnect from session so that the updates on updatedWorkGroupMetric are not directly saved in db
        em.detach(updatedWorkGroupMetric);

        restWorkGroupMetricMockMvc.perform(put("/api/work-group-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWorkGroupMetric)))
            .andExpect(status().isOk());

        // Validate the WorkGroupMetric in the database
        List<WorkGroupMetric> workGroupMetricList = workGroupMetricRepository.findAll();
        assertThat(workGroupMetricList).hasSize(databaseSizeBeforeUpdate);
        WorkGroupMetric testWorkGroupMetric = workGroupMetricList.get(workGroupMetricList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingWorkGroupMetric() throws Exception {
        int databaseSizeBeforeUpdate = workGroupMetricRepository.findAll().size();

        // Create the WorkGroupMetric

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkGroupMetricMockMvc.perform(put("/api/work-group-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workGroupMetric)))
            .andExpect(status().isBadRequest());

        // Validate the WorkGroupMetric in the database
        List<WorkGroupMetric> workGroupMetricList = workGroupMetricRepository.findAll();
        assertThat(workGroupMetricList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWorkGroupMetric() throws Exception {
        // Initialize the database
        workGroupMetricRepository.saveAndFlush(workGroupMetric);

        int databaseSizeBeforeDelete = workGroupMetricRepository.findAll().size();

        // Delete the workGroupMetric
        restWorkGroupMetricMockMvc.perform(delete("/api/work-group-metrics/{id}", workGroupMetric.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkGroupMetric> workGroupMetricList = workGroupMetricRepository.findAll();
        assertThat(workGroupMetricList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkGroupMetric.class);
        WorkGroupMetric workGroupMetric1 = new WorkGroupMetric();
        workGroupMetric1.setId(1L);
        WorkGroupMetric workGroupMetric2 = new WorkGroupMetric();
        workGroupMetric2.setId(workGroupMetric1.getId());
        assertThat(workGroupMetric1).isEqualTo(workGroupMetric2);
        workGroupMetric2.setId(2L);
        assertThat(workGroupMetric1).isNotEqualTo(workGroupMetric2);
        workGroupMetric1.setId(null);
        assertThat(workGroupMetric1).isNotEqualTo(workGroupMetric2);
    }
}
