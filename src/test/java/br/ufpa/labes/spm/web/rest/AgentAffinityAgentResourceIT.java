package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.AgentAffinityAgent;
import br.ufpa.labes.spm.repository.AgentAffinityAgentRepository;
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

/** Integration tests for the {@link AgentAffinityAgentResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AgentAffinityAgentResourceIT {

  private static final Integer DEFAULT_DEGREE = 1;
  private static final Integer UPDATED_DEGREE = 2;
  private static final Integer SMALLER_DEGREE = 1 - 1;

  @Autowired private AgentAffinityAgentRepository agentAffinityAgentRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restAgentAffinityAgentMockMvc;

  private AgentAffinityAgent agentAffinityAgent;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final AgentAffinityAgentResource agentAffinityAgentResource =
        new AgentAffinityAgentResource(agentAffinityAgentRepository);
    this.restAgentAffinityAgentMockMvc =
        MockMvcBuilders.standaloneSetup(agentAffinityAgentResource)
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
  public static AgentAffinityAgent createEntity(EntityManager em) {
    AgentAffinityAgent agentAffinityAgent = new AgentAffinityAgent().degree(DEFAULT_DEGREE);
    return agentAffinityAgent;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static AgentAffinityAgent createUpdatedEntity(EntityManager em) {
    AgentAffinityAgent agentAffinityAgent = new AgentAffinityAgent().degree(UPDATED_DEGREE);
    return agentAffinityAgent;
  }

  @BeforeEach
  public void initTest() {
    agentAffinityAgent = createEntity(em);
  }

  @Test
  @Transactional
  public void createAgentAffinityAgent() throws Exception {
    int databaseSizeBeforeCreate = agentAffinityAgentRepository.findAll().size();

    // Create the AgentAffinityAgent
    restAgentAffinityAgentMockMvc
        .perform(
            post("/api/agent-affinity-agents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agentAffinityAgent)))
        .andExpect(status().isCreated());

    // Validate the AgentAffinityAgent in the database
    List<AgentAffinityAgent> agentAffinityAgentList = agentAffinityAgentRepository.findAll();
    assertThat(agentAffinityAgentList).hasSize(databaseSizeBeforeCreate + 1);
    AgentAffinityAgent testAgentAffinityAgent =
        agentAffinityAgentList.get(agentAffinityAgentList.size() - 1);
    assertThat(testAgentAffinityAgent.getDegree()).isEqualTo(DEFAULT_DEGREE);
  }

  @Test
  @Transactional
  public void createAgentAffinityAgentWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = agentAffinityAgentRepository.findAll().size();

    // Create the AgentAffinityAgent with an existing ID
    agentAffinityAgent.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restAgentAffinityAgentMockMvc
        .perform(
            post("/api/agent-affinity-agents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agentAffinityAgent)))
        .andExpect(status().isBadRequest());

    // Validate the AgentAffinityAgent in the database
    List<AgentAffinityAgent> agentAffinityAgentList = agentAffinityAgentRepository.findAll();
    assertThat(agentAffinityAgentList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllAgentAffinityAgents() throws Exception {
    // Initialize the database
    agentAffinityAgentRepository.saveAndFlush(agentAffinityAgent);

    // Get all the agentAffinityAgentList
    restAgentAffinityAgentMockMvc
        .perform(get("/api/agent-affinity-agents?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(agentAffinityAgent.getId().intValue())))
        .andExpect(jsonPath("$.[*].degree").value(hasItem(DEFAULT_DEGREE)));
  }

  @Test
  @Transactional
  public void getAgentAffinityAgent() throws Exception {
    // Initialize the database
    agentAffinityAgentRepository.saveAndFlush(agentAffinityAgent);

    // Get the agentAffinityAgent
    restAgentAffinityAgentMockMvc
        .perform(get("/api/agent-affinity-agents/{id}", agentAffinityAgent.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(agentAffinityAgent.getId().intValue()))
        .andExpect(jsonPath("$.degree").value(DEFAULT_DEGREE));
  }

  @Test
  @Transactional
  public void getNonExistingAgentAffinityAgent() throws Exception {
    // Get the agentAffinityAgent
    restAgentAffinityAgentMockMvc
        .perform(get("/api/agent-affinity-agents/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateAgentAffinityAgent() throws Exception {
    // Initialize the database
    agentAffinityAgentRepository.saveAndFlush(agentAffinityAgent);

    int databaseSizeBeforeUpdate = agentAffinityAgentRepository.findAll().size();

    // Update the agentAffinityAgent
    AgentAffinityAgent updatedAgentAffinityAgent =
        agentAffinityAgentRepository.findById(agentAffinityAgent.getId()).get();
    // Disconnect from session so that the updates on updatedAgentAffinityAgent are not directly
    // saved in db
    em.detach(updatedAgentAffinityAgent);
    updatedAgentAffinityAgent.degree(UPDATED_DEGREE);

    restAgentAffinityAgentMockMvc
        .perform(
            put("/api/agent-affinity-agents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAgentAffinityAgent)))
        .andExpect(status().isOk());

    // Validate the AgentAffinityAgent in the database
    List<AgentAffinityAgent> agentAffinityAgentList = agentAffinityAgentRepository.findAll();
    assertThat(agentAffinityAgentList).hasSize(databaseSizeBeforeUpdate);
    AgentAffinityAgent testAgentAffinityAgent =
        agentAffinityAgentList.get(agentAffinityAgentList.size() - 1);
    assertThat(testAgentAffinityAgent.getDegree()).isEqualTo(UPDATED_DEGREE);
  }

  @Test
  @Transactional
  public void updateNonExistingAgentAffinityAgent() throws Exception {
    int databaseSizeBeforeUpdate = agentAffinityAgentRepository.findAll().size();

    // Create the AgentAffinityAgent

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAgentAffinityAgentMockMvc
        .perform(
            put("/api/agent-affinity-agents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agentAffinityAgent)))
        .andExpect(status().isBadRequest());

    // Validate the AgentAffinityAgent in the database
    List<AgentAffinityAgent> agentAffinityAgentList = agentAffinityAgentRepository.findAll();
    assertThat(agentAffinityAgentList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteAgentAffinityAgent() throws Exception {
    // Initialize the database
    agentAffinityAgentRepository.saveAndFlush(agentAffinityAgent);

    int databaseSizeBeforeDelete = agentAffinityAgentRepository.findAll().size();

    // Delete the agentAffinityAgent
    restAgentAffinityAgentMockMvc
        .perform(
            delete("/api/agent-affinity-agents/{id}", agentAffinityAgent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<AgentAffinityAgent> agentAffinityAgentList = agentAffinityAgentRepository.findAll();
    assertThat(agentAffinityAgentList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(AgentAffinityAgent.class);
    AgentAffinityAgent agentAffinityAgent1 = new AgentAffinityAgent();
    agentAffinityAgent1.setId(1L);
    AgentAffinityAgent agentAffinityAgent2 = new AgentAffinityAgent();
    agentAffinityAgent2.setId(agentAffinityAgent1.getId());
    assertThat(agentAffinityAgent1).isEqualTo(agentAffinityAgent2);
    agentAffinityAgent2.setId(2L);
    assertThat(agentAffinityAgent1).isNotEqualTo(agentAffinityAgent2);
    agentAffinityAgent1.setId(null);
    assertThat(agentAffinityAgent1).isNotEqualTo(agentAffinityAgent2);
  }
}
