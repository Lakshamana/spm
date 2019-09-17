package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.BranchANDCon;
import br.ufpa.labes.spm.repository.BranchANDConRepository;
import br.ufpa.labes.spm.service.BranchANDConService;
import br.ufpa.labes.spm.service.dto.BranchANDConDTO;
import br.ufpa.labes.spm.service.mapper.BranchANDConMapper;
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

/** Integration tests for the {@link BranchANDConResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class BranchANDConResourceIT {

  @Autowired private BranchANDConRepository branchANDConRepository;

  @Mock private BranchANDConRepository branchANDConRepositoryMock;

  @Autowired private BranchANDConMapper branchANDConMapper;

  @Mock private BranchANDConService branchANDConServiceMock;

  @Autowired private BranchANDConService branchANDConService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restBranchANDConMockMvc;

  private BranchANDCon branchANDCon;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final BranchANDConResource branchANDConResource = new BranchANDConResource(branchANDConService);
    this.restBranchANDConMockMvc =
        MockMvcBuilders.standaloneSetup(branchANDConResource)
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
  public static BranchANDCon createEntity(EntityManager em) {
    BranchANDCon branchANDCon = new BranchANDCon();
    return branchANDCon;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static BranchANDCon createUpdatedEntity(EntityManager em) {
    BranchANDCon branchANDCon = new BranchANDCon();
    return branchANDCon;
  }

  @BeforeEach
  public void initTest() {
    branchANDCon = createEntity(em);
  }

  @Test
  @Transactional
  public void createBranchANDCon() throws Exception {
    int databaseSizeBeforeCreate = branchANDConRepository.findAll().size();

    // Create the BranchANDCon
    BranchANDConDTO branchANDConDTO = branchANDConMapper.toDto(branchANDCon);
    restBranchANDConMockMvc
        .perform(
            post("/api/branch-and-cons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchANDConDTO)))
        .andExpect(status().isCreated());

    // Validate the BranchANDCon in the database
    List<BranchANDCon> branchANDConList = branchANDConRepository.findAll();
    assertThat(branchANDConList).hasSize(databaseSizeBeforeCreate + 1);
    BranchANDCon testBranchANDCon = branchANDConList.get(branchANDConList.size() - 1);
  }

  @Test
  @Transactional
  public void createBranchANDConWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = branchANDConRepository.findAll().size();

    // Create the BranchANDCon with an existing ID
    branchANDCon.setId(1L);
    BranchANDConDTO branchANDConDTO = branchANDConMapper.toDto(branchANDCon);

    // An entity with an existing ID cannot be created, so this API call must fail
    restBranchANDConMockMvc
        .perform(
            post("/api/branch-and-cons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchANDConDTO)))
        .andExpect(status().isBadRequest());

    // Validate the BranchANDCon in the database
    List<BranchANDCon> branchANDConList = branchANDConRepository.findAll();
    assertThat(branchANDConList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllBranchANDCons() throws Exception {
    // Initialize the database
    branchANDConRepository.saveAndFlush(branchANDCon);

    // Get all the branchANDConList
    restBranchANDConMockMvc
        .perform(get("/api/branch-and-cons?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(branchANDCon.getId().intValue())));
  }

  @SuppressWarnings({"unchecked"})
  public void getAllBranchANDConsWithEagerRelationshipsIsEnabled() throws Exception {
    BranchANDConResource branchANDConResource = new BranchANDConResource(branchANDConServiceMock);
    when(branchANDConServiceMock.findAllWithEagerRelationships(any()))
        .thenReturn(new PageImpl(new ArrayList<>()));

    MockMvc restBranchANDConMockMvc =
        MockMvcBuilders.standaloneSetup(branchANDConResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .build();

    restBranchANDConMockMvc
        .perform(get("/api/branch-and-cons?eagerload=true"))
        .andExpect(status().isOk());

    verify(branchANDConServiceMock, times(1)).findAllWithEagerRelationships(any());
  }

  @SuppressWarnings({"unchecked"})
  public void getAllBranchANDConsWithEagerRelationshipsIsNotEnabled() throws Exception {
    BranchANDConResource branchANDConResource = new BranchANDConResource(branchANDConServiceMock);
    when(branchANDConServiceMock.findAllWithEagerRelationships(any()))
        .thenReturn(new PageImpl(new ArrayList<>()));
    MockMvc restBranchANDConMockMvc =
        MockMvcBuilders.standaloneSetup(branchANDConResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .build();

    restBranchANDConMockMvc
        .perform(get("/api/branch-and-cons?eagerload=true"))
        .andExpect(status().isOk());

    verify(branchANDConServiceMock, times(1)).findAllWithEagerRelationships(any());
  }

  @Test
  @Transactional
  public void getBranchANDCon() throws Exception {
    // Initialize the database
    branchANDConRepository.saveAndFlush(branchANDCon);

    // Get the branchANDCon
    restBranchANDConMockMvc
        .perform(get("/api/branch-and-cons/{id}", branchANDCon.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(branchANDCon.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingBranchANDCon() throws Exception {
    // Get the branchANDCon
    restBranchANDConMockMvc
        .perform(get("/api/branch-and-cons/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateBranchANDCon() throws Exception {
    // Initialize the database
    branchANDConRepository.saveAndFlush(branchANDCon);

    int databaseSizeBeforeUpdate = branchANDConRepository.findAll().size();

    // Update the branchANDCon
    BranchANDCon updatedBranchANDCon = branchANDConRepository.findById(branchANDCon.getId()).get();
    // Disconnect from session so that the updates on updatedBranchANDCon are not directly saved in
    // db
    em.detach(updatedBranchANDCon);
    BranchANDConDTO branchANDConDTO = branchANDConMapper.toDto(updatedBranchANDCon);

    restBranchANDConMockMvc
        .perform(
            put("/api/branch-and-cons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchANDConDTO)))
        .andExpect(status().isOk());

    // Validate the BranchANDCon in the database
    List<BranchANDCon> branchANDConList = branchANDConRepository.findAll();
    assertThat(branchANDConList).hasSize(databaseSizeBeforeUpdate);
    BranchANDCon testBranchANDCon = branchANDConList.get(branchANDConList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingBranchANDCon() throws Exception {
    int databaseSizeBeforeUpdate = branchANDConRepository.findAll().size();

    // Create the BranchANDCon
    BranchANDConDTO branchANDConDTO = branchANDConMapper.toDto(branchANDCon);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restBranchANDConMockMvc
        .perform(
            put("/api/branch-and-cons")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(branchANDConDTO)))
        .andExpect(status().isBadRequest());

    // Validate the BranchANDCon in the database
    List<BranchANDCon> branchANDConList = branchANDConRepository.findAll();
    assertThat(branchANDConList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteBranchANDCon() throws Exception {
    // Initialize the database
    branchANDConRepository.saveAndFlush(branchANDCon);

    int databaseSizeBeforeDelete = branchANDConRepository.findAll().size();

    // Delete the branchANDCon
    restBranchANDConMockMvc
        .perform(
            delete("/api/branch-and-cons/{id}", branchANDCon.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<BranchANDCon> branchANDConList = branchANDConRepository.findAll();
    assertThat(branchANDConList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(BranchANDCon.class);
    BranchANDCon branchANDCon1 = new BranchANDCon();
    branchANDCon1.setId(1L);
    BranchANDCon branchANDCon2 = new BranchANDCon();
    branchANDCon2.setId(branchANDCon1.getId());
    assertThat(branchANDCon1).isEqualTo(branchANDCon2);
    branchANDCon2.setId(2L);
    assertThat(branchANDCon1).isNotEqualTo(branchANDCon2);
    branchANDCon1.setId(null);
    assertThat(branchANDCon1).isNotEqualTo(branchANDCon2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(BranchANDConDTO.class);
    BranchANDConDTO branchANDConDTO1 = new BranchANDConDTO();
    branchANDConDTO1.setId(1L);
    BranchANDConDTO branchANDConDTO2 = new BranchANDConDTO();
    assertThat(branchANDConDTO1).isNotEqualTo(branchANDConDTO2);
    branchANDConDTO2.setId(branchANDConDTO1.getId());
    assertThat(branchANDConDTO1).isEqualTo(branchANDConDTO2);
    branchANDConDTO2.setId(2L);
    assertThat(branchANDConDTO1).isNotEqualTo(branchANDConDTO2);
    branchANDConDTO1.setId(null);
    assertThat(branchANDConDTO1).isNotEqualTo(branchANDConDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(branchANDConMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(branchANDConMapper.fromId(null)).isNull();
  }
}
