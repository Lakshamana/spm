package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ResourceEvent;
import br.ufpa.labes.spm.repository.ResourceEventRepository;
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

/** Integration tests for the {@link ResourceEventResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ResourceEventResourceIT {

  @Autowired private ResourceEventRepository resourceEventRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restResourceEventMockMvc;

  private ResourceEvent resourceEvent;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ResourceEventResource resourceEventResource =
        new ResourceEventResource(resourceEventRepository);
    this.restResourceEventMockMvc =
        MockMvcBuilders.standaloneSetup(resourceEventResource)
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
  public static ResourceEvent createEntity(EntityManager em) {
    ResourceEvent resourceEvent = new ResourceEvent();
    return resourceEvent;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ResourceEvent createUpdatedEntity(EntityManager em) {
    ResourceEvent resourceEvent = new ResourceEvent();
    return resourceEvent;
  }

  @BeforeEach
  public void initTest() {
    resourceEvent = createEntity(em);
  }

  @Test
  @Transactional
  public void createResourceEvent() throws Exception {
    int databaseSizeBeforeCreate = resourceEventRepository.findAll().size();

    // Create the ResourceEvent
    restResourceEventMockMvc
        .perform(
            post("/api/resource-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourceEvent)))
        .andExpect(status().isCreated());

    // Validate the ResourceEvent in the database
    List<ResourceEvent> resourceEventList = resourceEventRepository.findAll();
    assertThat(resourceEventList).hasSize(databaseSizeBeforeCreate + 1);
    ResourceEvent testResourceEvent = resourceEventList.get(resourceEventList.size() - 1);
  }

  @Test
  @Transactional
  public void createResourceEventWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = resourceEventRepository.findAll().size();

    // Create the ResourceEvent with an existing ID
    resourceEvent.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restResourceEventMockMvc
        .perform(
            post("/api/resource-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourceEvent)))
        .andExpect(status().isBadRequest());

    // Validate the ResourceEvent in the database
    List<ResourceEvent> resourceEventList = resourceEventRepository.findAll();
    assertThat(resourceEventList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllResourceEvents() throws Exception {
    // Initialize the database
    resourceEventRepository.saveAndFlush(resourceEvent);

    // Get all the resourceEventList
    restResourceEventMockMvc
        .perform(get("/api/resource-events?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(resourceEvent.getId().intValue())));
  }

  @Test
  @Transactional
  public void getResourceEvent() throws Exception {
    // Initialize the database
    resourceEventRepository.saveAndFlush(resourceEvent);

    // Get the resourceEvent
    restResourceEventMockMvc
        .perform(get("/api/resource-events/{id}", resourceEvent.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(resourceEvent.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingResourceEvent() throws Exception {
    // Get the resourceEvent
    restResourceEventMockMvc
        .perform(get("/api/resource-events/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateResourceEvent() throws Exception {
    // Initialize the database
    resourceEventRepository.saveAndFlush(resourceEvent);

    int databaseSizeBeforeUpdate = resourceEventRepository.findAll().size();

    // Update the resourceEvent
    ResourceEvent updatedResourceEvent =
        resourceEventRepository.findById(resourceEvent.getId()).get();
    // Disconnect from session so that the updates on updatedResourceEvent are not directly saved in
    // db
    em.detach(updatedResourceEvent);

    restResourceEventMockMvc
        .perform(
            put("/api/resource-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedResourceEvent)))
        .andExpect(status().isOk());

    // Validate the ResourceEvent in the database
    List<ResourceEvent> resourceEventList = resourceEventRepository.findAll();
    assertThat(resourceEventList).hasSize(databaseSizeBeforeUpdate);
    ResourceEvent testResourceEvent = resourceEventList.get(resourceEventList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingResourceEvent() throws Exception {
    int databaseSizeBeforeUpdate = resourceEventRepository.findAll().size();

    // Create the ResourceEvent

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restResourceEventMockMvc
        .perform(
            put("/api/resource-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(resourceEvent)))
        .andExpect(status().isBadRequest());

    // Validate the ResourceEvent in the database
    List<ResourceEvent> resourceEventList = resourceEventRepository.findAll();
    assertThat(resourceEventList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteResourceEvent() throws Exception {
    // Initialize the database
    resourceEventRepository.saveAndFlush(resourceEvent);

    int databaseSizeBeforeDelete = resourceEventRepository.findAll().size();

    // Delete the resourceEvent
    restResourceEventMockMvc
        .perform(
            delete("/api/resource-events/{id}", resourceEvent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ResourceEvent> resourceEventList = resourceEventRepository.findAll();
    assertThat(resourceEventList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ResourceEvent.class);
    ResourceEvent resourceEvent1 = new ResourceEvent();
    resourceEvent1.setId(1L);
    ResourceEvent resourceEvent2 = new ResourceEvent();
    resourceEvent2.setId(resourceEvent1.getId());
    assertThat(resourceEvent1).isEqualTo(resourceEvent2);
    resourceEvent2.setId(2L);
    assertThat(resourceEvent1).isNotEqualTo(resourceEvent2);
    resourceEvent1.setId(null);
    assertThat(resourceEvent1).isNotEqualTo(resourceEvent2);
  }
}
