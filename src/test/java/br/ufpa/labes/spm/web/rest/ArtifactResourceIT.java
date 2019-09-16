package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.repository.ArtifactRepository;
import br.ufpa.labes.spm.service.ArtifactService;
import br.ufpa.labes.spm.service.dto.ArtifactDTO;
import br.ufpa.labes.spm.service.mapper.ArtifactMapper;
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

/** Integration tests for the {@link ArtifactResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ArtifactResourceIT {

  private static final String DEFAULT_IDENT = "AAAAAAAAAA";
  private static final String UPDATED_IDENT = "BBBBBBBBBB";

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  private static final String DEFAULT_PATH_NAME = "AAAAAAAAAA";
  private static final String UPDATED_PATH_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
  private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_LATEST_VERSION = "AAAAAAAAAA";
  private static final String UPDATED_LATEST_VERSION = "BBBBBBBBBB";

  private static final Boolean DEFAULT_IS_TEMPLATE = false;
  private static final Boolean UPDATED_IS_TEMPLATE = true;

  private static final Boolean DEFAULT_IS_ACTIVE = false;
  private static final Boolean UPDATED_IS_ACTIVE = true;

  @Autowired private ArtifactRepository artifactRepository;

  @Autowired private ArtifactMapper artifactMapper;

  @Autowired private ArtifactService artifactService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restArtifactMockMvc;

  private Artifact artifact;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ArtifactResource artifactResource = new ArtifactResource(artifactService);
    this.restArtifactMockMvc =
        MockMvcBuilders.standaloneSetup(artifactResource)
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
  public static Artifact createEntity(EntityManager em) {
    Artifact artifact =
        new Artifact()
            .ident(DEFAULT_IDENT)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .pathName(DEFAULT_PATH_NAME)
            .fileName(DEFAULT_FILE_NAME)
            .latestVersion(DEFAULT_LATEST_VERSION)
            .isTemplate(DEFAULT_IS_TEMPLATE)
            .isActive(DEFAULT_IS_ACTIVE);
    return artifact;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Artifact createUpdatedEntity(EntityManager em) {
    Artifact artifact =
        new Artifact()
            .ident(UPDATED_IDENT)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .pathName(UPDATED_PATH_NAME)
            .fileName(UPDATED_FILE_NAME)
            .latestVersion(UPDATED_LATEST_VERSION)
            .isTemplate(UPDATED_IS_TEMPLATE)
            .isActive(UPDATED_IS_ACTIVE);
    return artifact;
  }

  @BeforeEach
  public void initTest() {
    artifact = createEntity(em);
  }

  @Test
  @Transactional
  public void createArtifact() throws Exception {
    int databaseSizeBeforeCreate = artifactRepository.findAll().size();

    // Create the Artifact
    ArtifactDTO artifactDTO = artifactMapper.toDto(artifact);
    restArtifactMockMvc
        .perform(
            post("/api/artifacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artifactDTO)))
        .andExpect(status().isCreated());

    // Validate the Artifact in the database
    List<Artifact> artifactList = artifactRepository.findAll();
    assertThat(artifactList).hasSize(databaseSizeBeforeCreate + 1);
    Artifact testArtifact = artifactList.get(artifactList.size() - 1);
    assertThat(testArtifact.getIdent()).isEqualTo(DEFAULT_IDENT);
    assertThat(testArtifact.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testArtifact.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testArtifact.getPathName()).isEqualTo(DEFAULT_PATH_NAME);
    assertThat(testArtifact.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
    assertThat(testArtifact.getLatestVersion()).isEqualTo(DEFAULT_LATEST_VERSION);
    assertThat(testArtifact.isIsTemplate()).isEqualTo(DEFAULT_IS_TEMPLATE);
    assertThat(testArtifact.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
  }

  @Test
  @Transactional
  public void createArtifactWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = artifactRepository.findAll().size();

    // Create the Artifact with an existing ID
    artifact.setId(1L);
    ArtifactDTO artifactDTO = artifactMapper.toDto(artifact);

    // An entity with an existing ID cannot be created, so this API call must fail
    restArtifactMockMvc
        .perform(
            post("/api/artifacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artifactDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Artifact in the database
    List<Artifact> artifactList = artifactRepository.findAll();
    assertThat(artifactList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllArtifacts() throws Exception {
    // Initialize the database
    artifactRepository.saveAndFlush(artifact);

    // Get all the artifactList
    restArtifactMockMvc
        .perform(get("/api/artifacts?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(artifact.getId().intValue())))
        .andExpect(jsonPath("$.[*].ident").value(hasItem(DEFAULT_IDENT.toString())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
        .andExpect(jsonPath("$.[*].pathName").value(hasItem(DEFAULT_PATH_NAME.toString())))
        .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
        .andExpect(
            jsonPath("$.[*].latestVersion").value(hasItem(DEFAULT_LATEST_VERSION.toString())))
        .andExpect(jsonPath("$.[*].isTemplate").value(hasItem(DEFAULT_IS_TEMPLATE.booleanValue())))
        .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
  }

  @Test
  @Transactional
  public void getArtifact() throws Exception {
    // Initialize the database
    artifactRepository.saveAndFlush(artifact);

    // Get the artifact
    restArtifactMockMvc
        .perform(get("/api/artifacts/{id}", artifact.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(artifact.getId().intValue()))
        .andExpect(jsonPath("$.ident").value(DEFAULT_IDENT.toString()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
        .andExpect(jsonPath("$.pathName").value(DEFAULT_PATH_NAME.toString()))
        .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
        .andExpect(jsonPath("$.latestVersion").value(DEFAULT_LATEST_VERSION.toString()))
        .andExpect(jsonPath("$.isTemplate").value(DEFAULT_IS_TEMPLATE.booleanValue()))
        .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
  }

  @Test
  @Transactional
  public void getNonExistingArtifact() throws Exception {
    // Get the artifact
    restArtifactMockMvc
        .perform(get("/api/artifacts/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateArtifact() throws Exception {
    // Initialize the database
    artifactRepository.saveAndFlush(artifact);

    int databaseSizeBeforeUpdate = artifactRepository.findAll().size();

    // Update the artifact
    Artifact updatedArtifact = artifactRepository.findById(artifact.getId()).get();
    // Disconnect from session so that the updates on updatedArtifact are not directly saved in db
    em.detach(updatedArtifact);
    updatedArtifact
        .ident(UPDATED_IDENT)
        .name(UPDATED_NAME)
        .description(UPDATED_DESCRIPTION)
        .pathName(UPDATED_PATH_NAME)
        .fileName(UPDATED_FILE_NAME)
        .latestVersion(UPDATED_LATEST_VERSION)
        .isTemplate(UPDATED_IS_TEMPLATE)
        .isActive(UPDATED_IS_ACTIVE);
    ArtifactDTO artifactDTO = artifactMapper.toDto(updatedArtifact);

    restArtifactMockMvc
        .perform(
            put("/api/artifacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artifactDTO)))
        .andExpect(status().isOk());

    // Validate the Artifact in the database
    List<Artifact> artifactList = artifactRepository.findAll();
    assertThat(artifactList).hasSize(databaseSizeBeforeUpdate);
    Artifact testArtifact = artifactList.get(artifactList.size() - 1);
    assertThat(testArtifact.getIdent()).isEqualTo(UPDATED_IDENT);
    assertThat(testArtifact.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testArtifact.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    assertThat(testArtifact.getPathName()).isEqualTo(UPDATED_PATH_NAME);
    assertThat(testArtifact.getFileName()).isEqualTo(UPDATED_FILE_NAME);
    assertThat(testArtifact.getLatestVersion()).isEqualTo(UPDATED_LATEST_VERSION);
    assertThat(testArtifact.isIsTemplate()).isEqualTo(UPDATED_IS_TEMPLATE);
    assertThat(testArtifact.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
  }

  @Test
  @Transactional
  public void updateNonExistingArtifact() throws Exception {
    int databaseSizeBeforeUpdate = artifactRepository.findAll().size();

    // Create the Artifact
    ArtifactDTO artifactDTO = artifactMapper.toDto(artifact);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restArtifactMockMvc
        .perform(
            put("/api/artifacts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artifactDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Artifact in the database
    List<Artifact> artifactList = artifactRepository.findAll();
    assertThat(artifactList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteArtifact() throws Exception {
    // Initialize the database
    artifactRepository.saveAndFlush(artifact);

    int databaseSizeBeforeDelete = artifactRepository.findAll().size();

    // Delete the artifact
    restArtifactMockMvc
        .perform(
            delete("/api/artifacts/{id}", artifact.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Artifact> artifactList = artifactRepository.findAll();
    assertThat(artifactList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Artifact.class);
    Artifact artifact1 = new Artifact();
    artifact1.setId(1L);
    Artifact artifact2 = new Artifact();
    artifact2.setId(artifact1.getId());
    assertThat(artifact1).isEqualTo(artifact2);
    artifact2.setId(2L);
    assertThat(artifact1).isNotEqualTo(artifact2);
    artifact1.setId(null);
    assertThat(artifact1).isNotEqualTo(artifact2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(ArtifactDTO.class);
    ArtifactDTO artifactDTO1 = new ArtifactDTO();
    artifactDTO1.setId(1L);
    ArtifactDTO artifactDTO2 = new ArtifactDTO();
    assertThat(artifactDTO1).isNotEqualTo(artifactDTO2);
    artifactDTO2.setId(artifactDTO1.getId());
    assertThat(artifactDTO1).isEqualTo(artifactDTO2);
    artifactDTO2.setId(2L);
    assertThat(artifactDTO1).isNotEqualTo(artifactDTO2);
    artifactDTO1.setId(null);
    assertThat(artifactDTO1).isNotEqualTo(artifactDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(artifactMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(artifactMapper.fromId(null)).isNull();
  }
}
