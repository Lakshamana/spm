package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.OrganizationMetric;
import br.ufpa.labes.spm.repository.OrganizationMetricRepository;
import br.ufpa.labes.spm.service.OrganizationMetricService;
import br.ufpa.labes.spm.service.dto.OrganizationMetricDTO;
import br.ufpa.labes.spm.service.mapper.OrganizationMetricMapper;
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

/** Integration tests for the {@link OrganizationMetricResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class OrganizationMetricResourceIT {

  @Autowired private OrganizationMetricRepository organizationMetricRepository;

  @Autowired private OrganizationMetricMapper organizationMetricMapper;

  @Autowired private OrganizationMetricService organizationMetricService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restOrganizationMetricMockMvc;

  private OrganizationMetric organizationMetric;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final OrganizationMetricResource organizationMetricResource =
        new OrganizationMetricResource(organizationMetricService);
    this.restOrganizationMetricMockMvc =
        MockMvcBuilders.standaloneSetup(organizationMetricResource)
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
  public static OrganizationMetric createEntity(EntityManager em) {
    OrganizationMetric organizationMetric = new OrganizationMetric();
    return organizationMetric;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static OrganizationMetric createUpdatedEntity(EntityManager em) {
    OrganizationMetric organizationMetric = new OrganizationMetric();
    return organizationMetric;
  }

  @BeforeEach
  public void initTest() {
    organizationMetric = createEntity(em);
  }

  @Test
  @Transactional
  public void createOrganizationMetric() throws Exception {
    int databaseSizeBeforeCreate = organizationMetricRepository.findAll().size();

    // Create the OrganizationMetric
    OrganizationMetricDTO organizationMetricDTO =
        organizationMetricMapper.toDto(organizationMetric);
    restOrganizationMetricMockMvc
        .perform(
            post("/api/organization-metrics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organizationMetricDTO)))
        .andExpect(status().isCreated());

    // Validate the OrganizationMetric in the database
    List<OrganizationMetric> organizationMetricList = organizationMetricRepository.findAll();
    assertThat(organizationMetricList).hasSize(databaseSizeBeforeCreate + 1);
    OrganizationMetric testOrganizationMetric =
        organizationMetricList.get(organizationMetricList.size() - 1);
  }

  @Test
  @Transactional
  public void createOrganizationMetricWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = organizationMetricRepository.findAll().size();

    // Create the OrganizationMetric with an existing ID
    organizationMetric.setId(1L);
    OrganizationMetricDTO organizationMetricDTO =
        organizationMetricMapper.toDto(organizationMetric);

    // An entity with an existing ID cannot be created, so this API call must fail
    restOrganizationMetricMockMvc
        .perform(
            post("/api/organization-metrics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organizationMetricDTO)))
        .andExpect(status().isBadRequest());

    // Validate the OrganizationMetric in the database
    List<OrganizationMetric> organizationMetricList = organizationMetricRepository.findAll();
    assertThat(organizationMetricList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllOrganizationMetrics() throws Exception {
    // Initialize the database
    organizationMetricRepository.saveAndFlush(organizationMetric);

    // Get all the organizationMetricList
    restOrganizationMetricMockMvc
        .perform(get("/api/organization-metrics?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(organizationMetric.getId().intValue())));
  }

  @Test
  @Transactional
  public void getOrganizationMetric() throws Exception {
    // Initialize the database
    organizationMetricRepository.saveAndFlush(organizationMetric);

    // Get the organizationMetric
    restOrganizationMetricMockMvc
        .perform(get("/api/organization-metrics/{id}", organizationMetric.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(organizationMetric.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingOrganizationMetric() throws Exception {
    // Get the organizationMetric
    restOrganizationMetricMockMvc
        .perform(get("/api/organization-metrics/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateOrganizationMetric() throws Exception {
    // Initialize the database
    organizationMetricRepository.saveAndFlush(organizationMetric);

    int databaseSizeBeforeUpdate = organizationMetricRepository.findAll().size();

    // Update the organizationMetric
    OrganizationMetric updatedOrganizationMetric =
        organizationMetricRepository.findById(organizationMetric.getId()).get();
    // Disconnect from session so that the updates on updatedOrganizationMetric are not directly
    // saved in db
    em.detach(updatedOrganizationMetric);
    OrganizationMetricDTO organizationMetricDTO =
        organizationMetricMapper.toDto(updatedOrganizationMetric);

    restOrganizationMetricMockMvc
        .perform(
            put("/api/organization-metrics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organizationMetricDTO)))
        .andExpect(status().isOk());

    // Validate the OrganizationMetric in the database
    List<OrganizationMetric> organizationMetricList = organizationMetricRepository.findAll();
    assertThat(organizationMetricList).hasSize(databaseSizeBeforeUpdate);
    OrganizationMetric testOrganizationMetric =
        organizationMetricList.get(organizationMetricList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingOrganizationMetric() throws Exception {
    int databaseSizeBeforeUpdate = organizationMetricRepository.findAll().size();

    // Create the OrganizationMetric
    OrganizationMetricDTO organizationMetricDTO =
        organizationMetricMapper.toDto(organizationMetric);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restOrganizationMetricMockMvc
        .perform(
            put("/api/organization-metrics")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(organizationMetricDTO)))
        .andExpect(status().isBadRequest());

    // Validate the OrganizationMetric in the database
    List<OrganizationMetric> organizationMetricList = organizationMetricRepository.findAll();
    assertThat(organizationMetricList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteOrganizationMetric() throws Exception {
    // Initialize the database
    organizationMetricRepository.saveAndFlush(organizationMetric);

    int databaseSizeBeforeDelete = organizationMetricRepository.findAll().size();

    // Delete the organizationMetric
    restOrganizationMetricMockMvc
        .perform(
            delete("/api/organization-metrics/{id}", organizationMetric.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<OrganizationMetric> organizationMetricList = organizationMetricRepository.findAll();
    assertThat(organizationMetricList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(OrganizationMetric.class);
    OrganizationMetric organizationMetric1 = new OrganizationMetric();
    organizationMetric1.setId(1L);
    OrganizationMetric organizationMetric2 = new OrganizationMetric();
    organizationMetric2.setId(organizationMetric1.getId());
    assertThat(organizationMetric1).isEqualTo(organizationMetric2);
    organizationMetric2.setId(2L);
    assertThat(organizationMetric1).isNotEqualTo(organizationMetric2);
    organizationMetric1.setId(null);
    assertThat(organizationMetric1).isNotEqualTo(organizationMetric2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(OrganizationMetricDTO.class);
    OrganizationMetricDTO organizationMetricDTO1 = new OrganizationMetricDTO();
    organizationMetricDTO1.setId(1L);
    OrganizationMetricDTO organizationMetricDTO2 = new OrganizationMetricDTO();
    assertThat(organizationMetricDTO1).isNotEqualTo(organizationMetricDTO2);
    organizationMetricDTO2.setId(organizationMetricDTO1.getId());
    assertThat(organizationMetricDTO1).isEqualTo(organizationMetricDTO2);
    organizationMetricDTO2.setId(2L);
    assertThat(organizationMetricDTO1).isNotEqualTo(organizationMetricDTO2);
    organizationMetricDTO1.setId(null);
    assertThat(organizationMetricDTO1).isNotEqualTo(organizationMetricDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(organizationMetricMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(organizationMetricMapper.fromId(null)).isNull();
  }
}
