package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Ocurrence;
import br.ufpa.labes.spm.repository.OcurrenceRepository;
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
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static br.ufpa.labes.spm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Integration tests for the {@link OcurrenceResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class OcurrenceResourceIT {

  private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

  private static final Instant DEFAULT_TIME = Instant.ofEpochMilli(0L);
  private static final Instant UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);
  private static final Instant SMALLER_TIME = Instant.ofEpochMilli(-1L);

  private static final String DEFAULT_EVENT = "AAAAAAAAAA";
  private static final String UPDATED_EVENT = "BBBBBBBBBB";

  @Autowired private OcurrenceRepository ocurrenceRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restOcurrenceMockMvc;

  private Ocurrence ocurrence;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final OcurrenceResource ocurrenceResource = new OcurrenceResource(ocurrenceRepository);
    this.restOcurrenceMockMvc =
        MockMvcBuilders.standaloneSetup(ocurrenceResource)
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
  public static Ocurrence createEntity(EntityManager em) {
    Ocurrence ocurrence =
        new Ocurrence().date(DEFAULT_DATE).time(DEFAULT_TIME).event(DEFAULT_EVENT);
    return ocurrence;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Ocurrence createUpdatedEntity(EntityManager em) {
    Ocurrence ocurrence =
        new Ocurrence().date(UPDATED_DATE).time(UPDATED_TIME).event(UPDATED_EVENT);
    return ocurrence;
  }

  @BeforeEach
  public void initTest() {
    ocurrence = createEntity(em);
  }

  @Test
  @Transactional
  public void createOcurrence() throws Exception {
    int databaseSizeBeforeCreate = ocurrenceRepository.findAll().size();

    // Create the Ocurrence
    restOcurrenceMockMvc
        .perform(
            post("/api/ocurrences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocurrence)))
        .andExpect(status().isCreated());

    // Validate the Ocurrence in the database
    List<Ocurrence> ocurrenceList = ocurrenceRepository.findAll();
    assertThat(ocurrenceList).hasSize(databaseSizeBeforeCreate + 1);
    Ocurrence testOcurrence = ocurrenceList.get(ocurrenceList.size() - 1);
    assertThat(testOcurrence.getDate()).isEqualTo(DEFAULT_DATE);
    assertThat(testOcurrence.getTime()).isEqualTo(DEFAULT_TIME);
    assertThat(testOcurrence.getEvent()).isEqualTo(DEFAULT_EVENT);
  }

  @Test
  @Transactional
  public void createOcurrenceWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = ocurrenceRepository.findAll().size();

    // Create the Ocurrence with an existing ID
    ocurrence.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restOcurrenceMockMvc
        .perform(
            post("/api/ocurrences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocurrence)))
        .andExpect(status().isBadRequest());

    // Validate the Ocurrence in the database
    List<Ocurrence> ocurrenceList = ocurrenceRepository.findAll();
    assertThat(ocurrenceList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllOcurrences() throws Exception {
    // Initialize the database
    ocurrenceRepository.saveAndFlush(ocurrence);

    // Get all the ocurrenceList
    restOcurrenceMockMvc
        .perform(get("/api/ocurrences?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(ocurrence.getId().intValue())))
        .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
        .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
        .andExpect(jsonPath("$.[*].event").value(hasItem(DEFAULT_EVENT.toString())));
  }

  @Test
  @Transactional
  public void getOcurrence() throws Exception {
    // Initialize the database
    ocurrenceRepository.saveAndFlush(ocurrence);

    // Get the ocurrence
    restOcurrenceMockMvc
        .perform(get("/api/ocurrences/{id}", ocurrence.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(ocurrence.getId().intValue()))
        .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
        .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
        .andExpect(jsonPath("$.event").value(DEFAULT_EVENT.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingOcurrence() throws Exception {
    // Get the ocurrence
    restOcurrenceMockMvc
        .perform(get("/api/ocurrences/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateOcurrence() throws Exception {
    // Initialize the database
    ocurrenceRepository.saveAndFlush(ocurrence);

    int databaseSizeBeforeUpdate = ocurrenceRepository.findAll().size();

    // Update the ocurrence
    Ocurrence updatedOcurrence = ocurrenceRepository.findById(ocurrence.getId()).get();
    // Disconnect from session so that the updates on updatedOcurrence are not directly saved in db
    em.detach(updatedOcurrence);
    updatedOcurrence.date(UPDATED_DATE).time(UPDATED_TIME).event(UPDATED_EVENT);

    restOcurrenceMockMvc
        .perform(
            put("/api/ocurrences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOcurrence)))
        .andExpect(status().isOk());

    // Validate the Ocurrence in the database
    List<Ocurrence> ocurrenceList = ocurrenceRepository.findAll();
    assertThat(ocurrenceList).hasSize(databaseSizeBeforeUpdate);
    Ocurrence testOcurrence = ocurrenceList.get(ocurrenceList.size() - 1);
    assertThat(testOcurrence.getDate()).isEqualTo(UPDATED_DATE);
    assertThat(testOcurrence.getTime()).isEqualTo(UPDATED_TIME);
    assertThat(testOcurrence.getEvent()).isEqualTo(UPDATED_EVENT);
  }

  @Test
  @Transactional
  public void updateNonExistingOcurrence() throws Exception {
    int databaseSizeBeforeUpdate = ocurrenceRepository.findAll().size();

    // Create the Ocurrence

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restOcurrenceMockMvc
        .perform(
            put("/api/ocurrences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(ocurrence)))
        .andExpect(status().isBadRequest());

    // Validate the Ocurrence in the database
    List<Ocurrence> ocurrenceList = ocurrenceRepository.findAll();
    assertThat(ocurrenceList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteOcurrence() throws Exception {
    // Initialize the database
    ocurrenceRepository.saveAndFlush(ocurrence);

    int databaseSizeBeforeDelete = ocurrenceRepository.findAll().size();

    // Delete the ocurrence
    restOcurrenceMockMvc
        .perform(
            delete("/api/ocurrences/{id}", ocurrence.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Ocurrence> ocurrenceList = ocurrenceRepository.findAll();
    assertThat(ocurrenceList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Ocurrence.class);
    Ocurrence ocurrence1 = new Ocurrence();
    ocurrence1.setId(1L);
    Ocurrence ocurrence2 = new Ocurrence();
    ocurrence2.setId(ocurrence1.getId());
    assertThat(ocurrence1).isEqualTo(ocurrence2);
    ocurrence2.setId(2L);
    assertThat(ocurrence1).isNotEqualTo(ocurrence2);
    ocurrence1.setId(null);
    assertThat(ocurrence1).isNotEqualTo(ocurrence2);
  }
}
