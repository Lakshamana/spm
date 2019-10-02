package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ProcessEstimation;
import br.ufpa.labes.spm.repository.ProcessEstimationRepository;
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
 * Integration tests for the {@link ProcessEstimationResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ProcessEstimationResourceIT {

    @Autowired
    private ProcessEstimationRepository processEstimationRepository;

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

    private MockMvc restProcessEstimationMockMvc;

    private ProcessEstimation processEstimation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProcessEstimationResource processEstimationResource = new ProcessEstimationResource(processEstimationRepository);
        this.restProcessEstimationMockMvc = MockMvcBuilders.standaloneSetup(processEstimationResource)
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
    public static ProcessEstimation createEntity(EntityManager em) {
        ProcessEstimation processEstimation = new ProcessEstimation();
        return processEstimation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessEstimation createUpdatedEntity(EntityManager em) {
        ProcessEstimation processEstimation = new ProcessEstimation();
        return processEstimation;
    }

    @BeforeEach
    public void initTest() {
        processEstimation = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcessEstimation() throws Exception {
        int databaseSizeBeforeCreate = processEstimationRepository.findAll().size();

        // Create the ProcessEstimation
        restProcessEstimationMockMvc.perform(post("/api/process-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processEstimation)))
            .andExpect(status().isCreated());

        // Validate the ProcessEstimation in the database
        List<ProcessEstimation> processEstimationList = processEstimationRepository.findAll();
        assertThat(processEstimationList).hasSize(databaseSizeBeforeCreate + 1);
        ProcessEstimation testProcessEstimation = processEstimationList.get(processEstimationList.size() - 1);
    }

    @Test
    @Transactional
    public void createProcessEstimationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = processEstimationRepository.findAll().size();

        // Create the ProcessEstimation with an existing ID
        processEstimation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessEstimationMockMvc.perform(post("/api/process-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processEstimation)))
            .andExpect(status().isBadRequest());

        // Validate the ProcessEstimation in the database
        List<ProcessEstimation> processEstimationList = processEstimationRepository.findAll();
        assertThat(processEstimationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProcessEstimations() throws Exception {
        // Initialize the database
        processEstimationRepository.saveAndFlush(processEstimation);

        // Get all the processEstimationList
        restProcessEstimationMockMvc.perform(get("/api/process-estimations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processEstimation.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getProcessEstimation() throws Exception {
        // Initialize the database
        processEstimationRepository.saveAndFlush(processEstimation);

        // Get the processEstimation
        restProcessEstimationMockMvc.perform(get("/api/process-estimations/{id}", processEstimation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(processEstimation.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProcessEstimation() throws Exception {
        // Get the processEstimation
        restProcessEstimationMockMvc.perform(get("/api/process-estimations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcessEstimation() throws Exception {
        // Initialize the database
        processEstimationRepository.saveAndFlush(processEstimation);

        int databaseSizeBeforeUpdate = processEstimationRepository.findAll().size();

        // Update the processEstimation
        ProcessEstimation updatedProcessEstimation = processEstimationRepository.findById(processEstimation.getId()).get();
        // Disconnect from session so that the updates on updatedProcessEstimation are not directly saved in db
        em.detach(updatedProcessEstimation);

        restProcessEstimationMockMvc.perform(put("/api/process-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProcessEstimation)))
            .andExpect(status().isOk());

        // Validate the ProcessEstimation in the database
        List<ProcessEstimation> processEstimationList = processEstimationRepository.findAll();
        assertThat(processEstimationList).hasSize(databaseSizeBeforeUpdate);
        ProcessEstimation testProcessEstimation = processEstimationList.get(processEstimationList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingProcessEstimation() throws Exception {
        int databaseSizeBeforeUpdate = processEstimationRepository.findAll().size();

        // Create the ProcessEstimation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessEstimationMockMvc.perform(put("/api/process-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processEstimation)))
            .andExpect(status().isBadRequest());

        // Validate the ProcessEstimation in the database
        List<ProcessEstimation> processEstimationList = processEstimationRepository.findAll();
        assertThat(processEstimationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProcessEstimation() throws Exception {
        // Initialize the database
        processEstimationRepository.saveAndFlush(processEstimation);

        int databaseSizeBeforeDelete = processEstimationRepository.findAll().size();

        // Delete the processEstimation
        restProcessEstimationMockMvc.perform(delete("/api/process-estimations/{id}", processEstimation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProcessEstimation> processEstimationList = processEstimationRepository.findAll();
        assertThat(processEstimationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessEstimation.class);
        ProcessEstimation processEstimation1 = new ProcessEstimation();
        processEstimation1.setId(1L);
        ProcessEstimation processEstimation2 = new ProcessEstimation();
        processEstimation2.setId(processEstimation1.getId());
        assertThat(processEstimation1).isEqualTo(processEstimation2);
        processEstimation2.setId(2L);
        assertThat(processEstimation1).isNotEqualTo(processEstimation2);
        processEstimation1.setId(null);
        assertThat(processEstimation1).isNotEqualTo(processEstimation2);
    }
}
