package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ReqAgentRequiresAbility;
import br.ufpa.labes.spm.repository.ReqAgentRequiresAbilityRepository;
import br.ufpa.labes.spm.service.ReqAgentRequiresAbilityService;
import br.ufpa.labes.spm.service.dto.ReqAgentRequiresAbilityDTO;
import br.ufpa.labes.spm.service.mapper.ReqAgentRequiresAbilityMapper;
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

/** Integration tests for the {@link ReqAgentRequiresAbilityResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ReqAgentRequiresAbilityResourceIT {

  private static final Integer DEFAULT_DEGREE = 1;
  private static final Integer UPDATED_DEGREE = 2;
  private static final Integer SMALLER_DEGREE = 1 - 1;

  @Autowired private ReqAgentRequiresAbilityRepository reqAgentRequiresAbilityRepository;

  @Autowired private ReqAgentRequiresAbilityMapper reqAgentRequiresAbilityMapper;

  @Autowired private ReqAgentRequiresAbilityService reqAgentRequiresAbilityService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restReqAgentRequiresAbilityMockMvc;

  private ReqAgentRequiresAbility reqAgentRequiresAbility;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ReqAgentRequiresAbilityResource reqAgentRequiresAbilityResource =
        new ReqAgentRequiresAbilityResource(reqAgentRequiresAbilityService);
    this.restReqAgentRequiresAbilityMockMvc =
        MockMvcBuilders.standaloneSetup(reqAgentRequiresAbilityResource)
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
  public static ReqAgentRequiresAbility createEntity(EntityManager em) {
    ReqAgentRequiresAbility reqAgentRequiresAbility =
        new ReqAgentRequiresAbility().degree(DEFAULT_DEGREE);
    return reqAgentRequiresAbility;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ReqAgentRequiresAbility createUpdatedEntity(EntityManager em) {
    ReqAgentRequiresAbility reqAgentRequiresAbility =
        new ReqAgentRequiresAbility().degree(UPDATED_DEGREE);
    return reqAgentRequiresAbility;
  }

  @BeforeEach
  public void initTest() {
    reqAgentRequiresAbility = createEntity(em);
  }

  @Test
  @Transactional
  public void createReqAgentRequiresAbility() throws Exception {
    int databaseSizeBeforeCreate = reqAgentRequiresAbilityRepository.findAll().size();

    // Create the ReqAgentRequiresAbility
    ReqAgentRequiresAbilityDTO reqAgentRequiresAbilityDTO =
        reqAgentRequiresAbilityMapper.toDto(reqAgentRequiresAbility);
    restReqAgentRequiresAbilityMockMvc
        .perform(
            post("/api/req-agent-requires-abilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reqAgentRequiresAbilityDTO)))
        .andExpect(status().isCreated());

    // Validate the ReqAgentRequiresAbility in the database
    List<ReqAgentRequiresAbility> reqAgentRequiresAbilityList =
        reqAgentRequiresAbilityRepository.findAll();
    assertThat(reqAgentRequiresAbilityList).hasSize(databaseSizeBeforeCreate + 1);
    ReqAgentRequiresAbility testReqAgentRequiresAbility =
        reqAgentRequiresAbilityList.get(reqAgentRequiresAbilityList.size() - 1);
    assertThat(testReqAgentRequiresAbility.getDegree()).isEqualTo(DEFAULT_DEGREE);
  }

  @Test
  @Transactional
  public void createReqAgentRequiresAbilityWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = reqAgentRequiresAbilityRepository.findAll().size();

    // Create the ReqAgentRequiresAbility with an existing ID
    reqAgentRequiresAbility.setId(1L);
    ReqAgentRequiresAbilityDTO reqAgentRequiresAbilityDTO =
        reqAgentRequiresAbilityMapper.toDto(reqAgentRequiresAbility);

    // An entity with an existing ID cannot be created, so this API call must fail
    restReqAgentRequiresAbilityMockMvc
        .perform(
            post("/api/req-agent-requires-abilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reqAgentRequiresAbilityDTO)))
        .andExpect(status().isBadRequest());

    // Validate the ReqAgentRequiresAbility in the database
    List<ReqAgentRequiresAbility> reqAgentRequiresAbilityList =
        reqAgentRequiresAbilityRepository.findAll();
    assertThat(reqAgentRequiresAbilityList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllReqAgentRequiresAbilities() throws Exception {
    // Initialize the database
    reqAgentRequiresAbilityRepository.saveAndFlush(reqAgentRequiresAbility);

    // Get all the reqAgentRequiresAbilityList
    restReqAgentRequiresAbilityMockMvc
        .perform(get("/api/req-agent-requires-abilities?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(reqAgentRequiresAbility.getId().intValue())))
        .andExpect(jsonPath("$.[*].degree").value(hasItem(DEFAULT_DEGREE)));
  }

  @Test
  @Transactional
  public void getReqAgentRequiresAbility() throws Exception {
    // Initialize the database
    reqAgentRequiresAbilityRepository.saveAndFlush(reqAgentRequiresAbility);

    // Get the reqAgentRequiresAbility
    restReqAgentRequiresAbilityMockMvc
        .perform(get("/api/req-agent-requires-abilities/{id}", reqAgentRequiresAbility.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(reqAgentRequiresAbility.getId().intValue()))
        .andExpect(jsonPath("$.degree").value(DEFAULT_DEGREE));
  }

  @Test
  @Transactional
  public void getNonExistingReqAgentRequiresAbility() throws Exception {
    // Get the reqAgentRequiresAbility
    restReqAgentRequiresAbilityMockMvc
        .perform(get("/api/req-agent-requires-abilities/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateReqAgentRequiresAbility() throws Exception {
    // Initialize the database
    reqAgentRequiresAbilityRepository.saveAndFlush(reqAgentRequiresAbility);

    int databaseSizeBeforeUpdate = reqAgentRequiresAbilityRepository.findAll().size();

    // Update the reqAgentRequiresAbility
    ReqAgentRequiresAbility updatedReqAgentRequiresAbility =
        reqAgentRequiresAbilityRepository.findById(reqAgentRequiresAbility.getId()).get();
    // Disconnect from session so that the updates on updatedReqAgentRequiresAbility are not
    // directly saved in db
    em.detach(updatedReqAgentRequiresAbility);
    updatedReqAgentRequiresAbility.degree(UPDATED_DEGREE);
    ReqAgentRequiresAbilityDTO reqAgentRequiresAbilityDTO =
        reqAgentRequiresAbilityMapper.toDto(updatedReqAgentRequiresAbility);

    restReqAgentRequiresAbilityMockMvc
        .perform(
            put("/api/req-agent-requires-abilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reqAgentRequiresAbilityDTO)))
        .andExpect(status().isOk());

    // Validate the ReqAgentRequiresAbility in the database
    List<ReqAgentRequiresAbility> reqAgentRequiresAbilityList =
        reqAgentRequiresAbilityRepository.findAll();
    assertThat(reqAgentRequiresAbilityList).hasSize(databaseSizeBeforeUpdate);
    ReqAgentRequiresAbility testReqAgentRequiresAbility =
        reqAgentRequiresAbilityList.get(reqAgentRequiresAbilityList.size() - 1);
    assertThat(testReqAgentRequiresAbility.getDegree()).isEqualTo(UPDATED_DEGREE);
  }

  @Test
  @Transactional
  public void updateNonExistingReqAgentRequiresAbility() throws Exception {
    int databaseSizeBeforeUpdate = reqAgentRequiresAbilityRepository.findAll().size();

    // Create the ReqAgentRequiresAbility
    ReqAgentRequiresAbilityDTO reqAgentRequiresAbilityDTO =
        reqAgentRequiresAbilityMapper.toDto(reqAgentRequiresAbility);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restReqAgentRequiresAbilityMockMvc
        .perform(
            put("/api/req-agent-requires-abilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reqAgentRequiresAbilityDTO)))
        .andExpect(status().isBadRequest());

    // Validate the ReqAgentRequiresAbility in the database
    List<ReqAgentRequiresAbility> reqAgentRequiresAbilityList =
        reqAgentRequiresAbilityRepository.findAll();
    assertThat(reqAgentRequiresAbilityList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteReqAgentRequiresAbility() throws Exception {
    // Initialize the database
    reqAgentRequiresAbilityRepository.saveAndFlush(reqAgentRequiresAbility);

    int databaseSizeBeforeDelete = reqAgentRequiresAbilityRepository.findAll().size();

    // Delete the reqAgentRequiresAbility
    restReqAgentRequiresAbilityMockMvc
        .perform(
            delete("/api/req-agent-requires-abilities/{id}", reqAgentRequiresAbility.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ReqAgentRequiresAbility> reqAgentRequiresAbilityList =
        reqAgentRequiresAbilityRepository.findAll();
    assertThat(reqAgentRequiresAbilityList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ReqAgentRequiresAbility.class);
    ReqAgentRequiresAbility reqAgentRequiresAbility1 = new ReqAgentRequiresAbility();
    reqAgentRequiresAbility1.setId(1L);
    ReqAgentRequiresAbility reqAgentRequiresAbility2 = new ReqAgentRequiresAbility();
    reqAgentRequiresAbility2.setId(reqAgentRequiresAbility1.getId());
    assertThat(reqAgentRequiresAbility1).isEqualTo(reqAgentRequiresAbility2);
    reqAgentRequiresAbility2.setId(2L);
    assertThat(reqAgentRequiresAbility1).isNotEqualTo(reqAgentRequiresAbility2);
    reqAgentRequiresAbility1.setId(null);
    assertThat(reqAgentRequiresAbility1).isNotEqualTo(reqAgentRequiresAbility2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(ReqAgentRequiresAbilityDTO.class);
    ReqAgentRequiresAbilityDTO reqAgentRequiresAbilityDTO1 = new ReqAgentRequiresAbilityDTO();
    reqAgentRequiresAbilityDTO1.setId(1L);
    ReqAgentRequiresAbilityDTO reqAgentRequiresAbilityDTO2 = new ReqAgentRequiresAbilityDTO();
    assertThat(reqAgentRequiresAbilityDTO1).isNotEqualTo(reqAgentRequiresAbilityDTO2);
    reqAgentRequiresAbilityDTO2.setId(reqAgentRequiresAbilityDTO1.getId());
    assertThat(reqAgentRequiresAbilityDTO1).isEqualTo(reqAgentRequiresAbilityDTO2);
    reqAgentRequiresAbilityDTO2.setId(2L);
    assertThat(reqAgentRequiresAbilityDTO1).isNotEqualTo(reqAgentRequiresAbilityDTO2);
    reqAgentRequiresAbilityDTO1.setId(null);
    assertThat(reqAgentRequiresAbilityDTO1).isNotEqualTo(reqAgentRequiresAbilityDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(reqAgentRequiresAbilityMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(reqAgentRequiresAbilityMapper.fromId(null)).isNull();
  }
}
