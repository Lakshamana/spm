package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ArtifactCon;
import br.ufpa.labes.spm.repository.ArtifactConRepository;
import br.ufpa.labes.spm.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static br.ufpa.labes.spm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Integration tests for the {@link ArtifactConResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ArtifactConResourceIT {

  @Autowired private ArtifactConRepository artifactConRepository;

  @Mock private ArtifactConRepository artifactConRepositoryMock;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restArtifactConMockMvc;

  private ArtifactCon artifactCon;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ArtifactConResource artifactConResource = new ArtifactConResource(artifactConRepository);
    this.restArtifactConMockMvc =
        MockMvcBuilders.standaloneSetup(artifactConResource)
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
  public static ArtifactCon createEntity(EntityManager em) {
    ArtifactCon artifactCon = new ArtifactCon();
    return artifactCon;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ArtifactCon createUpdatedEntity(EntityManager em) {
    ArtifactCon artifactCon = new ArtifactCon();
    return artifactCon;
  }

  @BeforeEach
  public void initTest() {
    artifactCon = createEntity(em);
  }

  @Test
  @Transactional
  public void createArtifactCon() throws Exception {
    int databaseSizeBeforeCreate = artifactConRepository.findAll().size();

    // Create the ArtifactCon
    restArtifactConMockMvc
        .perform(
            post("/api/artifact-cons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artifactCon)))
        .andExpect(status().isCreated());

    // Validate the ArtifactCon in the database
    List<ArtifactCon> artifactConList = artifactConRepository.findAll();
    assertThat(artifactConList).hasSize(databaseSizeBeforeCreate + 1);
    ArtifactCon testArtifactCon = artifactConList.get(artifactConList.size() - 1);
  }

  @Test
  @Transactional
  public void createArtifactConWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = artifactConRepository.findAll().size();

    // Create the ArtifactCon with an existing ID
    artifactCon.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restArtifactConMockMvc
        .perform(
            post("/api/artifact-cons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artifactCon)))
        .andExpect(status().isBadRequest());

    // Validate the ArtifactCon in the database
    List<ArtifactCon> artifactConList = artifactConRepository.findAll();
    assertThat(artifactConList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllArtifactCons() throws Exception {
    // Initialize the database
    artifactConRepository.saveAndFlush(artifactCon);

    // Get all the artifactConList
    restArtifactConMockMvc
        .perform(get("/api/artifact-cons?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(artifactCon.getId().intValue())));
  }

  @SuppressWarnings({"unchecked"})
  public void getAllArtifactConsWithEagerRelationshipsIsEnabled() throws Exception {
    ArtifactConResource artifactConResource = new ArtifactConResource(artifactConRepositoryMock);
    when(artifactConRepositoryMock.findAllWithEagerRelationships(any()))
        .thenReturn(new PageImpl(new ArrayList<>()));

    MockMvc restArtifactConMockMvc =
        MockMvcBuilders.standaloneSetup(artifactConResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .build();

    restArtifactConMockMvc
        .perform(get("/api/artifact-cons?eagerload=true"))
        .andExpect(status().isOk());

    verify(artifactConRepositoryMock, times(1)).findAllWithEagerRelationships(any());
  }

  @SuppressWarnings({"unchecked"})
  public void getAllArtifactConsWithEagerRelationshipsIsNotEnabled() throws Exception {
    ArtifactConResource artifactConResource = new ArtifactConResource(artifactConRepositoryMock);
    when(artifactConRepositoryMock.findAllWithEagerRelationships(any()))
        .thenReturn(new PageImpl(new ArrayList<>()));
    MockMvc restArtifactConMockMvc =
        MockMvcBuilders.standaloneSetup(artifactConResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .build();

    restArtifactConMockMvc
        .perform(get("/api/artifact-cons?eagerload=true"))
        .andExpect(status().isOk());

    verify(artifactConRepositoryMock, times(1)).findAllWithEagerRelationships(any());
  }

  @Test
  @Transactional
  public void getArtifactCon() throws Exception {
    // Initialize the database
    artifactConRepository.saveAndFlush(artifactCon);

    // Get the artifactCon
    restArtifactConMockMvc
        .perform(get("/api/artifact-cons/{id}", artifactCon.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(artifactCon.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingArtifactCon() throws Exception {
    // Get the artifactCon
    restArtifactConMockMvc
        .perform(get("/api/artifact-cons/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateArtifactCon() throws Exception {
    // Initialize the database
    artifactConRepository.saveAndFlush(artifactCon);

    int databaseSizeBeforeUpdate = artifactConRepository.findAll().size();

    // Update the artifactCon
    ArtifactCon updatedArtifactCon = artifactConRepository.findById(artifactCon.getId()).get();
    // Disconnect from session so that the updates on updatedArtifactCon are not directly saved in
    // db
    em.detach(updatedArtifactCon);

    restArtifactConMockMvc
        .perform(
            put("/api/artifact-cons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedArtifactCon)))
        .andExpect(status().isOk());

    // Validate the ArtifactCon in the database
    List<ArtifactCon> artifactConList = artifactConRepository.findAll();
    assertThat(artifactConList).hasSize(databaseSizeBeforeUpdate);
    ArtifactCon testArtifactCon = artifactConList.get(artifactConList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingArtifactCon() throws Exception {
    int databaseSizeBeforeUpdate = artifactConRepository.findAll().size();

    // Create the ArtifactCon

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restArtifactConMockMvc
        .perform(
            put("/api/artifact-cons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(artifactCon)))
        .andExpect(status().isBadRequest());

    // Validate the ArtifactCon in the database
    List<ArtifactCon> artifactConList = artifactConRepository.findAll();
    assertThat(artifactConList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteArtifactCon() throws Exception {
    // Initialize the database
    artifactConRepository.saveAndFlush(artifactCon);

    int databaseSizeBeforeDelete = artifactConRepository.findAll().size();

    // Delete the artifactCon
    restArtifactConMockMvc
        .perform(
            delete("/api/artifact-cons/{id}", artifactCon.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ArtifactCon> artifactConList = artifactConRepository.findAll();
    assertThat(artifactConList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ArtifactCon.class);
    ArtifactCon artifactCon1 = new ArtifactCon();
    artifactCon1.setId(1L);
    ArtifactCon artifactCon2 = new ArtifactCon();
    artifactCon2.setId(artifactCon1.getId());
    assertThat(artifactCon1).isEqualTo(artifactCon2);
    artifactCon2.setId(2L);
    assertThat(artifactCon1).isNotEqualTo(artifactCon2);
    artifactCon1.setId(null);
    assertThat(artifactCon1).isNotEqualTo(artifactCon2);
  }
}
