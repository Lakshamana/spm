package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.SpmLog;
import br.ufpa.labes.spm.repository.SpmLogRepository;
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

/** Integration tests for the {@link SpmLogResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class SpmLogResourceIT {

  @Autowired private SpmLogRepository spmLogRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restSpmLogMockMvc;

  private SpmLog spmLog;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final SpmLogResource spmLogResource = new SpmLogResource(spmLogRepository);
    this.restSpmLogMockMvc =
        MockMvcBuilders.standaloneSetup(spmLogResource)
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
  public static SpmLog createEntity(EntityManager em) {
    SpmLog spmLog = new SpmLog();
    return spmLog;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static SpmLog createUpdatedEntity(EntityManager em) {
    SpmLog spmLog = new SpmLog();
    return spmLog;
  }

  @BeforeEach
  public void initTest() {
    spmLog = createEntity(em);
  }

  @Test
  @Transactional
  public void createSpmLog() throws Exception {
    int databaseSizeBeforeCreate = spmLogRepository.findAll().size();

    // Create the SpmLog
    restSpmLogMockMvc
        .perform(
            post("/api/spm-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spmLog)))
        .andExpect(status().isCreated());

    // Validate the SpmLog in the database
    List<SpmLog> spmLogList = spmLogRepository.findAll();
    assertThat(spmLogList).hasSize(databaseSizeBeforeCreate + 1);
    SpmLog testSpmLog = spmLogList.get(spmLogList.size() - 1);
  }

  @Test
  @Transactional
  public void createSpmLogWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = spmLogRepository.findAll().size();

    // Create the SpmLog with an existing ID
    spmLog.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restSpmLogMockMvc
        .perform(
            post("/api/spm-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spmLog)))
        .andExpect(status().isBadRequest());

    // Validate the SpmLog in the database
    List<SpmLog> spmLogList = spmLogRepository.findAll();
    assertThat(spmLogList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllSpmLogs() throws Exception {
    // Initialize the database
    spmLogRepository.saveAndFlush(spmLog);

    // Get all the spmLogList
    restSpmLogMockMvc
        .perform(get("/api/spm-logs?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(spmLog.getId().intValue())));
  }

  @Test
  @Transactional
  public void getSpmLog() throws Exception {
    // Initialize the database
    spmLogRepository.saveAndFlush(spmLog);

    // Get the spmLog
    restSpmLogMockMvc
        .perform(get("/api/spm-logs/{id}", spmLog.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(spmLog.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingSpmLog() throws Exception {
    // Get the spmLog
    restSpmLogMockMvc
        .perform(get("/api/spm-logs/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateSpmLog() throws Exception {
    // Initialize the database
    spmLogRepository.saveAndFlush(spmLog);

    int databaseSizeBeforeUpdate = spmLogRepository.findAll().size();

    // Update the spmLog
    SpmLog updatedSpmLog = spmLogRepository.findById(spmLog.getId()).get();
    // Disconnect from session so that the updates on updatedSpmLog are not directly saved in db
    em.detach(updatedSpmLog);

    restSpmLogMockMvc
        .perform(
            put("/api/spm-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSpmLog)))
        .andExpect(status().isOk());

    // Validate the SpmLog in the database
    List<SpmLog> spmLogList = spmLogRepository.findAll();
    assertThat(spmLogList).hasSize(databaseSizeBeforeUpdate);
    SpmLog testSpmLog = spmLogList.get(spmLogList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingSpmLog() throws Exception {
    int databaseSizeBeforeUpdate = spmLogRepository.findAll().size();

    // Create the SpmLog

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restSpmLogMockMvc
        .perform(
            put("/api/spm-logs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(spmLog)))
        .andExpect(status().isBadRequest());

    // Validate the SpmLog in the database
    List<SpmLog> spmLogList = spmLogRepository.findAll();
    assertThat(spmLogList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteSpmLog() throws Exception {
    // Initialize the database
    spmLogRepository.saveAndFlush(spmLog);

    int databaseSizeBeforeDelete = spmLogRepository.findAll().size();

    // Delete the spmLog
    restSpmLogMockMvc
        .perform(
            delete("/api/spm-logs/{id}", spmLog.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<SpmLog> spmLogList = spmLogRepository.findAll();
    assertThat(spmLogList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(SpmLog.class);
    SpmLog spmLog1 = new SpmLog();
    spmLog1.setId(1L);
    SpmLog spmLog2 = new SpmLog();
    spmLog2.setId(spmLog1.getId());
    assertThat(spmLog1).isEqualTo(spmLog2);
    spmLog2.setId(2L);
    assertThat(spmLog1).isNotEqualTo(spmLog2);
    spmLog1.setId(null);
    assertThat(spmLog1).isNotEqualTo(spmLog2);
  }
}
