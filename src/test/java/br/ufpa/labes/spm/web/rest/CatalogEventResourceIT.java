package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.CatalogEvent;
import br.ufpa.labes.spm.repository.CatalogEventRepository;
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

/** Integration tests for the {@link CatalogEventResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class CatalogEventResourceIT {

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  @Autowired private CatalogEventRepository catalogEventRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restCatalogEventMockMvc;

  private CatalogEvent catalogEvent;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final CatalogEventResource catalogEventResource =
        new CatalogEventResource(catalogEventRepository);
    this.restCatalogEventMockMvc =
        MockMvcBuilders.standaloneSetup(catalogEventResource)
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
  public static CatalogEvent createEntity(EntityManager em) {
    CatalogEvent catalogEvent = new CatalogEvent().description(DEFAULT_DESCRIPTION);
    return catalogEvent;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static CatalogEvent createUpdatedEntity(EntityManager em) {
    CatalogEvent catalogEvent = new CatalogEvent().description(UPDATED_DESCRIPTION);
    return catalogEvent;
  }

  @BeforeEach
  public void initTest() {
    catalogEvent = createEntity(em);
  }

  @Test
  @Transactional
  public void createCatalogEvent() throws Exception {
    int databaseSizeBeforeCreate = catalogEventRepository.findAll().size();

    // Create the CatalogEvent
    restCatalogEventMockMvc
        .perform(
            post("/api/catalog-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(catalogEvent)))
        .andExpect(status().isCreated());

    // Validate the CatalogEvent in the database
    List<CatalogEvent> catalogEventList = catalogEventRepository.findAll();
    assertThat(catalogEventList).hasSize(databaseSizeBeforeCreate + 1);
    CatalogEvent testCatalogEvent = catalogEventList.get(catalogEventList.size() - 1);
    assertThat(testCatalogEvent.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
  }

  @Test
  @Transactional
  public void createCatalogEventWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = catalogEventRepository.findAll().size();

    // Create the CatalogEvent with an existing ID
    catalogEvent.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restCatalogEventMockMvc
        .perform(
            post("/api/catalog-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(catalogEvent)))
        .andExpect(status().isBadRequest());

    // Validate the CatalogEvent in the database
    List<CatalogEvent> catalogEventList = catalogEventRepository.findAll();
    assertThat(catalogEventList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllCatalogEvents() throws Exception {
    // Initialize the database
    catalogEventRepository.saveAndFlush(catalogEvent);

    // Get all the catalogEventList
    restCatalogEventMockMvc
        .perform(get("/api/catalog-events?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(catalogEvent.getId().intValue())))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
  }

  @Test
  @Transactional
  public void getCatalogEvent() throws Exception {
    // Initialize the database
    catalogEventRepository.saveAndFlush(catalogEvent);

    // Get the catalogEvent
    restCatalogEventMockMvc
        .perform(get("/api/catalog-events/{id}", catalogEvent.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(catalogEvent.getId().intValue()))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingCatalogEvent() throws Exception {
    // Get the catalogEvent
    restCatalogEventMockMvc
        .perform(get("/api/catalog-events/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateCatalogEvent() throws Exception {
    // Initialize the database
    catalogEventRepository.saveAndFlush(catalogEvent);

    int databaseSizeBeforeUpdate = catalogEventRepository.findAll().size();

    // Update the catalogEvent
    CatalogEvent updatedCatalogEvent = catalogEventRepository.findById(catalogEvent.getId()).get();
    // Disconnect from session so that the updates on updatedCatalogEvent are not directly saved in
    // db
    em.detach(updatedCatalogEvent);
    updatedCatalogEvent.description(UPDATED_DESCRIPTION);

    restCatalogEventMockMvc
        .perform(
            put("/api/catalog-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCatalogEvent)))
        .andExpect(status().isOk());

    // Validate the CatalogEvent in the database
    List<CatalogEvent> catalogEventList = catalogEventRepository.findAll();
    assertThat(catalogEventList).hasSize(databaseSizeBeforeUpdate);
    CatalogEvent testCatalogEvent = catalogEventList.get(catalogEventList.size() - 1);
    assertThat(testCatalogEvent.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  }

  @Test
  @Transactional
  public void updateNonExistingCatalogEvent() throws Exception {
    int databaseSizeBeforeUpdate = catalogEventRepository.findAll().size();

    // Create the CatalogEvent

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restCatalogEventMockMvc
        .perform(
            put("/api/catalog-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(catalogEvent)))
        .andExpect(status().isBadRequest());

    // Validate the CatalogEvent in the database
    List<CatalogEvent> catalogEventList = catalogEventRepository.findAll();
    assertThat(catalogEventList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteCatalogEvent() throws Exception {
    // Initialize the database
    catalogEventRepository.saveAndFlush(catalogEvent);

    int databaseSizeBeforeDelete = catalogEventRepository.findAll().size();

    // Delete the catalogEvent
    restCatalogEventMockMvc
        .perform(
            delete("/api/catalog-events/{id}", catalogEvent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<CatalogEvent> catalogEventList = catalogEventRepository.findAll();
    assertThat(catalogEventList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(CatalogEvent.class);
    CatalogEvent catalogEvent1 = new CatalogEvent();
    catalogEvent1.setId(1L);
    CatalogEvent catalogEvent2 = new CatalogEvent();
    catalogEvent2.setId(catalogEvent1.getId());
    assertThat(catalogEvent1).isEqualTo(catalogEvent2);
    catalogEvent2.setId(2L);
    assertThat(catalogEvent1).isNotEqualTo(catalogEvent2);
    catalogEvent1.setId(null);
    assertThat(catalogEvent1).isNotEqualTo(catalogEvent2);
  }
}
