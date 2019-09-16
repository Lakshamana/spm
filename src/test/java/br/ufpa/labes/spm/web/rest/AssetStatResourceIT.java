package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.AssetStat;
import br.ufpa.labes.spm.repository.AssetStatRepository;
import br.ufpa.labes.spm.service.AssetStatService;
import br.ufpa.labes.spm.service.dto.AssetStatDTO;
import br.ufpa.labes.spm.service.mapper.AssetStatMapper;
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

/** Integration tests for the {@link AssetStatResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AssetStatResourceIT {

  private static final Long DEFAULT_VOTE_COUNT = 1L;
  private static final Long UPDATED_VOTE_COUNT = 2L;
  private static final Long SMALLER_VOTE_COUNT = 1L - 1L;

  private static final Long DEFAULT_VISIT_COUNT = 1L;
  private static final Long UPDATED_VISIT_COUNT = 2L;
  private static final Long SMALLER_VISIT_COUNT = 1L - 1L;

  private static final Long DEFAULT_DOWNLOAD_COUNT = 1L;
  private static final Long UPDATED_DOWNLOAD_COUNT = 2L;
  private static final Long SMALLER_DOWNLOAD_COUNT = 1L - 1L;

  private static final Double DEFAULT_T_VOTES = 1D;
  private static final Double UPDATED_T_VOTES = 2D;
  private static final Double SMALLER_T_VOTES = 1D - 1D;

  private static final Double DEFAULT_RATE = 1D;
  private static final Double UPDATED_RATE = 2D;
  private static final Double SMALLER_RATE = 1D - 1D;

  @Autowired private AssetStatRepository assetStatRepository;

  @Autowired private AssetStatMapper assetStatMapper;

  @Autowired private AssetStatService assetStatService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restAssetStatMockMvc;

  private AssetStat assetStat;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final AssetStatResource assetStatResource = new AssetStatResource(assetStatService);
    this.restAssetStatMockMvc =
        MockMvcBuilders.standaloneSetup(assetStatResource)
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
  public static AssetStat createEntity(EntityManager em) {
    AssetStat assetStat =
        new AssetStat()
            .voteCount(DEFAULT_VOTE_COUNT)
            .visitCount(DEFAULT_VISIT_COUNT)
            .downloadCount(DEFAULT_DOWNLOAD_COUNT)
            .tVotes(DEFAULT_T_VOTES)
            .rate(DEFAULT_RATE);
    return assetStat;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static AssetStat createUpdatedEntity(EntityManager em) {
    AssetStat assetStat =
        new AssetStat()
            .voteCount(UPDATED_VOTE_COUNT)
            .visitCount(UPDATED_VISIT_COUNT)
            .downloadCount(UPDATED_DOWNLOAD_COUNT)
            .tVotes(UPDATED_T_VOTES)
            .rate(UPDATED_RATE);
    return assetStat;
  }

  @BeforeEach
  public void initTest() {
    assetStat = createEntity(em);
  }

  @Test
  @Transactional
  public void createAssetStat() throws Exception {
    int databaseSizeBeforeCreate = assetStatRepository.findAll().size();

    // Create the AssetStat
    AssetStatDTO assetStatDTO = assetStatMapper.toDto(assetStat);
    restAssetStatMockMvc
        .perform(
            post("/api/asset-stats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetStatDTO)))
        .andExpect(status().isCreated());

    // Validate the AssetStat in the database
    List<AssetStat> assetStatList = assetStatRepository.findAll();
    assertThat(assetStatList).hasSize(databaseSizeBeforeCreate + 1);
    AssetStat testAssetStat = assetStatList.get(assetStatList.size() - 1);
    assertThat(testAssetStat.getVoteCount()).isEqualTo(DEFAULT_VOTE_COUNT);
    assertThat(testAssetStat.getVisitCount()).isEqualTo(DEFAULT_VISIT_COUNT);
    assertThat(testAssetStat.getDownloadCount()).isEqualTo(DEFAULT_DOWNLOAD_COUNT);
    assertThat(testAssetStat.gettVotes()).isEqualTo(DEFAULT_T_VOTES);
    assertThat(testAssetStat.getRate()).isEqualTo(DEFAULT_RATE);
  }

  @Test
  @Transactional
  public void createAssetStatWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = assetStatRepository.findAll().size();

    // Create the AssetStat with an existing ID
    assetStat.setId(1L);
    AssetStatDTO assetStatDTO = assetStatMapper.toDto(assetStat);

    // An entity with an existing ID cannot be created, so this API call must fail
    restAssetStatMockMvc
        .perform(
            post("/api/asset-stats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetStatDTO)))
        .andExpect(status().isBadRequest());

    // Validate the AssetStat in the database
    List<AssetStat> assetStatList = assetStatRepository.findAll();
    assertThat(assetStatList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllAssetStats() throws Exception {
    // Initialize the database
    assetStatRepository.saveAndFlush(assetStat);

    // Get all the assetStatList
    restAssetStatMockMvc
        .perform(get("/api/asset-stats?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(assetStat.getId().intValue())))
        .andExpect(jsonPath("$.[*].voteCount").value(hasItem(DEFAULT_VOTE_COUNT.intValue())))
        .andExpect(jsonPath("$.[*].visitCount").value(hasItem(DEFAULT_VISIT_COUNT.intValue())))
        .andExpect(
            jsonPath("$.[*].downloadCount").value(hasItem(DEFAULT_DOWNLOAD_COUNT.intValue())))
        .andExpect(jsonPath("$.[*].tVotes").value(hasItem(DEFAULT_T_VOTES.doubleValue())))
        .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())));
  }

  @Test
  @Transactional
  public void getAssetStat() throws Exception {
    // Initialize the database
    assetStatRepository.saveAndFlush(assetStat);

    // Get the assetStat
    restAssetStatMockMvc
        .perform(get("/api/asset-stats/{id}", assetStat.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(assetStat.getId().intValue()))
        .andExpect(jsonPath("$.voteCount").value(DEFAULT_VOTE_COUNT.intValue()))
        .andExpect(jsonPath("$.visitCount").value(DEFAULT_VISIT_COUNT.intValue()))
        .andExpect(jsonPath("$.downloadCount").value(DEFAULT_DOWNLOAD_COUNT.intValue()))
        .andExpect(jsonPath("$.tVotes").value(DEFAULT_T_VOTES.doubleValue()))
        .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()));
  }

  @Test
  @Transactional
  public void getNonExistingAssetStat() throws Exception {
    // Get the assetStat
    restAssetStatMockMvc
        .perform(get("/api/asset-stats/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateAssetStat() throws Exception {
    // Initialize the database
    assetStatRepository.saveAndFlush(assetStat);

    int databaseSizeBeforeUpdate = assetStatRepository.findAll().size();

    // Update the assetStat
    AssetStat updatedAssetStat = assetStatRepository.findById(assetStat.getId()).get();
    // Disconnect from session so that the updates on updatedAssetStat are not directly saved in db
    em.detach(updatedAssetStat);
    updatedAssetStat
        .voteCount(UPDATED_VOTE_COUNT)
        .visitCount(UPDATED_VISIT_COUNT)
        .downloadCount(UPDATED_DOWNLOAD_COUNT)
        .tVotes(UPDATED_T_VOTES)
        .rate(UPDATED_RATE);
    AssetStatDTO assetStatDTO = assetStatMapper.toDto(updatedAssetStat);

    restAssetStatMockMvc
        .perform(
            put("/api/asset-stats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetStatDTO)))
        .andExpect(status().isOk());

    // Validate the AssetStat in the database
    List<AssetStat> assetStatList = assetStatRepository.findAll();
    assertThat(assetStatList).hasSize(databaseSizeBeforeUpdate);
    AssetStat testAssetStat = assetStatList.get(assetStatList.size() - 1);
    assertThat(testAssetStat.getVoteCount()).isEqualTo(UPDATED_VOTE_COUNT);
    assertThat(testAssetStat.getVisitCount()).isEqualTo(UPDATED_VISIT_COUNT);
    assertThat(testAssetStat.getDownloadCount()).isEqualTo(UPDATED_DOWNLOAD_COUNT);
    assertThat(testAssetStat.gettVotes()).isEqualTo(UPDATED_T_VOTES);
    assertThat(testAssetStat.getRate()).isEqualTo(UPDATED_RATE);
  }

  @Test
  @Transactional
  public void updateNonExistingAssetStat() throws Exception {
    int databaseSizeBeforeUpdate = assetStatRepository.findAll().size();

    // Create the AssetStat
    AssetStatDTO assetStatDTO = assetStatMapper.toDto(assetStat);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAssetStatMockMvc
        .perform(
            put("/api/asset-stats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetStatDTO)))
        .andExpect(status().isBadRequest());

    // Validate the AssetStat in the database
    List<AssetStat> assetStatList = assetStatRepository.findAll();
    assertThat(assetStatList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteAssetStat() throws Exception {
    // Initialize the database
    assetStatRepository.saveAndFlush(assetStat);

    int databaseSizeBeforeDelete = assetStatRepository.findAll().size();

    // Delete the assetStat
    restAssetStatMockMvc
        .perform(
            delete("/api/asset-stats/{id}", assetStat.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<AssetStat> assetStatList = assetStatRepository.findAll();
    assertThat(assetStatList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(AssetStat.class);
    AssetStat assetStat1 = new AssetStat();
    assetStat1.setId(1L);
    AssetStat assetStat2 = new AssetStat();
    assetStat2.setId(assetStat1.getId());
    assertThat(assetStat1).isEqualTo(assetStat2);
    assetStat2.setId(2L);
    assertThat(assetStat1).isNotEqualTo(assetStat2);
    assetStat1.setId(null);
    assertThat(assetStat1).isNotEqualTo(assetStat2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(AssetStatDTO.class);
    AssetStatDTO assetStatDTO1 = new AssetStatDTO();
    assetStatDTO1.setId(1L);
    AssetStatDTO assetStatDTO2 = new AssetStatDTO();
    assertThat(assetStatDTO1).isNotEqualTo(assetStatDTO2);
    assetStatDTO2.setId(assetStatDTO1.getId());
    assertThat(assetStatDTO1).isEqualTo(assetStatDTO2);
    assetStatDTO2.setId(2L);
    assertThat(assetStatDTO1).isNotEqualTo(assetStatDTO2);
    assetStatDTO1.setId(null);
    assertThat(assetStatDTO1).isNotEqualTo(assetStatDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(assetStatMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(assetStatMapper.fromId(null)).isNull();
  }
}
