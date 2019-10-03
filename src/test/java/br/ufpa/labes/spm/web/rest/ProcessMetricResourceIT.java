package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ProcessMetric;
import br.ufpa.labes.spm.repository.ProcessMetricRepository;
import br.ufpa.labes.spm.service.ProcessMetricService;
import br.ufpa.labes.spm.service.dto.ProcessMetricDTO;
import br.ufpa.labes.spm.service.mapper.ProcessMetricMapper;
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
 * Integration tests for the {@link ProcessMetricResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ProcessMetricResourceIT {

    @Autowired
    private ProcessMetricRepository processMetricRepository;

    @Autowired
    private ProcessMetricMapper processMetricMapper;

    @Autowired
    private ProcessMetricService processMetricService;

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

    private MockMvc restProcessMetricMockMvc;

    private ProcessMetric processMetric;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProcessMetricResource processMetricResource = new ProcessMetricResource(processMetricService);
        this.restProcessMetricMockMvc = MockMvcBuilders.standaloneSetup(processMetricResource)
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
    public static ProcessMetric createEntity(EntityManager em) {
        ProcessMetric processMetric = new ProcessMetric();
        return processMetric;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessMetric createUpdatedEntity(EntityManager em) {
        ProcessMetric processMetric = new ProcessMetric();
        return processMetric;
    }

    @BeforeEach
    public void initTest() {
        processMetric = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcessMetric() throws Exception {
        int databaseSizeBeforeCreate = processMetricRepository.findAll().size();

        // Create the ProcessMetric
        ProcessMetricDTO processMetricDTO = processMetricMapper.toDto(processMetric);
        restProcessMetricMockMvc.perform(post("/api/process-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processMetricDTO)))
            .andExpect(status().isCreated());

        // Validate the ProcessMetric in the database
        List<ProcessMetric> processMetricList = processMetricRepository.findAll();
        assertThat(processMetricList).hasSize(databaseSizeBeforeCreate + 1);
        ProcessMetric testProcessMetric = processMetricList.get(processMetricList.size() - 1);
    }

    @Test
    @Transactional
    public void createProcessMetricWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = processMetricRepository.findAll().size();

        // Create the ProcessMetric with an existing ID
        processMetric.setId(1L);
        ProcessMetricDTO processMetricDTO = processMetricMapper.toDto(processMetric);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessMetricMockMvc.perform(post("/api/process-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processMetricDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProcessMetric in the database
        List<ProcessMetric> processMetricList = processMetricRepository.findAll();
        assertThat(processMetricList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProcessMetrics() throws Exception {
        // Initialize the database
        processMetricRepository.saveAndFlush(processMetric);

        // Get all the processMetricList
        restProcessMetricMockMvc.perform(get("/api/process-metrics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processMetric.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getProcessMetric() throws Exception {
        // Initialize the database
        processMetricRepository.saveAndFlush(processMetric);

        // Get the processMetric
        restProcessMetricMockMvc.perform(get("/api/process-metrics/{id}", processMetric.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(processMetric.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProcessMetric() throws Exception {
        // Get the processMetric
        restProcessMetricMockMvc.perform(get("/api/process-metrics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcessMetric() throws Exception {
        // Initialize the database
        processMetricRepository.saveAndFlush(processMetric);

        int databaseSizeBeforeUpdate = processMetricRepository.findAll().size();

        // Update the processMetric
        ProcessMetric updatedProcessMetric = processMetricRepository.findById(processMetric.getId()).get();
        // Disconnect from session so that the updates on updatedProcessMetric are not directly saved in db
        em.detach(updatedProcessMetric);
        ProcessMetricDTO processMetricDTO = processMetricMapper.toDto(updatedProcessMetric);

        restProcessMetricMockMvc.perform(put("/api/process-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processMetricDTO)))
            .andExpect(status().isOk());

        // Validate the ProcessMetric in the database
        List<ProcessMetric> processMetricList = processMetricRepository.findAll();
        assertThat(processMetricList).hasSize(databaseSizeBeforeUpdate);
        ProcessMetric testProcessMetric = processMetricList.get(processMetricList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingProcessMetric() throws Exception {
        int databaseSizeBeforeUpdate = processMetricRepository.findAll().size();

        // Create the ProcessMetric
        ProcessMetricDTO processMetricDTO = processMetricMapper.toDto(processMetric);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessMetricMockMvc.perform(put("/api/process-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processMetricDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProcessMetric in the database
        List<ProcessMetric> processMetricList = processMetricRepository.findAll();
        assertThat(processMetricList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProcessMetric() throws Exception {
        // Initialize the database
        processMetricRepository.saveAndFlush(processMetric);

        int databaseSizeBeforeDelete = processMetricRepository.findAll().size();

        // Delete the processMetric
        restProcessMetricMockMvc.perform(delete("/api/process-metrics/{id}", processMetric.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProcessMetric> processMetricList = processMetricRepository.findAll();
        assertThat(processMetricList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessMetric.class);
        ProcessMetric processMetric1 = new ProcessMetric();
        processMetric1.setId(1L);
        ProcessMetric processMetric2 = new ProcessMetric();
        processMetric2.setId(processMetric1.getId());
        assertThat(processMetric1).isEqualTo(processMetric2);
        processMetric2.setId(2L);
        assertThat(processMetric1).isNotEqualTo(processMetric2);
        processMetric1.setId(null);
        assertThat(processMetric1).isNotEqualTo(processMetric2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessMetricDTO.class);
        ProcessMetricDTO processMetricDTO1 = new ProcessMetricDTO();
        processMetricDTO1.setId(1L);
        ProcessMetricDTO processMetricDTO2 = new ProcessMetricDTO();
        assertThat(processMetricDTO1).isNotEqualTo(processMetricDTO2);
        processMetricDTO2.setId(processMetricDTO1.getId());
        assertThat(processMetricDTO1).isEqualTo(processMetricDTO2);
        processMetricDTO2.setId(2L);
        assertThat(processMetricDTO1).isNotEqualTo(processMetricDTO2);
        processMetricDTO1.setId(null);
        assertThat(processMetricDTO1).isNotEqualTo(processMetricDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(processMetricMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(processMetricMapper.fromId(null)).isNull();
    }
}
