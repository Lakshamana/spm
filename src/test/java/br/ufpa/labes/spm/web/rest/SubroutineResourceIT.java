package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Subroutine;
import br.ufpa.labes.spm.repository.SubroutineRepository;
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

/** Integration tests for the {@link SubroutineResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class SubroutineResourceIT {

  @Autowired private SubroutineRepository subroutineRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restSubroutineMockMvc;

  private Subroutine subroutine;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final SubroutineResource subroutineResource = new SubroutineResource(subroutineRepository);
    this.restSubroutineMockMvc =
        MockMvcBuilders.standaloneSetup(subroutineResource)
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
  public static Subroutine createEntity(EntityManager em) {
    Subroutine subroutine = new Subroutine();
    return subroutine;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Subroutine createUpdatedEntity(EntityManager em) {
    Subroutine subroutine = new Subroutine();
    return subroutine;
  }

  @BeforeEach
  public void initTest() {
    subroutine = createEntity(em);
  }

  @Test
  @Transactional
  public void createSubroutine() throws Exception {
    int databaseSizeBeforeCreate = subroutineRepository.findAll().size();

    // Create the Subroutine
    restSubroutineMockMvc
        .perform(
            post("/api/subroutines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(subroutine)))
        .andExpect(status().isCreated());

    // Validate the Subroutine in the database
    List<Subroutine> subroutineList = subroutineRepository.findAll();
    assertThat(subroutineList).hasSize(databaseSizeBeforeCreate + 1);
    Subroutine testSubroutine = subroutineList.get(subroutineList.size() - 1);
  }

  @Test
  @Transactional
  public void createSubroutineWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = subroutineRepository.findAll().size();

    // Create the Subroutine with an existing ID
    subroutine.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restSubroutineMockMvc
        .perform(
            post("/api/subroutines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(subroutine)))
        .andExpect(status().isBadRequest());

    // Validate the Subroutine in the database
    List<Subroutine> subroutineList = subroutineRepository.findAll();
    assertThat(subroutineList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllSubroutines() throws Exception {
    // Initialize the database
    subroutineRepository.saveAndFlush(subroutine);

    // Get all the subroutineList
    restSubroutineMockMvc
        .perform(get("/api/subroutines?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(subroutine.getId().intValue())));
  }

  @Test
  @Transactional
  public void getSubroutine() throws Exception {
    // Initialize the database
    subroutineRepository.saveAndFlush(subroutine);

    // Get the subroutine
    restSubroutineMockMvc
        .perform(get("/api/subroutines/{id}", subroutine.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(subroutine.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingSubroutine() throws Exception {
    // Get the subroutine
    restSubroutineMockMvc
        .perform(get("/api/subroutines/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateSubroutine() throws Exception {
    // Initialize the database
    subroutineRepository.saveAndFlush(subroutine);

    int databaseSizeBeforeUpdate = subroutineRepository.findAll().size();

    // Update the subroutine
    Subroutine updatedSubroutine = subroutineRepository.findById(subroutine.getId()).get();
    // Disconnect from session so that the updates on updatedSubroutine are not directly saved in db
    em.detach(updatedSubroutine);

    restSubroutineMockMvc
        .perform(
            put("/api/subroutines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSubroutine)))
        .andExpect(status().isOk());

    // Validate the Subroutine in the database
    List<Subroutine> subroutineList = subroutineRepository.findAll();
    assertThat(subroutineList).hasSize(databaseSizeBeforeUpdate);
    Subroutine testSubroutine = subroutineList.get(subroutineList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingSubroutine() throws Exception {
    int databaseSizeBeforeUpdate = subroutineRepository.findAll().size();

    // Create the Subroutine

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restSubroutineMockMvc
        .perform(
            put("/api/subroutines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(subroutine)))
        .andExpect(status().isBadRequest());

    // Validate the Subroutine in the database
    List<Subroutine> subroutineList = subroutineRepository.findAll();
    assertThat(subroutineList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteSubroutine() throws Exception {
    // Initialize the database
    subroutineRepository.saveAndFlush(subroutine);

    int databaseSizeBeforeDelete = subroutineRepository.findAll().size();

    // Delete the subroutine
    restSubroutineMockMvc
        .perform(
            delete("/api/subroutines/{id}", subroutine.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Subroutine> subroutineList = subroutineRepository.findAll();
    assertThat(subroutineList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Subroutine.class);
    Subroutine subroutine1 = new Subroutine();
    subroutine1.setId(1L);
    Subroutine subroutine2 = new Subroutine();
    subroutine2.setId(subroutine1.getId());
    assertThat(subroutine1).isEqualTo(subroutine2);
    subroutine2.setId(2L);
    assertThat(subroutine1).isNotEqualTo(subroutine2);
    subroutine1.setId(null);
    assertThat(subroutine1).isNotEqualTo(subroutine2);
  }
}
