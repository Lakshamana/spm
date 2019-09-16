package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.MetricType;
import br.ufpa.labes.spm.repository.MetricTypeRepository;
import br.ufpa.labes.spm.service.MetricTypeService;
import br.ufpa.labes.spm.service.dto.MetricTypeDTO;
import br.ufpa.labes.spm.service.mapper.MetricTypeMapper;
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

/** Integration tests for the {@link MetricTypeResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class MetricTypeResourceIT {

  @Autowired private MetricTypeRepository metricTypeRepository;

  @Autowired private MetricTypeMapper metricTypeMapper;

  @Autowired private MetricTypeService metricTypeService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restMetricTypeMockMvc;

  private MetricType metricType;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final MetricTypeResource metricTypeResource = new MetricTypeResource(metricTypeService);
    this.restMetricTypeMockMvc =
        MockMvcBuilders.standaloneSetup(metricTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator)
            .build();
  }

  /**
   * Create an entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static MetricType createEntity(EntityManager em) {
    MetricType metricType = new MetricType();
    return metricType;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static MetricType createUpdatedEntity(EntityManager em) {
    MetricType metricType = new MetricType();
    return metricType;
  }

  @BeforeEach
  public void initTest() {
    metricType = createEntity(em);
  }

  @Test
  @Transactional
  public void createMetricType() throws Exception {
    int databaseSizeBeforeCreate = metricTypeRepository.findAll().size();

    // Create the MetricType
    MetricTypeDTO metricTypeDTO = metricTypeMapper.toDto(metricType);
    restMetricTypeMockMvc
        .perform(
            post("/api/metric-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metricTypeDTO)))
        .andExpect(status().isCreated());

    // Validate the MetricType in the database
    List<MetricType> metricTypeList = metricTypeRepository.findAll();
    assertThat(metricTypeList).hasSize(databaseSizeBeforeCreate + 1);
    MetricType testMetricType = metricTypeList.get(metricTypeList.size() - 1);
  }

  @Test
  @Transactional
  public void createMetricTypeWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = metricTypeRepository.findAll().size();

    // Create the MetricType with an existing ID
    metricType.setId(1L);
    MetricTypeDTO metricTypeDTO = metricTypeMapper.toDto(metricType);

    // An entity with an existing ID cannot be created, so this API call must fail
    restMetricTypeMockMvc
        .perform(
            post("/api/metric-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metricTypeDTO)))
        .andExpect(status().isBadRequest());

    // Validate the MetricType in the database
    List<MetricType> metricTypeList = metricTypeRepository.findAll();
    assertThat(metricTypeList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllMetricTypes() throws Exception {
    // Initialize the database
    metricTypeRepository.saveAndFlush(metricType);

    // Get all the metricTypeList
    restMetricTypeMockMvc
        .perform(get("/api/metric-types?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(metricType.getId().intValue())));
  }

  @Test
  @Transactional
  public void getMetricType() throws Exception {
    // Initialize the database
    metricTypeRepository.saveAndFlush(metricType);

    // Get the metricType
    restMetricTypeMockMvc
        .perform(get("/api/metric-types/{id}", metricType.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(metricType.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingMetricType() throws Exception {
    // Get the metricType
    restMetricTypeMockMvc
        .perform(get("/api/metric-types/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateMetricType() throws Exception {
    // Initialize the database
    metricTypeRepository.saveAndFlush(metricType);

    int databaseSizeBeforeUpdate = metricTypeRepository.findAll().size();

    // Update the metricType
    MetricType updatedMetricType = metricTypeRepository.findById(metricType.getId()).get();
    // Disconnect from session so that the updates on updatedMetricType are not directly saved in db
    em.detach(updatedMetricType);
    MetricTypeDTO metricTypeDTO = metricTypeMapper.toDto(updatedMetricType);

    restMetricTypeMockMvc
        .perform(
            put("/api/metric-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metricTypeDTO)))
        .andExpect(status().isOk());

    // Validate the MetricType in the database
    List<MetricType> metricTypeList = metricTypeRepository.findAll();
    assertThat(metricTypeList).hasSize(databaseSizeBeforeUpdate);
    MetricType testMetricType = metricTypeList.get(metricTypeList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingMetricType() throws Exception {
    int databaseSizeBeforeUpdate = metricTypeRepository.findAll().size();

    // Create the MetricType
    MetricTypeDTO metricTypeDTO = metricTypeMapper.toDto(metricType);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restMetricTypeMockMvc
        .perform(
            put("/api/metric-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metricTypeDTO)))
        .andExpect(status().isBadRequest());

    // Validate the MetricType in the database
    List<MetricType> metricTypeList = metricTypeRepository.findAll();
    assertThat(metricTypeList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteMetricType() throws Exception {
    // Initialize the database
    metricTypeRepository.saveAndFlush(metricType);

    int databaseSizeBeforeDelete = metricTypeRepository.findAll().size();

    // Delete the metricType
    restMetricTypeMockMvc
        .perform(
            delete("/api/metric-types/{id}", metricType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<MetricType> metricTypeList = metricTypeRepository.findAll();
    assertThat(metricTypeList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(MetricType.class);
    MetricType metricType1 = new MetricType();
    metricType1.setId(1L);
    MetricType metricType2 = new MetricType();
    metricType2.setId(metricType1.getId());
    assertThat(metricType1).isEqualTo(metricType2);
    metricType2.setId(2L);
    assertThat(metricType1).isNotEqualTo(metricType2);
    metricType1.setId(null);
    assertThat(metricType1).isNotEqualTo(metricType2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(MetricTypeDTO.class);
    MetricTypeDTO metricTypeDTO1 = new MetricTypeDTO();
    metricTypeDTO1.setId(1L);
    MetricTypeDTO metricTypeDTO2 = new MetricTypeDTO();
    assertThat(metricTypeDTO1).isNotEqualTo(metricTypeDTO2);
    metricTypeDTO2.setId(metricTypeDTO1.getId());
    assertThat(metricTypeDTO1).isEqualTo(metricTypeDTO2);
    metricTypeDTO2.setId(2L);
    assertThat(metricTypeDTO1).isNotEqualTo(metricTypeDTO2);
    metricTypeDTO1.setId(null);
    assertThat(metricTypeDTO1).isNotEqualTo(metricTypeDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(metricTypeMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(metricTypeMapper.fromId(null)).isNull();
  }
}
