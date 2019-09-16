package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Description;
import br.ufpa.labes.spm.repository.DescriptionRepository;
import br.ufpa.labes.spm.service.DescriptionService;
import br.ufpa.labes.spm.service.dto.DescriptionDTO;
import br.ufpa.labes.spm.service.mapper.DescriptionMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static br.ufpa.labes.spm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Integration tests for the {@link DescriptionResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class DescriptionResourceIT {

  private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

  private static final String DEFAULT_WHY = "AAAAAAAAAA";
  private static final String UPDATED_WHY = "BBBBBBBBBB";

  @Autowired private DescriptionRepository descriptionRepository;

  @Autowired private DescriptionMapper descriptionMapper;

  @Autowired private DescriptionService descriptionService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restDescriptionMockMvc;

  private Description description;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final DescriptionResource descriptionResource = new DescriptionResource(descriptionService);
    this.restDescriptionMockMvc =
        MockMvcBuilders.standaloneSetup(descriptionResource)
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
  public static Description createEntity(EntityManager em) {
    Description description = new Description().date(DEFAULT_DATE).why(DEFAULT_WHY);
    return description;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Description createUpdatedEntity(EntityManager em) {
    Description description = new Description().date(UPDATED_DATE).why(UPDATED_WHY);
    return description;
  }

  @BeforeEach
  public void initTest() {
    description = createEntity(em);
  }

  @Test
  @Transactional
  public void createDescription() throws Exception {
    int databaseSizeBeforeCreate = descriptionRepository.findAll().size();

    // Create the Description
    DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);
    restDescriptionMockMvc
        .perform(
            post("/api/descriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(descriptionDTO)))
        .andExpect(status().isCreated());

    // Validate the Description in the database
    List<Description> descriptionList = descriptionRepository.findAll();
    assertThat(descriptionList).hasSize(databaseSizeBeforeCreate + 1);
    Description testDescription = descriptionList.get(descriptionList.size() - 1);
    assertThat(testDescription.getDate()).isEqualTo(DEFAULT_DATE);
    assertThat(testDescription.getWhy()).isEqualTo(DEFAULT_WHY);
  }

  @Test
  @Transactional
  public void createDescriptionWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = descriptionRepository.findAll().size();

    // Create the Description with an existing ID
    description.setId(1L);
    DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

    // An entity with an existing ID cannot be created, so this API call must fail
    restDescriptionMockMvc
        .perform(
            post("/api/descriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(descriptionDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Description in the database
    List<Description> descriptionList = descriptionRepository.findAll();
    assertThat(descriptionList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllDescriptions() throws Exception {
    // Initialize the database
    descriptionRepository.saveAndFlush(description);

    // Get all the descriptionList
    restDescriptionMockMvc
        .perform(get("/api/descriptions?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(description.getId().intValue())))
        .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
        .andExpect(jsonPath("$.[*].why").value(hasItem(DEFAULT_WHY.toString())));
  }

  @Test
  @Transactional
  public void getDescription() throws Exception {
    // Initialize the database
    descriptionRepository.saveAndFlush(description);

    // Get the description
    restDescriptionMockMvc
        .perform(get("/api/descriptions/{id}", description.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(description.getId().intValue()))
        .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
        .andExpect(jsonPath("$.why").value(DEFAULT_WHY.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingDescription() throws Exception {
    // Get the description
    restDescriptionMockMvc
        .perform(get("/api/descriptions/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateDescription() throws Exception {
    // Initialize the database
    descriptionRepository.saveAndFlush(description);

    int databaseSizeBeforeUpdate = descriptionRepository.findAll().size();

    // Update the description
    Description updatedDescription = descriptionRepository.findById(description.getId()).get();
    // Disconnect from session so that the updates on updatedDescription are not directly saved in
    // db
    em.detach(updatedDescription);
    updatedDescription.date(UPDATED_DATE).why(UPDATED_WHY);
    DescriptionDTO descriptionDTO = descriptionMapper.toDto(updatedDescription);

    restDescriptionMockMvc
        .perform(
            put("/api/descriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(descriptionDTO)))
        .andExpect(status().isOk());

    // Validate the Description in the database
    List<Description> descriptionList = descriptionRepository.findAll();
    assertThat(descriptionList).hasSize(databaseSizeBeforeUpdate);
    Description testDescription = descriptionList.get(descriptionList.size() - 1);
    assertThat(testDescription.getDate()).isEqualTo(UPDATED_DATE);
    assertThat(testDescription.getWhy()).isEqualTo(UPDATED_WHY);
  }

  @Test
  @Transactional
  public void updateNonExistingDescription() throws Exception {
    int databaseSizeBeforeUpdate = descriptionRepository.findAll().size();

    // Create the Description
    DescriptionDTO descriptionDTO = descriptionMapper.toDto(description);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restDescriptionMockMvc
        .perform(
            put("/api/descriptions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(descriptionDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Description in the database
    List<Description> descriptionList = descriptionRepository.findAll();
    assertThat(descriptionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteDescription() throws Exception {
    // Initialize the database
    descriptionRepository.saveAndFlush(description);

    int databaseSizeBeforeDelete = descriptionRepository.findAll().size();

    // Delete the description
    restDescriptionMockMvc
        .perform(
            delete("/api/descriptions/{id}", description.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Description> descriptionList = descriptionRepository.findAll();
    assertThat(descriptionList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Description.class);
    Description description1 = new Description();
    description1.setId(1L);
    Description description2 = new Description();
    description2.setId(description1.getId());
    assertThat(description1).isEqualTo(description2);
    description2.setId(2L);
    assertThat(description1).isNotEqualTo(description2);
    description1.setId(null);
    assertThat(description1).isNotEqualTo(description2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(DescriptionDTO.class);
    DescriptionDTO descriptionDTO1 = new DescriptionDTO();
    descriptionDTO1.setId(1L);
    DescriptionDTO descriptionDTO2 = new DescriptionDTO();
    assertThat(descriptionDTO1).isNotEqualTo(descriptionDTO2);
    descriptionDTO2.setId(descriptionDTO1.getId());
    assertThat(descriptionDTO1).isEqualTo(descriptionDTO2);
    descriptionDTO2.setId(2L);
    assertThat(descriptionDTO1).isNotEqualTo(descriptionDTO2);
    descriptionDTO1.setId(null);
    assertThat(descriptionDTO1).isNotEqualTo(descriptionDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(descriptionMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(descriptionMapper.fromId(null)).isNull();
  }
}
