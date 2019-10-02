package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ReqAgent;
import br.ufpa.labes.spm.repository.ReqAgentRepository;
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

/** Integration tests for the {@link ReqAgentResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ReqAgentResourceIT {

  @Autowired private ReqAgentRepository reqAgentRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restReqAgentMockMvc;

  private ReqAgent reqAgent;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ReqAgentResource reqAgentResource = new ReqAgentResource(reqAgentRepository);
    this.restReqAgentMockMvc =
        MockMvcBuilders.standaloneSetup(reqAgentResource)
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
  public static ReqAgent createEntity(EntityManager em) {
    ReqAgent reqAgent = new ReqAgent();
    return reqAgent;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ReqAgent createUpdatedEntity(EntityManager em) {
    ReqAgent reqAgent = new ReqAgent();
    return reqAgent;
  }

  @BeforeEach
  public void initTest() {
    reqAgent = createEntity(em);
  }

  @Test
  @Transactional
  public void createReqAgent() throws Exception {
    int databaseSizeBeforeCreate = reqAgentRepository.findAll().size();

    // Create the ReqAgent
    restReqAgentMockMvc
        .perform(
            post("/api/req-agents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reqAgent)))
        .andExpect(status().isCreated());

    // Validate the ReqAgent in the database
    List<ReqAgent> reqAgentList = reqAgentRepository.findAll();
    assertThat(reqAgentList).hasSize(databaseSizeBeforeCreate + 1);
    ReqAgent testReqAgent = reqAgentList.get(reqAgentList.size() - 1);
  }

  @Test
  @Transactional
  public void createReqAgentWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = reqAgentRepository.findAll().size();

    // Create the ReqAgent with an existing ID
    reqAgent.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restReqAgentMockMvc
        .perform(
            post("/api/req-agents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reqAgent)))
        .andExpect(status().isBadRequest());

    // Validate the ReqAgent in the database
    List<ReqAgent> reqAgentList = reqAgentRepository.findAll();
    assertThat(reqAgentList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllReqAgents() throws Exception {
    // Initialize the database
    reqAgentRepository.saveAndFlush(reqAgent);

    // Get all the reqAgentList
    restReqAgentMockMvc
        .perform(get("/api/req-agents?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(reqAgent.getId().intValue())));
  }

  @Test
  @Transactional
  public void getReqAgent() throws Exception {
    // Initialize the database
    reqAgentRepository.saveAndFlush(reqAgent);

    // Get the reqAgent
    restReqAgentMockMvc
        .perform(get("/api/req-agents/{id}", reqAgent.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(reqAgent.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingReqAgent() throws Exception {
    // Get the reqAgent
    restReqAgentMockMvc
        .perform(get("/api/req-agents/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateReqAgent() throws Exception {
    // Initialize the database
    reqAgentRepository.saveAndFlush(reqAgent);

    int databaseSizeBeforeUpdate = reqAgentRepository.findAll().size();

    // Update the reqAgent
    ReqAgent updatedReqAgent = reqAgentRepository.findById(reqAgent.getId()).get();
    // Disconnect from session so that the updates on updatedReqAgent are not directly saved in db
    em.detach(updatedReqAgent);

    restReqAgentMockMvc
        .perform(
            put("/api/req-agents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedReqAgent)))
        .andExpect(status().isOk());

    // Validate the ReqAgent in the database
    List<ReqAgent> reqAgentList = reqAgentRepository.findAll();
    assertThat(reqAgentList).hasSize(databaseSizeBeforeUpdate);
    ReqAgent testReqAgent = reqAgentList.get(reqAgentList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingReqAgent() throws Exception {
    int databaseSizeBeforeUpdate = reqAgentRepository.findAll().size();

    // Create the ReqAgent

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restReqAgentMockMvc
        .perform(
            put("/api/req-agents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reqAgent)))
        .andExpect(status().isBadRequest());

    // Validate the ReqAgent in the database
    List<ReqAgent> reqAgentList = reqAgentRepository.findAll();
    assertThat(reqAgentList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteReqAgent() throws Exception {
    // Initialize the database
    reqAgentRepository.saveAndFlush(reqAgent);

    int databaseSizeBeforeDelete = reqAgentRepository.findAll().size();

    // Delete the reqAgent
    restReqAgentMockMvc
        .perform(
            delete("/api/req-agents/{id}", reqAgent.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ReqAgent> reqAgentList = reqAgentRepository.findAll();
    assertThat(reqAgentList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ReqAgent.class);
    ReqAgent reqAgent1 = new ReqAgent();
    reqAgent1.setId(1L);
    ReqAgent reqAgent2 = new ReqAgent();
    reqAgent2.setId(reqAgent1.getId());
    assertThat(reqAgent1).isEqualTo(reqAgent2);
    reqAgent2.setId(2L);
    assertThat(reqAgent1).isNotEqualTo(reqAgent2);
    reqAgent1.setId(null);
    assertThat(reqAgent1).isNotEqualTo(reqAgent2);
  }
}
