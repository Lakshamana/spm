package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Shareable;
import br.ufpa.labes.spm.repository.ShareableRepository;
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

/** Integration tests for the {@link ShareableResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ShareableResourceIT {

  private static final String DEFAULT_STATE = "AAAAAAAAAA";
  private static final String UPDATED_STATE = "BBBBBBBBBB";

  private static final String DEFAULT_UNIT_OF_COST = "AAAAAAAAAA";
  private static final String UPDATED_UNIT_OF_COST = "BBBBBBBBBB";

  @Autowired private ShareableRepository shareableRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restShareableMockMvc;

  private Shareable shareable;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ShareableResource shareableResource = new ShareableResource(shareableRepository);
    this.restShareableMockMvc =
        MockMvcBuilders.standaloneSetup(shareableResource)
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
  public static Shareable createEntity(EntityManager em) {
    Shareable shareable = new Shareable().state(DEFAULT_STATE).unitOfCost(DEFAULT_UNIT_OF_COST);
    return shareable;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Shareable createUpdatedEntity(EntityManager em) {
    Shareable shareable = new Shareable().state(UPDATED_STATE).unitOfCost(UPDATED_UNIT_OF_COST);
    return shareable;
  }

  @BeforeEach
  public void initTest() {
    shareable = createEntity(em);
  }

  @Test
  @Transactional
  public void createShareable() throws Exception {
    int databaseSizeBeforeCreate = shareableRepository.findAll().size();

    // Create the Shareable
    restShareableMockMvc
        .perform(
            post("/api/shareables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shareable)))
        .andExpect(status().isCreated());

    // Validate the Shareable in the database
    List<Shareable> shareableList = shareableRepository.findAll();
    assertThat(shareableList).hasSize(databaseSizeBeforeCreate + 1);
    Shareable testShareable = shareableList.get(shareableList.size() - 1);
    assertThat(testShareable.getState()).isEqualTo(DEFAULT_STATE);
    assertThat(testShareable.getUnitOfCost()).isEqualTo(DEFAULT_UNIT_OF_COST);
  }

  @Test
  @Transactional
  public void createShareableWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = shareableRepository.findAll().size();

    // Create the Shareable with an existing ID
    shareable.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restShareableMockMvc
        .perform(
            post("/api/shareables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shareable)))
        .andExpect(status().isBadRequest());

    // Validate the Shareable in the database
    List<Shareable> shareableList = shareableRepository.findAll();
    assertThat(shareableList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllShareables() throws Exception {
    // Initialize the database
    shareableRepository.saveAndFlush(shareable);

    // Get all the shareableList
    restShareableMockMvc
        .perform(get("/api/shareables?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(shareable.getId().intValue())))
        .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
        .andExpect(jsonPath("$.[*].unitOfCost").value(hasItem(DEFAULT_UNIT_OF_COST.toString())));
  }

  @Test
  @Transactional
  public void getShareable() throws Exception {
    // Initialize the database
    shareableRepository.saveAndFlush(shareable);

    // Get the shareable
    restShareableMockMvc
        .perform(get("/api/shareables/{id}", shareable.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(shareable.getId().intValue()))
        .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
        .andExpect(jsonPath("$.unitOfCost").value(DEFAULT_UNIT_OF_COST.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingShareable() throws Exception {
    // Get the shareable
    restShareableMockMvc
        .perform(get("/api/shareables/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateShareable() throws Exception {
    // Initialize the database
    shareableRepository.saveAndFlush(shareable);

    int databaseSizeBeforeUpdate = shareableRepository.findAll().size();

    // Update the shareable
    Shareable updatedShareable = shareableRepository.findById(shareable.getId()).get();
    // Disconnect from session so that the updates on updatedShareable are not directly saved in db
    em.detach(updatedShareable);
    updatedShareable.state(UPDATED_STATE).unitOfCost(UPDATED_UNIT_OF_COST);

    restShareableMockMvc
        .perform(
            put("/api/shareables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedShareable)))
        .andExpect(status().isOk());

    // Validate the Shareable in the database
    List<Shareable> shareableList = shareableRepository.findAll();
    assertThat(shareableList).hasSize(databaseSizeBeforeUpdate);
    Shareable testShareable = shareableList.get(shareableList.size() - 1);
    assertThat(testShareable.getState()).isEqualTo(UPDATED_STATE);
    assertThat(testShareable.getUnitOfCost()).isEqualTo(UPDATED_UNIT_OF_COST);
  }

  @Test
  @Transactional
  public void updateNonExistingShareable() throws Exception {
    int databaseSizeBeforeUpdate = shareableRepository.findAll().size();

    // Create the Shareable

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restShareableMockMvc
        .perform(
            put("/api/shareables")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(shareable)))
        .andExpect(status().isBadRequest());

    // Validate the Shareable in the database
    List<Shareable> shareableList = shareableRepository.findAll();
    assertThat(shareableList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteShareable() throws Exception {
    // Initialize the database
    shareableRepository.saveAndFlush(shareable);

    int databaseSizeBeforeDelete = shareableRepository.findAll().size();

    // Delete the shareable
    restShareableMockMvc
        .perform(
            delete("/api/shareables/{id}", shareable.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Shareable> shareableList = shareableRepository.findAll();
    assertThat(shareableList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Shareable.class);
    Shareable shareable1 = new Shareable();
    shareable1.setId(1L);
    Shareable shareable2 = new Shareable();
    shareable2.setId(shareable1.getId());
    assertThat(shareable1).isEqualTo(shareable2);
    shareable2.setId(2L);
    assertThat(shareable1).isNotEqualTo(shareable2);
    shareable1.setId(null);
    assertThat(shareable1).isNotEqualTo(shareable2);
  }
}
