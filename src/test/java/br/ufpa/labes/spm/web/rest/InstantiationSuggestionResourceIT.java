package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.InstantiationSuggestion;
import br.ufpa.labes.spm.repository.InstantiationSuggestionRepository;
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

/** Integration tests for the {@link InstantiationSuggestionResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class InstantiationSuggestionResourceIT {

  @Autowired private InstantiationSuggestionRepository instantiationSuggestionRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restInstantiationSuggestionMockMvc;

  private InstantiationSuggestion instantiationSuggestion;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final InstantiationSuggestionResource instantiationSuggestionResource =
        new InstantiationSuggestionResource(instantiationSuggestionRepository);
    this.restInstantiationSuggestionMockMvc =
        MockMvcBuilders.standaloneSetup(instantiationSuggestionResource)
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
  public static InstantiationSuggestion createEntity(EntityManager em) {
    InstantiationSuggestion instantiationSuggestion = new InstantiationSuggestion();
    return instantiationSuggestion;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static InstantiationSuggestion createUpdatedEntity(EntityManager em) {
    InstantiationSuggestion instantiationSuggestion = new InstantiationSuggestion();
    return instantiationSuggestion;
  }

  @BeforeEach
  public void initTest() {
    instantiationSuggestion = createEntity(em);
  }

  @Test
  @Transactional
  public void createInstantiationSuggestion() throws Exception {
    int databaseSizeBeforeCreate = instantiationSuggestionRepository.findAll().size();

    // Create the InstantiationSuggestion
    restInstantiationSuggestionMockMvc
        .perform(
            post("/api/instantiation-suggestions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instantiationSuggestion)))
        .andExpect(status().isCreated());

    // Validate the InstantiationSuggestion in the database
    List<InstantiationSuggestion> instantiationSuggestionList =
        instantiationSuggestionRepository.findAll();
    assertThat(instantiationSuggestionList).hasSize(databaseSizeBeforeCreate + 1);
    InstantiationSuggestion testInstantiationSuggestion =
        instantiationSuggestionList.get(instantiationSuggestionList.size() - 1);
  }

  @Test
  @Transactional
  public void createInstantiationSuggestionWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = instantiationSuggestionRepository.findAll().size();

    // Create the InstantiationSuggestion with an existing ID
    instantiationSuggestion.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restInstantiationSuggestionMockMvc
        .perform(
            post("/api/instantiation-suggestions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instantiationSuggestion)))
        .andExpect(status().isBadRequest());

    // Validate the InstantiationSuggestion in the database
    List<InstantiationSuggestion> instantiationSuggestionList =
        instantiationSuggestionRepository.findAll();
    assertThat(instantiationSuggestionList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllInstantiationSuggestions() throws Exception {
    // Initialize the database
    instantiationSuggestionRepository.saveAndFlush(instantiationSuggestion);

    // Get all the instantiationSuggestionList
    restInstantiationSuggestionMockMvc
        .perform(get("/api/instantiation-suggestions?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(instantiationSuggestion.getId().intValue())));
  }

  @Test
  @Transactional
  public void getInstantiationSuggestion() throws Exception {
    // Initialize the database
    instantiationSuggestionRepository.saveAndFlush(instantiationSuggestion);

    // Get the instantiationSuggestion
    restInstantiationSuggestionMockMvc
        .perform(get("/api/instantiation-suggestions/{id}", instantiationSuggestion.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(instantiationSuggestion.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingInstantiationSuggestion() throws Exception {
    // Get the instantiationSuggestion
    restInstantiationSuggestionMockMvc
        .perform(get("/api/instantiation-suggestions/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateInstantiationSuggestion() throws Exception {
    // Initialize the database
    instantiationSuggestionRepository.saveAndFlush(instantiationSuggestion);

    int databaseSizeBeforeUpdate = instantiationSuggestionRepository.findAll().size();

    // Update the instantiationSuggestion
    InstantiationSuggestion updatedInstantiationSuggestion =
        instantiationSuggestionRepository.findById(instantiationSuggestion.getId()).get();
    // Disconnect from session so that the updates on updatedInstantiationSuggestion are not
    // directly saved in db
    em.detach(updatedInstantiationSuggestion);

    restInstantiationSuggestionMockMvc
        .perform(
            put("/api/instantiation-suggestions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstantiationSuggestion)))
        .andExpect(status().isOk());

    // Validate the InstantiationSuggestion in the database
    List<InstantiationSuggestion> instantiationSuggestionList =
        instantiationSuggestionRepository.findAll();
    assertThat(instantiationSuggestionList).hasSize(databaseSizeBeforeUpdate);
    InstantiationSuggestion testInstantiationSuggestion =
        instantiationSuggestionList.get(instantiationSuggestionList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingInstantiationSuggestion() throws Exception {
    int databaseSizeBeforeUpdate = instantiationSuggestionRepository.findAll().size();

    // Create the InstantiationSuggestion

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restInstantiationSuggestionMockMvc
        .perform(
            put("/api/instantiation-suggestions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(instantiationSuggestion)))
        .andExpect(status().isBadRequest());

    // Validate the InstantiationSuggestion in the database
    List<InstantiationSuggestion> instantiationSuggestionList =
        instantiationSuggestionRepository.findAll();
    assertThat(instantiationSuggestionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteInstantiationSuggestion() throws Exception {
    // Initialize the database
    instantiationSuggestionRepository.saveAndFlush(instantiationSuggestion);

    int databaseSizeBeforeDelete = instantiationSuggestionRepository.findAll().size();

    // Delete the instantiationSuggestion
    restInstantiationSuggestionMockMvc
        .perform(
            delete("/api/instantiation-suggestions/{id}", instantiationSuggestion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<InstantiationSuggestion> instantiationSuggestionList =
        instantiationSuggestionRepository.findAll();
    assertThat(instantiationSuggestionList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(InstantiationSuggestion.class);
    InstantiationSuggestion instantiationSuggestion1 = new InstantiationSuggestion();
    instantiationSuggestion1.setId(1L);
    InstantiationSuggestion instantiationSuggestion2 = new InstantiationSuggestion();
    instantiationSuggestion2.setId(instantiationSuggestion1.getId());
    assertThat(instantiationSuggestion1).isEqualTo(instantiationSuggestion2);
    instantiationSuggestion2.setId(2L);
    assertThat(instantiationSuggestion1).isNotEqualTo(instantiationSuggestion2);
    instantiationSuggestion1.setId(null);
    assertThat(instantiationSuggestion1).isNotEqualTo(instantiationSuggestion2);
  }
}
