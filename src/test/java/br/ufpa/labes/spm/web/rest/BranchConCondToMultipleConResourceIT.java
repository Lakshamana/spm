package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.BranchConCondToMultipleCon;
import br.ufpa.labes.spm.repository.BranchConCondToMultipleConRepository;
import br.ufpa.labes.spm.service.BranchConCondToMultipleConService;
import br.ufpa.labes.spm.service.dto.BranchConCondToMultipleConDTO;
import br.ufpa.labes.spm.service.mapper.BranchConCondToMultipleConMapper;
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

/** Integration tests for the {@link BranchConCondToMultipleConResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class BranchConCondToMultipleConResourceIT {

  @Autowired private BranchConCondToMultipleConRepository branchConCondToMultipleConRepository;

  @Autowired private BranchConCondToMultipleConMapper branchConCondToMultipleConMapper;

  @Autowired private BranchConCondToMultipleConService branchConCondToMultipleConService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restBranchConCondToMultipleConMockMvc;

  private BranchConCondToMultipleCon branchConCondToMultipleCon;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final BranchConCondToMultipleConResource branchConCondToMultipleConResource =
        new BranchConCondToMultipleConResource(branchConCondToMultipleConService);
    this.restBranchConCondToMultipleConMockMvc =
        MockMvcBuilders.standaloneSetup(branchConCondToMultipleConResource)
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
  public static BranchConCondToMultipleCon createEntity(EntityManager em) {
    BranchConCondToMultipleCon branchConCondToMultipleCon = new BranchConCondToMultipleCon();
    return branchConCondToMultipleCon;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static BranchConCondToMultipleCon createUpdatedEntity(EntityManager em) {
    BranchConCondToMultipleCon branchConCondToMultipleCon = new BranchConCondToMultipleCon();
    return branchConCondToMultipleCon;
  }

  @BeforeEach
  public void initTest() {
    branchConCondToMultipleCon = createEntity(em);
  }

  @Test
  @Transactional
  public void createBranchConCondToMultipleCon() throws Exception {
    int databaseSizeBeforeCreate = branchConCondToMultipleConRepository.findAll().size();

    // Create the BranchConCondToMultipleCon
    BranchConCondToMultipleConDTO branchConCondToMultipleConDTO =
        branchConCondToMultipleConMapper.toDto(branchConCondToMultipleCon);
    restBranchConCondToMultipleConMockMvc
        .perform(
            post("/api/branch-con-cond-to-multiple-cons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchConCondToMultipleConDTO)))
        .andExpect(status().isCreated());

    // Validate the BranchConCondToMultipleCon in the database
    List<BranchConCondToMultipleCon> branchConCondToMultipleConList =
        branchConCondToMultipleConRepository.findAll();
    assertThat(branchConCondToMultipleConList).hasSize(databaseSizeBeforeCreate + 1);
    BranchConCondToMultipleCon testBranchConCondToMultipleCon =
        branchConCondToMultipleConList.get(branchConCondToMultipleConList.size() - 1);
  }

  @Test
  @Transactional
  public void createBranchConCondToMultipleConWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = branchConCondToMultipleConRepository.findAll().size();

    // Create the BranchConCondToMultipleCon with an existing ID
    branchConCondToMultipleCon.setId(1L);
    BranchConCondToMultipleConDTO branchConCondToMultipleConDTO =
        branchConCondToMultipleConMapper.toDto(branchConCondToMultipleCon);

    // An entity with an existing ID cannot be created, so this API call must fail
    restBranchConCondToMultipleConMockMvc
        .perform(
            post("/api/branch-con-cond-to-multiple-cons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchConCondToMultipleConDTO)))
        .andExpect(status().isBadRequest());

    // Validate the BranchConCondToMultipleCon in the database
    List<BranchConCondToMultipleCon> branchConCondToMultipleConList =
        branchConCondToMultipleConRepository.findAll();
    assertThat(branchConCondToMultipleConList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllBranchConCondToMultipleCons() throws Exception {
    // Initialize the database
    branchConCondToMultipleConRepository.saveAndFlush(branchConCondToMultipleCon);

    // Get all the branchConCondToMultipleConList
    restBranchConCondToMultipleConMockMvc
        .perform(get("/api/branch-con-cond-to-multiple-cons?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(
            jsonPath("$.[*].id").value(hasItem(branchConCondToMultipleCon.getId().intValue())));
  }

  @Test
  @Transactional
  public void getBranchConCondToMultipleCon() throws Exception {
    // Initialize the database
    branchConCondToMultipleConRepository.saveAndFlush(branchConCondToMultipleCon);

    // Get the branchConCondToMultipleCon
    restBranchConCondToMultipleConMockMvc
        .perform(
            get("/api/branch-con-cond-to-multiple-cons/{id}", branchConCondToMultipleCon.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(branchConCondToMultipleCon.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingBranchConCondToMultipleCon() throws Exception {
    // Get the branchConCondToMultipleCon
    restBranchConCondToMultipleConMockMvc
        .perform(get("/api/branch-con-cond-to-multiple-cons/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateBranchConCondToMultipleCon() throws Exception {
    // Initialize the database
    branchConCondToMultipleConRepository.saveAndFlush(branchConCondToMultipleCon);

    int databaseSizeBeforeUpdate = branchConCondToMultipleConRepository.findAll().size();

    // Update the branchConCondToMultipleCon
    BranchConCondToMultipleCon updatedBranchConCondToMultipleCon =
        branchConCondToMultipleConRepository.findById(branchConCondToMultipleCon.getId()).get();
    // Disconnect from session so that the updates on updatedBranchConCondToMultipleCon are not
    // directly saved in db
    em.detach(updatedBranchConCondToMultipleCon);
    BranchConCondToMultipleConDTO branchConCondToMultipleConDTO =
        branchConCondToMultipleConMapper.toDto(updatedBranchConCondToMultipleCon);

    restBranchConCondToMultipleConMockMvc
        .perform(
            put("/api/branch-con-cond-to-multiple-cons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchConCondToMultipleConDTO)))
        .andExpect(status().isOk());

    // Validate the BranchConCondToMultipleCon in the database
    List<BranchConCondToMultipleCon> branchConCondToMultipleConList =
        branchConCondToMultipleConRepository.findAll();
    assertThat(branchConCondToMultipleConList).hasSize(databaseSizeBeforeUpdate);
    BranchConCondToMultipleCon testBranchConCondToMultipleCon =
        branchConCondToMultipleConList.get(branchConCondToMultipleConList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingBranchConCondToMultipleCon() throws Exception {
    int databaseSizeBeforeUpdate = branchConCondToMultipleConRepository.findAll().size();

    // Create the BranchConCondToMultipleCon
    BranchConCondToMultipleConDTO branchConCondToMultipleConDTO =
        branchConCondToMultipleConMapper.toDto(branchConCondToMultipleCon);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restBranchConCondToMultipleConMockMvc
        .perform(
            put("/api/branch-con-cond-to-multiple-cons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchConCondToMultipleConDTO)))
        .andExpect(status().isBadRequest());

    // Validate the BranchConCondToMultipleCon in the database
    List<BranchConCondToMultipleCon> branchConCondToMultipleConList =
        branchConCondToMultipleConRepository.findAll();
    assertThat(branchConCondToMultipleConList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteBranchConCondToMultipleCon() throws Exception {
    // Initialize the database
    branchConCondToMultipleConRepository.saveAndFlush(branchConCondToMultipleCon);

    int databaseSizeBeforeDelete = branchConCondToMultipleConRepository.findAll().size();

    // Delete the branchConCondToMultipleCon
    restBranchConCondToMultipleConMockMvc
        .perform(
            delete("/api/branch-con-cond-to-multiple-cons/{id}", branchConCondToMultipleCon.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<BranchConCondToMultipleCon> branchConCondToMultipleConList =
        branchConCondToMultipleConRepository.findAll();
    assertThat(branchConCondToMultipleConList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(BranchConCondToMultipleCon.class);
    BranchConCondToMultipleCon branchConCondToMultipleCon1 = new BranchConCondToMultipleCon();
    branchConCondToMultipleCon1.setId(1L);
    BranchConCondToMultipleCon branchConCondToMultipleCon2 = new BranchConCondToMultipleCon();
    branchConCondToMultipleCon2.setId(branchConCondToMultipleCon1.getId());
    assertThat(branchConCondToMultipleCon1).isEqualTo(branchConCondToMultipleCon2);
    branchConCondToMultipleCon2.setId(2L);
    assertThat(branchConCondToMultipleCon1).isNotEqualTo(branchConCondToMultipleCon2);
    branchConCondToMultipleCon1.setId(null);
    assertThat(branchConCondToMultipleCon1).isNotEqualTo(branchConCondToMultipleCon2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(BranchConCondToMultipleConDTO.class);
    BranchConCondToMultipleConDTO branchConCondToMultipleConDTO1 =
        new BranchConCondToMultipleConDTO();
    branchConCondToMultipleConDTO1.setId(1L);
    BranchConCondToMultipleConDTO branchConCondToMultipleConDTO2 =
        new BranchConCondToMultipleConDTO();
    assertThat(branchConCondToMultipleConDTO1).isNotEqualTo(branchConCondToMultipleConDTO2);
    branchConCondToMultipleConDTO2.setId(branchConCondToMultipleConDTO1.getId());
    assertThat(branchConCondToMultipleConDTO1).isEqualTo(branchConCondToMultipleConDTO2);
    branchConCondToMultipleConDTO2.setId(2L);
    assertThat(branchConCondToMultipleConDTO1).isNotEqualTo(branchConCondToMultipleConDTO2);
    branchConCondToMultipleConDTO1.setId(null);
    assertThat(branchConCondToMultipleConDTO1).isNotEqualTo(branchConCondToMultipleConDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(branchConCondToMultipleConMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(branchConCondToMultipleConMapper.fromId(null)).isNull();
  }
}
