package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.AgentHasAbility;
import br.ufpa.labes.spm.repository.AgentHasAbilityRepository;
import br.ufpa.labes.spm.service.AgentHasAbilityService;
import br.ufpa.labes.spm.service.dto.AgentHasAbilityDTO;
import br.ufpa.labes.spm.service.mapper.AgentHasAbilityMapper;
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

/** Integration tests for the {@link AgentHasAbilityResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AgentHasAbilityResourceIT {

  private static final Integer DEFAULT_DEGREE = 1;
  private static final Integer UPDATED_DEGREE = 2;
  private static final Integer SMALLER_DEGREE = 1 - 1;

  @Autowired private AgentHasAbilityRepository agentHasAbilityRepository;

  @Autowired private AgentHasAbilityMapper agentHasAbilityMapper;

  @Autowired private AgentHasAbilityService agentHasAbilityService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restAgentHasAbilityMockMvc;

  private AgentHasAbility agentHasAbility;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final AgentHasAbilityResource agentHasAbilityResource =
        new AgentHasAbilityResource(agentHasAbilityService);
    this.restAgentHasAbilityMockMvc =
        MockMvcBuilders.standaloneSetup(agentHasAbilityResource)
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
  public static AgentHasAbility createEntity(EntityManager em) {
    AgentHasAbility agentHasAbility = new AgentHasAbility().degree(DEFAULT_DEGREE);
    return agentHasAbility;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static AgentHasAbility createUpdatedEntity(EntityManager em) {
    AgentHasAbility agentHasAbility = new AgentHasAbility().degree(UPDATED_DEGREE);
    return agentHasAbility;
  }

  @BeforeEach
  public void initTest() {
    agentHasAbility = createEntity(em);
  }

  @Test
  @Transactional
  public void createAgentHasAbility() throws Exception {
    int databaseSizeBeforeCreate = agentHasAbilityRepository.findAll().size();

    // Create the AgentHasAbility
    AgentHasAbilityDTO agentHasAbilityDTO = agentHasAbilityMapper.toDto(agentHasAbility);
    restAgentHasAbilityMockMvc
        .perform(
            post("/api/agent-has-abilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agentHasAbilityDTO)))
        .andExpect(status().isCreated());

    // Validate the AgentHasAbility in the database
    List<AgentHasAbility> agentHasAbilityList = agentHasAbilityRepository.findAll();
    assertThat(agentHasAbilityList).hasSize(databaseSizeBeforeCreate + 1);
    AgentHasAbility testAgentHasAbility = agentHasAbilityList.get(agentHasAbilityList.size() - 1);
    assertThat(testAgentHasAbility.getDegree()).isEqualTo(DEFAULT_DEGREE);
  }

  @Test
  @Transactional
  public void createAgentHasAbilityWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = agentHasAbilityRepository.findAll().size();

    // Create the AgentHasAbility with an existing ID
    agentHasAbility.setId(1L);
    AgentHasAbilityDTO agentHasAbilityDTO = agentHasAbilityMapper.toDto(agentHasAbility);

    // An entity with an existing ID cannot be created, so this API call must fail
    restAgentHasAbilityMockMvc
        .perform(
            post("/api/agent-has-abilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agentHasAbilityDTO)))
        .andExpect(status().isBadRequest());

    // Validate the AgentHasAbility in the database
    List<AgentHasAbility> agentHasAbilityList = agentHasAbilityRepository.findAll();
    assertThat(agentHasAbilityList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllAgentHasAbilities() throws Exception {
    // Initialize the database
    agentHasAbilityRepository.saveAndFlush(agentHasAbility);

    // Get all the agentHasAbilityList
    restAgentHasAbilityMockMvc
        .perform(get("/api/agent-has-abilities?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(agentHasAbility.getId().intValue())))
        .andExpect(jsonPath("$.[*].degree").value(hasItem(DEFAULT_DEGREE)));
  }

  @Test
  @Transactional
  public void getAgentHasAbility() throws Exception {
    // Initialize the database
    agentHasAbilityRepository.saveAndFlush(agentHasAbility);

    // Get the agentHasAbility
    restAgentHasAbilityMockMvc
        .perform(get("/api/agent-has-abilities/{id}", agentHasAbility.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(agentHasAbility.getId().intValue()))
        .andExpect(jsonPath("$.degree").value(DEFAULT_DEGREE));
  }

  @Test
  @Transactional
  public void getNonExistingAgentHasAbility() throws Exception {
    // Get the agentHasAbility
    restAgentHasAbilityMockMvc
        .perform(get("/api/agent-has-abilities/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateAgentHasAbility() throws Exception {
    // Initialize the database
    agentHasAbilityRepository.saveAndFlush(agentHasAbility);

    int databaseSizeBeforeUpdate = agentHasAbilityRepository.findAll().size();

    // Update the agentHasAbility
    AgentHasAbility updatedAgentHasAbility =
        agentHasAbilityRepository.findById(agentHasAbility.getId()).get();
    // Disconnect from session so that the updates on updatedAgentHasAbility are not directly saved
    // in db
    em.detach(updatedAgentHasAbility);
    updatedAgentHasAbility.degree(UPDATED_DEGREE);
    AgentHasAbilityDTO agentHasAbilityDTO = agentHasAbilityMapper.toDto(updatedAgentHasAbility);

    restAgentHasAbilityMockMvc
        .perform(
            put("/api/agent-has-abilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agentHasAbilityDTO)))
        .andExpect(status().isOk());

    // Validate the AgentHasAbility in the database
    List<AgentHasAbility> agentHasAbilityList = agentHasAbilityRepository.findAll();
    assertThat(agentHasAbilityList).hasSize(databaseSizeBeforeUpdate);
    AgentHasAbility testAgentHasAbility = agentHasAbilityList.get(agentHasAbilityList.size() - 1);
    assertThat(testAgentHasAbility.getDegree()).isEqualTo(UPDATED_DEGREE);
  }

  @Test
  @Transactional
  public void updateNonExistingAgentHasAbility() throws Exception {
    int databaseSizeBeforeUpdate = agentHasAbilityRepository.findAll().size();

    // Create the AgentHasAbility
    AgentHasAbilityDTO agentHasAbilityDTO = agentHasAbilityMapper.toDto(agentHasAbility);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAgentHasAbilityMockMvc
        .perform(
            put("/api/agent-has-abilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agentHasAbilityDTO)))
        .andExpect(status().isBadRequest());

    // Validate the AgentHasAbility in the database
    List<AgentHasAbility> agentHasAbilityList = agentHasAbilityRepository.findAll();
    assertThat(agentHasAbilityList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteAgentHasAbility() throws Exception {
    // Initialize the database
    agentHasAbilityRepository.saveAndFlush(agentHasAbility);

    int databaseSizeBeforeDelete = agentHasAbilityRepository.findAll().size();

    // Delete the agentHasAbility
    restAgentHasAbilityMockMvc
        .perform(
            delete("/api/agent-has-abilities/{id}", agentHasAbility.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<AgentHasAbility> agentHasAbilityList = agentHasAbilityRepository.findAll();
    assertThat(agentHasAbilityList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(AgentHasAbility.class);
    AgentHasAbility agentHasAbility1 = new AgentHasAbility();
    agentHasAbility1.setId(1L);
    AgentHasAbility agentHasAbility2 = new AgentHasAbility();
    agentHasAbility2.setId(agentHasAbility1.getId());
    assertThat(agentHasAbility1).isEqualTo(agentHasAbility2);
    agentHasAbility2.setId(2L);
    assertThat(agentHasAbility1).isNotEqualTo(agentHasAbility2);
    agentHasAbility1.setId(null);
    assertThat(agentHasAbility1).isNotEqualTo(agentHasAbility2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(AgentHasAbilityDTO.class);
    AgentHasAbilityDTO agentHasAbilityDTO1 = new AgentHasAbilityDTO();
    agentHasAbilityDTO1.setId(1L);
    AgentHasAbilityDTO agentHasAbilityDTO2 = new AgentHasAbilityDTO();
    assertThat(agentHasAbilityDTO1).isNotEqualTo(agentHasAbilityDTO2);
    agentHasAbilityDTO2.setId(agentHasAbilityDTO1.getId());
    assertThat(agentHasAbilityDTO1).isEqualTo(agentHasAbilityDTO2);
    agentHasAbilityDTO2.setId(2L);
    assertThat(agentHasAbilityDTO1).isNotEqualTo(agentHasAbilityDTO2);
    agentHasAbilityDTO1.setId(null);
    assertThat(agentHasAbilityDTO1).isNotEqualTo(agentHasAbilityDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(agentHasAbilityMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(agentHasAbilityMapper.fromId(null)).isNull();
  }
}
