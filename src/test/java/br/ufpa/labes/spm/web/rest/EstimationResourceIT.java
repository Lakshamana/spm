package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Estimation;
import br.ufpa.labes.spm.repository.EstimationRepository;
import br.ufpa.labes.spm.service.EstimationService;
import br.ufpa.labes.spm.service.dto.EstimationDTO;
import br.ufpa.labes.spm.service.mapper.EstimationMapper;
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

/** Integration tests for the {@link EstimationResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class EstimationResourceIT {

  private static final Float DEFAULT_VALUE = 1F;
  private static final Float UPDATED_VALUE = 2F;
  private static final Float SMALLER_VALUE = 1F - 1F;

  private static final String DEFAULT_UNIT = "AAAAAAAAAA";
  private static final String UPDATED_UNIT = "BBBBBBBBBB";

  @Autowired private EstimationRepository estimationRepository;

  @Autowired private EstimationMapper estimationMapper;

  @Autowired private EstimationService estimationService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restEstimationMockMvc;

  private Estimation estimation;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final EstimationResource estimationResource = new EstimationResource(estimationService);
    this.restEstimationMockMvc =
        MockMvcBuilders.standaloneSetup(estimationResource)
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
  public static Estimation createEntity(EntityManager em) {
    Estimation estimation = new Estimation().value(DEFAULT_VALUE).unit(DEFAULT_UNIT);
    return estimation;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Estimation createUpdatedEntity(EntityManager em) {
    Estimation estimation = new Estimation().value(UPDATED_VALUE).unit(UPDATED_UNIT);
    return estimation;
  }

  @BeforeEach
  public void initTest() {
    estimation = createEntity(em);
  }

  @Test
  @Transactional
  public void createEstimation() throws Exception {
    int databaseSizeBeforeCreate = estimationRepository.findAll().size();

    // Create the Estimation
    EstimationDTO estimationDTO = estimationMapper.toDto(estimation);
    restEstimationMockMvc
        .perform(
            post("/api/estimations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estimationDTO)))
        .andExpect(status().isCreated());

    // Validate the Estimation in the database
    List<Estimation> estimationList = estimationRepository.findAll();
    assertThat(estimationList).hasSize(databaseSizeBeforeCreate + 1);
    Estimation testEstimation = estimationList.get(estimationList.size() - 1);
    assertThat(testEstimation.getValue()).isEqualTo(DEFAULT_VALUE);
    assertThat(testEstimation.getUnit()).isEqualTo(DEFAULT_UNIT);
  }

  @Test
  @Transactional
  public void createEstimationWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = estimationRepository.findAll().size();

    // Create the Estimation with an existing ID
    estimation.setId(1L);
    EstimationDTO estimationDTO = estimationMapper.toDto(estimation);

    // An entity with an existing ID cannot be created, so this API call must fail
    restEstimationMockMvc
        .perform(
            post("/api/estimations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estimationDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Estimation in the database
    List<Estimation> estimationList = estimationRepository.findAll();
    assertThat(estimationList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllEstimations() throws Exception {
    // Initialize the database
    estimationRepository.saveAndFlush(estimation);

    // Get all the estimationList
    restEstimationMockMvc
        .perform(get("/api/estimations?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(estimation.getId().intValue())))
        .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
        .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())));
  }

  @Test
  @Transactional
  public void getEstimation() throws Exception {
    // Initialize the database
    estimationRepository.saveAndFlush(estimation);

    // Get the estimation
    restEstimationMockMvc
        .perform(get("/api/estimations/{id}", estimation.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(estimation.getId().intValue()))
        .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()))
        .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingEstimation() throws Exception {
    // Get the estimation
    restEstimationMockMvc
        .perform(get("/api/estimations/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateEstimation() throws Exception {
    // Initialize the database
    estimationRepository.saveAndFlush(estimation);

    int databaseSizeBeforeUpdate = estimationRepository.findAll().size();

    // Update the estimation
    Estimation updatedEstimation = estimationRepository.findById(estimation.getId()).get();
    // Disconnect from session so that the updates on updatedEstimation are not directly saved in db
    em.detach(updatedEstimation);
    updatedEstimation.value(UPDATED_VALUE).unit(UPDATED_UNIT);
    EstimationDTO estimationDTO = estimationMapper.toDto(updatedEstimation);

    restEstimationMockMvc
        .perform(
            put("/api/estimations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estimationDTO)))
        .andExpect(status().isOk());

    // Validate the Estimation in the database
    List<Estimation> estimationList = estimationRepository.findAll();
    assertThat(estimationList).hasSize(databaseSizeBeforeUpdate);
    Estimation testEstimation = estimationList.get(estimationList.size() - 1);
    assertThat(testEstimation.getValue()).isEqualTo(UPDATED_VALUE);
    assertThat(testEstimation.getUnit()).isEqualTo(UPDATED_UNIT);
  }

  @Test
  @Transactional
  public void updateNonExistingEstimation() throws Exception {
    int databaseSizeBeforeUpdate = estimationRepository.findAll().size();

    // Create the Estimation
    EstimationDTO estimationDTO = estimationMapper.toDto(estimation);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restEstimationMockMvc
        .perform(
            put("/api/estimations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(estimationDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Estimation in the database
    List<Estimation> estimationList = estimationRepository.findAll();
    assertThat(estimationList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteEstimation() throws Exception {
    // Initialize the database
    estimationRepository.saveAndFlush(estimation);

    int databaseSizeBeforeDelete = estimationRepository.findAll().size();

    // Delete the estimation
    restEstimationMockMvc
        .perform(
            delete("/api/estimations/{id}", estimation.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Estimation> estimationList = estimationRepository.findAll();
    assertThat(estimationList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Estimation.class);
    Estimation estimation1 = new Estimation();
    estimation1.setId(1L);
    Estimation estimation2 = new Estimation();
    estimation2.setId(estimation1.getId());
    assertThat(estimation1).isEqualTo(estimation2);
    estimation2.setId(2L);
    assertThat(estimation1).isNotEqualTo(estimation2);
    estimation1.setId(null);
    assertThat(estimation1).isNotEqualTo(estimation2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(EstimationDTO.class);
    EstimationDTO estimationDTO1 = new EstimationDTO();
    estimationDTO1.setId(1L);
    EstimationDTO estimationDTO2 = new EstimationDTO();
    assertThat(estimationDTO1).isNotEqualTo(estimationDTO2);
    estimationDTO2.setId(estimationDTO1.getId());
    assertThat(estimationDTO1).isEqualTo(estimationDTO2);
    estimationDTO2.setId(2L);
    assertThat(estimationDTO1).isNotEqualTo(estimationDTO2);
    estimationDTO1.setId(null);
    assertThat(estimationDTO1).isNotEqualTo(estimationDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(estimationMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(estimationMapper.fromId(null)).isNull();
  }
}
