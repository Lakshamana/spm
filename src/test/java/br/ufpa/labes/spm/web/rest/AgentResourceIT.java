package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.repository.AgentRepository;
import br.ufpa.labes.spm.service.AgentService;
import br.ufpa.labes.spm.service.dto.AgentDTO;
import br.ufpa.labes.spm.service.mapper.AgentMapper;
import br.ufpa.labes.spm.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static br.ufpa.labes.spm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Integration tests for the {@link AgentResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AgentResourceIT {

  private static final String DEFAULT_IDENT = "AAAAAAAAAA";
  private static final String UPDATED_IDENT = "BBBBBBBBBB";

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
  private static final String UPDATED_EMAIL = "BBBBBBBBBB";

  private static final Float DEFAULT_COST_HOUR = 1F;
  private static final Float UPDATED_COST_HOUR = 2F;
  private static final Float SMALLER_COST_HOUR = 1F - 1F;

  private static final String DEFAULT_PASSWORD_HASH = "AAAAAAAAAA";
  private static final String UPDATED_PASSWORD_HASH = "BBBBBBBBBB";

  private static final Integer DEFAULT_TIPO_USER = 1;
  private static final Integer UPDATED_TIPO_USER = 2;
  private static final Integer SMALLER_TIPO_USER = 1 - 1;

  private static final Boolean DEFAULT_IS_ACTIVE = false;
  private static final Boolean UPDATED_IS_ACTIVE = true;

  private static final Boolean DEFAULT_ONLINE = false;
  private static final Boolean UPDATED_ONLINE = true;

  private static final String DEFAULT_PHOTO_URL = "AAAAAAAAAA";
  private static final String UPDATED_PHOTO_URL = "BBBBBBBBBB";

  private static final String DEFAULT_UPLOAD = "AAAAAAAAAA";
  private static final String UPDATED_UPLOAD = "BBBBBBBBBB";

  @Autowired private AgentRepository agentRepository;

  @Mock private AgentRepository agentRepositoryMock;

  @Autowired private AgentMapper agentMapper;

  @Mock private AgentService agentServiceMock;

  @Autowired private AgentService agentService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restAgentMockMvc;

  private Agent agent;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final AgentResource agentResource = new AgentResource(agentService);
    this.restAgentMockMvc =
        MockMvcBuilders.standaloneSetup(agentResource)
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
  public static Agent createEntity(EntityManager em) {
    Agent agent =
        new Agent()
            .ident(DEFAULT_IDENT)
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .costHour(DEFAULT_COST_HOUR)
            .passwordHash(DEFAULT_PASSWORD_HASH)
            .tipoUser(DEFAULT_TIPO_USER)
            .isActive(DEFAULT_IS_ACTIVE)
            .online(DEFAULT_ONLINE)
            .photoURL(DEFAULT_PHOTO_URL)
            .upload(DEFAULT_UPLOAD);
    return agent;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Agent createUpdatedEntity(EntityManager em) {
    Agent agent =
        new Agent()
            .ident(UPDATED_IDENT)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .costHour(UPDATED_COST_HOUR)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .tipoUser(UPDATED_TIPO_USER)
            .isActive(UPDATED_IS_ACTIVE)
            .online(UPDATED_ONLINE)
            .photoURL(UPDATED_PHOTO_URL)
            .upload(UPDATED_UPLOAD);
    return agent;
  }

  @BeforeEach
  public void initTest() {
    agent = createEntity(em);
  }

  @Test
  @Transactional
  public void createAgent() throws Exception {
    int databaseSizeBeforeCreate = agentRepository.findAll().size();

    // Create the Agent
    AgentDTO agentDTO = agentMapper.toDto(agent);
    restAgentMockMvc
        .perform(
            post("/api/agents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agentDTO)))
        .andExpect(status().isCreated());

    // Validate the Agent in the database
    List<Agent> agentList = agentRepository.findAll();
    assertThat(agentList).hasSize(databaseSizeBeforeCreate + 1);
    Agent testAgent = agentList.get(agentList.size() - 1);
    assertThat(testAgent.getIdent()).isEqualTo(DEFAULT_IDENT);
    assertThat(testAgent.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testAgent.getEmail()).isEqualTo(DEFAULT_EMAIL);
    assertThat(testAgent.getCostHour()).isEqualTo(DEFAULT_COST_HOUR);
    assertThat(testAgent.getPasswordHash()).isEqualTo(DEFAULT_PASSWORD_HASH);
    assertThat(testAgent.getTipoUser()).isEqualTo(DEFAULT_TIPO_USER);
    assertThat(testAgent.isActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    assertThat(testAgent.isOnline()).isEqualTo(DEFAULT_ONLINE);
    assertThat(testAgent.getPhotoURL()).isEqualTo(DEFAULT_PHOTO_URL);
    assertThat(testAgent.getUpload()).isEqualTo(DEFAULT_UPLOAD);
  }

  @Test
  @Transactional
  public void createAgentWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = agentRepository.findAll().size();

    // Create the Agent with an existing ID
    agent.setId(1L);
    AgentDTO agentDTO = agentMapper.toDto(agent);

    // An entity with an existing ID cannot be created, so this API call must fail
    restAgentMockMvc
        .perform(
            post("/api/agents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agentDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Agent in the database
    List<Agent> agentList = agentRepository.findAll();
    assertThat(agentList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllAgents() throws Exception {
    // Initialize the database
    agentRepository.saveAndFlush(agent);

    // Get all the agentList
    restAgentMockMvc
        .perform(get("/api/agents?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(agent.getId().intValue())))
        .andExpect(jsonPath("$.[*].ident").value(hasItem(DEFAULT_IDENT.toString())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
        .andExpect(jsonPath("$.[*].costHour").value(hasItem(DEFAULT_COST_HOUR.doubleValue())))
        .andExpect(jsonPath("$.[*].passwordHash").value(hasItem(DEFAULT_PASSWORD_HASH.toString())))
        .andExpect(jsonPath("$.[*].tipoUser").value(hasItem(DEFAULT_TIPO_USER)))
        .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
        .andExpect(jsonPath("$.[*].online").value(hasItem(DEFAULT_ONLINE.booleanValue())))
        .andExpect(jsonPath("$.[*].photoURL").value(hasItem(DEFAULT_PHOTO_URL.toString())))
        .andExpect(jsonPath("$.[*].upload").value(hasItem(DEFAULT_UPLOAD.toString())));
  }

  @SuppressWarnings({"unchecked"})
  public void getAllAgentsWithEagerRelationshipsIsEnabled() throws Exception {
    AgentResource agentResource = new AgentResource(agentServiceMock);
    when(agentServiceMock.findAllWithEagerRelationships(any()))
        .thenReturn(new PageImpl(new ArrayList<>()));

    MockMvc restAgentMockMvc =
        MockMvcBuilders.standaloneSetup(agentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .build();

    restAgentMockMvc.perform(get("/api/agents?eagerload=true")).andExpect(status().isOk());

    verify(agentServiceMock, times(1)).findAllWithEagerRelationships(any());
  }

  @SuppressWarnings({"unchecked"})
  public void getAllAgentsWithEagerRelationshipsIsNotEnabled() throws Exception {
    AgentResource agentResource = new AgentResource(agentServiceMock);
    when(agentServiceMock.findAllWithEagerRelationships(any()))
        .thenReturn(new PageImpl(new ArrayList<>()));
    MockMvc restAgentMockMvc =
        MockMvcBuilders.standaloneSetup(agentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .build();

    restAgentMockMvc.perform(get("/api/agents?eagerload=true")).andExpect(status().isOk());

    verify(agentServiceMock, times(1)).findAllWithEagerRelationships(any());
  }

  @Test
  @Transactional
  public void getAgent() throws Exception {
    // Initialize the database
    agentRepository.saveAndFlush(agent);

    // Get the agent
    restAgentMockMvc
        .perform(get("/api/agents/{id}", agent.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(agent.getId().intValue()))
        .andExpect(jsonPath("$.ident").value(DEFAULT_IDENT.toString()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
        .andExpect(jsonPath("$.costHour").value(DEFAULT_COST_HOUR.doubleValue()))
        .andExpect(jsonPath("$.passwordHash").value(DEFAULT_PASSWORD_HASH.toString()))
        .andExpect(jsonPath("$.tipoUser").value(DEFAULT_TIPO_USER))
        .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
        .andExpect(jsonPath("$.online").value(DEFAULT_ONLINE.booleanValue()))
        .andExpect(jsonPath("$.photoURL").value(DEFAULT_PHOTO_URL.toString()))
        .andExpect(jsonPath("$.upload").value(DEFAULT_UPLOAD.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingAgent() throws Exception {
    // Get the agent
    restAgentMockMvc
        .perform(get("/api/agents/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateAgent() throws Exception {
    // Initialize the database
    agentRepository.saveAndFlush(agent);

    int databaseSizeBeforeUpdate = agentRepository.findAll().size();

    // Update the agent
    Agent updatedAgent = agentRepository.findById(agent.getId()).get();
    // Disconnect from session so that the updates on updatedAgent are not directly saved in db
    em.detach(updatedAgent);
    updatedAgent
        .ident(UPDATED_IDENT)
        .name(UPDATED_NAME)
        .email(UPDATED_EMAIL)
        .costHour(UPDATED_COST_HOUR)
        .passwordHash(UPDATED_PASSWORD_HASH)
        .tipoUser(UPDATED_TIPO_USER)
        .isActive(UPDATED_IS_ACTIVE)
        .online(UPDATED_ONLINE)
        .photoURL(UPDATED_PHOTO_URL)
        .upload(UPDATED_UPLOAD);
    AgentDTO agentDTO = agentMapper.toDto(updatedAgent);

    restAgentMockMvc
        .perform(
            put("/api/agents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agentDTO)))
        .andExpect(status().isOk());

    // Validate the Agent in the database
    List<Agent> agentList = agentRepository.findAll();
    assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    Agent testAgent = agentList.get(agentList.size() - 1);
    assertThat(testAgent.getIdent()).isEqualTo(UPDATED_IDENT);
    assertThat(testAgent.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testAgent.getEmail()).isEqualTo(UPDATED_EMAIL);
    assertThat(testAgent.getCostHour()).isEqualTo(UPDATED_COST_HOUR);
    assertThat(testAgent.getPasswordHash()).isEqualTo(UPDATED_PASSWORD_HASH);
    assertThat(testAgent.getTipoUser()).isEqualTo(UPDATED_TIPO_USER);
    assertThat(testAgent.isActive()).isEqualTo(UPDATED_IS_ACTIVE);
    assertThat(testAgent.isOnline()).isEqualTo(UPDATED_ONLINE);
    assertThat(testAgent.getPhotoURL()).isEqualTo(UPDATED_PHOTO_URL);
    assertThat(testAgent.getUpload()).isEqualTo(UPDATED_UPLOAD);
  }

  @Test
  @Transactional
  public void updateNonExistingAgent() throws Exception {
    int databaseSizeBeforeUpdate = agentRepository.findAll().size();

    // Create the Agent
    AgentDTO agentDTO = agentMapper.toDto(agent);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAgentMockMvc
        .perform(
            put("/api/agents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agentDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Agent in the database
    List<Agent> agentList = agentRepository.findAll();
    assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteAgent() throws Exception {
    // Initialize the database
    agentRepository.saveAndFlush(agent);

    int databaseSizeBeforeDelete = agentRepository.findAll().size();

    // Delete the agent
    restAgentMockMvc
        .perform(delete("/api/agents/{id}", agent.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Agent> agentList = agentRepository.findAll();
    assertThat(agentList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Agent.class);
    Agent agent1 = new Agent();
    agent1.setId(1L);
    Agent agent2 = new Agent();
    agent2.setId(agent1.getId());
    assertThat(agent1).isEqualTo(agent2);
    agent2.setId(2L);
    assertThat(agent1).isNotEqualTo(agent2);
    agent1.setId(null);
    assertThat(agent1).isNotEqualTo(agent2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(AgentDTO.class);
    AgentDTO agentDTO1 = new AgentDTO();
    agentDTO1.setId(1L);
    AgentDTO agentDTO2 = new AgentDTO();
    assertThat(agentDTO1).isNotEqualTo(agentDTO2);
    agentDTO2.setId(agentDTO1.getId());
    assertThat(agentDTO1).isEqualTo(agentDTO2);
    agentDTO2.setId(2L);
    assertThat(agentDTO1).isNotEqualTo(agentDTO2);
    agentDTO1.setId(null);
    assertThat(agentDTO1).isNotEqualTo(agentDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(agentMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(agentMapper.fromId(null)).isNull();
  }
}
