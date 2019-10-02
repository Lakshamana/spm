package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.BranchCon;
import br.ufpa.labes.spm.repository.BranchConRepository;
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

/** Integration tests for the {@link BranchConResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class BranchConResourceIT {

  @Autowired private BranchConRepository branchConRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restBranchConMockMvc;

  private BranchCon branchCon;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final BranchConResource branchConResource = new BranchConResource(branchConRepository);
    this.restBranchConMockMvc =
        MockMvcBuilders.standaloneSetup(branchConResource)
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
  public static BranchCon createEntity(EntityManager em) {
    BranchCon branchCon = new BranchCon();
    return branchCon;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static BranchCon createUpdatedEntity(EntityManager em) {
    BranchCon branchCon = new BranchCon();
    return branchCon;
  }

  @BeforeEach
  public void initTest() {
    branchCon = createEntity(em);
  }

  @Test
  @Transactional
  public void createBranchCon() throws Exception {
    int databaseSizeBeforeCreate = branchConRepository.findAll().size();

    // Create the BranchCon
    restBranchConMockMvc
        .perform(
            post("/api/branch-cons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchCon)))
        .andExpect(status().isCreated());

    // Validate the BranchCon in the database
    List<BranchCon> branchConList = branchConRepository.findAll();
    assertThat(branchConList).hasSize(databaseSizeBeforeCreate + 1);
    BranchCon testBranchCon = branchConList.get(branchConList.size() - 1);
  }

  @Test
  @Transactional
  public void createBranchConWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = branchConRepository.findAll().size();

    // Create the BranchCon with an existing ID
    branchCon.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restBranchConMockMvc
        .perform(
            post("/api/branch-cons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchCon)))
        .andExpect(status().isBadRequest());

    // Validate the BranchCon in the database
    List<BranchCon> branchConList = branchConRepository.findAll();
    assertThat(branchConList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllBranchCons() throws Exception {
    // Initialize the database
    branchConRepository.saveAndFlush(branchCon);

    // Get all the branchConList
    restBranchConMockMvc
        .perform(get("/api/branch-cons?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(branchCon.getId().intValue())));
  }

  @Test
  @Transactional
  public void getBranchCon() throws Exception {
    // Initialize the database
    branchConRepository.saveAndFlush(branchCon);

    // Get the branchCon
    restBranchConMockMvc
        .perform(get("/api/branch-cons/{id}", branchCon.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(branchCon.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingBranchCon() throws Exception {
    // Get the branchCon
    restBranchConMockMvc
        .perform(get("/api/branch-cons/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateBranchCon() throws Exception {
    // Initialize the database
    branchConRepository.saveAndFlush(branchCon);

    int databaseSizeBeforeUpdate = branchConRepository.findAll().size();

    // Update the branchCon
    BranchCon updatedBranchCon = branchConRepository.findById(branchCon.getId()).get();
    // Disconnect from session so that the updates on updatedBranchCon are not directly saved in db
    em.detach(updatedBranchCon);

    restBranchConMockMvc
        .perform(
            put("/api/branch-cons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBranchCon)))
        .andExpect(status().isOk());

    // Validate the BranchCon in the database
    List<BranchCon> branchConList = branchConRepository.findAll();
    assertThat(branchConList).hasSize(databaseSizeBeforeUpdate);
    BranchCon testBranchCon = branchConList.get(branchConList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingBranchCon() throws Exception {
    int databaseSizeBeforeUpdate = branchConRepository.findAll().size();

    // Create the BranchCon

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restBranchConMockMvc
        .perform(
            put("/api/branch-cons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchCon)))
        .andExpect(status().isBadRequest());

    // Validate the BranchCon in the database
    List<BranchCon> branchConList = branchConRepository.findAll();
    assertThat(branchConList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteBranchCon() throws Exception {
    // Initialize the database
    branchConRepository.saveAndFlush(branchCon);

    int databaseSizeBeforeDelete = branchConRepository.findAll().size();

    // Delete the branchCon
    restBranchConMockMvc
        .perform(
            delete("/api/branch-cons/{id}", branchCon.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<BranchCon> branchConList = branchConRepository.findAll();
    assertThat(branchConList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(BranchCon.class);
    BranchCon branchCon1 = new BranchCon();
    branchCon1.setId(1L);
    BranchCon branchCon2 = new BranchCon();
    branchCon2.setId(branchCon1.getId());
    assertThat(branchCon1).isEqualTo(branchCon2);
    branchCon2.setId(2L);
    assertThat(branchCon1).isNotEqualTo(branchCon2);
    branchCon1.setId(null);
    assertThat(branchCon1).isNotEqualTo(branchCon2);
  }
}
