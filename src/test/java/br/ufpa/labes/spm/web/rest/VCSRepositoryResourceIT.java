package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.VCSRepository;
import br.ufpa.labes.spm.repository.VCSRepositoryRepository;
import br.ufpa.labes.spm.service.VCSRepositoryService;
import br.ufpa.labes.spm.service.dto.VCSRepositoryDTO;
import br.ufpa.labes.spm.service.mapper.VCSRepositoryMapper;
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

/** Integration tests for the {@link VCSRepositoryResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class VCSRepositoryResourceIT {

  private static final String DEFAULT_IDENT = "AAAAAAAAAA";
  private static final String UPDATED_IDENT = "BBBBBBBBBB";

  private static final String DEFAULT_CONTROL_VERSION_SYSTEM = "AAAAAAAAAA";
  private static final String UPDATED_CONTROL_VERSION_SYSTEM = "BBBBBBBBBB";

  private static final String DEFAULT_SERVER = "AAAAAAAAAA";
  private static final String UPDATED_SERVER = "BBBBBBBBBB";

  private static final String DEFAULT_REPOSITORY_PATH = "AAAAAAAAAA";
  private static final String UPDATED_REPOSITORY_PATH = "BBBBBBBBBB";

  @Autowired private VCSRepositoryRepository vCSRepositoryRepository;

  @Autowired private VCSRepositoryMapper vCSRepositoryMapper;

  @Autowired private VCSRepositoryService vCSRepositoryService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restVCSRepositoryMockMvc;

  private VCSRepository vCSRepository;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final VCSRepositoryResource vCSRepositoryResource =
        new VCSRepositoryResource(vCSRepositoryService);
    this.restVCSRepositoryMockMvc =
        MockMvcBuilders.standaloneSetup(vCSRepositoryResource)
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
  public static VCSRepository createEntity(EntityManager em) {
    VCSRepository vCSRepository =
        new VCSRepository()
            .ident(DEFAULT_IDENT)
            .controlVersionSystem(DEFAULT_CONTROL_VERSION_SYSTEM)
            .server(DEFAULT_SERVER)
            .repositoryPath(DEFAULT_REPOSITORY_PATH);
    return vCSRepository;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static VCSRepository createUpdatedEntity(EntityManager em) {
    VCSRepository vCSRepository =
        new VCSRepository()
            .ident(UPDATED_IDENT)
            .controlVersionSystem(UPDATED_CONTROL_VERSION_SYSTEM)
            .server(UPDATED_SERVER)
            .repositoryPath(UPDATED_REPOSITORY_PATH);
    return vCSRepository;
  }

  @BeforeEach
  public void initTest() {
    vCSRepository = createEntity(em);
  }

  @Test
  @Transactional
  public void createVCSRepository() throws Exception {
    int databaseSizeBeforeCreate = vCSRepositoryRepository.findAll().size();

    // Create the VCSRepository
    VCSRepositoryDTO vCSRepositoryDTO = vCSRepositoryMapper.toDto(vCSRepository);
    restVCSRepositoryMockMvc
        .perform(
            post("/api/vcs-repositories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vCSRepositoryDTO)))
        .andExpect(status().isCreated());

    // Validate the VCSRepository in the database
    List<VCSRepository> vCSRepositoryList = vCSRepositoryRepository.findAll();
    assertThat(vCSRepositoryList).hasSize(databaseSizeBeforeCreate + 1);
    VCSRepository testVCSRepository = vCSRepositoryList.get(vCSRepositoryList.size() - 1);
    assertThat(testVCSRepository.getIdent()).isEqualTo(DEFAULT_IDENT);
    assertThat(testVCSRepository.getControlVersionSystem())
        .isEqualTo(DEFAULT_CONTROL_VERSION_SYSTEM);
    assertThat(testVCSRepository.getServer()).isEqualTo(DEFAULT_SERVER);
    assertThat(testVCSRepository.getRepositoryPath()).isEqualTo(DEFAULT_REPOSITORY_PATH);
  }

  @Test
  @Transactional
  public void createVCSRepositoryWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = vCSRepositoryRepository.findAll().size();

    // Create the VCSRepository with an existing ID
    vCSRepository.setId(1L);
    VCSRepositoryDTO vCSRepositoryDTO = vCSRepositoryMapper.toDto(vCSRepository);

    // An entity with an existing ID cannot be created, so this API call must fail
    restVCSRepositoryMockMvc
        .perform(
            post("/api/vcs-repositories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vCSRepositoryDTO)))
        .andExpect(status().isBadRequest());

    // Validate the VCSRepository in the database
    List<VCSRepository> vCSRepositoryList = vCSRepositoryRepository.findAll();
    assertThat(vCSRepositoryList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllVCSRepositories() throws Exception {
    // Initialize the database
    vCSRepositoryRepository.saveAndFlush(vCSRepository);

    // Get all the vCSRepositoryList
    restVCSRepositoryMockMvc
        .perform(get("/api/vcs-repositories?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(vCSRepository.getId().intValue())))
        .andExpect(jsonPath("$.[*].ident").value(hasItem(DEFAULT_IDENT.toString())))
        .andExpect(
            jsonPath("$.[*].controlVersionSystem")
                .value(hasItem(DEFAULT_CONTROL_VERSION_SYSTEM.toString())))
        .andExpect(jsonPath("$.[*].server").value(hasItem(DEFAULT_SERVER.toString())))
        .andExpect(
            jsonPath("$.[*].repositoryPath").value(hasItem(DEFAULT_REPOSITORY_PATH.toString())));
  }

  @Test
  @Transactional
  public void getVCSRepository() throws Exception {
    // Initialize the database
    vCSRepositoryRepository.saveAndFlush(vCSRepository);

    // Get the vCSRepository
    restVCSRepositoryMockMvc
        .perform(get("/api/vcs-repositories/{id}", vCSRepository.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(vCSRepository.getId().intValue()))
        .andExpect(jsonPath("$.ident").value(DEFAULT_IDENT.toString()))
        .andExpect(
            jsonPath("$.controlVersionSystem").value(DEFAULT_CONTROL_VERSION_SYSTEM.toString()))
        .andExpect(jsonPath("$.server").value(DEFAULT_SERVER.toString()))
        .andExpect(jsonPath("$.repositoryPath").value(DEFAULT_REPOSITORY_PATH.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingVCSRepository() throws Exception {
    // Get the vCSRepository
    restVCSRepositoryMockMvc
        .perform(get("/api/vcs-repositories/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateVCSRepository() throws Exception {
    // Initialize the database
    vCSRepositoryRepository.saveAndFlush(vCSRepository);

    int databaseSizeBeforeUpdate = vCSRepositoryRepository.findAll().size();

    // Update the vCSRepository
    VCSRepository updatedVCSRepository =
        vCSRepositoryRepository.findById(vCSRepository.getId()).get();
    // Disconnect from session so that the updates on updatedVCSRepository are not directly saved in
    // db
    em.detach(updatedVCSRepository);
    updatedVCSRepository
        .ident(UPDATED_IDENT)
        .controlVersionSystem(UPDATED_CONTROL_VERSION_SYSTEM)
        .server(UPDATED_SERVER)
        .repositoryPath(UPDATED_REPOSITORY_PATH);
    VCSRepositoryDTO vCSRepositoryDTO = vCSRepositoryMapper.toDto(updatedVCSRepository);

    restVCSRepositoryMockMvc
        .perform(
            put("/api/vcs-repositories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vCSRepositoryDTO)))
        .andExpect(status().isOk());

    // Validate the VCSRepository in the database
    List<VCSRepository> vCSRepositoryList = vCSRepositoryRepository.findAll();
    assertThat(vCSRepositoryList).hasSize(databaseSizeBeforeUpdate);
    VCSRepository testVCSRepository = vCSRepositoryList.get(vCSRepositoryList.size() - 1);
    assertThat(testVCSRepository.getIdent()).isEqualTo(UPDATED_IDENT);
    assertThat(testVCSRepository.getControlVersionSystem())
        .isEqualTo(UPDATED_CONTROL_VERSION_SYSTEM);
    assertThat(testVCSRepository.getServer()).isEqualTo(UPDATED_SERVER);
    assertThat(testVCSRepository.getRepositoryPath()).isEqualTo(UPDATED_REPOSITORY_PATH);
  }

  @Test
  @Transactional
  public void updateNonExistingVCSRepository() throws Exception {
    int databaseSizeBeforeUpdate = vCSRepositoryRepository.findAll().size();

    // Create the VCSRepository
    VCSRepositoryDTO vCSRepositoryDTO = vCSRepositoryMapper.toDto(vCSRepository);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restVCSRepositoryMockMvc
        .perform(
            put("/api/vcs-repositories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(vCSRepositoryDTO)))
        .andExpect(status().isBadRequest());

    // Validate the VCSRepository in the database
    List<VCSRepository> vCSRepositoryList = vCSRepositoryRepository.findAll();
    assertThat(vCSRepositoryList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteVCSRepository() throws Exception {
    // Initialize the database
    vCSRepositoryRepository.saveAndFlush(vCSRepository);

    int databaseSizeBeforeDelete = vCSRepositoryRepository.findAll().size();

    // Delete the vCSRepository
    restVCSRepositoryMockMvc
        .perform(
            delete("/api/vcs-repositories/{id}", vCSRepository.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<VCSRepository> vCSRepositoryList = vCSRepositoryRepository.findAll();
    assertThat(vCSRepositoryList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(VCSRepository.class);
    VCSRepository vCSRepository1 = new VCSRepository();
    vCSRepository1.setId(1L);
    VCSRepository vCSRepository2 = new VCSRepository();
    vCSRepository2.setId(vCSRepository1.getId());
    assertThat(vCSRepository1).isEqualTo(vCSRepository2);
    vCSRepository2.setId(2L);
    assertThat(vCSRepository1).isNotEqualTo(vCSRepository2);
    vCSRepository1.setId(null);
    assertThat(vCSRepository1).isNotEqualTo(vCSRepository2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(VCSRepositoryDTO.class);
    VCSRepositoryDTO vCSRepositoryDTO1 = new VCSRepositoryDTO();
    vCSRepositoryDTO1.setId(1L);
    VCSRepositoryDTO vCSRepositoryDTO2 = new VCSRepositoryDTO();
    assertThat(vCSRepositoryDTO1).isNotEqualTo(vCSRepositoryDTO2);
    vCSRepositoryDTO2.setId(vCSRepositoryDTO1.getId());
    assertThat(vCSRepositoryDTO1).isEqualTo(vCSRepositoryDTO2);
    vCSRepositoryDTO2.setId(2L);
    assertThat(vCSRepositoryDTO1).isNotEqualTo(vCSRepositoryDTO2);
    vCSRepositoryDTO1.setId(null);
    assertThat(vCSRepositoryDTO1).isNotEqualTo(vCSRepositoryDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(vCSRepositoryMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(vCSRepositoryMapper.fromId(null)).isNull();
  }
}
