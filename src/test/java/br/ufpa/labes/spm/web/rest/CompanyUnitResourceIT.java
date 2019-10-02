package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.CompanyUnit;
import br.ufpa.labes.spm.repository.CompanyUnitRepository;
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

/** Integration tests for the {@link CompanyUnitResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class CompanyUnitResourceIT {

  private static final String DEFAULT_IDENT = "AAAAAAAAAA";
  private static final String UPDATED_IDENT = "BBBBBBBBBB";

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  @Autowired private CompanyUnitRepository companyUnitRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restCompanyUnitMockMvc;

  private CompanyUnit companyUnit;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final CompanyUnitResource companyUnitResource = new CompanyUnitResource(companyUnitRepository);
    this.restCompanyUnitMockMvc =
        MockMvcBuilders.standaloneSetup(companyUnitResource)
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
  public static CompanyUnit createEntity(EntityManager em) {
    CompanyUnit companyUnit =
        new CompanyUnit().ident(DEFAULT_IDENT).name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    return companyUnit;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static CompanyUnit createUpdatedEntity(EntityManager em) {
    CompanyUnit companyUnit =
        new CompanyUnit().ident(UPDATED_IDENT).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    return companyUnit;
  }

  @BeforeEach
  public void initTest() {
    companyUnit = createEntity(em);
  }

  @Test
  @Transactional
  public void createCompanyUnit() throws Exception {
    int databaseSizeBeforeCreate = companyUnitRepository.findAll().size();

    // Create the CompanyUnit
    restCompanyUnitMockMvc
        .perform(
            post("/api/company-units")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyUnit)))
        .andExpect(status().isCreated());

    // Validate the CompanyUnit in the database
    List<CompanyUnit> companyUnitList = companyUnitRepository.findAll();
    assertThat(companyUnitList).hasSize(databaseSizeBeforeCreate + 1);
    CompanyUnit testCompanyUnit = companyUnitList.get(companyUnitList.size() - 1);
    assertThat(testCompanyUnit.getIdent()).isEqualTo(DEFAULT_IDENT);
    assertThat(testCompanyUnit.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testCompanyUnit.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
  }

  @Test
  @Transactional
  public void createCompanyUnitWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = companyUnitRepository.findAll().size();

    // Create the CompanyUnit with an existing ID
    companyUnit.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restCompanyUnitMockMvc
        .perform(
            post("/api/company-units")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyUnit)))
        .andExpect(status().isBadRequest());

    // Validate the CompanyUnit in the database
    List<CompanyUnit> companyUnitList = companyUnitRepository.findAll();
    assertThat(companyUnitList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllCompanyUnits() throws Exception {
    // Initialize the database
    companyUnitRepository.saveAndFlush(companyUnit);

    // Get all the companyUnitList
    restCompanyUnitMockMvc
        .perform(get("/api/company-units?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(companyUnit.getId().intValue())))
        .andExpect(jsonPath("$.[*].ident").value(hasItem(DEFAULT_IDENT.toString())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
  }

  @Test
  @Transactional
  public void getCompanyUnit() throws Exception {
    // Initialize the database
    companyUnitRepository.saveAndFlush(companyUnit);

    // Get the companyUnit
    restCompanyUnitMockMvc
        .perform(get("/api/company-units/{id}", companyUnit.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(companyUnit.getId().intValue()))
        .andExpect(jsonPath("$.ident").value(DEFAULT_IDENT.toString()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingCompanyUnit() throws Exception {
    // Get the companyUnit
    restCompanyUnitMockMvc
        .perform(get("/api/company-units/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateCompanyUnit() throws Exception {
    // Initialize the database
    companyUnitRepository.saveAndFlush(companyUnit);

    int databaseSizeBeforeUpdate = companyUnitRepository.findAll().size();

    // Update the companyUnit
    CompanyUnit updatedCompanyUnit = companyUnitRepository.findById(companyUnit.getId()).get();
    // Disconnect from session so that the updates on updatedCompanyUnit are not directly saved in
    // db
    em.detach(updatedCompanyUnit);
    updatedCompanyUnit.ident(UPDATED_IDENT).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

    restCompanyUnitMockMvc
        .perform(
            put("/api/company-units")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCompanyUnit)))
        .andExpect(status().isOk());

    // Validate the CompanyUnit in the database
    List<CompanyUnit> companyUnitList = companyUnitRepository.findAll();
    assertThat(companyUnitList).hasSize(databaseSizeBeforeUpdate);
    CompanyUnit testCompanyUnit = companyUnitList.get(companyUnitList.size() - 1);
    assertThat(testCompanyUnit.getIdent()).isEqualTo(UPDATED_IDENT);
    assertThat(testCompanyUnit.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testCompanyUnit.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  }

  @Test
  @Transactional
  public void updateNonExistingCompanyUnit() throws Exception {
    int databaseSizeBeforeUpdate = companyUnitRepository.findAll().size();

    // Create the CompanyUnit

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restCompanyUnitMockMvc
        .perform(
            put("/api/company-units")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyUnit)))
        .andExpect(status().isBadRequest());

    // Validate the CompanyUnit in the database
    List<CompanyUnit> companyUnitList = companyUnitRepository.findAll();
    assertThat(companyUnitList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteCompanyUnit() throws Exception {
    // Initialize the database
    companyUnitRepository.saveAndFlush(companyUnit);

    int databaseSizeBeforeDelete = companyUnitRepository.findAll().size();

    // Delete the companyUnit
    restCompanyUnitMockMvc
        .perform(
            delete("/api/company-units/{id}", companyUnit.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<CompanyUnit> companyUnitList = companyUnitRepository.findAll();
    assertThat(companyUnitList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(CompanyUnit.class);
    CompanyUnit companyUnit1 = new CompanyUnit();
    companyUnit1.setId(1L);
    CompanyUnit companyUnit2 = new CompanyUnit();
    companyUnit2.setId(companyUnit1.getId());
    assertThat(companyUnit1).isEqualTo(companyUnit2);
    companyUnit2.setId(2L);
    assertThat(companyUnit1).isNotEqualTo(companyUnit2);
    companyUnit1.setId(null);
    assertThat(companyUnit1).isNotEqualTo(companyUnit2);
  }
}
