package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Automatic;
import br.ufpa.labes.spm.repository.AutomaticRepository;
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

/** Integration tests for the {@link AutomaticResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AutomaticResourceIT {

  @Autowired private AutomaticRepository automaticRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restAutomaticMockMvc;

  private Automatic automatic;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final AutomaticResource automaticResource = new AutomaticResource(automaticRepository);
    this.restAutomaticMockMvc =
        MockMvcBuilders.standaloneSetup(automaticResource)
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
  public static Automatic createEntity(EntityManager em) {
    Automatic automatic = new Automatic();
    return automatic;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Automatic createUpdatedEntity(EntityManager em) {
    Automatic automatic = new Automatic();
    return automatic;
  }

  @BeforeEach
  public void initTest() {
    automatic = createEntity(em);
  }

  @Test
  @Transactional
  public void createAutomatic() throws Exception {
    int databaseSizeBeforeCreate = automaticRepository.findAll().size();

    // Create the Automatic
    restAutomaticMockMvc
        .perform(
            post("/api/automatics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(automatic)))
        .andExpect(status().isCreated());

    // Validate the Automatic in the database
    List<Automatic> automaticList = automaticRepository.findAll();
    assertThat(automaticList).hasSize(databaseSizeBeforeCreate + 1);
    Automatic testAutomatic = automaticList.get(automaticList.size() - 1);
  }

  @Test
  @Transactional
  public void createAutomaticWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = automaticRepository.findAll().size();

    // Create the Automatic with an existing ID
    automatic.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restAutomaticMockMvc
        .perform(
            post("/api/automatics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(automatic)))
        .andExpect(status().isBadRequest());

    // Validate the Automatic in the database
    List<Automatic> automaticList = automaticRepository.findAll();
    assertThat(automaticList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllAutomatics() throws Exception {
    // Initialize the database
    automaticRepository.saveAndFlush(automatic);

    // Get all the automaticList
    restAutomaticMockMvc
        .perform(get("/api/automatics?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(automatic.getId().intValue())));
  }

  @Test
  @Transactional
  public void getAutomatic() throws Exception {
    // Initialize the database
    automaticRepository.saveAndFlush(automatic);

    // Get the automatic
    restAutomaticMockMvc
        .perform(get("/api/automatics/{id}", automatic.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(automatic.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingAutomatic() throws Exception {
    // Get the automatic
    restAutomaticMockMvc
        .perform(get("/api/automatics/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateAutomatic() throws Exception {
    // Initialize the database
    automaticRepository.saveAndFlush(automatic);

    int databaseSizeBeforeUpdate = automaticRepository.findAll().size();

    // Update the automatic
    Automatic updatedAutomatic = automaticRepository.findById(automatic.getId()).get();
    // Disconnect from session so that the updates on updatedAutomatic are not directly saved in db
    em.detach(updatedAutomatic);

    restAutomaticMockMvc
        .perform(
            put("/api/automatics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAutomatic)))
        .andExpect(status().isOk());

    // Validate the Automatic in the database
    List<Automatic> automaticList = automaticRepository.findAll();
    assertThat(automaticList).hasSize(databaseSizeBeforeUpdate);
    Automatic testAutomatic = automaticList.get(automaticList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingAutomatic() throws Exception {
    int databaseSizeBeforeUpdate = automaticRepository.findAll().size();

    // Create the Automatic

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAutomaticMockMvc
        .perform(
            put("/api/automatics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(automatic)))
        .andExpect(status().isBadRequest());

    // Validate the Automatic in the database
    List<Automatic> automaticList = automaticRepository.findAll();
    assertThat(automaticList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteAutomatic() throws Exception {
    // Initialize the database
    automaticRepository.saveAndFlush(automatic);

    int databaseSizeBeforeDelete = automaticRepository.findAll().size();

    // Delete the automatic
    restAutomaticMockMvc
        .perform(
            delete("/api/automatics/{id}", automatic.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Automatic> automaticList = automaticRepository.findAll();
    assertThat(automaticList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Automatic.class);
    Automatic automatic1 = new Automatic();
    automatic1.setId(1L);
    Automatic automatic2 = new Automatic();
    automatic2.setId(automatic1.getId());
    assertThat(automatic1).isEqualTo(automatic2);
    automatic2.setId(2L);
    assertThat(automatic1).isNotEqualTo(automatic2);
    automatic1.setId(null);
    assertThat(automatic1).isNotEqualTo(automatic2);
  }
}
