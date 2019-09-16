package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Asset;
import br.ufpa.labes.spm.repository.AssetRepository;
import br.ufpa.labes.spm.service.AssetService;
import br.ufpa.labes.spm.service.dto.AssetDTO;
import br.ufpa.labes.spm.service.mapper.AssetMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static br.ufpa.labes.spm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.ufpa.labes.spm.domain.enumeration.AssetVisibility;
/** Integration tests for the {@link AssetResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AssetResourceIT {

  private static final String DEFAULT_UID = "AAAAAAAAAA";
  private static final String UPDATED_UID = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate SMALLER_CREATION_DATE = LocalDate.ofEpochDay(-1L);

  private static final LocalDate DEFAULT_PUBLISH_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_PUBLISH_DATE = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate SMALLER_PUBLISH_DATE = LocalDate.ofEpochDay(-1L);

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  private static final String DEFAULT_APPLICABILITY = "AAAAAAAAAA";
  private static final String UPDATED_APPLICABILITY = "BBBBBBBBBB";

  private static final String DEFAULT_TAGS = "AAAAAAAAAA";
  private static final String UPDATED_TAGS = "BBBBBBBBBB";

  private static final String DEFAULT_PATH = "AAAAAAAAAA";
  private static final String UPDATED_PATH = "BBBBBBBBBB";

  private static final String DEFAULT_LATEST_VERSION = "AAAAAAAAAA";
  private static final String UPDATED_LATEST_VERSION = "BBBBBBBBBB";

  private static final Boolean DEFAULT_READ_ONLY = false;
  private static final Boolean UPDATED_READ_ONLY = true;

  private static final AssetVisibility DEFAULT_ASSET_VISIBILITY = AssetVisibility.VISIBILITY_PUBLIC;
  private static final AssetVisibility UPDATED_ASSET_VISIBILITY =
      AssetVisibility.VISIBILITY_PRIVATE;

  @Autowired private AssetRepository assetRepository;

  @Mock private AssetRepository assetRepositoryMock;

  @Autowired private AssetMapper assetMapper;

  @Mock private AssetService assetServiceMock;

  @Autowired private AssetService assetService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restAssetMockMvc;

  private Asset asset;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final AssetResource assetResource = new AssetResource(assetService);
    this.restAssetMockMvc =
        MockMvcBuilders.standaloneSetup(assetResource)
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
  public static Asset createEntity(EntityManager em) {
    Asset asset =
        new Asset()
            .uid(DEFAULT_UID)
            .creationDate(DEFAULT_CREATION_DATE)
            .publishDate(DEFAULT_PUBLISH_DATE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .applicability(DEFAULT_APPLICABILITY)
            .tags(DEFAULT_TAGS)
            .path(DEFAULT_PATH)
            .latestVersion(DEFAULT_LATEST_VERSION)
            .readOnly(DEFAULT_READ_ONLY)
            .assetVisibility(DEFAULT_ASSET_VISIBILITY);
    return asset;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Asset createUpdatedEntity(EntityManager em) {
    Asset asset =
        new Asset()
            .uid(UPDATED_UID)
            .creationDate(UPDATED_CREATION_DATE)
            .publishDate(UPDATED_PUBLISH_DATE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .applicability(UPDATED_APPLICABILITY)
            .tags(UPDATED_TAGS)
            .path(UPDATED_PATH)
            .latestVersion(UPDATED_LATEST_VERSION)
            .readOnly(UPDATED_READ_ONLY)
            .assetVisibility(UPDATED_ASSET_VISIBILITY);
    return asset;
  }

  @BeforeEach
  public void initTest() {
    asset = createEntity(em);
  }

  @Test
  @Transactional
  public void createAsset() throws Exception {
    int databaseSizeBeforeCreate = assetRepository.findAll().size();

    // Create the Asset
    AssetDTO assetDTO = assetMapper.toDto(asset);
    restAssetMockMvc
        .perform(
            post("/api/assets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetDTO)))
        .andExpect(status().isCreated());

    // Validate the Asset in the database
    List<Asset> assetList = assetRepository.findAll();
    assertThat(assetList).hasSize(databaseSizeBeforeCreate + 1);
    Asset testAsset = assetList.get(assetList.size() - 1);
    assertThat(testAsset.getUid()).isEqualTo(DEFAULT_UID);
    assertThat(testAsset.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
    assertThat(testAsset.getPublishDate()).isEqualTo(DEFAULT_PUBLISH_DATE);
    assertThat(testAsset.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testAsset.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testAsset.getApplicability()).isEqualTo(DEFAULT_APPLICABILITY);
    assertThat(testAsset.getTags()).isEqualTo(DEFAULT_TAGS);
    assertThat(testAsset.getPath()).isEqualTo(DEFAULT_PATH);
    assertThat(testAsset.getLatestVersion()).isEqualTo(DEFAULT_LATEST_VERSION);
    assertThat(testAsset.isReadOnly()).isEqualTo(DEFAULT_READ_ONLY);
    assertThat(testAsset.getAssetVisibility()).isEqualTo(DEFAULT_ASSET_VISIBILITY);
  }

  @Test
  @Transactional
  public void createAssetWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = assetRepository.findAll().size();

    // Create the Asset with an existing ID
    asset.setId(1L);
    AssetDTO assetDTO = assetMapper.toDto(asset);

    // An entity with an existing ID cannot be created, so this API call must fail
    restAssetMockMvc
        .perform(
            post("/api/assets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Asset in the database
    List<Asset> assetList = assetRepository.findAll();
    assertThat(assetList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllAssets() throws Exception {
    // Initialize the database
    assetRepository.saveAndFlush(asset);

    // Get all the assetList
    restAssetMockMvc
        .perform(get("/api/assets?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(asset.getId().intValue())))
        .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
        .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
        .andExpect(jsonPath("$.[*].publishDate").value(hasItem(DEFAULT_PUBLISH_DATE.toString())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
        .andExpect(jsonPath("$.[*].applicability").value(hasItem(DEFAULT_APPLICABILITY.toString())))
        .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS.toString())))
        .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
        .andExpect(
            jsonPath("$.[*].latestVersion").value(hasItem(DEFAULT_LATEST_VERSION.toString())))
        .andExpect(jsonPath("$.[*].readOnly").value(hasItem(DEFAULT_READ_ONLY.booleanValue())))
        .andExpect(
            jsonPath("$.[*].assetVisibility").value(hasItem(DEFAULT_ASSET_VISIBILITY.toString())));
  }

  @SuppressWarnings({"unchecked"})
  public void getAllAssetsWithEagerRelationshipsIsEnabled() throws Exception {
    AssetResource assetResource = new AssetResource(assetServiceMock);
    when(assetServiceMock.findAllWithEagerRelationships(any()))
        .thenReturn(new PageImpl(new ArrayList<>()));

    MockMvc restAssetMockMvc =
        MockMvcBuilders.standaloneSetup(assetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .build();

    restAssetMockMvc.perform(get("/api/assets?eagerload=true")).andExpect(status().isOk());

    verify(assetServiceMock, times(1)).findAllWithEagerRelationships(any());
  }

  @SuppressWarnings({"unchecked"})
  public void getAllAssetsWithEagerRelationshipsIsNotEnabled() throws Exception {
    AssetResource assetResource = new AssetResource(assetServiceMock);
    when(assetServiceMock.findAllWithEagerRelationships(any()))
        .thenReturn(new PageImpl(new ArrayList<>()));
    MockMvc restAssetMockMvc =
        MockMvcBuilders.standaloneSetup(assetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .build();

    restAssetMockMvc.perform(get("/api/assets?eagerload=true")).andExpect(status().isOk());

    verify(assetServiceMock, times(1)).findAllWithEagerRelationships(any());
  }

  @Test
  @Transactional
  public void getAsset() throws Exception {
    // Initialize the database
    assetRepository.saveAndFlush(asset);

    // Get the asset
    restAssetMockMvc
        .perform(get("/api/assets/{id}", asset.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(asset.getId().intValue()))
        .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
        .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
        .andExpect(jsonPath("$.publishDate").value(DEFAULT_PUBLISH_DATE.toString()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
        .andExpect(jsonPath("$.applicability").value(DEFAULT_APPLICABILITY.toString()))
        .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS.toString()))
        .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()))
        .andExpect(jsonPath("$.latestVersion").value(DEFAULT_LATEST_VERSION.toString()))
        .andExpect(jsonPath("$.readOnly").value(DEFAULT_READ_ONLY.booleanValue()))
        .andExpect(jsonPath("$.assetVisibility").value(DEFAULT_ASSET_VISIBILITY.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingAsset() throws Exception {
    // Get the asset
    restAssetMockMvc
        .perform(get("/api/assets/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateAsset() throws Exception {
    // Initialize the database
    assetRepository.saveAndFlush(asset);

    int databaseSizeBeforeUpdate = assetRepository.findAll().size();

    // Update the asset
    Asset updatedAsset = assetRepository.findById(asset.getId()).get();
    // Disconnect from session so that the updates on updatedAsset are not directly saved in db
    em.detach(updatedAsset);
    updatedAsset
        .uid(UPDATED_UID)
        .creationDate(UPDATED_CREATION_DATE)
        .publishDate(UPDATED_PUBLISH_DATE)
        .name(UPDATED_NAME)
        .description(UPDATED_DESCRIPTION)
        .applicability(UPDATED_APPLICABILITY)
        .tags(UPDATED_TAGS)
        .path(UPDATED_PATH)
        .latestVersion(UPDATED_LATEST_VERSION)
        .readOnly(UPDATED_READ_ONLY)
        .assetVisibility(UPDATED_ASSET_VISIBILITY);
    AssetDTO assetDTO = assetMapper.toDto(updatedAsset);

    restAssetMockMvc
        .perform(
            put("/api/assets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetDTO)))
        .andExpect(status().isOk());

    // Validate the Asset in the database
    List<Asset> assetList = assetRepository.findAll();
    assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
    Asset testAsset = assetList.get(assetList.size() - 1);
    assertThat(testAsset.getUid()).isEqualTo(UPDATED_UID);
    assertThat(testAsset.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
    assertThat(testAsset.getPublishDate()).isEqualTo(UPDATED_PUBLISH_DATE);
    assertThat(testAsset.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testAsset.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    assertThat(testAsset.getApplicability()).isEqualTo(UPDATED_APPLICABILITY);
    assertThat(testAsset.getTags()).isEqualTo(UPDATED_TAGS);
    assertThat(testAsset.getPath()).isEqualTo(UPDATED_PATH);
    assertThat(testAsset.getLatestVersion()).isEqualTo(UPDATED_LATEST_VERSION);
    assertThat(testAsset.isReadOnly()).isEqualTo(UPDATED_READ_ONLY);
    assertThat(testAsset.getAssetVisibility()).isEqualTo(UPDATED_ASSET_VISIBILITY);
  }

  @Test
  @Transactional
  public void updateNonExistingAsset() throws Exception {
    int databaseSizeBeforeUpdate = assetRepository.findAll().size();

    // Create the Asset
    AssetDTO assetDTO = assetMapper.toDto(asset);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAssetMockMvc
        .perform(
            put("/api/assets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Asset in the database
    List<Asset> assetList = assetRepository.findAll();
    assertThat(assetList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteAsset() throws Exception {
    // Initialize the database
    assetRepository.saveAndFlush(asset);

    int databaseSizeBeforeDelete = assetRepository.findAll().size();

    // Delete the asset
    restAssetMockMvc
        .perform(delete("/api/assets/{id}", asset.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Asset> assetList = assetRepository.findAll();
    assertThat(assetList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Asset.class);
    Asset asset1 = new Asset();
    asset1.setId(1L);
    Asset asset2 = new Asset();
    asset2.setId(asset1.getId());
    assertThat(asset1).isEqualTo(asset2);
    asset2.setId(2L);
    assertThat(asset1).isNotEqualTo(asset2);
    asset1.setId(null);
    assertThat(asset1).isNotEqualTo(asset2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(AssetDTO.class);
    AssetDTO assetDTO1 = new AssetDTO();
    assetDTO1.setId(1L);
    AssetDTO assetDTO2 = new AssetDTO();
    assertThat(assetDTO1).isNotEqualTo(assetDTO2);
    assetDTO2.setId(assetDTO1.getId());
    assertThat(assetDTO1).isEqualTo(assetDTO2);
    assetDTO2.setId(2L);
    assertThat(assetDTO1).isNotEqualTo(assetDTO2);
    assetDTO1.setId(null);
    assertThat(assetDTO1).isNotEqualTo(assetDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(assetMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(assetMapper.fromId(null)).isNull();
  }
}
