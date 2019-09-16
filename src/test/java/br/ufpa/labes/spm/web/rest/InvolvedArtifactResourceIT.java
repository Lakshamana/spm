package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.InvolvedArtifact;
import br.ufpa.labes.spm.repository.InvolvedArtifactRepository;
import br.ufpa.labes.spm.service.InvolvedArtifactService;
import br.ufpa.labes.spm.service.dto.InvolvedArtifactDTO;
import br.ufpa.labes.spm.service.mapper.InvolvedArtifactMapper;
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

/** Integration tests for the {@link InvolvedArtifactResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class InvolvedArtifactResourceIT {

  @Autowired private InvolvedArtifactRepository involvedArtifactRepository;

  @Autowired private InvolvedArtifactMapper involvedArtifactMapper;

  @Autowired private InvolvedArtifactService involvedArtifactService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restInvolvedArtifactMockMvc;

  private InvolvedArtifact involvedArtifact;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final InvolvedArtifactResource involvedArtifactResource =
        new InvolvedArtifactResource(involvedArtifactService);
    this.restInvolvedArtifactMockMvc =
        MockMvcBuilders.standaloneSetup(involvedArtifactResource)
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
  public static InvolvedArtifact createEntity(EntityManager em) {
    InvolvedArtifact involvedArtifact = new InvolvedArtifact();
    return involvedArtifact;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static InvolvedArtifact createUpdatedEntity(EntityManager em) {
    InvolvedArtifact involvedArtifact = new InvolvedArtifact();
    return involvedArtifact;
  }

  @BeforeEach
  public void initTest() {
    involvedArtifact = createEntity(em);
  }

  @Test
  @Transactional
  public void createInvolvedArtifact() throws Exception {
    int databaseSizeBeforeCreate = involvedArtifactRepository.findAll().size();

    // Create the InvolvedArtifact
    InvolvedArtifactDTO involvedArtifactDTO = involvedArtifactMapper.toDto(involvedArtifact);
    restInvolvedArtifactMockMvc
        .perform(
            post("/api/involved-artifacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(involvedArtifactDTO)))
        .andExpect(status().isCreated());

    // Validate the InvolvedArtifact in the database
    List<InvolvedArtifact> involvedArtifactList = involvedArtifactRepository.findAll();
    assertThat(involvedArtifactList).hasSize(databaseSizeBeforeCreate + 1);
    InvolvedArtifact testInvolvedArtifact =
        involvedArtifactList.get(involvedArtifactList.size() - 1);
  }

  @Test
  @Transactional
  public void createInvolvedArtifactWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = involvedArtifactRepository.findAll().size();

    // Create the InvolvedArtifact with an existing ID
    involvedArtifact.setId(1L);
    InvolvedArtifactDTO involvedArtifactDTO = involvedArtifactMapper.toDto(involvedArtifact);

    // An entity with an existing ID cannot be created, so this API call must fail
    restInvolvedArtifactMockMvc
        .perform(
            post("/api/involved-artifacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(involvedArtifactDTO)))
        .andExpect(status().isBadRequest());

    // Validate the InvolvedArtifact in the database
    List<InvolvedArtifact> involvedArtifactList = involvedArtifactRepository.findAll();
    assertThat(involvedArtifactList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllInvolvedArtifacts() throws Exception {
    // Initialize the database
    involvedArtifactRepository.saveAndFlush(involvedArtifact);

    // Get all the involvedArtifactList
    restInvolvedArtifactMockMvc
        .perform(get("/api/involved-artifacts?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(involvedArtifact.getId().intValue())));
  }

  @Test
  @Transactional
  public void getInvolvedArtifact() throws Exception {
    // Initialize the database
    involvedArtifactRepository.saveAndFlush(involvedArtifact);

    // Get the involvedArtifact
    restInvolvedArtifactMockMvc
        .perform(get("/api/involved-artifacts/{id}", involvedArtifact.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(involvedArtifact.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingInvolvedArtifact() throws Exception {
    // Get the involvedArtifact
    restInvolvedArtifactMockMvc
        .perform(get("/api/involved-artifacts/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateInvolvedArtifact() throws Exception {
    // Initialize the database
    involvedArtifactRepository.saveAndFlush(involvedArtifact);

    int databaseSizeBeforeUpdate = involvedArtifactRepository.findAll().size();

    // Update the involvedArtifact
    InvolvedArtifact updatedInvolvedArtifact =
        involvedArtifactRepository.findById(involvedArtifact.getId()).get();
    // Disconnect from session so that the updates on updatedInvolvedArtifact are not directly saved
    // in db
    em.detach(updatedInvolvedArtifact);
    InvolvedArtifactDTO involvedArtifactDTO = involvedArtifactMapper.toDto(updatedInvolvedArtifact);

    restInvolvedArtifactMockMvc
        .perform(
            put("/api/involved-artifacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(involvedArtifactDTO)))
        .andExpect(status().isOk());

    // Validate the InvolvedArtifact in the database
    List<InvolvedArtifact> involvedArtifactList = involvedArtifactRepository.findAll();
    assertThat(involvedArtifactList).hasSize(databaseSizeBeforeUpdate);
    InvolvedArtifact testInvolvedArtifact =
        involvedArtifactList.get(involvedArtifactList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingInvolvedArtifact() throws Exception {
    int databaseSizeBeforeUpdate = involvedArtifactRepository.findAll().size();

    // Create the InvolvedArtifact
    InvolvedArtifactDTO involvedArtifactDTO = involvedArtifactMapper.toDto(involvedArtifact);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restInvolvedArtifactMockMvc
        .perform(
            put("/api/involved-artifacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(involvedArtifactDTO)))
        .andExpect(status().isBadRequest());

    // Validate the InvolvedArtifact in the database
    List<InvolvedArtifact> involvedArtifactList = involvedArtifactRepository.findAll();
    assertThat(involvedArtifactList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteInvolvedArtifact() throws Exception {
    // Initialize the database
    involvedArtifactRepository.saveAndFlush(involvedArtifact);

    int databaseSizeBeforeDelete = involvedArtifactRepository.findAll().size();

    // Delete the involvedArtifact
    restInvolvedArtifactMockMvc
        .perform(
            delete("/api/involved-artifacts/{id}", involvedArtifact.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<InvolvedArtifact> involvedArtifactList = involvedArtifactRepository.findAll();
    assertThat(involvedArtifactList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(InvolvedArtifact.class);
    InvolvedArtifact involvedArtifact1 = new InvolvedArtifact();
    involvedArtifact1.setId(1L);
    InvolvedArtifact involvedArtifact2 = new InvolvedArtifact();
    involvedArtifact2.setId(involvedArtifact1.getId());
    assertThat(involvedArtifact1).isEqualTo(involvedArtifact2);
    involvedArtifact2.setId(2L);
    assertThat(involvedArtifact1).isNotEqualTo(involvedArtifact2);
    involvedArtifact1.setId(null);
    assertThat(involvedArtifact1).isNotEqualTo(involvedArtifact2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(InvolvedArtifactDTO.class);
    InvolvedArtifactDTO involvedArtifactDTO1 = new InvolvedArtifactDTO();
    involvedArtifactDTO1.setId(1L);
    InvolvedArtifactDTO involvedArtifactDTO2 = new InvolvedArtifactDTO();
    assertThat(involvedArtifactDTO1).isNotEqualTo(involvedArtifactDTO2);
    involvedArtifactDTO2.setId(involvedArtifactDTO1.getId());
    assertThat(involvedArtifactDTO1).isEqualTo(involvedArtifactDTO2);
    involvedArtifactDTO2.setId(2L);
    assertThat(involvedArtifactDTO1).isNotEqualTo(involvedArtifactDTO2);
    involvedArtifactDTO1.setId(null);
    assertThat(involvedArtifactDTO1).isNotEqualTo(involvedArtifactDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(involvedArtifactMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(involvedArtifactMapper.fromId(null)).isNull();
  }
}
