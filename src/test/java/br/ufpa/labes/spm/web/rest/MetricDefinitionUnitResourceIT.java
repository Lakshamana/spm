package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.MetricDefinitionUnit;
import br.ufpa.labes.spm.repository.MetricDefinitionUnitRepository;
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
 * Integration tests for the {@link MetricDefinitionUnitResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class MetricDefinitionUnitResourceIT {

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    @Autowired
    private MetricDefinitionUnitRepository metricDefinitionUnitRepository;

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

    private MockMvc restMetricDefinitionUnitMockMvc;

    private MetricDefinitionUnit metricDefinitionUnit;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MetricDefinitionUnitResource metricDefinitionUnitResource = new MetricDefinitionUnitResource(metricDefinitionUnitRepository);
        this.restMetricDefinitionUnitMockMvc = MockMvcBuilders.standaloneSetup(metricDefinitionUnitResource)
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
    public static MetricDefinitionUnit createEntity(EntityManager em) {
        MetricDefinitionUnit metricDefinitionUnit = new MetricDefinitionUnit()
            .unit(DEFAULT_UNIT);
        return metricDefinitionUnit;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetricDefinitionUnit createUpdatedEntity(EntityManager em) {
        MetricDefinitionUnit metricDefinitionUnit = new MetricDefinitionUnit()
            .unit(UPDATED_UNIT);
        return metricDefinitionUnit;
    }

    @BeforeEach
    public void initTest() {
        metricDefinitionUnit = createEntity(em);
    }

    @Test
    @Transactional
    public void createMetricDefinitionUnit() throws Exception {
        int databaseSizeBeforeCreate = metricDefinitionUnitRepository.findAll().size();

        // Create the MetricDefinitionUnit
        restMetricDefinitionUnitMockMvc.perform(post("/api/metric-definition-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metricDefinitionUnit)))
            .andExpect(status().isCreated());

        // Validate the MetricDefinitionUnit in the database
        List<MetricDefinitionUnit> metricDefinitionUnitList = metricDefinitionUnitRepository.findAll();
        assertThat(metricDefinitionUnitList).hasSize(databaseSizeBeforeCreate + 1);
        MetricDefinitionUnit testMetricDefinitionUnit = metricDefinitionUnitList.get(metricDefinitionUnitList.size() - 1);
        assertThat(testMetricDefinitionUnit.getUnit()).isEqualTo(DEFAULT_UNIT);
    }

    @Test
    @Transactional
    public void createMetricDefinitionUnitWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = metricDefinitionUnitRepository.findAll().size();

        // Create the MetricDefinitionUnit with an existing ID
        metricDefinitionUnit.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetricDefinitionUnitMockMvc.perform(post("/api/metric-definition-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metricDefinitionUnit)))
            .andExpect(status().isBadRequest());

        // Validate the MetricDefinitionUnit in the database
        List<MetricDefinitionUnit> metricDefinitionUnitList = metricDefinitionUnitRepository.findAll();
        assertThat(metricDefinitionUnitList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMetricDefinitionUnits() throws Exception {
        // Initialize the database
        metricDefinitionUnitRepository.saveAndFlush(metricDefinitionUnit);

        // Get all the metricDefinitionUnitList
        restMetricDefinitionUnitMockMvc.perform(get("/api/metric-definition-units?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metricDefinitionUnit.getId().intValue())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())));
    }
    
    @Test
    @Transactional
    public void getMetricDefinitionUnit() throws Exception {
        // Initialize the database
        metricDefinitionUnitRepository.saveAndFlush(metricDefinitionUnit);

        // Get the metricDefinitionUnit
        restMetricDefinitionUnitMockMvc.perform(get("/api/metric-definition-units/{id}", metricDefinitionUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(metricDefinitionUnit.getId().intValue()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMetricDefinitionUnit() throws Exception {
        // Get the metricDefinitionUnit
        restMetricDefinitionUnitMockMvc.perform(get("/api/metric-definition-units/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMetricDefinitionUnit() throws Exception {
        // Initialize the database
        metricDefinitionUnitRepository.saveAndFlush(metricDefinitionUnit);

        int databaseSizeBeforeUpdate = metricDefinitionUnitRepository.findAll().size();

        // Update the metricDefinitionUnit
        MetricDefinitionUnit updatedMetricDefinitionUnit = metricDefinitionUnitRepository.findById(metricDefinitionUnit.getId()).get();
        // Disconnect from session so that the updates on updatedMetricDefinitionUnit are not directly saved in db
        em.detach(updatedMetricDefinitionUnit);
        updatedMetricDefinitionUnit
            .unit(UPDATED_UNIT);

        restMetricDefinitionUnitMockMvc.perform(put("/api/metric-definition-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMetricDefinitionUnit)))
            .andExpect(status().isOk());

        // Validate the MetricDefinitionUnit in the database
        List<MetricDefinitionUnit> metricDefinitionUnitList = metricDefinitionUnitRepository.findAll();
        assertThat(metricDefinitionUnitList).hasSize(databaseSizeBeforeUpdate);
        MetricDefinitionUnit testMetricDefinitionUnit = metricDefinitionUnitList.get(metricDefinitionUnitList.size() - 1);
        assertThat(testMetricDefinitionUnit.getUnit()).isEqualTo(UPDATED_UNIT);
    }

    @Test
    @Transactional
    public void updateNonExistingMetricDefinitionUnit() throws Exception {
        int databaseSizeBeforeUpdate = metricDefinitionUnitRepository.findAll().size();

        // Create the MetricDefinitionUnit

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetricDefinitionUnitMockMvc.perform(put("/api/metric-definition-units")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(metricDefinitionUnit)))
            .andExpect(status().isBadRequest());

        // Validate the MetricDefinitionUnit in the database
        List<MetricDefinitionUnit> metricDefinitionUnitList = metricDefinitionUnitRepository.findAll();
        assertThat(metricDefinitionUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMetricDefinitionUnit() throws Exception {
        // Initialize the database
        metricDefinitionUnitRepository.saveAndFlush(metricDefinitionUnit);

        int databaseSizeBeforeDelete = metricDefinitionUnitRepository.findAll().size();

        // Delete the metricDefinitionUnit
        restMetricDefinitionUnitMockMvc.perform(delete("/api/metric-definition-units/{id}", metricDefinitionUnit.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MetricDefinitionUnit> metricDefinitionUnitList = metricDefinitionUnitRepository.findAll();
        assertThat(metricDefinitionUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MetricDefinitionUnit.class);
        MetricDefinitionUnit metricDefinitionUnit1 = new MetricDefinitionUnit();
        metricDefinitionUnit1.setId(1L);
        MetricDefinitionUnit metricDefinitionUnit2 = new MetricDefinitionUnit();
        metricDefinitionUnit2.setId(metricDefinitionUnit1.getId());
        assertThat(metricDefinitionUnit1).isEqualTo(metricDefinitionUnit2);
        metricDefinitionUnit2.setId(2L);
        assertThat(metricDefinitionUnit1).isNotEqualTo(metricDefinitionUnit2);
        metricDefinitionUnit1.setId(null);
        assertThat(metricDefinitionUnit1).isNotEqualTo(metricDefinitionUnit2);
    }
}
