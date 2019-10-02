package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.OutOfWorkPeriod;
import br.ufpa.labes.spm.repository.OutOfWorkPeriodRepository;
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

/** Integration tests for the {@link OutOfWorkPeriodResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class OutOfWorkPeriodResourceIT {

  private static final String DEFAULT_WHY = "AAAAAAAAAA";
  private static final String UPDATED_WHY = "BBBBBBBBBB";

  private static final String DEFAULT_FROM_DATE = "AAAAAAAAAA";
  private static final String UPDATED_FROM_DATE = "BBBBBBBBBB";

  private static final String DEFAULT_TO_DATE = "AAAAAAAAAA";
  private static final String UPDATED_TO_DATE = "BBBBBBBBBB";

  @Autowired private OutOfWorkPeriodRepository outOfWorkPeriodRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restOutOfWorkPeriodMockMvc;

  private OutOfWorkPeriod outOfWorkPeriod;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final OutOfWorkPeriodResource outOfWorkPeriodResource =
        new OutOfWorkPeriodResource(outOfWorkPeriodRepository);
    this.restOutOfWorkPeriodMockMvc =
        MockMvcBuilders.standaloneSetup(outOfWorkPeriodResource)
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
  public static OutOfWorkPeriod createEntity(EntityManager em) {
    OutOfWorkPeriod outOfWorkPeriod =
        new OutOfWorkPeriod().why(DEFAULT_WHY).fromDate(DEFAULT_FROM_DATE).toDate(DEFAULT_TO_DATE);
    return outOfWorkPeriod;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static OutOfWorkPeriod createUpdatedEntity(EntityManager em) {
    OutOfWorkPeriod outOfWorkPeriod =
        new OutOfWorkPeriod().why(UPDATED_WHY).fromDate(UPDATED_FROM_DATE).toDate(UPDATED_TO_DATE);
    return outOfWorkPeriod;
  }

  @BeforeEach
  public void initTest() {
    outOfWorkPeriod = createEntity(em);
  }

  @Test
  @Transactional
  public void createOutOfWorkPeriod() throws Exception {
    int databaseSizeBeforeCreate = outOfWorkPeriodRepository.findAll().size();

    // Create the OutOfWorkPeriod
    restOutOfWorkPeriodMockMvc
        .perform(
            post("/api/out-of-work-periods")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(outOfWorkPeriod)))
        .andExpect(status().isCreated());

    // Validate the OutOfWorkPeriod in the database
    List<OutOfWorkPeriod> outOfWorkPeriodList = outOfWorkPeriodRepository.findAll();
    assertThat(outOfWorkPeriodList).hasSize(databaseSizeBeforeCreate + 1);
    OutOfWorkPeriod testOutOfWorkPeriod = outOfWorkPeriodList.get(outOfWorkPeriodList.size() - 1);
    assertThat(testOutOfWorkPeriod.getWhy()).isEqualTo(DEFAULT_WHY);
    assertThat(testOutOfWorkPeriod.getFromDate()).isEqualTo(DEFAULT_FROM_DATE);
    assertThat(testOutOfWorkPeriod.getToDate()).isEqualTo(DEFAULT_TO_DATE);
  }

  @Test
  @Transactional
  public void createOutOfWorkPeriodWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = outOfWorkPeriodRepository.findAll().size();

    // Create the OutOfWorkPeriod with an existing ID
    outOfWorkPeriod.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restOutOfWorkPeriodMockMvc
        .perform(
            post("/api/out-of-work-periods")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(outOfWorkPeriod)))
        .andExpect(status().isBadRequest());

    // Validate the OutOfWorkPeriod in the database
    List<OutOfWorkPeriod> outOfWorkPeriodList = outOfWorkPeriodRepository.findAll();
    assertThat(outOfWorkPeriodList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllOutOfWorkPeriods() throws Exception {
    // Initialize the database
    outOfWorkPeriodRepository.saveAndFlush(outOfWorkPeriod);

    // Get all the outOfWorkPeriodList
    restOutOfWorkPeriodMockMvc
        .perform(get("/api/out-of-work-periods?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(outOfWorkPeriod.getId().intValue())))
        .andExpect(jsonPath("$.[*].why").value(hasItem(DEFAULT_WHY.toString())))
        .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
        .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())));
  }

  @Test
  @Transactional
  public void getOutOfWorkPeriod() throws Exception {
    // Initialize the database
    outOfWorkPeriodRepository.saveAndFlush(outOfWorkPeriod);

    // Get the outOfWorkPeriod
    restOutOfWorkPeriodMockMvc
        .perform(get("/api/out-of-work-periods/{id}", outOfWorkPeriod.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(outOfWorkPeriod.getId().intValue()))
        .andExpect(jsonPath("$.why").value(DEFAULT_WHY.toString()))
        .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
        .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingOutOfWorkPeriod() throws Exception {
    // Get the outOfWorkPeriod
    restOutOfWorkPeriodMockMvc
        .perform(get("/api/out-of-work-periods/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateOutOfWorkPeriod() throws Exception {
    // Initialize the database
    outOfWorkPeriodRepository.saveAndFlush(outOfWorkPeriod);

    int databaseSizeBeforeUpdate = outOfWorkPeriodRepository.findAll().size();

    // Update the outOfWorkPeriod
    OutOfWorkPeriod updatedOutOfWorkPeriod =
        outOfWorkPeriodRepository.findById(outOfWorkPeriod.getId()).get();
    // Disconnect from session so that the updates on updatedOutOfWorkPeriod are not directly saved
    // in db
    em.detach(updatedOutOfWorkPeriod);
    updatedOutOfWorkPeriod.why(UPDATED_WHY).fromDate(UPDATED_FROM_DATE).toDate(UPDATED_TO_DATE);

    restOutOfWorkPeriodMockMvc
        .perform(
            put("/api/out-of-work-periods")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOutOfWorkPeriod)))
        .andExpect(status().isOk());

    // Validate the OutOfWorkPeriod in the database
    List<OutOfWorkPeriod> outOfWorkPeriodList = outOfWorkPeriodRepository.findAll();
    assertThat(outOfWorkPeriodList).hasSize(databaseSizeBeforeUpdate);
    OutOfWorkPeriod testOutOfWorkPeriod = outOfWorkPeriodList.get(outOfWorkPeriodList.size() - 1);
    assertThat(testOutOfWorkPeriod.getWhy()).isEqualTo(UPDATED_WHY);
    assertThat(testOutOfWorkPeriod.getFromDate()).isEqualTo(UPDATED_FROM_DATE);
    assertThat(testOutOfWorkPeriod.getToDate()).isEqualTo(UPDATED_TO_DATE);
  }

  @Test
  @Transactional
  public void updateNonExistingOutOfWorkPeriod() throws Exception {
    int databaseSizeBeforeUpdate = outOfWorkPeriodRepository.findAll().size();

    // Create the OutOfWorkPeriod

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restOutOfWorkPeriodMockMvc
        .perform(
            put("/api/out-of-work-periods")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(outOfWorkPeriod)))
        .andExpect(status().isBadRequest());

    // Validate the OutOfWorkPeriod in the database
    List<OutOfWorkPeriod> outOfWorkPeriodList = outOfWorkPeriodRepository.findAll();
    assertThat(outOfWorkPeriodList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteOutOfWorkPeriod() throws Exception {
    // Initialize the database
    outOfWorkPeriodRepository.saveAndFlush(outOfWorkPeriod);

    int databaseSizeBeforeDelete = outOfWorkPeriodRepository.findAll().size();

    // Delete the outOfWorkPeriod
    restOutOfWorkPeriodMockMvc
        .perform(
            delete("/api/out-of-work-periods/{id}", outOfWorkPeriod.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<OutOfWorkPeriod> outOfWorkPeriodList = outOfWorkPeriodRepository.findAll();
    assertThat(outOfWorkPeriodList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(OutOfWorkPeriod.class);
    OutOfWorkPeriod outOfWorkPeriod1 = new OutOfWorkPeriod();
    outOfWorkPeriod1.setId(1L);
    OutOfWorkPeriod outOfWorkPeriod2 = new OutOfWorkPeriod();
    outOfWorkPeriod2.setId(outOfWorkPeriod1.getId());
    assertThat(outOfWorkPeriod1).isEqualTo(outOfWorkPeriod2);
    outOfWorkPeriod2.setId(2L);
    assertThat(outOfWorkPeriod1).isNotEqualTo(outOfWorkPeriod2);
    outOfWorkPeriod1.setId(null);
    assertThat(outOfWorkPeriod1).isNotEqualTo(outOfWorkPeriod2);
  }
}
