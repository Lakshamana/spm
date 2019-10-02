package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ArtifactType;
import br.ufpa.labes.spm.repository.ArtifactTypeRepository;
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

/** Integration tests for the {@link ArtifactTypeResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ArtifactTypeResourceIT {

  @Autowired private ArtifactTypeRepository artifactTypeRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restArtifactTypeMockMvc;

  private ArtifactType artifactType;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ArtifactTypeResource artifactTypeResource =
        new ArtifactTypeResource(artifactTypeRepository);
    this.restArtifactTypeMockMvc =
        MockMvcBuilders.standaloneSetup(artifactTypeResource)
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
  public static ArtifactType createEntity(EntityManager em) {
    ArtifactType artifactType = new ArtifactType();
    return artifactType;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ArtifactType createUpdatedEntity(EntityManager em) {
    ArtifactType artifactType = new ArtifactType();
    return artifactType;
  }

  @BeforeEach
  public void initTest() {
    artifactType = createEntity(em);
  }

  @Test
  @Transactional
  public void createArtifactType() throws Exception {
    int databaseSizeBeforeCreate = artifactTypeRepository.findAll().size();

    // Create the ArtifactType
    restArtifactTypeMockMvc
        .perform(
            post("/api/artifact-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artifactType)))
        .andExpect(status().isCreated());

    // Validate the ArtifactType in the database
    List<ArtifactType> artifactTypeList = artifactTypeRepository.findAll();
    assertThat(artifactTypeList).hasSize(databaseSizeBeforeCreate + 1);
    ArtifactType testArtifactType = artifactTypeList.get(artifactTypeList.size() - 1);
  }

  @Test
  @Transactional
  public void createArtifactTypeWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = artifactTypeRepository.findAll().size();

    // Create the ArtifactType with an existing ID
    artifactType.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restArtifactTypeMockMvc
        .perform(
            post("/api/artifact-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artifactType)))
        .andExpect(status().isBadRequest());

    // Validate the ArtifactType in the database
    List<ArtifactType> artifactTypeList = artifactTypeRepository.findAll();
    assertThat(artifactTypeList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllArtifactTypes() throws Exception {
    // Initialize the database
    artifactTypeRepository.saveAndFlush(artifactType);

    // Get all the artifactTypeList
    restArtifactTypeMockMvc
        .perform(get("/api/artifact-types?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(artifactType.getId().intValue())));
  }

  @Test
  @Transactional
  public void getArtifactType() throws Exception {
    // Initialize the database
    artifactTypeRepository.saveAndFlush(artifactType);

    // Get the artifactType
    restArtifactTypeMockMvc
        .perform(get("/api/artifact-types/{id}", artifactType.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(artifactType.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingArtifactType() throws Exception {
    // Get the artifactType
    restArtifactTypeMockMvc
        .perform(get("/api/artifact-types/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateArtifactType() throws Exception {
    // Initialize the database
    artifactTypeRepository.saveAndFlush(artifactType);

    int databaseSizeBeforeUpdate = artifactTypeRepository.findAll().size();

    // Update the artifactType
    ArtifactType updatedArtifactType = artifactTypeRepository.findById(artifactType.getId()).get();
    // Disconnect from session so that the updates on updatedArtifactType are not directly saved in
    // db
    em.detach(updatedArtifactType);

    restArtifactTypeMockMvc
        .perform(
            put("/api/artifact-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedArtifactType)))
        .andExpect(status().isOk());

    // Validate the ArtifactType in the database
    List<ArtifactType> artifactTypeList = artifactTypeRepository.findAll();
    assertThat(artifactTypeList).hasSize(databaseSizeBeforeUpdate);
    ArtifactType testArtifactType = artifactTypeList.get(artifactTypeList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingArtifactType() throws Exception {
    int databaseSizeBeforeUpdate = artifactTypeRepository.findAll().size();

    // Create the ArtifactType

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restArtifactTypeMockMvc
        .perform(
            put("/api/artifact-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artifactType)))
        .andExpect(status().isBadRequest());

    // Validate the ArtifactType in the database
    List<ArtifactType> artifactTypeList = artifactTypeRepository.findAll();
    assertThat(artifactTypeList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteArtifactType() throws Exception {
    // Initialize the database
    artifactTypeRepository.saveAndFlush(artifactType);

    int databaseSizeBeforeDelete = artifactTypeRepository.findAll().size();

    // Delete the artifactType
    restArtifactTypeMockMvc
        .perform(
            delete("/api/artifact-types/{id}", artifactType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ArtifactType> artifactTypeList = artifactTypeRepository.findAll();
    assertThat(artifactTypeList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ArtifactType.class);
    ArtifactType artifactType1 = new ArtifactType();
    artifactType1.setId(1L);
    ArtifactType artifactType2 = new ArtifactType();
    artifactType2.setId(artifactType1.getId());
    assertThat(artifactType1).isEqualTo(artifactType2);
    artifactType2.setId(2L);
    assertThat(artifactType1).isNotEqualTo(artifactType2);
    artifactType1.setId(null);
    assertThat(artifactType1).isNotEqualTo(artifactType2);
  }
}
