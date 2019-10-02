package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.LessonLearned;
import br.ufpa.labes.spm.repository.LessonLearnedRepository;
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
import java.time.ZoneId;
import java.util.List;

import static br.ufpa.labes.spm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Integration tests for the {@link LessonLearnedResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class LessonLearnedResourceIT {

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate SMALLER_CREATION_DATE = LocalDate.ofEpochDay(-1L);

  @Autowired private LessonLearnedRepository lessonLearnedRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restLessonLearnedMockMvc;

  private LessonLearned lessonLearned;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final LessonLearnedResource lessonLearnedResource =
        new LessonLearnedResource(lessonLearnedRepository);
    this.restLessonLearnedMockMvc =
        MockMvcBuilders.standaloneSetup(lessonLearnedResource)
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
  public static LessonLearned createEntity(EntityManager em) {
    LessonLearned lessonLearned =
        new LessonLearned()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .creationDate(DEFAULT_CREATION_DATE);
    return lessonLearned;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static LessonLearned createUpdatedEntity(EntityManager em) {
    LessonLearned lessonLearned =
        new LessonLearned()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE);
    return lessonLearned;
  }

  @BeforeEach
  public void initTest() {
    lessonLearned = createEntity(em);
  }

  @Test
  @Transactional
  public void createLessonLearned() throws Exception {
    int databaseSizeBeforeCreate = lessonLearnedRepository.findAll().size();

    // Create the LessonLearned
    restLessonLearnedMockMvc
        .perform(
            post("/api/lesson-learneds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lessonLearned)))
        .andExpect(status().isCreated());

    // Validate the LessonLearned in the database
    List<LessonLearned> lessonLearnedList = lessonLearnedRepository.findAll();
    assertThat(lessonLearnedList).hasSize(databaseSizeBeforeCreate + 1);
    LessonLearned testLessonLearned = lessonLearnedList.get(lessonLearnedList.size() - 1);
    assertThat(testLessonLearned.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testLessonLearned.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testLessonLearned.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
  }

  @Test
  @Transactional
  public void createLessonLearnedWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = lessonLearnedRepository.findAll().size();

    // Create the LessonLearned with an existing ID
    lessonLearned.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restLessonLearnedMockMvc
        .perform(
            post("/api/lesson-learneds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lessonLearned)))
        .andExpect(status().isBadRequest());

    // Validate the LessonLearned in the database
    List<LessonLearned> lessonLearnedList = lessonLearnedRepository.findAll();
    assertThat(lessonLearnedList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllLessonLearneds() throws Exception {
    // Initialize the database
    lessonLearnedRepository.saveAndFlush(lessonLearned);

    // Get all the lessonLearnedList
    restLessonLearnedMockMvc
        .perform(get("/api/lesson-learneds?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(lessonLearned.getId().intValue())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
        .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())));
  }

  @Test
  @Transactional
  public void getLessonLearned() throws Exception {
    // Initialize the database
    lessonLearnedRepository.saveAndFlush(lessonLearned);

    // Get the lessonLearned
    restLessonLearnedMockMvc
        .perform(get("/api/lesson-learneds/{id}", lessonLearned.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(lessonLearned.getId().intValue()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
        .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingLessonLearned() throws Exception {
    // Get the lessonLearned
    restLessonLearnedMockMvc
        .perform(get("/api/lesson-learneds/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateLessonLearned() throws Exception {
    // Initialize the database
    lessonLearnedRepository.saveAndFlush(lessonLearned);

    int databaseSizeBeforeUpdate = lessonLearnedRepository.findAll().size();

    // Update the lessonLearned
    LessonLearned updatedLessonLearned =
        lessonLearnedRepository.findById(lessonLearned.getId()).get();
    // Disconnect from session so that the updates on updatedLessonLearned are not directly saved in
    // db
    em.detach(updatedLessonLearned);
    updatedLessonLearned
        .name(UPDATED_NAME)
        .description(UPDATED_DESCRIPTION)
        .creationDate(UPDATED_CREATION_DATE);

    restLessonLearnedMockMvc
        .perform(
            put("/api/lesson-learneds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLessonLearned)))
        .andExpect(status().isOk());

    // Validate the LessonLearned in the database
    List<LessonLearned> lessonLearnedList = lessonLearnedRepository.findAll();
    assertThat(lessonLearnedList).hasSize(databaseSizeBeforeUpdate);
    LessonLearned testLessonLearned = lessonLearnedList.get(lessonLearnedList.size() - 1);
    assertThat(testLessonLearned.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testLessonLearned.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    assertThat(testLessonLearned.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
  }

  @Test
  @Transactional
  public void updateNonExistingLessonLearned() throws Exception {
    int databaseSizeBeforeUpdate = lessonLearnedRepository.findAll().size();

    // Create the LessonLearned

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restLessonLearnedMockMvc
        .perform(
            put("/api/lesson-learneds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lessonLearned)))
        .andExpect(status().isBadRequest());

    // Validate the LessonLearned in the database
    List<LessonLearned> lessonLearnedList = lessonLearnedRepository.findAll();
    assertThat(lessonLearnedList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteLessonLearned() throws Exception {
    // Initialize the database
    lessonLearnedRepository.saveAndFlush(lessonLearned);

    int databaseSizeBeforeDelete = lessonLearnedRepository.findAll().size();

    // Delete the lessonLearned
    restLessonLearnedMockMvc
        .perform(
            delete("/api/lesson-learneds/{id}", lessonLearned.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<LessonLearned> lessonLearnedList = lessonLearnedRepository.findAll();
    assertThat(lessonLearnedList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(LessonLearned.class);
    LessonLearned lessonLearned1 = new LessonLearned();
    lessonLearned1.setId(1L);
    LessonLearned lessonLearned2 = new LessonLearned();
    lessonLearned2.setId(lessonLearned1.getId());
    assertThat(lessonLearned1).isEqualTo(lessonLearned2);
    lessonLearned2.setId(2L);
    assertThat(lessonLearned1).isNotEqualTo(lessonLearned2);
    lessonLearned1.setId(null);
    assertThat(lessonLearned1).isNotEqualTo(lessonLearned2);
  }
}
