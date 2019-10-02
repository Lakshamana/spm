package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ResourcePossibleUse;
import br.ufpa.labes.spm.repository.ResourcePossibleUseRepository;
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

/** Integration tests for the {@link ResourcePossibleUseResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ResourcePossibleUseResourceIT {

  private static final LocalDate DEFAULT_BEGIN = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_BEGIN = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate SMALLER_BEGIN = LocalDate.ofEpochDay(-1L);

  private static final LocalDate DEFAULT_END = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_END = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate SMALLER_END = LocalDate.ofEpochDay(-1L);

  private static final Float DEFAULT_AMOUNT_NEEDED = 1F;
  private static final Float UPDATED_AMOUNT_NEEDED = 2F;
  private static final Float SMALLER_AMOUNT_NEEDED = 1F - 1F;

  @Autowired private ResourcePossibleUseRepository resourcePossibleUseRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restResourcePossibleUseMockMvc;

  private ResourcePossibleUse resourcePossibleUse;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ResourcePossibleUseResource resourcePossibleUseResource =
        new ResourcePossibleUseResource(resourcePossibleUseRepository);
    this.restResourcePossibleUseMockMvc =
        MockMvcBuilders.standaloneSetup(resourcePossibleUseResource)
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
  public static ResourcePossibleUse createEntity(EntityManager em) {
    ResourcePossibleUse resourcePossibleUse =
        new ResourcePossibleUse()
            .begin(DEFAULT_BEGIN)
            .end(DEFAULT_END)
            .amountNeeded(DEFAULT_AMOUNT_NEEDED);
    return resourcePossibleUse;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ResourcePossibleUse createUpdatedEntity(EntityManager em) {
    ResourcePossibleUse resourcePossibleUse =
        new ResourcePossibleUse()
            .begin(UPDATED_BEGIN)
            .end(UPDATED_END)
            .amountNeeded(UPDATED_AMOUNT_NEEDED);
    return resourcePossibleUse;
  }

  @BeforeEach
  public void initTest() {
    resourcePossibleUse = createEntity(em);
  }

  @Test
  @Transactional
  public void createResourcePossibleUse() throws Exception {
    int databaseSizeBeforeCreate = resourcePossibleUseRepository.findAll().size();

    // Create the ResourcePossibleUse
    restResourcePossibleUseMockMvc
        .perform(
            post("/api/resource-possible-uses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourcePossibleUse)))
        .andExpect(status().isCreated());

    // Validate the ResourcePossibleUse in the database
    List<ResourcePossibleUse> resourcePossibleUseList = resourcePossibleUseRepository.findAll();
    assertThat(resourcePossibleUseList).hasSize(databaseSizeBeforeCreate + 1);
    ResourcePossibleUse testResourcePossibleUse =
        resourcePossibleUseList.get(resourcePossibleUseList.size() - 1);
    assertThat(testResourcePossibleUse.getBegin()).isEqualTo(DEFAULT_BEGIN);
    assertThat(testResourcePossibleUse.getEnd()).isEqualTo(DEFAULT_END);
    assertThat(testResourcePossibleUse.getAmountNeeded()).isEqualTo(DEFAULT_AMOUNT_NEEDED);
  }

  @Test
  @Transactional
  public void createResourcePossibleUseWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = resourcePossibleUseRepository.findAll().size();

    // Create the ResourcePossibleUse with an existing ID
    resourcePossibleUse.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restResourcePossibleUseMockMvc
        .perform(
            post("/api/resource-possible-uses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourcePossibleUse)))
        .andExpect(status().isBadRequest());

    // Validate the ResourcePossibleUse in the database
    List<ResourcePossibleUse> resourcePossibleUseList = resourcePossibleUseRepository.findAll();
    assertThat(resourcePossibleUseList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllResourcePossibleUses() throws Exception {
    // Initialize the database
    resourcePossibleUseRepository.saveAndFlush(resourcePossibleUse);

    // Get all the resourcePossibleUseList
    restResourcePossibleUseMockMvc
        .perform(get("/api/resource-possible-uses?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(resourcePossibleUse.getId().intValue())))
        .andExpect(jsonPath("$.[*].begin").value(hasItem(DEFAULT_BEGIN.toString())))
        .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())))
        .andExpect(
            jsonPath("$.[*].amountNeeded").value(hasItem(DEFAULT_AMOUNT_NEEDED.doubleValue())));
  }

  @Test
  @Transactional
  public void getResourcePossibleUse() throws Exception {
    // Initialize the database
    resourcePossibleUseRepository.saveAndFlush(resourcePossibleUse);

    // Get the resourcePossibleUse
    restResourcePossibleUseMockMvc
        .perform(get("/api/resource-possible-uses/{id}", resourcePossibleUse.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(resourcePossibleUse.getId().intValue()))
        .andExpect(jsonPath("$.begin").value(DEFAULT_BEGIN.toString()))
        .andExpect(jsonPath("$.end").value(DEFAULT_END.toString()))
        .andExpect(jsonPath("$.amountNeeded").value(DEFAULT_AMOUNT_NEEDED.doubleValue()));
  }

  @Test
  @Transactional
  public void getNonExistingResourcePossibleUse() throws Exception {
    // Get the resourcePossibleUse
    restResourcePossibleUseMockMvc
        .perform(get("/api/resource-possible-uses/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateResourcePossibleUse() throws Exception {
    // Initialize the database
    resourcePossibleUseRepository.saveAndFlush(resourcePossibleUse);

    int databaseSizeBeforeUpdate = resourcePossibleUseRepository.findAll().size();

    // Update the resourcePossibleUse
    ResourcePossibleUse updatedResourcePossibleUse =
        resourcePossibleUseRepository.findById(resourcePossibleUse.getId()).get();
    // Disconnect from session so that the updates on updatedResourcePossibleUse are not directly
    // saved in db
    em.detach(updatedResourcePossibleUse);
    updatedResourcePossibleUse
        .begin(UPDATED_BEGIN)
        .end(UPDATED_END)
        .amountNeeded(UPDATED_AMOUNT_NEEDED);

    restResourcePossibleUseMockMvc
        .perform(
            put("/api/resource-possible-uses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedResourcePossibleUse)))
        .andExpect(status().isOk());

    // Validate the ResourcePossibleUse in the database
    List<ResourcePossibleUse> resourcePossibleUseList = resourcePossibleUseRepository.findAll();
    assertThat(resourcePossibleUseList).hasSize(databaseSizeBeforeUpdate);
    ResourcePossibleUse testResourcePossibleUse =
        resourcePossibleUseList.get(resourcePossibleUseList.size() - 1);
    assertThat(testResourcePossibleUse.getBegin()).isEqualTo(UPDATED_BEGIN);
    assertThat(testResourcePossibleUse.getEnd()).isEqualTo(UPDATED_END);
    assertThat(testResourcePossibleUse.getAmountNeeded()).isEqualTo(UPDATED_AMOUNT_NEEDED);
  }

  @Test
  @Transactional
  public void updateNonExistingResourcePossibleUse() throws Exception {
    int databaseSizeBeforeUpdate = resourcePossibleUseRepository.findAll().size();

    // Create the ResourcePossibleUse

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restResourcePossibleUseMockMvc
        .perform(
            put("/api/resource-possible-uses")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourcePossibleUse)))
        .andExpect(status().isBadRequest());

    // Validate the ResourcePossibleUse in the database
    List<ResourcePossibleUse> resourcePossibleUseList = resourcePossibleUseRepository.findAll();
    assertThat(resourcePossibleUseList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteResourcePossibleUse() throws Exception {
    // Initialize the database
    resourcePossibleUseRepository.saveAndFlush(resourcePossibleUse);

    int databaseSizeBeforeDelete = resourcePossibleUseRepository.findAll().size();

    // Delete the resourcePossibleUse
    restResourcePossibleUseMockMvc
        .perform(
            delete("/api/resource-possible-uses/{id}", resourcePossibleUse.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ResourcePossibleUse> resourcePossibleUseList = resourcePossibleUseRepository.findAll();
    assertThat(resourcePossibleUseList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ResourcePossibleUse.class);
    ResourcePossibleUse resourcePossibleUse1 = new ResourcePossibleUse();
    resourcePossibleUse1.setId(1L);
    ResourcePossibleUse resourcePossibleUse2 = new ResourcePossibleUse();
    resourcePossibleUse2.setId(resourcePossibleUse1.getId());
    assertThat(resourcePossibleUse1).isEqualTo(resourcePossibleUse2);
    resourcePossibleUse2.setId(2L);
    assertThat(resourcePossibleUse1).isNotEqualTo(resourcePossibleUse2);
    resourcePossibleUse1.setId(null);
    assertThat(resourcePossibleUse1).isNotEqualTo(resourcePossibleUse2);
  }
}
