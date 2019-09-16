package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ProcessModelEvent;
import br.ufpa.labes.spm.repository.ProcessModelEventRepository;
import br.ufpa.labes.spm.service.ProcessModelEventService;
import br.ufpa.labes.spm.service.dto.ProcessModelEventDTO;
import br.ufpa.labes.spm.service.mapper.ProcessModelEventMapper;
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

/** Integration tests for the {@link ProcessModelEventResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ProcessModelEventResourceIT {

  @Autowired private ProcessModelEventRepository processModelEventRepository;

  @Autowired private ProcessModelEventMapper processModelEventMapper;

  @Autowired private ProcessModelEventService processModelEventService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restProcessModelEventMockMvc;

  private ProcessModelEvent processModelEvent;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ProcessModelEventResource processModelEventResource =
        new ProcessModelEventResource(processModelEventService);
    this.restProcessModelEventMockMvc =
        MockMvcBuilders.standaloneSetup(processModelEventResource)
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
  public static ProcessModelEvent createEntity(EntityManager em) {
    ProcessModelEvent processModelEvent = new ProcessModelEvent();
    return processModelEvent;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ProcessModelEvent createUpdatedEntity(EntityManager em) {
    ProcessModelEvent processModelEvent = new ProcessModelEvent();
    return processModelEvent;
  }

  @BeforeEach
  public void initTest() {
    processModelEvent = createEntity(em);
  }

  @Test
  @Transactional
  public void createProcessModelEvent() throws Exception {
    int databaseSizeBeforeCreate = processModelEventRepository.findAll().size();

    // Create the ProcessModelEvent
    ProcessModelEventDTO processModelEventDTO = processModelEventMapper.toDto(processModelEvent);
    restProcessModelEventMockMvc
        .perform(
            post("/api/process-model-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(processModelEventDTO)))
        .andExpect(status().isCreated());

    // Validate the ProcessModelEvent in the database
    List<ProcessModelEvent> processModelEventList = processModelEventRepository.findAll();
    assertThat(processModelEventList).hasSize(databaseSizeBeforeCreate + 1);
    ProcessModelEvent testProcessModelEvent =
        processModelEventList.get(processModelEventList.size() - 1);
  }

  @Test
  @Transactional
  public void createProcessModelEventWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = processModelEventRepository.findAll().size();

    // Create the ProcessModelEvent with an existing ID
    processModelEvent.setId(1L);
    ProcessModelEventDTO processModelEventDTO = processModelEventMapper.toDto(processModelEvent);

    // An entity with an existing ID cannot be created, so this API call must fail
    restProcessModelEventMockMvc
        .perform(
            post("/api/process-model-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(processModelEventDTO)))
        .andExpect(status().isBadRequest());

    // Validate the ProcessModelEvent in the database
    List<ProcessModelEvent> processModelEventList = processModelEventRepository.findAll();
    assertThat(processModelEventList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllProcessModelEvents() throws Exception {
    // Initialize the database
    processModelEventRepository.saveAndFlush(processModelEvent);

    // Get all the processModelEventList
    restProcessModelEventMockMvc
        .perform(get("/api/process-model-events?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(processModelEvent.getId().intValue())));
  }

  @Test
  @Transactional
  public void getProcessModelEvent() throws Exception {
    // Initialize the database
    processModelEventRepository.saveAndFlush(processModelEvent);

    // Get the processModelEvent
    restProcessModelEventMockMvc
        .perform(get("/api/process-model-events/{id}", processModelEvent.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(processModelEvent.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingProcessModelEvent() throws Exception {
    // Get the processModelEvent
    restProcessModelEventMockMvc
        .perform(get("/api/process-model-events/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateProcessModelEvent() throws Exception {
    // Initialize the database
    processModelEventRepository.saveAndFlush(processModelEvent);

    int databaseSizeBeforeUpdate = processModelEventRepository.findAll().size();

    // Update the processModelEvent
    ProcessModelEvent updatedProcessModelEvent =
        processModelEventRepository.findById(processModelEvent.getId()).get();
    // Disconnect from session so that the updates on updatedProcessModelEvent are not directly
    // saved in db
    em.detach(updatedProcessModelEvent);
    ProcessModelEventDTO processModelEventDTO =
        processModelEventMapper.toDto(updatedProcessModelEvent);

    restProcessModelEventMockMvc
        .perform(
            put("/api/process-model-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(processModelEventDTO)))
        .andExpect(status().isOk());

    // Validate the ProcessModelEvent in the database
    List<ProcessModelEvent> processModelEventList = processModelEventRepository.findAll();
    assertThat(processModelEventList).hasSize(databaseSizeBeforeUpdate);
    ProcessModelEvent testProcessModelEvent =
        processModelEventList.get(processModelEventList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingProcessModelEvent() throws Exception {
    int databaseSizeBeforeUpdate = processModelEventRepository.findAll().size();

    // Create the ProcessModelEvent
    ProcessModelEventDTO processModelEventDTO = processModelEventMapper.toDto(processModelEvent);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restProcessModelEventMockMvc
        .perform(
            put("/api/process-model-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(processModelEventDTO)))
        .andExpect(status().isBadRequest());

    // Validate the ProcessModelEvent in the database
    List<ProcessModelEvent> processModelEventList = processModelEventRepository.findAll();
    assertThat(processModelEventList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteProcessModelEvent() throws Exception {
    // Initialize the database
    processModelEventRepository.saveAndFlush(processModelEvent);

    int databaseSizeBeforeDelete = processModelEventRepository.findAll().size();

    // Delete the processModelEvent
    restProcessModelEventMockMvc
        .perform(
            delete("/api/process-model-events/{id}", processModelEvent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ProcessModelEvent> processModelEventList = processModelEventRepository.findAll();
    assertThat(processModelEventList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ProcessModelEvent.class);
    ProcessModelEvent processModelEvent1 = new ProcessModelEvent();
    processModelEvent1.setId(1L);
    ProcessModelEvent processModelEvent2 = new ProcessModelEvent();
    processModelEvent2.setId(processModelEvent1.getId());
    assertThat(processModelEvent1).isEqualTo(processModelEvent2);
    processModelEvent2.setId(2L);
    assertThat(processModelEvent1).isNotEqualTo(processModelEvent2);
    processModelEvent1.setId(null);
    assertThat(processModelEvent1).isNotEqualTo(processModelEvent2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(ProcessModelEventDTO.class);
    ProcessModelEventDTO processModelEventDTO1 = new ProcessModelEventDTO();
    processModelEventDTO1.setId(1L);
    ProcessModelEventDTO processModelEventDTO2 = new ProcessModelEventDTO();
    assertThat(processModelEventDTO1).isNotEqualTo(processModelEventDTO2);
    processModelEventDTO2.setId(processModelEventDTO1.getId());
    assertThat(processModelEventDTO1).isEqualTo(processModelEventDTO2);
    processModelEventDTO2.setId(2L);
    assertThat(processModelEventDTO1).isNotEqualTo(processModelEventDTO2);
    processModelEventDTO1.setId(null);
    assertThat(processModelEventDTO1).isNotEqualTo(processModelEventDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(processModelEventMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(processModelEventMapper.fromId(null)).isNull();
  }
}
