package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.AssetStat;
import br.ufpa.labes.spm.repository.AssetStatRepository;
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

  @Autowired private AssetStatRepository assetStatRepository;

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
    final AssetStatResource assetStatResource = new AssetStatResource(assetStatRepository);
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
    AssetStat assetStat = new AssetStat();
    return assetStat;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static AssetStat createUpdatedEntity(EntityManager em) {
    AssetStat assetStat = new AssetStat();
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
    restAssetStatMockMvc
        .perform(
            post("/api/asset-stats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetStat)))
        .andExpect(status().isCreated());

    // Validate the AssetStat in the database
    List<AssetStat> assetStatList = assetStatRepository.findAll();
    assertThat(assetStatList).hasSize(databaseSizeBeforeCreate + 1);
    AssetStat testAssetStat = assetStatList.get(assetStatList.size() - 1);
  }

  @Test
  @Transactional
  public void createAssetStatWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = assetStatRepository.findAll().size();

    // Create the AssetStat with an existing ID
    assetStat.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restAssetStatMockMvc
        .perform(
            post("/api/asset-stats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetStat)))
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
        .andExpect(jsonPath("$.[*].id").value(hasItem(assetStat.getId().intValue())));
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
        .andExpect(jsonPath("$.id").value(assetStat.getId().intValue()));
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

    restAssetStatMockMvc
        .perform(
            put("/api/asset-stats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAssetStat)))
        .andExpect(status().isOk());

    // Validate the AssetStat in the database
    List<AssetStat> assetStatList = assetStatRepository.findAll();
    assertThat(assetStatList).hasSize(databaseSizeBeforeUpdate);
    AssetStat testAssetStat = assetStatList.get(assetStatList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingAssetStat() throws Exception {
    int databaseSizeBeforeUpdate = assetStatRepository.findAll().size();

    // Create the AssetStat

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAssetStatMockMvc
        .perform(
            put("/api/asset-stats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assetStat)))
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
}
