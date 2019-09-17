package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.repository.NormalRepository;
import br.ufpa.labes.spm.service.NormalService;
import br.ufpa.labes.spm.service.dto.NormalDTO;
import br.ufpa.labes.spm.service.mapper.NormalMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static br.ufpa.labes.spm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Integration tests for the {@link NormalResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class NormalResourceIT {

  private static final String DEFAULT_IDENT = "AAAAAAAAAA";
  private static final String UPDATED_IDENT = "BBBBBBBBBB";

  private static final Float DEFAULT_HOW_LONG = 1F;
  private static final Float UPDATED_HOW_LONG = 2F;
  private static final Float SMALLER_HOW_LONG = 1F - 1F;

  private static final String DEFAULT_HOW_LONG_UNIT = "AAAAAAAAAA";
  private static final String UPDATED_HOW_LONG_UNIT = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_PLANNED_BEGIN = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_PLANNED_BEGIN = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate SMALLER_PLANNED_BEGIN = LocalDate.ofEpochDay(-1L);

  private static final LocalDate DEFAULT_PLANNED_END = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_PLANNED_END = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate SMALLER_PLANNED_END = LocalDate.ofEpochDay(-1L);

  private static final String DEFAULT_SCRIPT = "AAAAAAAAAA";
  private static final String UPDATED_SCRIPT = "BBBBBBBBBB";

  private static final Boolean DEFAULT_DELEGABLE = false;
  private static final Boolean UPDATED_DELEGABLE = true;

  private static final Boolean DEFAULT_AUTO_ALLOCABLE = false;
  private static final Boolean UPDATED_AUTO_ALLOCABLE = true;

  @Autowired private NormalRepository normalRepository;

  @Autowired private NormalMapper normalMapper;

  @Autowired private NormalService normalService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restNormalMockMvc;

  private Normal normal;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final NormalResource normalResource = new NormalResource(normalService);
    this.restNormalMockMvc =
        MockMvcBuilders.standaloneSetup(normalResource)
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
  public static Normal createEntity(EntityManager em) {
    Normal normal =
        new Normal()
            .ident(DEFAULT_IDENT)
            .howLong(DEFAULT_HOW_LONG)
            .howLongUnit(DEFAULT_HOW_LONG_UNIT)
            .plannedBegin(DEFAULT_PLANNED_BEGIN)
            .plannedEnd(DEFAULT_PLANNED_END)
            .script(DEFAULT_SCRIPT)
            .delegable(DEFAULT_DELEGABLE)
            .autoAllocable(DEFAULT_AUTO_ALLOCABLE);
    return normal;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Normal createUpdatedEntity(EntityManager em) {
    Normal normal =
        new Normal()
            .ident(UPDATED_IDENT)
            .howLong(UPDATED_HOW_LONG)
            .howLongUnit(UPDATED_HOW_LONG_UNIT)
            .plannedBegin(UPDATED_PLANNED_BEGIN)
            .plannedEnd(UPDATED_PLANNED_END)
            .script(UPDATED_SCRIPT)
            .delegable(UPDATED_DELEGABLE)
            .autoAllocable(UPDATED_AUTO_ALLOCABLE);
    return normal;
  }

  @BeforeEach
  public void initTest() {
    normal = createEntity(em);
  }

  @Test
  @Transactional
  public void createNormal() throws Exception {
    int databaseSizeBeforeCreate = normalRepository.findAll().size();

    // Create the Normal
    NormalDTO normalDTO = normalMapper.toDto(normal);
    restNormalMockMvc
        .perform(
            post("/api/normals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(normalDTO)))
        .andExpect(status().isCreated());

    // Validate the Normal in the database
    List<Normal> normalList = normalRepository.findAll();
    assertThat(normalList).hasSize(databaseSizeBeforeCreate + 1);
    Normal testNormal = normalList.get(normalList.size() - 1);
    assertThat(testNormal.getIdent()).isEqualTo(DEFAULT_IDENT);
    assertThat(testNormal.getHowLong()).isEqualTo(DEFAULT_HOW_LONG);
    assertThat(testNormal.getHowLongUnit()).isEqualTo(DEFAULT_HOW_LONG_UNIT);
    assertThat(testNormal.getPlannedBegin()).isEqualTo(DEFAULT_PLANNED_BEGIN);
    assertThat(testNormal.getPlannedEnd()).isEqualTo(DEFAULT_PLANNED_END);
    assertThat(testNormal.getScript()).isEqualTo(DEFAULT_SCRIPT);
    assertThat(testNormal.isDelegable()).isEqualTo(DEFAULT_DELEGABLE);
    assertThat(testNormal.isAutoAllocable()).isEqualTo(DEFAULT_AUTO_ALLOCABLE);
  }

  @Test
  @Transactional
  public void createNormalWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = normalRepository.findAll().size();

    // Create the Normal with an existing ID
    normal.setId(1L);
    NormalDTO normalDTO = normalMapper.toDto(normal);

    // An entity with an existing ID cannot be created, so this API call must fail
    restNormalMockMvc
        .perform(
            post("/api/normals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(normalDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Normal in the database
    List<Normal> normalList = normalRepository.findAll();
    assertThat(normalList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllNormals() throws Exception {
    // Initialize the database
    normalRepository.saveAndFlush(normal);

    // Get all the normalList
    restNormalMockMvc
        .perform(get("/api/normals?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(normal.getId().intValue())))
        .andExpect(jsonPath("$.[*].ident").value(hasItem(DEFAULT_IDENT.toString())))
        .andExpect(jsonPath("$.[*].howLong").value(hasItem(DEFAULT_HOW_LONG.doubleValue())))
        .andExpect(jsonPath("$.[*].howLongUnit").value(hasItem(DEFAULT_HOW_LONG_UNIT.toString())))
        .andExpect(jsonPath("$.[*].plannedBegin").value(hasItem(DEFAULT_PLANNED_BEGIN.toString())))
        .andExpect(jsonPath("$.[*].plannedEnd").value(hasItem(DEFAULT_PLANNED_END.toString())))
        .andExpect(jsonPath("$.[*].script").value(hasItem(DEFAULT_SCRIPT.toString())))
        .andExpect(jsonPath("$.[*].delegable").value(hasItem(DEFAULT_DELEGABLE.booleanValue())))
        .andExpect(
            jsonPath("$.[*].autoAllocable").value(hasItem(DEFAULT_AUTO_ALLOCABLE.booleanValue())));
  }

  @Test
  @Transactional
  public void getNormal() throws Exception {
    // Initialize the database
    normalRepository.saveAndFlush(normal);

    // Get the normal
    restNormalMockMvc
        .perform(get("/api/normals/{id}", normal.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(normal.getId().intValue()))
        .andExpect(jsonPath("$.ident").value(DEFAULT_IDENT.toString()))
        .andExpect(jsonPath("$.howLong").value(DEFAULT_HOW_LONG.doubleValue()))
        .andExpect(jsonPath("$.howLongUnit").value(DEFAULT_HOW_LONG_UNIT.toString()))
        .andExpect(jsonPath("$.plannedBegin").value(DEFAULT_PLANNED_BEGIN.toString()))
        .andExpect(jsonPath("$.plannedEnd").value(DEFAULT_PLANNED_END.toString()))
        .andExpect(jsonPath("$.script").value(DEFAULT_SCRIPT.toString()))
        .andExpect(jsonPath("$.delegable").value(DEFAULT_DELEGABLE.booleanValue()))
        .andExpect(jsonPath("$.autoAllocable").value(DEFAULT_AUTO_ALLOCABLE.booleanValue()));
  }

  @Test
  @Transactional
  public void getNonExistingNormal() throws Exception {
    // Get the normal
    restNormalMockMvc
        .perform(get("/api/normals/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateNormal() throws Exception {
    // Initialize the database
    normalRepository.saveAndFlush(normal);

    int databaseSizeBeforeUpdate = normalRepository.findAll().size();

    // Update the normal
    Normal updatedNormal = normalRepository.findById(normal.getId()).get();
    // Disconnect from session so that the updates on updatedNormal are not directly saved in db
    em.detach(updatedNormal);
    updatedNormal
        .ident(UPDATED_IDENT)
        .howLong(UPDATED_HOW_LONG)
        .howLongUnit(UPDATED_HOW_LONG_UNIT)
        .plannedBegin(UPDATED_PLANNED_BEGIN)
        .plannedEnd(UPDATED_PLANNED_END)
        .script(UPDATED_SCRIPT)
        .delegable(UPDATED_DELEGABLE)
        .autoAllocable(UPDATED_AUTO_ALLOCABLE);
    NormalDTO normalDTO = normalMapper.toDto(updatedNormal);

    restNormalMockMvc
        .perform(
            put("/api/normals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(normalDTO)))
        .andExpect(status().isOk());

    // Validate the Normal in the database
    List<Normal> normalList = normalRepository.findAll();
    assertThat(normalList).hasSize(databaseSizeBeforeUpdate);
    Normal testNormal = normalList.get(normalList.size() - 1);
    assertThat(testNormal.getIdent()).isEqualTo(UPDATED_IDENT);
    assertThat(testNormal.getHowLong()).isEqualTo(UPDATED_HOW_LONG);
    assertThat(testNormal.getHowLongUnit()).isEqualTo(UPDATED_HOW_LONG_UNIT);
    assertThat(testNormal.getPlannedBegin()).isEqualTo(UPDATED_PLANNED_BEGIN);
    assertThat(testNormal.getPlannedEnd()).isEqualTo(UPDATED_PLANNED_END);
    assertThat(testNormal.getScript()).isEqualTo(UPDATED_SCRIPT);
    assertThat(testNormal.isDelegable()).isEqualTo(UPDATED_DELEGABLE);
    assertThat(testNormal.isAutoAllocable()).isEqualTo(UPDATED_AUTO_ALLOCABLE);
  }

  @Test
  @Transactional
  public void updateNonExistingNormal() throws Exception {
    int databaseSizeBeforeUpdate = normalRepository.findAll().size();

    // Create the Normal
    NormalDTO normalDTO = normalMapper.toDto(normal);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restNormalMockMvc
        .perform(
            put("/api/normals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(normalDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Normal in the database
    List<Normal> normalList = normalRepository.findAll();
    assertThat(normalList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteNormal() throws Exception {
    // Initialize the database
    normalRepository.saveAndFlush(normal);

    int databaseSizeBeforeDelete = normalRepository.findAll().size();

    // Delete the normal
    restNormalMockMvc
        .perform(delete("/api/normals/{id}", normal.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Normal> normalList = normalRepository.findAll();
    assertThat(normalList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Normal.class);
    Normal normal1 = new Normal();
    normal1.setId(1L);
    Normal normal2 = new Normal();
    normal2.setId(normal1.getId());
    assertThat(normal1).isEqualTo(normal2);
    normal2.setId(2L);
    assertThat(normal1).isNotEqualTo(normal2);
    normal1.setId(null);
    assertThat(normal1).isNotEqualTo(normal2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(NormalDTO.class);
    NormalDTO normalDTO1 = new NormalDTO();
    normalDTO1.setId(1L);
    NormalDTO normalDTO2 = new NormalDTO();
    assertThat(normalDTO1).isNotEqualTo(normalDTO2);
    normalDTO2.setId(normalDTO1.getId());
    assertThat(normalDTO1).isEqualTo(normalDTO2);
    normalDTO2.setId(2L);
    assertThat(normalDTO1).isNotEqualTo(normalDTO2);
    normalDTO1.setId(null);
    assertThat(normalDTO1).isNotEqualTo(normalDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(normalMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(normalMapper.fromId(null)).isNull();
  }
}
