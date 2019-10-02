package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ReqWorkGroup;
import br.ufpa.labes.spm.repository.ReqWorkGroupRepository;
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

/** Integration tests for the {@link ReqWorkGroupResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ReqWorkGroupResourceIT {

  @Autowired private ReqWorkGroupRepository reqWorkGroupRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restReqWorkGroupMockMvc;

  private ReqWorkGroup reqWorkGroup;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ReqWorkGroupResource reqWorkGroupResource =
        new ReqWorkGroupResource(reqWorkGroupRepository);
    this.restReqWorkGroupMockMvc =
        MockMvcBuilders.standaloneSetup(reqWorkGroupResource)
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
  public static ReqWorkGroup createEntity(EntityManager em) {
    ReqWorkGroup reqWorkGroup = new ReqWorkGroup();
    return reqWorkGroup;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ReqWorkGroup createUpdatedEntity(EntityManager em) {
    ReqWorkGroup reqWorkGroup = new ReqWorkGroup();
    return reqWorkGroup;
  }

  @BeforeEach
  public void initTest() {
    reqWorkGroup = createEntity(em);
  }

  @Test
  @Transactional
  public void createReqWorkGroup() throws Exception {
    int databaseSizeBeforeCreate = reqWorkGroupRepository.findAll().size();

    // Create the ReqWorkGroup
    restReqWorkGroupMockMvc
        .perform(
            post("/api/req-work-groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reqWorkGroup)))
        .andExpect(status().isCreated());

    // Validate the ReqWorkGroup in the database
    List<ReqWorkGroup> reqWorkGroupList = reqWorkGroupRepository.findAll();
    assertThat(reqWorkGroupList).hasSize(databaseSizeBeforeCreate + 1);
    ReqWorkGroup testReqWorkGroup = reqWorkGroupList.get(reqWorkGroupList.size() - 1);
  }

  @Test
  @Transactional
  public void createReqWorkGroupWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = reqWorkGroupRepository.findAll().size();

    // Create the ReqWorkGroup with an existing ID
    reqWorkGroup.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restReqWorkGroupMockMvc
        .perform(
            post("/api/req-work-groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reqWorkGroup)))
        .andExpect(status().isBadRequest());

    // Validate the ReqWorkGroup in the database
    List<ReqWorkGroup> reqWorkGroupList = reqWorkGroupRepository.findAll();
    assertThat(reqWorkGroupList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllReqWorkGroups() throws Exception {
    // Initialize the database
    reqWorkGroupRepository.saveAndFlush(reqWorkGroup);

    // Get all the reqWorkGroupList
    restReqWorkGroupMockMvc
        .perform(get("/api/req-work-groups?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(reqWorkGroup.getId().intValue())));
  }

  @Test
  @Transactional
  public void getReqWorkGroup() throws Exception {
    // Initialize the database
    reqWorkGroupRepository.saveAndFlush(reqWorkGroup);

    // Get the reqWorkGroup
    restReqWorkGroupMockMvc
        .perform(get("/api/req-work-groups/{id}", reqWorkGroup.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(reqWorkGroup.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingReqWorkGroup() throws Exception {
    // Get the reqWorkGroup
    restReqWorkGroupMockMvc
        .perform(get("/api/req-work-groups/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateReqWorkGroup() throws Exception {
    // Initialize the database
    reqWorkGroupRepository.saveAndFlush(reqWorkGroup);

    int databaseSizeBeforeUpdate = reqWorkGroupRepository.findAll().size();

    // Update the reqWorkGroup
    ReqWorkGroup updatedReqWorkGroup = reqWorkGroupRepository.findById(reqWorkGroup.getId()).get();
    // Disconnect from session so that the updates on updatedReqWorkGroup are not directly saved in
    // db
    em.detach(updatedReqWorkGroup);

    restReqWorkGroupMockMvc
        .perform(
            put("/api/req-work-groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedReqWorkGroup)))
        .andExpect(status().isOk());

    // Validate the ReqWorkGroup in the database
    List<ReqWorkGroup> reqWorkGroupList = reqWorkGroupRepository.findAll();
    assertThat(reqWorkGroupList).hasSize(databaseSizeBeforeUpdate);
    ReqWorkGroup testReqWorkGroup = reqWorkGroupList.get(reqWorkGroupList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingReqWorkGroup() throws Exception {
    int databaseSizeBeforeUpdate = reqWorkGroupRepository.findAll().size();

    // Create the ReqWorkGroup

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restReqWorkGroupMockMvc
        .perform(
            put("/api/req-work-groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(reqWorkGroup)))
        .andExpect(status().isBadRequest());

    // Validate the ReqWorkGroup in the database
    List<ReqWorkGroup> reqWorkGroupList = reqWorkGroupRepository.findAll();
    assertThat(reqWorkGroupList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteReqWorkGroup() throws Exception {
    // Initialize the database
    reqWorkGroupRepository.saveAndFlush(reqWorkGroup);

    int databaseSizeBeforeDelete = reqWorkGroupRepository.findAll().size();

    // Delete the reqWorkGroup
    restReqWorkGroupMockMvc
        .perform(
            delete("/api/req-work-groups/{id}", reqWorkGroup.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ReqWorkGroup> reqWorkGroupList = reqWorkGroupRepository.findAll();
    assertThat(reqWorkGroupList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ReqWorkGroup.class);
    ReqWorkGroup reqWorkGroup1 = new ReqWorkGroup();
    reqWorkGroup1.setId(1L);
    ReqWorkGroup reqWorkGroup2 = new ReqWorkGroup();
    reqWorkGroup2.setId(reqWorkGroup1.getId());
    assertThat(reqWorkGroup1).isEqualTo(reqWorkGroup2);
    reqWorkGroup2.setId(2L);
    assertThat(reqWorkGroup1).isNotEqualTo(reqWorkGroup2);
    reqWorkGroup1.setId(null);
    assertThat(reqWorkGroup1).isNotEqualTo(reqWorkGroup2);
  }
}
