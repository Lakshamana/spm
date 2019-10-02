package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ResourceEstimation;
import br.ufpa.labes.spm.repository.ResourceEstimationRepository;
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

/** Integration tests for the {@link ResourceEstimationResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ResourceEstimationResourceIT {

  @Autowired private ResourceEstimationRepository resourceEstimationRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restResourceEstimationMockMvc;

  private ResourceEstimation resourceEstimation;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ResourceEstimationResource resourceEstimationResource =
        new ResourceEstimationResource(resourceEstimationRepository);
    this.restResourceEstimationMockMvc =
        MockMvcBuilders.standaloneSetup(resourceEstimationResource)
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
  public static ResourceEstimation createEntity(EntityManager em) {
    ResourceEstimation resourceEstimation = new ResourceEstimation();
    return resourceEstimation;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ResourceEstimation createUpdatedEntity(EntityManager em) {
    ResourceEstimation resourceEstimation = new ResourceEstimation();
    return resourceEstimation;
  }

  @BeforeEach
  public void initTest() {
    resourceEstimation = createEntity(em);
  }

  @Test
  @Transactional
  public void createResourceEstimation() throws Exception {
    int databaseSizeBeforeCreate = resourceEstimationRepository.findAll().size();

    // Create the ResourceEstimation
    restResourceEstimationMockMvc
        .perform(
            post("/api/resource-estimations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourceEstimation)))
        .andExpect(status().isCreated());

    // Validate the ResourceEstimation in the database
    List<ResourceEstimation> resourceEstimationList = resourceEstimationRepository.findAll();
    assertThat(resourceEstimationList).hasSize(databaseSizeBeforeCreate + 1);
    ResourceEstimation testResourceEstimation =
        resourceEstimationList.get(resourceEstimationList.size() - 1);
  }

  @Test
  @Transactional
  public void createResourceEstimationWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = resourceEstimationRepository.findAll().size();

    // Create the ResourceEstimation with an existing ID
    resourceEstimation.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restResourceEstimationMockMvc
        .perform(
            post("/api/resource-estimations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourceEstimation)))
        .andExpect(status().isBadRequest());

    // Validate the ResourceEstimation in the database
    List<ResourceEstimation> resourceEstimationList = resourceEstimationRepository.findAll();
    assertThat(resourceEstimationList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllResourceEstimations() throws Exception {
    // Initialize the database
    resourceEstimationRepository.saveAndFlush(resourceEstimation);

    // Get all the resourceEstimationList
    restResourceEstimationMockMvc
        .perform(get("/api/resource-estimations?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(resourceEstimation.getId().intValue())));
  }

  @Test
  @Transactional
  public void getResourceEstimation() throws Exception {
    // Initialize the database
    resourceEstimationRepository.saveAndFlush(resourceEstimation);

    // Get the resourceEstimation
    restResourceEstimationMockMvc
        .perform(get("/api/resource-estimations/{id}", resourceEstimation.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(resourceEstimation.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingResourceEstimation() throws Exception {
    // Get the resourceEstimation
    restResourceEstimationMockMvc
        .perform(get("/api/resource-estimations/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateResourceEstimation() throws Exception {
    // Initialize the database
    resourceEstimationRepository.saveAndFlush(resourceEstimation);

    int databaseSizeBeforeUpdate = resourceEstimationRepository.findAll().size();

    // Update the resourceEstimation
    ResourceEstimation updatedResourceEstimation =
        resourceEstimationRepository.findById(resourceEstimation.getId()).get();
    // Disconnect from session so that the updates on updatedResourceEstimation are not directly
    // saved in db
    em.detach(updatedResourceEstimation);

    restResourceEstimationMockMvc
        .perform(
            put("/api/resource-estimations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedResourceEstimation)))
        .andExpect(status().isOk());

    // Validate the ResourceEstimation in the database
    List<ResourceEstimation> resourceEstimationList = resourceEstimationRepository.findAll();
    assertThat(resourceEstimationList).hasSize(databaseSizeBeforeUpdate);
    ResourceEstimation testResourceEstimation =
        resourceEstimationList.get(resourceEstimationList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingResourceEstimation() throws Exception {
    int databaseSizeBeforeUpdate = resourceEstimationRepository.findAll().size();

    // Create the ResourceEstimation

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restResourceEstimationMockMvc
        .perform(
            put("/api/resource-estimations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourceEstimation)))
        .andExpect(status().isBadRequest());

    // Validate the ResourceEstimation in the database
    List<ResourceEstimation> resourceEstimationList = resourceEstimationRepository.findAll();
    assertThat(resourceEstimationList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteResourceEstimation() throws Exception {
    // Initialize the database
    resourceEstimationRepository.saveAndFlush(resourceEstimation);

    int databaseSizeBeforeDelete = resourceEstimationRepository.findAll().size();

    // Delete the resourceEstimation
    restResourceEstimationMockMvc
        .perform(
            delete("/api/resource-estimations/{id}", resourceEstimation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ResourceEstimation> resourceEstimationList = resourceEstimationRepository.findAll();
    assertThat(resourceEstimationList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ResourceEstimation.class);
    ResourceEstimation resourceEstimation1 = new ResourceEstimation();
    resourceEstimation1.setId(1L);
    ResourceEstimation resourceEstimation2 = new ResourceEstimation();
    resourceEstimation2.setId(resourceEstimation1.getId());
    assertThat(resourceEstimation1).isEqualTo(resourceEstimation2);
    resourceEstimation2.setId(2L);
    assertThat(resourceEstimation1).isNotEqualTo(resourceEstimation2);
    resourceEstimation1.setId(null);
    assertThat(resourceEstimation1).isNotEqualTo(resourceEstimation2);
  }
}
