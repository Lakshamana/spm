package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.TagStats;
import br.ufpa.labes.spm.repository.TagStatRepository;
import br.ufpa.labes.spm.service.TagStatsService;
import br.ufpa.labes.spm.service.dto.TagStatsDTO;
import br.ufpa.labes.spm.service.mapper.TagStatsMapper;
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

/** Integration tests for the {@link TagStatResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class TagStatResourceIT {

  private static final Long DEFAULT_COUNT = 1L;
  private static final Long UPDATED_COUNT = 2L;
  private static final Long SMALLER_COUNT = 1L - 1L;

  @Autowired private TagStatRepository tagStatRepository;

  @Autowired private TagStatsMapper tagStatsMapper;

  @Autowired private TagStatsService tagStatsService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restTagStatMockMvc;

  private TagStats tagStat;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final TagStatResource tagStatResource = new TagStatResource(tagStatsService);
    this.restTagStatMockMvc =
        MockMvcBuilders.standaloneSetup(tagStatResource)
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
  public static TagStats createEntity(EntityManager em) {
    TagStats tagStat = new TagStats().count(DEFAULT_COUNT);
    return tagStat;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static TagStats createUpdatedEntity(EntityManager em) {
    TagStats tagStat = new TagStats().count(UPDATED_COUNT);
    return tagStat;
  }

  @BeforeEach
  public void initTest() {
    tagStat = createEntity(em);
  }

  @Test
  @Transactional
  public void createTagStat() throws Exception {
    int databaseSizeBeforeCreate = tagStatRepository.findAll().size();

    // Create the TagStats
    TagStatsDTO tagStatsDTO = tagStatsMapper.toDto(tagStat);
    restTagStatMockMvc
        .perform(
            post("/api/tag-stats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagStatsDTO)))
        .andExpect(status().isCreated());

    // Validate the TagStats in the database
    List<TagStats> tagStatList = tagStatRepository.findAll();
    assertThat(tagStatList).hasSize(databaseSizeBeforeCreate + 1);
    TagStats testTagStat = tagStatList.get(tagStatList.size() - 1);
    assertThat(testTagStat.getCount()).isEqualTo(DEFAULT_COUNT);
  }

  @Test
  @Transactional
  public void createTagStatWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = tagStatRepository.findAll().size();

    // Create the TagStats with an existing ID
    tagStat.setId(1L);
    TagStatsDTO tagStatsDTO = tagStatsMapper.toDto(tagStat);

    // An entity with an existing ID cannot be created, so this API call must fail
    restTagStatMockMvc
        .perform(
            post("/api/tag-stats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagStatsDTO)))
        .andExpect(status().isBadRequest());

    // Validate the TagStats in the database
    List<TagStats> tagStatList = tagStatRepository.findAll();
    assertThat(tagStatList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllTagStats() throws Exception {
    // Initialize the database
    tagStatRepository.saveAndFlush(tagStat);

    // Get all the tagStatList
    restTagStatMockMvc
        .perform(get("/api/tag-stats?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(tagStat.getId().intValue())))
        .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT.intValue())));
  }

  @Test
  @Transactional
  public void getTagStat() throws Exception {
    // Initialize the database
    tagStatRepository.saveAndFlush(tagStat);

    // Get the tagStat
    restTagStatMockMvc
        .perform(get("/api/tag-stats/{id}", tagStat.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(tagStat.getId().intValue()))
        .andExpect(jsonPath("$.count").value(DEFAULT_COUNT.intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingTagStat() throws Exception {
    // Get the tagStat
    restTagStatMockMvc
        .perform(get("/api/tag-stats/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateTagStat() throws Exception {
    // Initialize the database
    tagStatRepository.saveAndFlush(tagStat);

    int databaseSizeBeforeUpdate = tagStatRepository.findAll().size();

    // Update the tagStat
    TagStats updatedTagStat = tagStatRepository.findById(tagStat.getId()).get();
    // Disconnect from session so that the updates on updatedTagStat are not directly saved in db
    em.detach(updatedTagStat);
    updatedTagStat.count(UPDATED_COUNT);
    TagStatsDTO tagStatsDTO = tagStatsMapper.toDto(updatedTagStat);

    restTagStatMockMvc
        .perform(
            put("/api/tag-stats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagStatsDTO)))
        .andExpect(status().isOk());

    // Validate the TagStats in the database
    List<TagStats> tagStatList = tagStatRepository.findAll();
    assertThat(tagStatList).hasSize(databaseSizeBeforeUpdate);
    TagStats testTagStat = tagStatList.get(tagStatList.size() - 1);
    assertThat(testTagStat.getCount()).isEqualTo(UPDATED_COUNT);
  }

  @Test
  @Transactional
  public void updateNonExistingTagStat() throws Exception {
    int databaseSizeBeforeUpdate = tagStatRepository.findAll().size();

    // Create the TagStats
    TagStatsDTO tagStatsDTO = tagStatsMapper.toDto(tagStat);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restTagStatMockMvc
        .perform(
            put("/api/tag-stats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tagStatsDTO)))
        .andExpect(status().isBadRequest());

    // Validate the TagStats in the database
    List<TagStats> tagStatList = tagStatRepository.findAll();
    assertThat(tagStatList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteTagStat() throws Exception {
    // Initialize the database
    tagStatRepository.saveAndFlush(tagStat);

    int databaseSizeBeforeDelete = tagStatRepository.findAll().size();

    // Delete the tagStat
    restTagStatMockMvc
        .perform(
            delete("/api/tag-stats/{id}", tagStat.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<TagStats> tagStatList = tagStatRepository.findAll();
    assertThat(tagStatList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(TagStats.class);
    TagStats tagStat1 = new TagStats();
    tagStat1.setId(1L);
    TagStats tagStat2 = new TagStats();
    tagStat2.setId(tagStat1.getId());
    assertThat(tagStat1).isEqualTo(tagStat2);
    tagStat2.setId(2L);
    assertThat(tagStat1).isNotEqualTo(tagStat2);
    tagStat1.setId(null);
    assertThat(tagStat1).isNotEqualTo(tagStat2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(TagStatsDTO.class);
    TagStatsDTO tagStatsDTO1 = new TagStatsDTO();
    tagStatsDTO1.setId(1L);
    TagStatsDTO tagStatsDTO2 = new TagStatsDTO();
    assertThat(tagStatsDTO1).isNotEqualTo(tagStatsDTO2);
    tagStatsDTO2.setId(tagStatsDTO1.getId());
    assertThat(tagStatsDTO1).isEqualTo(tagStatsDTO2);
    tagStatsDTO2.setId(2L);
    assertThat(tagStatsDTO1).isNotEqualTo(tagStatsDTO2);
    tagStatsDTO1.setId(null);
    assertThat(tagStatsDTO1).isNotEqualTo(tagStatsDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(tagStatsMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(tagStatsMapper.fromId(null)).isNull();
  }
}
