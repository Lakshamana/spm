package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.AgendaEvent;
import br.ufpa.labes.spm.repository.AgendaEventRepository;
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

/** Integration tests for the {@link AgendaEventResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AgendaEventResourceIT {

  @Autowired private AgendaEventRepository agendaEventRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restAgendaEventMockMvc;

  private AgendaEvent agendaEvent;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final AgendaEventResource agendaEventResource = new AgendaEventResource(agendaEventRepository);
    this.restAgendaEventMockMvc =
        MockMvcBuilders.standaloneSetup(agendaEventResource)
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
  public static AgendaEvent createEntity(EntityManager em) {
    AgendaEvent agendaEvent = new AgendaEvent();
    return agendaEvent;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static AgendaEvent createUpdatedEntity(EntityManager em) {
    AgendaEvent agendaEvent = new AgendaEvent();
    return agendaEvent;
  }

  @BeforeEach
  public void initTest() {
    agendaEvent = createEntity(em);
  }

  @Test
  @Transactional
  public void createAgendaEvent() throws Exception {
    int databaseSizeBeforeCreate = agendaEventRepository.findAll().size();

    // Create the AgendaEvent
    restAgendaEventMockMvc
        .perform(
            post("/api/agenda-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agendaEvent)))
        .andExpect(status().isCreated());

    // Validate the AgendaEvent in the database
    List<AgendaEvent> agendaEventList = agendaEventRepository.findAll();
    assertThat(agendaEventList).hasSize(databaseSizeBeforeCreate + 1);
    AgendaEvent testAgendaEvent = agendaEventList.get(agendaEventList.size() - 1);
  }

  @Test
  @Transactional
  public void createAgendaEventWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = agendaEventRepository.findAll().size();

    // Create the AgendaEvent with an existing ID
    agendaEvent.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restAgendaEventMockMvc
        .perform(
            post("/api/agenda-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agendaEvent)))
        .andExpect(status().isBadRequest());

    // Validate the AgendaEvent in the database
    List<AgendaEvent> agendaEventList = agendaEventRepository.findAll();
    assertThat(agendaEventList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllAgendaEvents() throws Exception {
    // Initialize the database
    agendaEventRepository.saveAndFlush(agendaEvent);

    // Get all the agendaEventList
    restAgendaEventMockMvc
        .perform(get("/api/agenda-events?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(agendaEvent.getId().intValue())));
  }

  @Test
  @Transactional
  public void getAgendaEvent() throws Exception {
    // Initialize the database
    agendaEventRepository.saveAndFlush(agendaEvent);

    // Get the agendaEvent
    restAgendaEventMockMvc
        .perform(get("/api/agenda-events/{id}", agendaEvent.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(agendaEvent.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingAgendaEvent() throws Exception {
    // Get the agendaEvent
    restAgendaEventMockMvc
        .perform(get("/api/agenda-events/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateAgendaEvent() throws Exception {
    // Initialize the database
    agendaEventRepository.saveAndFlush(agendaEvent);

    int databaseSizeBeforeUpdate = agendaEventRepository.findAll().size();

    // Update the agendaEvent
    AgendaEvent updatedAgendaEvent = agendaEventRepository.findById(agendaEvent.getId()).get();
    // Disconnect from session so that the updates on updatedAgendaEvent are not directly saved in
    // db
    em.detach(updatedAgendaEvent);

    restAgendaEventMockMvc
        .perform(
            put("/api/agenda-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAgendaEvent)))
        .andExpect(status().isOk());

    // Validate the AgendaEvent in the database
    List<AgendaEvent> agendaEventList = agendaEventRepository.findAll();
    assertThat(agendaEventList).hasSize(databaseSizeBeforeUpdate);
    AgendaEvent testAgendaEvent = agendaEventList.get(agendaEventList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingAgendaEvent() throws Exception {
    int databaseSizeBeforeUpdate = agendaEventRepository.findAll().size();

    // Create the AgendaEvent

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAgendaEventMockMvc
        .perform(
            put("/api/agenda-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agendaEvent)))
        .andExpect(status().isBadRequest());

    // Validate the AgendaEvent in the database
    List<AgendaEvent> agendaEventList = agendaEventRepository.findAll();
    assertThat(agendaEventList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteAgendaEvent() throws Exception {
    // Initialize the database
    agendaEventRepository.saveAndFlush(agendaEvent);

    int databaseSizeBeforeDelete = agendaEventRepository.findAll().size();

    // Delete the agendaEvent
    restAgendaEventMockMvc
        .perform(
            delete("/api/agenda-events/{id}", agendaEvent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<AgendaEvent> agendaEventList = agendaEventRepository.findAll();
    assertThat(agendaEventList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(AgendaEvent.class);
    AgendaEvent agendaEvent1 = new AgendaEvent();
    agendaEvent1.setId(1L);
    AgendaEvent agendaEvent2 = new AgendaEvent();
    agendaEvent2.setId(agendaEvent1.getId());
    assertThat(agendaEvent1).isEqualTo(agendaEvent2);
    agendaEvent2.setId(2L);
    assertThat(agendaEvent1).isNotEqualTo(agendaEvent2);
    agendaEvent1.setId(null);
    assertThat(agendaEvent1).isNotEqualTo(agendaEvent2);
  }
}
