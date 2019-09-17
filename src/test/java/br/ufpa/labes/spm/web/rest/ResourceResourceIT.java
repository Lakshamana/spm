package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Resource;
import br.ufpa.labes.spm.repository.ResourceRepository;
import br.ufpa.labes.spm.service.ResourceService;
import br.ufpa.labes.spm.service.dto.ResourceDTO;
import br.ufpa.labes.spm.service.mapper.ResourceMapper;
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

/** Integration tests for the {@link ResourceResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ResourceResourceIT {

  private static final String DEFAULT_IDENT = "AAAAAAAAAA";
  private static final String UPDATED_IDENT = "BBBBBBBBBB";

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  private static final Float DEFAULT_MTBF_TIME = 1F;
  private static final Float UPDATED_MTBF_TIME = 2F;
  private static final Float SMALLER_MTBF_TIME = 1F - 1F;

  private static final String DEFAULT_MTBF_UNIT_TIME = "AAAAAAAAAA";
  private static final String UPDATED_MTBF_UNIT_TIME = "BBBBBBBBBB";

  private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
  private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

  private static final Float DEFAULT_COST = 1F;
  private static final Float UPDATED_COST = 2F;
  private static final Float SMALLER_COST = 1F - 1F;

  private static final Boolean DEFAULT_IS_ACTIVE = false;
  private static final Boolean UPDATED_IS_ACTIVE = true;

  @Autowired private ResourceRepository resourceRepository;

  @Autowired private ResourceMapper resourceMapper;

  @Autowired private ResourceService resourceService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restResourceMockMvc;

  private Resource resource;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ResourceResource resourceResource = new ResourceResource(resourceService);
    this.restResourceMockMvc =
        MockMvcBuilders.standaloneSetup(resourceResource)
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
  public static Resource createEntity(EntityManager em) {
    Resource resource =
        new Resource()
            .ident(DEFAULT_IDENT)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .mtbfTime(DEFAULT_MTBF_TIME)
            .mtbfUnitTime(DEFAULT_MTBF_UNIT_TIME)
            .currency(DEFAULT_CURRENCY)
            .cost(DEFAULT_COST)
            .isActive(DEFAULT_IS_ACTIVE);
    return resource;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Resource createUpdatedEntity(EntityManager em) {
    Resource resource =
        new Resource()
            .ident(UPDATED_IDENT)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .mtbfTime(UPDATED_MTBF_TIME)
            .mtbfUnitTime(UPDATED_MTBF_UNIT_TIME)
            .currency(UPDATED_CURRENCY)
            .cost(UPDATED_COST)
            .isActive(UPDATED_IS_ACTIVE);
    return resource;
  }

  @BeforeEach
  public void initTest() {
    resource = createEntity(em);
  }

  @Test
  @Transactional
  public void createResource() throws Exception {
    int databaseSizeBeforeCreate = resourceRepository.findAll().size();

    // Create the Resource
    ResourceDTO resourceDTO = resourceMapper.toDto(resource);
    restResourceMockMvc
        .perform(
            post("/api/resources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourceDTO)))
        .andExpect(status().isCreated());

    // Validate the Resource in the database
    List<Resource> resourceList = resourceRepository.findAll();
    assertThat(resourceList).hasSize(databaseSizeBeforeCreate + 1);
    Resource testResource = resourceList.get(resourceList.size() - 1);
    assertThat(testResource.getIdent()).isEqualTo(DEFAULT_IDENT);
    assertThat(testResource.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testResource.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    assertThat(testResource.getMtbfTime()).isEqualTo(DEFAULT_MTBF_TIME);
    assertThat(testResource.getMtbfUnitTime()).isEqualTo(DEFAULT_MTBF_UNIT_TIME);
    assertThat(testResource.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
    assertThat(testResource.getCost()).isEqualTo(DEFAULT_COST);
    assertThat(testResource.isActive()).isEqualTo(DEFAULT_IS_ACTIVE);
  }

  @Test
  @Transactional
  public void createResourceWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = resourceRepository.findAll().size();

    // Create the Resource with an existing ID
    resource.setId(1L);
    ResourceDTO resourceDTO = resourceMapper.toDto(resource);

    // An entity with an existing ID cannot be created, so this API call must fail
    restResourceMockMvc
        .perform(
            post("/api/resources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourceDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Resource in the database
    List<Resource> resourceList = resourceRepository.findAll();
    assertThat(resourceList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllResources() throws Exception {
    // Initialize the database
    resourceRepository.saveAndFlush(resource);

    // Get all the resourceList
    restResourceMockMvc
        .perform(get("/api/resources?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(resource.getId().intValue())))
        .andExpect(jsonPath("$.[*].ident").value(hasItem(DEFAULT_IDENT.toString())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
        .andExpect(jsonPath("$.[*].mtbfTime").value(hasItem(DEFAULT_MTBF_TIME.doubleValue())))
        .andExpect(jsonPath("$.[*].mtbfUnitTime").value(hasItem(DEFAULT_MTBF_UNIT_TIME.toString())))
        .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
        .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
        .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
  }

  @Test
  @Transactional
  public void getResource() throws Exception {
    // Initialize the database
    resourceRepository.saveAndFlush(resource);

    // Get the resource
    restResourceMockMvc
        .perform(get("/api/resources/{id}", resource.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(resource.getId().intValue()))
        .andExpect(jsonPath("$.ident").value(DEFAULT_IDENT.toString()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
        .andExpect(jsonPath("$.mtbfTime").value(DEFAULT_MTBF_TIME.doubleValue()))
        .andExpect(jsonPath("$.mtbfUnitTime").value(DEFAULT_MTBF_UNIT_TIME.toString()))
        .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
        .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
        .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
  }

  @Test
  @Transactional
  public void getNonExistingResource() throws Exception {
    // Get the resource
    restResourceMockMvc
        .perform(get("/api/resources/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateResource() throws Exception {
    // Initialize the database
    resourceRepository.saveAndFlush(resource);

    int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

    // Update the resource
    Resource updatedResource = resourceRepository.findById(resource.getId()).get();
    // Disconnect from session so that the updates on updatedResource are not directly saved in db
    em.detach(updatedResource);
    updatedResource
        .ident(UPDATED_IDENT)
        .name(UPDATED_NAME)
        .description(UPDATED_DESCRIPTION)
        .mtbfTime(UPDATED_MTBF_TIME)
        .mtbfUnitTime(UPDATED_MTBF_UNIT_TIME)
        .currency(UPDATED_CURRENCY)
        .cost(UPDATED_COST)
        .isActive(UPDATED_IS_ACTIVE);
    ResourceDTO resourceDTO = resourceMapper.toDto(updatedResource);

    restResourceMockMvc
        .perform(
            put("/api/resources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourceDTO)))
        .andExpect(status().isOk());

    // Validate the Resource in the database
    List<Resource> resourceList = resourceRepository.findAll();
    assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
    Resource testResource = resourceList.get(resourceList.size() - 1);
    assertThat(testResource.getIdent()).isEqualTo(UPDATED_IDENT);
    assertThat(testResource.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testResource.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    assertThat(testResource.getMtbfTime()).isEqualTo(UPDATED_MTBF_TIME);
    assertThat(testResource.getMtbfUnitTime()).isEqualTo(UPDATED_MTBF_UNIT_TIME);
    assertThat(testResource.getCurrency()).isEqualTo(UPDATED_CURRENCY);
    assertThat(testResource.getCost()).isEqualTo(UPDATED_COST);
    assertThat(testResource.isActive()).isEqualTo(UPDATED_IS_ACTIVE);
  }

  @Test
  @Transactional
  public void updateNonExistingResource() throws Exception {
    int databaseSizeBeforeUpdate = resourceRepository.findAll().size();

    // Create the Resource
    ResourceDTO resourceDTO = resourceMapper.toDto(resource);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restResourceMockMvc
        .perform(
            put("/api/resources")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourceDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Resource in the database
    List<Resource> resourceList = resourceRepository.findAll();
    assertThat(resourceList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteResource() throws Exception {
    // Initialize the database
    resourceRepository.saveAndFlush(resource);

    int databaseSizeBeforeDelete = resourceRepository.findAll().size();

    // Delete the resource
    restResourceMockMvc
        .perform(
            delete("/api/resources/{id}", resource.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Resource> resourceList = resourceRepository.findAll();
    assertThat(resourceList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Resource.class);
    Resource resource1 = new Resource();
    resource1.setId(1L);
    Resource resource2 = new Resource();
    resource2.setId(resource1.getId());
    assertThat(resource1).isEqualTo(resource2);
    resource2.setId(2L);
    assertThat(resource1).isNotEqualTo(resource2);
    resource1.setId(null);
    assertThat(resource1).isNotEqualTo(resource2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(ResourceDTO.class);
    ResourceDTO resourceDTO1 = new ResourceDTO();
    resourceDTO1.setId(1L);
    ResourceDTO resourceDTO2 = new ResourceDTO();
    assertThat(resourceDTO1).isNotEqualTo(resourceDTO2);
    resourceDTO2.setId(resourceDTO1.getId());
    assertThat(resourceDTO1).isEqualTo(resourceDTO2);
    resourceDTO2.setId(2L);
    assertThat(resourceDTO1).isNotEqualTo(resourceDTO2);
    resourceDTO1.setId(null);
    assertThat(resourceDTO1).isNotEqualTo(resourceDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(resourceMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(resourceMapper.fromId(null)).isNull();
  }
}
