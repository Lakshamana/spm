package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ActivityInstantiated;
import br.ufpa.labes.spm.repository.ActivityInstantiatedRepository;
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

/** Integration tests for the {@link ActivityInstantiatedResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ActivityInstantiatedResourceIT {

  @Autowired private ActivityInstantiatedRepository activityInstantiatedRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restActivityInstantiatedMockMvc;

  private ActivityInstantiated activityInstantiated;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ActivityInstantiatedResource activityInstantiatedResource =
        new ActivityInstantiatedResource(activityInstantiatedRepository);
    this.restActivityInstantiatedMockMvc =
        MockMvcBuilders.standaloneSetup(activityInstantiatedResource)
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
  public static ActivityInstantiated createEntity(EntityManager em) {
    ActivityInstantiated activityInstantiated = new ActivityInstantiated();
    return activityInstantiated;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ActivityInstantiated createUpdatedEntity(EntityManager em) {
    ActivityInstantiated activityInstantiated = new ActivityInstantiated();
    return activityInstantiated;
  }

  @BeforeEach
  public void initTest() {
    activityInstantiated = createEntity(em);
  }

  @Test
  @Transactional
  public void createActivityInstantiated() throws Exception {
    int databaseSizeBeforeCreate = activityInstantiatedRepository.findAll().size();

    // Create the ActivityInstantiated
    restActivityInstantiatedMockMvc
        .perform(
            post("/api/activity-instantiateds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activityInstantiated)))
        .andExpect(status().isCreated());

    // Validate the ActivityInstantiated in the database
    List<ActivityInstantiated> activityInstantiatedList = activityInstantiatedRepository.findAll();
    assertThat(activityInstantiatedList).hasSize(databaseSizeBeforeCreate + 1);
    ActivityInstantiated testActivityInstantiated =
        activityInstantiatedList.get(activityInstantiatedList.size() - 1);
  }

  @Test
  @Transactional
  public void createActivityInstantiatedWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = activityInstantiatedRepository.findAll().size();

    // Create the ActivityInstantiated with an existing ID
    activityInstantiated.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restActivityInstantiatedMockMvc
        .perform(
            post("/api/activity-instantiateds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activityInstantiated)))
        .andExpect(status().isBadRequest());

    // Validate the ActivityInstantiated in the database
    List<ActivityInstantiated> activityInstantiatedList = activityInstantiatedRepository.findAll();
    assertThat(activityInstantiatedList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllActivityInstantiateds() throws Exception {
    // Initialize the database
    activityInstantiatedRepository.saveAndFlush(activityInstantiated);

    // Get all the activityInstantiatedList
    restActivityInstantiatedMockMvc
        .perform(get("/api/activity-instantiateds?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(activityInstantiated.getId().intValue())));
  }

  @Test
  @Transactional
  public void getActivityInstantiated() throws Exception {
    // Initialize the database
    activityInstantiatedRepository.saveAndFlush(activityInstantiated);

    // Get the activityInstantiated
    restActivityInstantiatedMockMvc
        .perform(get("/api/activity-instantiateds/{id}", activityInstantiated.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(activityInstantiated.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingActivityInstantiated() throws Exception {
    // Get the activityInstantiated
    restActivityInstantiatedMockMvc
        .perform(get("/api/activity-instantiateds/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateActivityInstantiated() throws Exception {
    // Initialize the database
    activityInstantiatedRepository.saveAndFlush(activityInstantiated);

    int databaseSizeBeforeUpdate = activityInstantiatedRepository.findAll().size();

    // Update the activityInstantiated
    ActivityInstantiated updatedActivityInstantiated =
        activityInstantiatedRepository.findById(activityInstantiated.getId()).get();
    // Disconnect from session so that the updates on updatedActivityInstantiated are not directly
    // saved in db
    em.detach(updatedActivityInstantiated);

    restActivityInstantiatedMockMvc
        .perform(
            put("/api/activity-instantiateds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedActivityInstantiated)))
        .andExpect(status().isOk());

    // Validate the ActivityInstantiated in the database
    List<ActivityInstantiated> activityInstantiatedList = activityInstantiatedRepository.findAll();
    assertThat(activityInstantiatedList).hasSize(databaseSizeBeforeUpdate);
    ActivityInstantiated testActivityInstantiated =
        activityInstantiatedList.get(activityInstantiatedList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingActivityInstantiated() throws Exception {
    int databaseSizeBeforeUpdate = activityInstantiatedRepository.findAll().size();

    // Create the ActivityInstantiated

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restActivityInstantiatedMockMvc
        .perform(
            put("/api/activity-instantiateds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activityInstantiated)))
        .andExpect(status().isBadRequest());

    // Validate the ActivityInstantiated in the database
    List<ActivityInstantiated> activityInstantiatedList = activityInstantiatedRepository.findAll();
    assertThat(activityInstantiatedList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteActivityInstantiated() throws Exception {
    // Initialize the database
    activityInstantiatedRepository.saveAndFlush(activityInstantiated);

    int databaseSizeBeforeDelete = activityInstantiatedRepository.findAll().size();

    // Delete the activityInstantiated
    restActivityInstantiatedMockMvc
        .perform(
            delete("/api/activity-instantiateds/{id}", activityInstantiated.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ActivityInstantiated> activityInstantiatedList = activityInstantiatedRepository.findAll();
    assertThat(activityInstantiatedList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ActivityInstantiated.class);
    ActivityInstantiated activityInstantiated1 = new ActivityInstantiated();
    activityInstantiated1.setId(1L);
    ActivityInstantiated activityInstantiated2 = new ActivityInstantiated();
    activityInstantiated2.setId(activityInstantiated1.getId());
    assertThat(activityInstantiated1).isEqualTo(activityInstantiated2);
    activityInstantiated2.setId(2L);
    assertThat(activityInstantiated1).isNotEqualTo(activityInstantiated2);
    activityInstantiated1.setId(null);
    assertThat(activityInstantiated1).isNotEqualTo(activityInstantiated2);
  }
}
