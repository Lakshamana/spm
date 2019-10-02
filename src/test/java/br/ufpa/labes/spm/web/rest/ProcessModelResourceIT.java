package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ProcessModel;
import br.ufpa.labes.spm.repository.ProcessModelRepository;
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

/** Integration tests for the {@link ProcessModelResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ProcessModelResourceIT {

  private static final String DEFAULT_REQUIREMENTS = "AAAAAAAAAA";
  private static final String UPDATED_REQUIREMENTS = "BBBBBBBBBB";

  private static final String DEFAULT_PM_STATE = "AAAAAAAAAA";
  private static final String UPDATED_PM_STATE = "BBBBBBBBBB";

  @Autowired private ProcessModelRepository processModelRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restProcessModelMockMvc;

  private ProcessModel processModel;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ProcessModelResource processModelResource =
        new ProcessModelResource(processModelRepository);
    this.restProcessModelMockMvc =
        MockMvcBuilders.standaloneSetup(processModelResource)
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
  public static ProcessModel createEntity(EntityManager em) {
    ProcessModel processModel =
        new ProcessModel().requirements(DEFAULT_REQUIREMENTS).pmState(DEFAULT_PM_STATE);
    return processModel;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ProcessModel createUpdatedEntity(EntityManager em) {
    ProcessModel processModel =
        new ProcessModel().requirements(UPDATED_REQUIREMENTS).pmState(UPDATED_PM_STATE);
    return processModel;
  }

  @BeforeEach
  public void initTest() {
    processModel = createEntity(em);
  }

  @Test
  @Transactional
  public void createProcessModel() throws Exception {
    int databaseSizeBeforeCreate = processModelRepository.findAll().size();

    // Create the ProcessModel
    restProcessModelMockMvc
        .perform(
            post("/api/process-models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(processModel)))
        .andExpect(status().isCreated());

    // Validate the ProcessModel in the database
    List<ProcessModel> processModelList = processModelRepository.findAll();
    assertThat(processModelList).hasSize(databaseSizeBeforeCreate + 1);
    ProcessModel testProcessModel = processModelList.get(processModelList.size() - 1);
    assertThat(testProcessModel.getRequirements()).isEqualTo(DEFAULT_REQUIREMENTS);
    assertThat(testProcessModel.getPmState()).isEqualTo(DEFAULT_PM_STATE);
  }

  @Test
  @Transactional
  public void createProcessModelWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = processModelRepository.findAll().size();

    // Create the ProcessModel with an existing ID
    processModel.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restProcessModelMockMvc
        .perform(
            post("/api/process-models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(processModel)))
        .andExpect(status().isBadRequest());

    // Validate the ProcessModel in the database
    List<ProcessModel> processModelList = processModelRepository.findAll();
    assertThat(processModelList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllProcessModels() throws Exception {
    // Initialize the database
    processModelRepository.saveAndFlush(processModel);

    // Get all the processModelList
    restProcessModelMockMvc
        .perform(get("/api/process-models?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(processModel.getId().intValue())))
        .andExpect(jsonPath("$.[*].requirements").value(hasItem(DEFAULT_REQUIREMENTS.toString())))
        .andExpect(jsonPath("$.[*].pmState").value(hasItem(DEFAULT_PM_STATE.toString())));
  }

  @Test
  @Transactional
  public void getProcessModel() throws Exception {
    // Initialize the database
    processModelRepository.saveAndFlush(processModel);

    // Get the processModel
    restProcessModelMockMvc
        .perform(get("/api/process-models/{id}", processModel.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(processModel.getId().intValue()))
        .andExpect(jsonPath("$.requirements").value(DEFAULT_REQUIREMENTS.toString()))
        .andExpect(jsonPath("$.pmState").value(DEFAULT_PM_STATE.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingProcessModel() throws Exception {
    // Get the processModel
    restProcessModelMockMvc
        .perform(get("/api/process-models/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateProcessModel() throws Exception {
    // Initialize the database
    processModelRepository.saveAndFlush(processModel);

    int databaseSizeBeforeUpdate = processModelRepository.findAll().size();

    // Update the processModel
    ProcessModel updatedProcessModel = processModelRepository.findById(processModel.getId()).get();
    // Disconnect from session so that the updates on updatedProcessModel are not directly saved in
    // db
    em.detach(updatedProcessModel);
    updatedProcessModel.requirements(UPDATED_REQUIREMENTS).pmState(UPDATED_PM_STATE);

    restProcessModelMockMvc
        .perform(
            put("/api/process-models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProcessModel)))
        .andExpect(status().isOk());

    // Validate the ProcessModel in the database
    List<ProcessModel> processModelList = processModelRepository.findAll();
    assertThat(processModelList).hasSize(databaseSizeBeforeUpdate);
    ProcessModel testProcessModel = processModelList.get(processModelList.size() - 1);
    assertThat(testProcessModel.getRequirements()).isEqualTo(UPDATED_REQUIREMENTS);
    assertThat(testProcessModel.getPmState()).isEqualTo(UPDATED_PM_STATE);
  }

  @Test
  @Transactional
  public void updateNonExistingProcessModel() throws Exception {
    int databaseSizeBeforeUpdate = processModelRepository.findAll().size();

    // Create the ProcessModel

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restProcessModelMockMvc
        .perform(
            put("/api/process-models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(processModel)))
        .andExpect(status().isBadRequest());

    // Validate the ProcessModel in the database
    List<ProcessModel> processModelList = processModelRepository.findAll();
    assertThat(processModelList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteProcessModel() throws Exception {
    // Initialize the database
    processModelRepository.saveAndFlush(processModel);

    int databaseSizeBeforeDelete = processModelRepository.findAll().size();

    // Delete the processModel
    restProcessModelMockMvc
        .perform(
            delete("/api/process-models/{id}", processModel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ProcessModel> processModelList = processModelRepository.findAll();
    assertThat(processModelList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ProcessModel.class);
    ProcessModel processModel1 = new ProcessModel();
    processModel1.setId(1L);
    ProcessModel processModel2 = new ProcessModel();
    processModel2.setId(processModel1.getId());
    assertThat(processModel1).isEqualTo(processModel2);
    processModel2.setId(2L);
    assertThat(processModel1).isNotEqualTo(processModel2);
    processModel1.setId(null);
    assertThat(processModel1).isNotEqualTo(processModel2);
  }
}
