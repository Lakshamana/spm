package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.AgentPlaysRole;
import br.ufpa.labes.spm.repository.AgentPlaysRoleRepository;
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

/** Integration tests for the {@link AgentPlaysRoleResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AgentPlaysRoleResourceIT {

  private static final LocalDate DEFAULT_SINCE_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_SINCE_DATE = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate SMALLER_SINCE_DATE = LocalDate.ofEpochDay(-1L);

  @Autowired private AgentPlaysRoleRepository agentPlaysRoleRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restAgentPlaysRoleMockMvc;

  private AgentPlaysRole agentPlaysRole;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final AgentPlaysRoleResource agentPlaysRoleResource =
        new AgentPlaysRoleResource(agentPlaysRoleRepository);
    this.restAgentPlaysRoleMockMvc =
        MockMvcBuilders.standaloneSetup(agentPlaysRoleResource)
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
  public static AgentPlaysRole createEntity(EntityManager em) {
    AgentPlaysRole agentPlaysRole = new AgentPlaysRole().sinceDate(DEFAULT_SINCE_DATE);
    return agentPlaysRole;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static AgentPlaysRole createUpdatedEntity(EntityManager em) {
    AgentPlaysRole agentPlaysRole = new AgentPlaysRole().sinceDate(UPDATED_SINCE_DATE);
    return agentPlaysRole;
  }

  @BeforeEach
  public void initTest() {
    agentPlaysRole = createEntity(em);
  }

  @Test
  @Transactional
  public void createAgentPlaysRole() throws Exception {
    int databaseSizeBeforeCreate = agentPlaysRoleRepository.findAll().size();

    // Create the AgentPlaysRole
    restAgentPlaysRoleMockMvc
        .perform(
            post("/api/agent-plays-roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agentPlaysRole)))
        .andExpect(status().isCreated());

    // Validate the AgentPlaysRole in the database
    List<AgentPlaysRole> agentPlaysRoleList = agentPlaysRoleRepository.findAll();
    assertThat(agentPlaysRoleList).hasSize(databaseSizeBeforeCreate + 1);
    AgentPlaysRole testAgentPlaysRole = agentPlaysRoleList.get(agentPlaysRoleList.size() - 1);
    assertThat(testAgentPlaysRole.getSinceDate()).isEqualTo(DEFAULT_SINCE_DATE);
  }

  @Test
  @Transactional
  public void createAgentPlaysRoleWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = agentPlaysRoleRepository.findAll().size();

    // Create the AgentPlaysRole with an existing ID
    agentPlaysRole.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restAgentPlaysRoleMockMvc
        .perform(
            post("/api/agent-plays-roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agentPlaysRole)))
        .andExpect(status().isBadRequest());

    // Validate the AgentPlaysRole in the database
    List<AgentPlaysRole> agentPlaysRoleList = agentPlaysRoleRepository.findAll();
    assertThat(agentPlaysRoleList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllAgentPlaysRoles() throws Exception {
    // Initialize the database
    agentPlaysRoleRepository.saveAndFlush(agentPlaysRole);

    // Get all the agentPlaysRoleList
    restAgentPlaysRoleMockMvc
        .perform(get("/api/agent-plays-roles?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(agentPlaysRole.getId().intValue())))
        .andExpect(jsonPath("$.[*].sinceDate").value(hasItem(DEFAULT_SINCE_DATE.toString())));
  }

  @Test
  @Transactional
  public void getAgentPlaysRole() throws Exception {
    // Initialize the database
    agentPlaysRoleRepository.saveAndFlush(agentPlaysRole);

    // Get the agentPlaysRole
    restAgentPlaysRoleMockMvc
        .perform(get("/api/agent-plays-roles/{id}", agentPlaysRole.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(agentPlaysRole.getId().intValue()))
        .andExpect(jsonPath("$.sinceDate").value(DEFAULT_SINCE_DATE.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingAgentPlaysRole() throws Exception {
    // Get the agentPlaysRole
    restAgentPlaysRoleMockMvc
        .perform(get("/api/agent-plays-roles/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateAgentPlaysRole() throws Exception {
    // Initialize the database
    agentPlaysRoleRepository.saveAndFlush(agentPlaysRole);

    int databaseSizeBeforeUpdate = agentPlaysRoleRepository.findAll().size();

    // Update the agentPlaysRole
    AgentPlaysRole updatedAgentPlaysRole =
        agentPlaysRoleRepository.findById(agentPlaysRole.getId()).get();
    // Disconnect from session so that the updates on updatedAgentPlaysRole are not directly saved
    // in db
    em.detach(updatedAgentPlaysRole);
    updatedAgentPlaysRole.sinceDate(UPDATED_SINCE_DATE);

    restAgentPlaysRoleMockMvc
        .perform(
            put("/api/agent-plays-roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAgentPlaysRole)))
        .andExpect(status().isOk());

    // Validate the AgentPlaysRole in the database
    List<AgentPlaysRole> agentPlaysRoleList = agentPlaysRoleRepository.findAll();
    assertThat(agentPlaysRoleList).hasSize(databaseSizeBeforeUpdate);
    AgentPlaysRole testAgentPlaysRole = agentPlaysRoleList.get(agentPlaysRoleList.size() - 1);
    assertThat(testAgentPlaysRole.getSinceDate()).isEqualTo(UPDATED_SINCE_DATE);
  }

  @Test
  @Transactional
  public void updateNonExistingAgentPlaysRole() throws Exception {
    int databaseSizeBeforeUpdate = agentPlaysRoleRepository.findAll().size();

    // Create the AgentPlaysRole

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAgentPlaysRoleMockMvc
        .perform(
            put("/api/agent-plays-roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agentPlaysRole)))
        .andExpect(status().isBadRequest());

    // Validate the AgentPlaysRole in the database
    List<AgentPlaysRole> agentPlaysRoleList = agentPlaysRoleRepository.findAll();
    assertThat(agentPlaysRoleList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteAgentPlaysRole() throws Exception {
    // Initialize the database
    agentPlaysRoleRepository.saveAndFlush(agentPlaysRole);

    int databaseSizeBeforeDelete = agentPlaysRoleRepository.findAll().size();

    // Delete the agentPlaysRole
    restAgentPlaysRoleMockMvc
        .perform(
            delete("/api/agent-plays-roles/{id}", agentPlaysRole.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<AgentPlaysRole> agentPlaysRoleList = agentPlaysRoleRepository.findAll();
    assertThat(agentPlaysRoleList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(AgentPlaysRole.class);
    AgentPlaysRole agentPlaysRole1 = new AgentPlaysRole();
    agentPlaysRole1.setId(1L);
    AgentPlaysRole agentPlaysRole2 = new AgentPlaysRole();
    agentPlaysRole2.setId(agentPlaysRole1.getId());
    assertThat(agentPlaysRole1).isEqualTo(agentPlaysRole2);
    agentPlaysRole2.setId(2L);
    assertThat(agentPlaysRole1).isNotEqualTo(agentPlaysRole2);
    agentPlaysRole1.setId(null);
    assertThat(agentPlaysRole1).isNotEqualTo(agentPlaysRole2);
  }
}
