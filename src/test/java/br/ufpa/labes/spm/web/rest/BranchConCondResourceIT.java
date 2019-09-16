package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.BranchConCond;
import br.ufpa.labes.spm.repository.BranchConCondRepository;
import br.ufpa.labes.spm.service.BranchConCondService;
import br.ufpa.labes.spm.service.dto.BranchConCondDTO;
import br.ufpa.labes.spm.service.mapper.BranchConCondMapper;
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

/** Integration tests for the {@link BranchConCondResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class BranchConCondResourceIT {

  private static final String DEFAULT_KIND_BRANCH = "AAAAAAAAAA";
  private static final String UPDATED_KIND_BRANCH = "BBBBBBBBBB";

  @Autowired private BranchConCondRepository branchConCondRepository;

  @Autowired private BranchConCondMapper branchConCondMapper;

  @Autowired private BranchConCondService branchConCondService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restBranchConCondMockMvc;

  private BranchConCond branchConCond;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final BranchConCondResource branchConCondResource =
        new BranchConCondResource(branchConCondService);
    this.restBranchConCondMockMvc =
        MockMvcBuilders.standaloneSetup(branchConCondResource)
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
  public static BranchConCond createEntity(EntityManager em) {
    BranchConCond branchConCond = new BranchConCond().kindBranch(DEFAULT_KIND_BRANCH);
    return branchConCond;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static BranchConCond createUpdatedEntity(EntityManager em) {
    BranchConCond branchConCond = new BranchConCond().kindBranch(UPDATED_KIND_BRANCH);
    return branchConCond;
  }

  @BeforeEach
  public void initTest() {
    branchConCond = createEntity(em);
  }

  @Test
  @Transactional
  public void createBranchConCond() throws Exception {
    int databaseSizeBeforeCreate = branchConCondRepository.findAll().size();

    // Create the BranchConCond
    BranchConCondDTO branchConCondDTO = branchConCondMapper.toDto(branchConCond);
    restBranchConCondMockMvc
        .perform(
            post("/api/branch-con-conds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchConCondDTO)))
        .andExpect(status().isCreated());

    // Validate the BranchConCond in the database
    List<BranchConCond> branchConCondList = branchConCondRepository.findAll();
    assertThat(branchConCondList).hasSize(databaseSizeBeforeCreate + 1);
    BranchConCond testBranchConCond = branchConCondList.get(branchConCondList.size() - 1);
    assertThat(testBranchConCond.getKindBranch()).isEqualTo(DEFAULT_KIND_BRANCH);
  }

  @Test
  @Transactional
  public void createBranchConCondWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = branchConCondRepository.findAll().size();

    // Create the BranchConCond with an existing ID
    branchConCond.setId(1L);
    BranchConCondDTO branchConCondDTO = branchConCondMapper.toDto(branchConCond);

    // An entity with an existing ID cannot be created, so this API call must fail
    restBranchConCondMockMvc
        .perform(
            post("/api/branch-con-conds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchConCondDTO)))
        .andExpect(status().isBadRequest());

    // Validate the BranchConCond in the database
    List<BranchConCond> branchConCondList = branchConCondRepository.findAll();
    assertThat(branchConCondList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllBranchConConds() throws Exception {
    // Initialize the database
    branchConCondRepository.saveAndFlush(branchConCond);

    // Get all the branchConCondList
    restBranchConCondMockMvc
        .perform(get("/api/branch-con-conds?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(branchConCond.getId().intValue())))
        .andExpect(jsonPath("$.[*].kindBranch").value(hasItem(DEFAULT_KIND_BRANCH.toString())));
  }

  @Test
  @Transactional
  public void getBranchConCond() throws Exception {
    // Initialize the database
    branchConCondRepository.saveAndFlush(branchConCond);

    // Get the branchConCond
    restBranchConCondMockMvc
        .perform(get("/api/branch-con-conds/{id}", branchConCond.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(branchConCond.getId().intValue()))
        .andExpect(jsonPath("$.kindBranch").value(DEFAULT_KIND_BRANCH.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingBranchConCond() throws Exception {
    // Get the branchConCond
    restBranchConCondMockMvc
        .perform(get("/api/branch-con-conds/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateBranchConCond() throws Exception {
    // Initialize the database
    branchConCondRepository.saveAndFlush(branchConCond);

    int databaseSizeBeforeUpdate = branchConCondRepository.findAll().size();

    // Update the branchConCond
    BranchConCond updatedBranchConCond =
        branchConCondRepository.findById(branchConCond.getId()).get();
    // Disconnect from session so that the updates on updatedBranchConCond are not directly saved in
    // db
    em.detach(updatedBranchConCond);
    updatedBranchConCond.kindBranch(UPDATED_KIND_BRANCH);
    BranchConCondDTO branchConCondDTO = branchConCondMapper.toDto(updatedBranchConCond);

    restBranchConCondMockMvc
        .perform(
            put("/api/branch-con-conds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchConCondDTO)))
        .andExpect(status().isOk());

    // Validate the BranchConCond in the database
    List<BranchConCond> branchConCondList = branchConCondRepository.findAll();
    assertThat(branchConCondList).hasSize(databaseSizeBeforeUpdate);
    BranchConCond testBranchConCond = branchConCondList.get(branchConCondList.size() - 1);
    assertThat(testBranchConCond.getKindBranch()).isEqualTo(UPDATED_KIND_BRANCH);
  }

  @Test
  @Transactional
  public void updateNonExistingBranchConCond() throws Exception {
    int databaseSizeBeforeUpdate = branchConCondRepository.findAll().size();

    // Create the BranchConCond
    BranchConCondDTO branchConCondDTO = branchConCondMapper.toDto(branchConCond);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restBranchConCondMockMvc
        .perform(
            put("/api/branch-con-conds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchConCondDTO)))
        .andExpect(status().isBadRequest());

    // Validate the BranchConCond in the database
    List<BranchConCond> branchConCondList = branchConCondRepository.findAll();
    assertThat(branchConCondList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteBranchConCond() throws Exception {
    // Initialize the database
    branchConCondRepository.saveAndFlush(branchConCond);

    int databaseSizeBeforeDelete = branchConCondRepository.findAll().size();

    // Delete the branchConCond
    restBranchConCondMockMvc
        .perform(
            delete("/api/branch-con-conds/{id}", branchConCond.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<BranchConCond> branchConCondList = branchConCondRepository.findAll();
    assertThat(branchConCondList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(BranchConCond.class);
    BranchConCond branchConCond1 = new BranchConCond();
    branchConCond1.setId(1L);
    BranchConCond branchConCond2 = new BranchConCond();
    branchConCond2.setId(branchConCond1.getId());
    assertThat(branchConCond1).isEqualTo(branchConCond2);
    branchConCond2.setId(2L);
    assertThat(branchConCond1).isNotEqualTo(branchConCond2);
    branchConCond1.setId(null);
    assertThat(branchConCond1).isNotEqualTo(branchConCond2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(BranchConCondDTO.class);
    BranchConCondDTO branchConCondDTO1 = new BranchConCondDTO();
    branchConCondDTO1.setId(1L);
    BranchConCondDTO branchConCondDTO2 = new BranchConCondDTO();
    assertThat(branchConCondDTO1).isNotEqualTo(branchConCondDTO2);
    branchConCondDTO2.setId(branchConCondDTO1.getId());
    assertThat(branchConCondDTO1).isEqualTo(branchConCondDTO2);
    branchConCondDTO2.setId(2L);
    assertThat(branchConCondDTO1).isNotEqualTo(branchConCondDTO2);
    branchConCondDTO1.setId(null);
    assertThat(branchConCondDTO1).isNotEqualTo(branchConCondDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(branchConCondMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(branchConCondMapper.fromId(null)).isNull();
  }
}
