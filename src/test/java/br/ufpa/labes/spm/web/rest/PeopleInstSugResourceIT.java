package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.PeopleInstSug;
import br.ufpa.labes.spm.repository.PeopleInstSugRepository;
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

/** Integration tests for the {@link PeopleInstSugResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class PeopleInstSugResourceIT {

  @Autowired private PeopleInstSugRepository peopleInstSugRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restPeopleInstSugMockMvc;

  private PeopleInstSug peopleInstSug;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final PeopleInstSugResource peopleInstSugResource =
        new PeopleInstSugResource(peopleInstSugRepository);
    this.restPeopleInstSugMockMvc =
        MockMvcBuilders.standaloneSetup(peopleInstSugResource)
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
  public static PeopleInstSug createEntity(EntityManager em) {
    PeopleInstSug peopleInstSug = new PeopleInstSug();
    return peopleInstSug;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static PeopleInstSug createUpdatedEntity(EntityManager em) {
    PeopleInstSug peopleInstSug = new PeopleInstSug();
    return peopleInstSug;
  }

  @BeforeEach
  public void initTest() {
    peopleInstSug = createEntity(em);
  }

  @Test
  @Transactional
  public void createPeopleInstSug() throws Exception {
    int databaseSizeBeforeCreate = peopleInstSugRepository.findAll().size();

    // Create the PeopleInstSug
    restPeopleInstSugMockMvc
        .perform(
            post("/api/people-inst-sugs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(peopleInstSug)))
        .andExpect(status().isCreated());

    // Validate the PeopleInstSug in the database
    List<PeopleInstSug> peopleInstSugList = peopleInstSugRepository.findAll();
    assertThat(peopleInstSugList).hasSize(databaseSizeBeforeCreate + 1);
    PeopleInstSug testPeopleInstSug = peopleInstSugList.get(peopleInstSugList.size() - 1);
  }

  @Test
  @Transactional
  public void createPeopleInstSugWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = peopleInstSugRepository.findAll().size();

    // Create the PeopleInstSug with an existing ID
    peopleInstSug.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restPeopleInstSugMockMvc
        .perform(
            post("/api/people-inst-sugs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(peopleInstSug)))
        .andExpect(status().isBadRequest());

    // Validate the PeopleInstSug in the database
    List<PeopleInstSug> peopleInstSugList = peopleInstSugRepository.findAll();
    assertThat(peopleInstSugList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllPeopleInstSugs() throws Exception {
    // Initialize the database
    peopleInstSugRepository.saveAndFlush(peopleInstSug);

    // Get all the peopleInstSugList
    restPeopleInstSugMockMvc
        .perform(get("/api/people-inst-sugs?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(peopleInstSug.getId().intValue())));
  }

  @Test
  @Transactional
  public void getPeopleInstSug() throws Exception {
    // Initialize the database
    peopleInstSugRepository.saveAndFlush(peopleInstSug);

    // Get the peopleInstSug
    restPeopleInstSugMockMvc
        .perform(get("/api/people-inst-sugs/{id}", peopleInstSug.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(peopleInstSug.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingPeopleInstSug() throws Exception {
    // Get the peopleInstSug
    restPeopleInstSugMockMvc
        .perform(get("/api/people-inst-sugs/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updatePeopleInstSug() throws Exception {
    // Initialize the database
    peopleInstSugRepository.saveAndFlush(peopleInstSug);

    int databaseSizeBeforeUpdate = peopleInstSugRepository.findAll().size();

    // Update the peopleInstSug
    PeopleInstSug updatedPeopleInstSug =
        peopleInstSugRepository.findById(peopleInstSug.getId()).get();
    // Disconnect from session so that the updates on updatedPeopleInstSug are not directly saved in
    // db
    em.detach(updatedPeopleInstSug);

    restPeopleInstSugMockMvc
        .perform(
            put("/api/people-inst-sugs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPeopleInstSug)))
        .andExpect(status().isOk());

    // Validate the PeopleInstSug in the database
    List<PeopleInstSug> peopleInstSugList = peopleInstSugRepository.findAll();
    assertThat(peopleInstSugList).hasSize(databaseSizeBeforeUpdate);
    PeopleInstSug testPeopleInstSug = peopleInstSugList.get(peopleInstSugList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingPeopleInstSug() throws Exception {
    int databaseSizeBeforeUpdate = peopleInstSugRepository.findAll().size();

    // Create the PeopleInstSug

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restPeopleInstSugMockMvc
        .perform(
            put("/api/people-inst-sugs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(peopleInstSug)))
        .andExpect(status().isBadRequest());

    // Validate the PeopleInstSug in the database
    List<PeopleInstSug> peopleInstSugList = peopleInstSugRepository.findAll();
    assertThat(peopleInstSugList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deletePeopleInstSug() throws Exception {
    // Initialize the database
    peopleInstSugRepository.saveAndFlush(peopleInstSug);

    int databaseSizeBeforeDelete = peopleInstSugRepository.findAll().size();

    // Delete the peopleInstSug
    restPeopleInstSugMockMvc
        .perform(
            delete("/api/people-inst-sugs/{id}", peopleInstSug.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<PeopleInstSug> peopleInstSugList = peopleInstSugRepository.findAll();
    assertThat(peopleInstSugList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(PeopleInstSug.class);
    PeopleInstSug peopleInstSug1 = new PeopleInstSug();
    peopleInstSug1.setId(1L);
    PeopleInstSug peopleInstSug2 = new PeopleInstSug();
    peopleInstSug2.setId(peopleInstSug1.getId());
    assertThat(peopleInstSug1).isEqualTo(peopleInstSug2);
    peopleInstSug2.setId(2L);
    assertThat(peopleInstSug1).isNotEqualTo(peopleInstSug2);
    peopleInstSug1.setId(null);
    assertThat(peopleInstSug1).isNotEqualTo(peopleInstSug2);
  }
}
