package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ToolType;
import br.ufpa.labes.spm.repository.ToolTypeRepository;
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

/** Integration tests for the {@link ToolTypeResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ToolTypeResourceIT {

  @Autowired private ToolTypeRepository toolTypeRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restToolTypeMockMvc;

  private ToolType toolType;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ToolTypeResource toolTypeResource = new ToolTypeResource(toolTypeRepository);
    this.restToolTypeMockMvc =
        MockMvcBuilders.standaloneSetup(toolTypeResource)
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
  public static ToolType createEntity(EntityManager em) {
    ToolType toolType = new ToolType();
    return toolType;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ToolType createUpdatedEntity(EntityManager em) {
    ToolType toolType = new ToolType();
    return toolType;
  }

  @BeforeEach
  public void initTest() {
    toolType = createEntity(em);
  }

  @Test
  @Transactional
  public void createToolType() throws Exception {
    int databaseSizeBeforeCreate = toolTypeRepository.findAll().size();

    // Create the ToolType
    restToolTypeMockMvc
        .perform(
            post("/api/tool-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(toolType)))
        .andExpect(status().isCreated());

    // Validate the ToolType in the database
    List<ToolType> toolTypeList = toolTypeRepository.findAll();
    assertThat(toolTypeList).hasSize(databaseSizeBeforeCreate + 1);
    ToolType testToolType = toolTypeList.get(toolTypeList.size() - 1);
  }

  @Test
  @Transactional
  public void createToolTypeWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = toolTypeRepository.findAll().size();

    // Create the ToolType with an existing ID
    toolType.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restToolTypeMockMvc
        .perform(
            post("/api/tool-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(toolType)))
        .andExpect(status().isBadRequest());

    // Validate the ToolType in the database
    List<ToolType> toolTypeList = toolTypeRepository.findAll();
    assertThat(toolTypeList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllToolTypes() throws Exception {
    // Initialize the database
    toolTypeRepository.saveAndFlush(toolType);

    // Get all the toolTypeList
    restToolTypeMockMvc
        .perform(get("/api/tool-types?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(toolType.getId().intValue())));
  }

  @Test
  @Transactional
  public void getToolType() throws Exception {
    // Initialize the database
    toolTypeRepository.saveAndFlush(toolType);

    // Get the toolType
    restToolTypeMockMvc
        .perform(get("/api/tool-types/{id}", toolType.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(toolType.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingToolType() throws Exception {
    // Get the toolType
    restToolTypeMockMvc
        .perform(get("/api/tool-types/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateToolType() throws Exception {
    // Initialize the database
    toolTypeRepository.saveAndFlush(toolType);

    int databaseSizeBeforeUpdate = toolTypeRepository.findAll().size();

    // Update the toolType
    ToolType updatedToolType = toolTypeRepository.findById(toolType.getId()).get();
    // Disconnect from session so that the updates on updatedToolType are not directly saved in db
    em.detach(updatedToolType);

    restToolTypeMockMvc
        .perform(
            put("/api/tool-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedToolType)))
        .andExpect(status().isOk());

    // Validate the ToolType in the database
    List<ToolType> toolTypeList = toolTypeRepository.findAll();
    assertThat(toolTypeList).hasSize(databaseSizeBeforeUpdate);
    ToolType testToolType = toolTypeList.get(toolTypeList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingToolType() throws Exception {
    int databaseSizeBeforeUpdate = toolTypeRepository.findAll().size();

    // Create the ToolType

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restToolTypeMockMvc
        .perform(
            put("/api/tool-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(toolType)))
        .andExpect(status().isBadRequest());

    // Validate the ToolType in the database
    List<ToolType> toolTypeList = toolTypeRepository.findAll();
    assertThat(toolTypeList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteToolType() throws Exception {
    // Initialize the database
    toolTypeRepository.saveAndFlush(toolType);

    int databaseSizeBeforeDelete = toolTypeRepository.findAll().size();

    // Delete the toolType
    restToolTypeMockMvc
        .perform(
            delete("/api/tool-types/{id}", toolType.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ToolType> toolTypeList = toolTypeRepository.findAll();
    assertThat(toolTypeList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ToolType.class);
    ToolType toolType1 = new ToolType();
    toolType1.setId(1L);
    ToolType toolType2 = new ToolType();
    toolType2.setId(toolType1.getId());
    assertThat(toolType1).isEqualTo(toolType2);
    toolType2.setId(2L);
    assertThat(toolType1).isNotEqualTo(toolType2);
    toolType1.setId(null);
    assertThat(toolType1).isNotEqualTo(toolType2);
  }
}
