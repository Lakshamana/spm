package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ResourceMetric;
import br.ufpa.labes.spm.repository.ResourceMetricRepository;
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

/** Integration tests for the {@link ResourceMetricResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ResourceMetricResourceIT {

  @Autowired private ResourceMetricRepository resourceMetricRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restResourceMetricMockMvc;

  private ResourceMetric resourceMetric;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ResourceMetricResource resourceMetricResource =
        new ResourceMetricResource(resourceMetricRepository);
    this.restResourceMetricMockMvc =
        MockMvcBuilders.standaloneSetup(resourceMetricResource)
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
  public static ResourceMetric createEntity(EntityManager em) {
    ResourceMetric resourceMetric = new ResourceMetric();
    return resourceMetric;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ResourceMetric createUpdatedEntity(EntityManager em) {
    ResourceMetric resourceMetric = new ResourceMetric();
    return resourceMetric;
  }

  @BeforeEach
  public void initTest() {
    resourceMetric = createEntity(em);
  }

  @Test
  @Transactional
  public void createResourceMetric() throws Exception {
    int databaseSizeBeforeCreate = resourceMetricRepository.findAll().size();

    // Create the ResourceMetric
    restResourceMetricMockMvc
        .perform(
            post("/api/resource-metrics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourceMetric)))
        .andExpect(status().isCreated());

    // Validate the ResourceMetric in the database
    List<ResourceMetric> resourceMetricList = resourceMetricRepository.findAll();
    assertThat(resourceMetricList).hasSize(databaseSizeBeforeCreate + 1);
    ResourceMetric testResourceMetric = resourceMetricList.get(resourceMetricList.size() - 1);
  }

  @Test
  @Transactional
  public void createResourceMetricWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = resourceMetricRepository.findAll().size();

    // Create the ResourceMetric with an existing ID
    resourceMetric.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restResourceMetricMockMvc
        .perform(
            post("/api/resource-metrics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourceMetric)))
        .andExpect(status().isBadRequest());

    // Validate the ResourceMetric in the database
    List<ResourceMetric> resourceMetricList = resourceMetricRepository.findAll();
    assertThat(resourceMetricList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllResourceMetrics() throws Exception {
    // Initialize the database
    resourceMetricRepository.saveAndFlush(resourceMetric);

    // Get all the resourceMetricList
    restResourceMetricMockMvc
        .perform(get("/api/resource-metrics?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(resourceMetric.getId().intValue())));
  }

  @Test
  @Transactional
  public void getResourceMetric() throws Exception {
    // Initialize the database
    resourceMetricRepository.saveAndFlush(resourceMetric);

    // Get the resourceMetric
    restResourceMetricMockMvc
        .perform(get("/api/resource-metrics/{id}", resourceMetric.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(resourceMetric.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingResourceMetric() throws Exception {
    // Get the resourceMetric
    restResourceMetricMockMvc
        .perform(get("/api/resource-metrics/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateResourceMetric() throws Exception {
    // Initialize the database
    resourceMetricRepository.saveAndFlush(resourceMetric);

    int databaseSizeBeforeUpdate = resourceMetricRepository.findAll().size();

    // Update the resourceMetric
    ResourceMetric updatedResourceMetric =
        resourceMetricRepository.findById(resourceMetric.getId()).get();
    // Disconnect from session so that the updates on updatedResourceMetric are not directly saved
    // in db
    em.detach(updatedResourceMetric);

    restResourceMetricMockMvc
        .perform(
            put("/api/resource-metrics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedResourceMetric)))
        .andExpect(status().isOk());

    // Validate the ResourceMetric in the database
    List<ResourceMetric> resourceMetricList = resourceMetricRepository.findAll();
    assertThat(resourceMetricList).hasSize(databaseSizeBeforeUpdate);
    ResourceMetric testResourceMetric = resourceMetricList.get(resourceMetricList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingResourceMetric() throws Exception {
    int databaseSizeBeforeUpdate = resourceMetricRepository.findAll().size();

    // Create the ResourceMetric

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restResourceMetricMockMvc
        .perform(
            put("/api/resource-metrics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourceMetric)))
        .andExpect(status().isBadRequest());

    // Validate the ResourceMetric in the database
    List<ResourceMetric> resourceMetricList = resourceMetricRepository.findAll();
    assertThat(resourceMetricList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteResourceMetric() throws Exception {
    // Initialize the database
    resourceMetricRepository.saveAndFlush(resourceMetric);

    int databaseSizeBeforeDelete = resourceMetricRepository.findAll().size();

    // Delete the resourceMetric
    restResourceMetricMockMvc
        .perform(
            delete("/api/resource-metrics/{id}", resourceMetric.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ResourceMetric> resourceMetricList = resourceMetricRepository.findAll();
    assertThat(resourceMetricList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ResourceMetric.class);
    ResourceMetric resourceMetric1 = new ResourceMetric();
    resourceMetric1.setId(1L);
    ResourceMetric resourceMetric2 = new ResourceMetric();
    resourceMetric2.setId(resourceMetric1.getId());
    assertThat(resourceMetric1).isEqualTo(resourceMetric2);
    resourceMetric2.setId(2L);
    assertThat(resourceMetric1).isNotEqualTo(resourceMetric2);
    resourceMetric1.setId(null);
    assertThat(resourceMetric1).isNotEqualTo(resourceMetric2);
  }
}
