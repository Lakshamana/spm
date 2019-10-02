package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.WorkGroupEstimation;
import br.ufpa.labes.spm.repository.WorkGroupEstimationRepository;
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
 * Integration tests for the {@link WorkGroupEstimationResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class WorkGroupEstimationResourceIT {

    @Autowired
    private WorkGroupEstimationRepository workGroupEstimationRepository;

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

    private MockMvc restWorkGroupEstimationMockMvc;

    private WorkGroupEstimation workGroupEstimation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WorkGroupEstimationResource workGroupEstimationResource = new WorkGroupEstimationResource(workGroupEstimationRepository);
        this.restWorkGroupEstimationMockMvc = MockMvcBuilders.standaloneSetup(workGroupEstimationResource)
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
    public static WorkGroupEstimation createEntity(EntityManager em) {
        WorkGroupEstimation workGroupEstimation = new WorkGroupEstimation();
        return workGroupEstimation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkGroupEstimation createUpdatedEntity(EntityManager em) {
        WorkGroupEstimation workGroupEstimation = new WorkGroupEstimation();
        return workGroupEstimation;
    }

    @BeforeEach
    public void initTest() {
        workGroupEstimation = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkGroupEstimation() throws Exception {
        int databaseSizeBeforeCreate = workGroupEstimationRepository.findAll().size();

        // Create the WorkGroupEstimation
        restWorkGroupEstimationMockMvc.perform(post("/api/work-group-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workGroupEstimation)))
            .andExpect(status().isCreated());

        // Validate the WorkGroupEstimation in the database
        List<WorkGroupEstimation> workGroupEstimationList = workGroupEstimationRepository.findAll();
        assertThat(workGroupEstimationList).hasSize(databaseSizeBeforeCreate + 1);
        WorkGroupEstimation testWorkGroupEstimation = workGroupEstimationList.get(workGroupEstimationList.size() - 1);
    }

    @Test
    @Transactional
    public void createWorkGroupEstimationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workGroupEstimationRepository.findAll().size();

        // Create the WorkGroupEstimation with an existing ID
        workGroupEstimation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkGroupEstimationMockMvc.perform(post("/api/work-group-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workGroupEstimation)))
            .andExpect(status().isBadRequest());

        // Validate the WorkGroupEstimation in the database
        List<WorkGroupEstimation> workGroupEstimationList = workGroupEstimationRepository.findAll();
        assertThat(workGroupEstimationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllWorkGroupEstimations() throws Exception {
        // Initialize the database
        workGroupEstimationRepository.saveAndFlush(workGroupEstimation);

        // Get all the workGroupEstimationList
        restWorkGroupEstimationMockMvc.perform(get("/api/work-group-estimations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workGroupEstimation.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getWorkGroupEstimation() throws Exception {
        // Initialize the database
        workGroupEstimationRepository.saveAndFlush(workGroupEstimation);

        // Get the workGroupEstimation
        restWorkGroupEstimationMockMvc.perform(get("/api/work-group-estimations/{id}", workGroupEstimation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workGroupEstimation.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkGroupEstimation() throws Exception {
        // Get the workGroupEstimation
        restWorkGroupEstimationMockMvc.perform(get("/api/work-group-estimations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkGroupEstimation() throws Exception {
        // Initialize the database
        workGroupEstimationRepository.saveAndFlush(workGroupEstimation);

        int databaseSizeBeforeUpdate = workGroupEstimationRepository.findAll().size();

        // Update the workGroupEstimation
        WorkGroupEstimation updatedWorkGroupEstimation = workGroupEstimationRepository.findById(workGroupEstimation.getId()).get();
        // Disconnect from session so that the updates on updatedWorkGroupEstimation are not directly saved in db
        em.detach(updatedWorkGroupEstimation);

        restWorkGroupEstimationMockMvc.perform(put("/api/work-group-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWorkGroupEstimation)))
            .andExpect(status().isOk());

        // Validate the WorkGroupEstimation in the database
        List<WorkGroupEstimation> workGroupEstimationList = workGroupEstimationRepository.findAll();
        assertThat(workGroupEstimationList).hasSize(databaseSizeBeforeUpdate);
        WorkGroupEstimation testWorkGroupEstimation = workGroupEstimationList.get(workGroupEstimationList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingWorkGroupEstimation() throws Exception {
        int databaseSizeBeforeUpdate = workGroupEstimationRepository.findAll().size();

        // Create the WorkGroupEstimation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkGroupEstimationMockMvc.perform(put("/api/work-group-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workGroupEstimation)))
            .andExpect(status().isBadRequest());

        // Validate the WorkGroupEstimation in the database
        List<WorkGroupEstimation> workGroupEstimationList = workGroupEstimationRepository.findAll();
        assertThat(workGroupEstimationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWorkGroupEstimation() throws Exception {
        // Initialize the database
        workGroupEstimationRepository.saveAndFlush(workGroupEstimation);

        int databaseSizeBeforeDelete = workGroupEstimationRepository.findAll().size();

        // Delete the workGroupEstimation
        restWorkGroupEstimationMockMvc.perform(delete("/api/work-group-estimations/{id}", workGroupEstimation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkGroupEstimation> workGroupEstimationList = workGroupEstimationRepository.findAll();
        assertThat(workGroupEstimationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkGroupEstimation.class);
        WorkGroupEstimation workGroupEstimation1 = new WorkGroupEstimation();
        workGroupEstimation1.setId(1L);
        WorkGroupEstimation workGroupEstimation2 = new WorkGroupEstimation();
        workGroupEstimation2.setId(workGroupEstimation1.getId());
        assertThat(workGroupEstimation1).isEqualTo(workGroupEstimation2);
        workGroupEstimation2.setId(2L);
        assertThat(workGroupEstimation1).isNotEqualTo(workGroupEstimation2);
        workGroupEstimation1.setId(null);
        assertThat(workGroupEstimation1).isNotEqualTo(workGroupEstimation2);
    }
}
