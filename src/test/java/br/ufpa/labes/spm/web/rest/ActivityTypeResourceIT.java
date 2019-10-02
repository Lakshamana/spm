package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ActivityType;
import br.ufpa.labes.spm.repository.ActivityTypeRepository;
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

/** Integration tests for the {@link ActivityTypeResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ActivityTypeResourceIT {

  @Autowired private ActivityTypeRepository activityTypeRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restActivityTypeMockMvc;

  private ActivityType activityType;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ActivityTypeResource activityTypeResource =
        new ActivityTypeResource(activityTypeRepository);
    this.restActivityTypeMockMvc =
        MockMvcBuilders.standaloneSetup(activityTypeResource)
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
  public static ActivityType createEntity(EntityManager em) {
    ActivityType activityType = new ActivityType();
    return activityType;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ActivityType createUpdatedEntity(EntityManager em) {
    ActivityType activityType = new ActivityType();
    return activityType;
  }

  @BeforeEach
  public void initTest() {
    activityType = createEntity(em);
  }

  @Test
  @Transactional
  public void createActivityType() throws Exception {
    int databaseSizeBeforeCreate = activityTypeRepository.findAll().size();

    // Create the ActivityType
    restActivityTypeMockMvc
        .perform(
            post("/api/activity-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activityType)))
        .andExpect(status().isCreated());

    // Validate the ActivityType in the database
    List<ActivityType> activityTypeList = activityTypeRepository.findAll();
    assertThat(activityTypeList).hasSize(databaseSizeBeforeCreate + 1);
    ActivityType testActivityType = activityTypeList.get(activityTypeList.size() - 1);
  }

  @Test
  @Transactional
  public void createActivityTypeWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = activityTypeRepository.findAll().size();

    // Create the ActivityType with an existing ID
    activityType.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restActivityTypeMockMvc
        .perform(
            post("/api/activity-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activityType)))
        .andExpect(status().isBadRequest());

    // Validate the ActivityType in the database
    List<ActivityType> activityTypeList = activityTypeRepository.findAll();
    assertThat(activityTypeList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllActivityTypes() throws Exception {
    // Initialize the database
    activityTypeRepository.saveAndFlush(activityType);

    // Get all the activityTypeList
    restActivityTypeMockMvc
        .perform(get("/api/activity-types?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(activityType.getId().intValue())));
  }

  @Test
  @Transactional
  public void getActivityType() throws Exception {
    // Initialize the database
    activityTypeRepository.saveAndFlush(activityType);

    // Get the activityType
    restActivityTypeMockMvc
        .perform(get("/api/activity-types/{id}", activityType.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(activityType.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingActivityType() throws Exception {
    // Get the activityType
    restActivityTypeMockMvc
        .perform(get("/api/activity-types/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateActivityType() throws Exception {
    // Initialize the database
    activityTypeRepository.saveAndFlush(activityType);

    int databaseSizeBeforeUpdate = activityTypeRepository.findAll().size();

    // Update the activityType
    ActivityType updatedActivityType = activityTypeRepository.findById(activityType.getId()).get();
    // Disconnect from session so that the updates on updatedActivityType are not directly saved in
    // db
    em.detach(updatedActivityType);

    restActivityTypeMockMvc
        .perform(
            put("/api/activity-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedActivityType)))
        .andExpect(status().isOk());

    // Validate the ActivityType in the database
    List<ActivityType> activityTypeList = activityTypeRepository.findAll();
    assertThat(activityTypeList).hasSize(databaseSizeBeforeUpdate);
    ActivityType testActivityType = activityTypeList.get(activityTypeList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingActivityType() throws Exception {
    int databaseSizeBeforeUpdate = activityTypeRepository.findAll().size();

    // Create the ActivityType

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restActivityTypeMockMvc
        .perform(
            put("/api/activity-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activityType)))
        .andExpect(status().isBadRequest());

    // Validate the ActivityType in the database
    List<ActivityType> activityTypeList = activityTypeRepository.findAll();
    assertThat(activityTypeList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteActivityType() throws Exception {
    // Initialize the database
    activityTypeRepository.saveAndFlush(activityType);

    int databaseSizeBeforeDelete = activityTypeRepository.findAll().size();

    // Delete the activityType
    restActivityTypeMockMvc
        .perform(
            delete("/api/activity-types/{id}", activityType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ActivityType> activityTypeList = activityTypeRepository.findAll();
    assertThat(activityTypeList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ActivityType.class);
    ActivityType activityType1 = new ActivityType();
    activityType1.setId(1L);
    ActivityType activityType2 = new ActivityType();
    activityType2.setId(activityType1.getId());
    assertThat(activityType1).isEqualTo(activityType2);
    activityType2.setId(2L);
    assertThat(activityType1).isNotEqualTo(activityType2);
    activityType1.setId(null);
    assertThat(activityType1).isNotEqualTo(activityType2);
  }
}
