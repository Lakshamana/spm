package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.MetricDefinition;
import br.ufpa.labes.spm.repository.MetricDefinitionRepository;
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

/** Integration tests for the {@link MetricDefinitionResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class MetricDefinitionResourceIT {

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  private static final String DEFAULT_KIND = "AAAAAAAAAA";
  private static final String UPDATED_KIND = "BBBBBBBBBB";

  private static final Float DEFAULT_RANGE_FROM = 1F;
  private static final Float UPDATED_RANGE_FROM = 2F;
  private static final Float SMALLER_RANGE_FROM = 1F - 1F;

  private static final Float DEFAULT_RANGE_TO = 1F;
  private static final Float UPDATED_RANGE_TO = 2F;
  private static final Float SMALLER_RANGE_TO = 1F - 1F;

  private static final String DEFAULT_HOW_TO_MEASURE = "AAAAAAAAAA";
  private static final String UPDATED_HOW_TO_MEASURE = "BBBBBBBBBB";

  @Autowired private MetricDefinitionRepository metricDefinitionRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restMetricDefinitionMockMvc;

  private MetricDefinition metricDefinition;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final MetricDefinitionResource metricDefinitionResource =
        new MetricDefinitionResource(metricDefinitionRepository);
    this.restMetricDefinitionMockMvc =
        MockMvcBuilders.standaloneSetup(metricDefinitionResource)
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
  public static MetricDefinition createEntity(EntityManager em) {
    MetricDefinition metricDefinition =
        new MetricDefinition()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .kind(DEFAULT_KIND)
            .rangeFrom(DEFAULT_RANGE_FROM)
            .rangeTo(DEFAULT_RANGE_TO)
            .howToMeasure(DEFAULT_HOW_TO_MEASURE);
    return metricDefinition;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static MetricDefinition createUpdatedEntity(EntityManager em) {
    MetricDefinition metricDefinition =
        new MetricDefinition()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .kind(UPDATED_KIND)
            .rangeFrom(UPDATED_RANGE_FROM)
            .rangeTo(UPDATED_RANGE_TO)
            .howToMeasure(UPDATED_HOW_TO_MEASURE);
    return metricDefinition;
  }

  @BeforeEach
  public void initTest() {
    metricDefinition = createEntity(em);
  }

  @Test
  @Transactional
  public void createMetricDefinition() throws Exception {
    int databaseSizeBeforeCreate = metricDefinitionRepository.findAll().size();

    // Create the MetricDefinition
    restMetricDefinitionMockMvc
        .perform(
            post("/api/metric-definitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metricDefinition)))
        .andExpect(status().isCreated());

    // Validate the MetricDefinition in the database
    List<MetricDefinition> metricDefinitionList = metricDefinitionRepository.findAll();
    assertThat(metricDefinitionList).hasSize(databaseSizeBeforeCreate + 1);
    MetricDefinition testMetricDefinition =
        metricDefinitionList.get(metricDefinitionList.size() - 1);
    assertThat(testMetricDefinition.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testMetricDefinition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testMetricDefinition.getKind()).isEqualTo(DEFAULT_KIND);
    assertThat(testMetricDefinition.getRangeFrom()).isEqualTo(DEFAULT_RANGE_FROM);
    assertThat(testMetricDefinition.getRangeTo()).isEqualTo(DEFAULT_RANGE_TO);
    assertThat(testMetricDefinition.getHowToMeasure()).isEqualTo(DEFAULT_HOW_TO_MEASURE);
  }

  @Test
  @Transactional
  public void createMetricDefinitionWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = metricDefinitionRepository.findAll().size();

    // Create the MetricDefinition with an existing ID
    metricDefinition.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restMetricDefinitionMockMvc
        .perform(
            post("/api/metric-definitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metricDefinition)))
        .andExpect(status().isBadRequest());

    // Validate the MetricDefinition in the database
    List<MetricDefinition> metricDefinitionList = metricDefinitionRepository.findAll();
    assertThat(metricDefinitionList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllMetricDefinitions() throws Exception {
    // Initialize the database
    metricDefinitionRepository.saveAndFlush(metricDefinition);

    // Get all the metricDefinitionList
    restMetricDefinitionMockMvc
        .perform(get("/api/metric-definitions?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(metricDefinition.getId().intValue())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
        .andExpect(jsonPath("$.[*].kind").value(hasItem(DEFAULT_KIND.toString())))
        .andExpect(jsonPath("$.[*].rangeFrom").value(hasItem(DEFAULT_RANGE_FROM.doubleValue())))
        .andExpect(jsonPath("$.[*].rangeTo").value(hasItem(DEFAULT_RANGE_TO.doubleValue())))
        .andExpect(
            jsonPath("$.[*].howToMeasure").value(hasItem(DEFAULT_HOW_TO_MEASURE.toString())));
  }

  @Test
  @Transactional
  public void getMetricDefinition() throws Exception {
    // Initialize the database
    metricDefinitionRepository.saveAndFlush(metricDefinition);

    // Get the metricDefinition
    restMetricDefinitionMockMvc
        .perform(get("/api/metric-definitions/{id}", metricDefinition.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(metricDefinition.getId().intValue()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
        .andExpect(jsonPath("$.kind").value(DEFAULT_KIND.toString()))
        .andExpect(jsonPath("$.rangeFrom").value(DEFAULT_RANGE_FROM.doubleValue()))
        .andExpect(jsonPath("$.rangeTo").value(DEFAULT_RANGE_TO.doubleValue()))
        .andExpect(jsonPath("$.howToMeasure").value(DEFAULT_HOW_TO_MEASURE.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingMetricDefinition() throws Exception {
    // Get the metricDefinition
    restMetricDefinitionMockMvc
        .perform(get("/api/metric-definitions/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateMetricDefinition() throws Exception {
    // Initialize the database
    metricDefinitionRepository.saveAndFlush(metricDefinition);

    int databaseSizeBeforeUpdate = metricDefinitionRepository.findAll().size();

    // Update the metricDefinition
    MetricDefinition updatedMetricDefinition =
        metricDefinitionRepository.findById(metricDefinition.getId()).get();
    // Disconnect from session so that the updates on updatedMetricDefinition are not directly saved
    // in db
    em.detach(updatedMetricDefinition);
    updatedMetricDefinition
        .name(UPDATED_NAME)
        .description(UPDATED_DESCRIPTION)
        .kind(UPDATED_KIND)
        .rangeFrom(UPDATED_RANGE_FROM)
        .rangeTo(UPDATED_RANGE_TO)
        .howToMeasure(UPDATED_HOW_TO_MEASURE);

    restMetricDefinitionMockMvc
        .perform(
            put("/api/metric-definitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMetricDefinition)))
        .andExpect(status().isOk());

    // Validate the MetricDefinition in the database
    List<MetricDefinition> metricDefinitionList = metricDefinitionRepository.findAll();
    assertThat(metricDefinitionList).hasSize(databaseSizeBeforeUpdate);
    MetricDefinition testMetricDefinition =
        metricDefinitionList.get(metricDefinitionList.size() - 1);
    assertThat(testMetricDefinition.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testMetricDefinition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    assertThat(testMetricDefinition.getKind()).isEqualTo(UPDATED_KIND);
    assertThat(testMetricDefinition.getRangeFrom()).isEqualTo(UPDATED_RANGE_FROM);
    assertThat(testMetricDefinition.getRangeTo()).isEqualTo(UPDATED_RANGE_TO);
    assertThat(testMetricDefinition.getHowToMeasure()).isEqualTo(UPDATED_HOW_TO_MEASURE);
  }

  @Test
  @Transactional
  public void updateNonExistingMetricDefinition() throws Exception {
    int databaseSizeBeforeUpdate = metricDefinitionRepository.findAll().size();

    // Create the MetricDefinition

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restMetricDefinitionMockMvc
        .perform(
            put("/api/metric-definitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(metricDefinition)))
        .andExpect(status().isBadRequest());

    // Validate the MetricDefinition in the database
    List<MetricDefinition> metricDefinitionList = metricDefinitionRepository.findAll();
    assertThat(metricDefinitionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteMetricDefinition() throws Exception {
    // Initialize the database
    metricDefinitionRepository.saveAndFlush(metricDefinition);

    int databaseSizeBeforeDelete = metricDefinitionRepository.findAll().size();

    // Delete the metricDefinition
    restMetricDefinitionMockMvc
        .perform(
            delete("/api/metric-definitions/{id}", metricDefinition.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<MetricDefinition> metricDefinitionList = metricDefinitionRepository.findAll();
    assertThat(metricDefinitionList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(MetricDefinition.class);
    MetricDefinition metricDefinition1 = new MetricDefinition();
    metricDefinition1.setId(1L);
    MetricDefinition metricDefinition2 = new MetricDefinition();
    metricDefinition2.setId(metricDefinition1.getId());
    assertThat(metricDefinition1).isEqualTo(metricDefinition2);
    metricDefinition2.setId(2L);
    assertThat(metricDefinition1).isNotEqualTo(metricDefinition2);
    metricDefinition1.setId(null);
    assertThat(metricDefinition1).isNotEqualTo(metricDefinition2);
  }
}
