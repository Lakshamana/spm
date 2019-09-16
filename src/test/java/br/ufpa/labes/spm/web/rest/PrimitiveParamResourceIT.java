package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.PrimitiveParam;
import br.ufpa.labes.spm.repository.PrimitiveParamRepository;
import br.ufpa.labes.spm.service.PrimitiveParamService;
import br.ufpa.labes.spm.service.dto.PrimitiveParamDTO;
import br.ufpa.labes.spm.service.mapper.PrimitiveParamMapper;
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

/** Integration tests for the {@link PrimitiveParamResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class PrimitiveParamResourceIT {

  @Autowired private PrimitiveParamRepository primitiveParamRepository;

  @Autowired private PrimitiveParamMapper primitiveParamMapper;

  @Autowired private PrimitiveParamService primitiveParamService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restPrimitiveParamMockMvc;

  private PrimitiveParam primitiveParam;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final PrimitiveParamResource primitiveParamResource =
        new PrimitiveParamResource(primitiveParamService);
    this.restPrimitiveParamMockMvc =
        MockMvcBuilders.standaloneSetup(primitiveParamResource)
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
  public static PrimitiveParam createEntity(EntityManager em) {
    PrimitiveParam primitiveParam = new PrimitiveParam();
    return primitiveParam;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static PrimitiveParam createUpdatedEntity(EntityManager em) {
    PrimitiveParam primitiveParam = new PrimitiveParam();
    return primitiveParam;
  }

  @BeforeEach
  public void initTest() {
    primitiveParam = createEntity(em);
  }

  @Test
  @Transactional
  public void createPrimitiveParam() throws Exception {
    int databaseSizeBeforeCreate = primitiveParamRepository.findAll().size();

    // Create the PrimitiveParam
    PrimitiveParamDTO primitiveParamDTO = primitiveParamMapper.toDto(primitiveParam);
    restPrimitiveParamMockMvc
        .perform(
            post("/api/primitive-params")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(primitiveParamDTO)))
        .andExpect(status().isCreated());

    // Validate the PrimitiveParam in the database
    List<PrimitiveParam> primitiveParamList = primitiveParamRepository.findAll();
    assertThat(primitiveParamList).hasSize(databaseSizeBeforeCreate + 1);
    PrimitiveParam testPrimitiveParam = primitiveParamList.get(primitiveParamList.size() - 1);
  }

  @Test
  @Transactional
  public void createPrimitiveParamWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = primitiveParamRepository.findAll().size();

    // Create the PrimitiveParam with an existing ID
    primitiveParam.setId(1L);
    PrimitiveParamDTO primitiveParamDTO = primitiveParamMapper.toDto(primitiveParam);

    // An entity with an existing ID cannot be created, so this API call must fail
    restPrimitiveParamMockMvc
        .perform(
            post("/api/primitive-params")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(primitiveParamDTO)))
        .andExpect(status().isBadRequest());

    // Validate the PrimitiveParam in the database
    List<PrimitiveParam> primitiveParamList = primitiveParamRepository.findAll();
    assertThat(primitiveParamList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllPrimitiveParams() throws Exception {
    // Initialize the database
    primitiveParamRepository.saveAndFlush(primitiveParam);

    // Get all the primitiveParamList
    restPrimitiveParamMockMvc
        .perform(get("/api/primitive-params?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(primitiveParam.getId().intValue())));
  }

  @Test
  @Transactional
  public void getPrimitiveParam() throws Exception {
    // Initialize the database
    primitiveParamRepository.saveAndFlush(primitiveParam);

    // Get the primitiveParam
    restPrimitiveParamMockMvc
        .perform(get("/api/primitive-params/{id}", primitiveParam.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(primitiveParam.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingPrimitiveParam() throws Exception {
    // Get the primitiveParam
    restPrimitiveParamMockMvc
        .perform(get("/api/primitive-params/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updatePrimitiveParam() throws Exception {
    // Initialize the database
    primitiveParamRepository.saveAndFlush(primitiveParam);

    int databaseSizeBeforeUpdate = primitiveParamRepository.findAll().size();

    // Update the primitiveParam
    PrimitiveParam updatedPrimitiveParam =
        primitiveParamRepository.findById(primitiveParam.getId()).get();
    // Disconnect from session so that the updates on updatedPrimitiveParam are not directly saved
    // in db
    em.detach(updatedPrimitiveParam);
    PrimitiveParamDTO primitiveParamDTO = primitiveParamMapper.toDto(updatedPrimitiveParam);

    restPrimitiveParamMockMvc
        .perform(
            put("/api/primitive-params")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(primitiveParamDTO)))
        .andExpect(status().isOk());

    // Validate the PrimitiveParam in the database
    List<PrimitiveParam> primitiveParamList = primitiveParamRepository.findAll();
    assertThat(primitiveParamList).hasSize(databaseSizeBeforeUpdate);
    PrimitiveParam testPrimitiveParam = primitiveParamList.get(primitiveParamList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingPrimitiveParam() throws Exception {
    int databaseSizeBeforeUpdate = primitiveParamRepository.findAll().size();

    // Create the PrimitiveParam
    PrimitiveParamDTO primitiveParamDTO = primitiveParamMapper.toDto(primitiveParam);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restPrimitiveParamMockMvc
        .perform(
            put("/api/primitive-params")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(primitiveParamDTO)))
        .andExpect(status().isBadRequest());

    // Validate the PrimitiveParam in the database
    List<PrimitiveParam> primitiveParamList = primitiveParamRepository.findAll();
    assertThat(primitiveParamList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deletePrimitiveParam() throws Exception {
    // Initialize the database
    primitiveParamRepository.saveAndFlush(primitiveParam);

    int databaseSizeBeforeDelete = primitiveParamRepository.findAll().size();

    // Delete the primitiveParam
    restPrimitiveParamMockMvc
        .perform(
            delete("/api/primitive-params/{id}", primitiveParam.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<PrimitiveParam> primitiveParamList = primitiveParamRepository.findAll();
    assertThat(primitiveParamList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(PrimitiveParam.class);
    PrimitiveParam primitiveParam1 = new PrimitiveParam();
    primitiveParam1.setId(1L);
    PrimitiveParam primitiveParam2 = new PrimitiveParam();
    primitiveParam2.setId(primitiveParam1.getId());
    assertThat(primitiveParam1).isEqualTo(primitiveParam2);
    primitiveParam2.setId(2L);
    assertThat(primitiveParam1).isNotEqualTo(primitiveParam2);
    primitiveParam1.setId(null);
    assertThat(primitiveParam1).isNotEqualTo(primitiveParam2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(PrimitiveParamDTO.class);
    PrimitiveParamDTO primitiveParamDTO1 = new PrimitiveParamDTO();
    primitiveParamDTO1.setId(1L);
    PrimitiveParamDTO primitiveParamDTO2 = new PrimitiveParamDTO();
    assertThat(primitiveParamDTO1).isNotEqualTo(primitiveParamDTO2);
    primitiveParamDTO2.setId(primitiveParamDTO1.getId());
    assertThat(primitiveParamDTO1).isEqualTo(primitiveParamDTO2);
    primitiveParamDTO2.setId(2L);
    assertThat(primitiveParamDTO1).isNotEqualTo(primitiveParamDTO2);
    primitiveParamDTO1.setId(null);
    assertThat(primitiveParamDTO1).isNotEqualTo(primitiveParamDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(primitiveParamMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(primitiveParamMapper.fromId(null)).isNull();
  }
}
