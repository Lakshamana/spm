package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Sequence;
import br.ufpa.labes.spm.repository.SequenceRepository;
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

/** Integration tests for the {@link SequenceResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class SequenceResourceIT {

  @Autowired private SequenceRepository sequenceRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restSequenceMockMvc;

  private Sequence sequence;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final SequenceResource sequenceResource = new SequenceResource(sequenceRepository);
    this.restSequenceMockMvc =
        MockMvcBuilders.standaloneSetup(sequenceResource)
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
  public static Sequence createEntity(EntityManager em) {
    Sequence sequence = new Sequence();
    return sequence;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Sequence createUpdatedEntity(EntityManager em) {
    Sequence sequence = new Sequence();
    return sequence;
  }

  @BeforeEach
  public void initTest() {
    sequence = createEntity(em);
  }

  @Test
  @Transactional
  public void createSequence() throws Exception {
    int databaseSizeBeforeCreate = sequenceRepository.findAll().size();

    // Create the Sequence
    restSequenceMockMvc
        .perform(
            post("/api/sequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sequence)))
        .andExpect(status().isCreated());

    // Validate the Sequence in the database
    List<Sequence> sequenceList = sequenceRepository.findAll();
    assertThat(sequenceList).hasSize(databaseSizeBeforeCreate + 1);
    Sequence testSequence = sequenceList.get(sequenceList.size() - 1);
  }

  @Test
  @Transactional
  public void createSequenceWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = sequenceRepository.findAll().size();

    // Create the Sequence with an existing ID
    sequence.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restSequenceMockMvc
        .perform(
            post("/api/sequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sequence)))
        .andExpect(status().isBadRequest());

    // Validate the Sequence in the database
    List<Sequence> sequenceList = sequenceRepository.findAll();
    assertThat(sequenceList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllSequences() throws Exception {
    // Initialize the database
    sequenceRepository.saveAndFlush(sequence);

    // Get all the sequenceList
    restSequenceMockMvc
        .perform(get("/api/sequences?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(sequence.getId().intValue())));
  }

  @Test
  @Transactional
  public void getSequence() throws Exception {
    // Initialize the database
    sequenceRepository.saveAndFlush(sequence);

    // Get the sequence
    restSequenceMockMvc
        .perform(get("/api/sequences/{id}", sequence.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(sequence.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingSequence() throws Exception {
    // Get the sequence
    restSequenceMockMvc
        .perform(get("/api/sequences/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateSequence() throws Exception {
    // Initialize the database
    sequenceRepository.saveAndFlush(sequence);

    int databaseSizeBeforeUpdate = sequenceRepository.findAll().size();

    // Update the sequence
    Sequence updatedSequence = sequenceRepository.findById(sequence.getId()).get();
    // Disconnect from session so that the updates on updatedSequence are not directly saved in db
    em.detach(updatedSequence);

    restSequenceMockMvc
        .perform(
            put("/api/sequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSequence)))
        .andExpect(status().isOk());

    // Validate the Sequence in the database
    List<Sequence> sequenceList = sequenceRepository.findAll();
    assertThat(sequenceList).hasSize(databaseSizeBeforeUpdate);
    Sequence testSequence = sequenceList.get(sequenceList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingSequence() throws Exception {
    int databaseSizeBeforeUpdate = sequenceRepository.findAll().size();

    // Create the Sequence

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restSequenceMockMvc
        .perform(
            put("/api/sequences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sequence)))
        .andExpect(status().isBadRequest());

    // Validate the Sequence in the database
    List<Sequence> sequenceList = sequenceRepository.findAll();
    assertThat(sequenceList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteSequence() throws Exception {
    // Initialize the database
    sequenceRepository.saveAndFlush(sequence);

    int databaseSizeBeforeDelete = sequenceRepository.findAll().size();

    // Delete the sequence
    restSequenceMockMvc
        .perform(
            delete("/api/sequences/{id}", sequence.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Sequence> sequenceList = sequenceRepository.findAll();
    assertThat(sequenceList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Sequence.class);
    Sequence sequence1 = new Sequence();
    sequence1.setId(1L);
    Sequence sequence2 = new Sequence();
    sequence2.setId(sequence1.getId());
    assertThat(sequence1).isEqualTo(sequence2);
    sequence2.setId(2L);
    assertThat(sequence1).isNotEqualTo(sequence2);
    sequence1.setId(null);
    assertThat(sequence1).isNotEqualTo(sequence2);
  }
}
