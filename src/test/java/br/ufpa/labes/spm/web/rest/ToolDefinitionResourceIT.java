package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ToolDefinition;
import br.ufpa.labes.spm.repository.ToolDefinitionRepository;
import br.ufpa.labes.spm.service.ToolDefinitionService;
import br.ufpa.labes.spm.service.dto.ToolDefinitionDTO;
import br.ufpa.labes.spm.service.mapper.ToolDefinitionMapper;
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

/** Integration tests for the {@link ToolDefinitionResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ToolDefinitionResourceIT {

  private static final String DEFAULT_IDENT = "AAAAAAAAAA";
  private static final String UPDATED_IDENT = "BBBBBBBBBB";

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  @Autowired private ToolDefinitionRepository toolDefinitionRepository;

  @Mock private ToolDefinitionRepository toolDefinitionRepositoryMock;

  @Autowired private ToolDefinitionMapper toolDefinitionMapper;

  @Mock private ToolDefinitionService toolDefinitionServiceMock;

  @Autowired private ToolDefinitionService toolDefinitionService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restToolDefinitionMockMvc;

  private ToolDefinition toolDefinition;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ToolDefinitionResource toolDefinitionResource =
        new ToolDefinitionResource(toolDefinitionService);
    this.restToolDefinitionMockMvc =
        MockMvcBuilders.standaloneSetup(toolDefinitionResource)
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
  public static ToolDefinition createEntity(EntityManager em) {
    ToolDefinition toolDefinition =
        new ToolDefinition()
            .ident(DEFAULT_IDENT)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
    return toolDefinition;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ToolDefinition createUpdatedEntity(EntityManager em) {
    ToolDefinition toolDefinition =
        new ToolDefinition()
            .ident(UPDATED_IDENT)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
    return toolDefinition;
  }

  @BeforeEach
  public void initTest() {
    toolDefinition = createEntity(em);
  }

  @Test
  @Transactional
  public void createToolDefinition() throws Exception {
    int databaseSizeBeforeCreate = toolDefinitionRepository.findAll().size();

    // Create the ToolDefinition
    ToolDefinitionDTO toolDefinitionDTO = toolDefinitionMapper.toDto(toolDefinition);
    restToolDefinitionMockMvc
        .perform(
            post("/api/tool-definitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(toolDefinitionDTO)))
        .andExpect(status().isCreated());

    // Validate the ToolDefinition in the database
    List<ToolDefinition> toolDefinitionList = toolDefinitionRepository.findAll();
    assertThat(toolDefinitionList).hasSize(databaseSizeBeforeCreate + 1);
    ToolDefinition testToolDefinition = toolDefinitionList.get(toolDefinitionList.size() - 1);
    assertThat(testToolDefinition.getIdent()).isEqualTo(DEFAULT_IDENT);
    assertThat(testToolDefinition.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testToolDefinition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
  }

  @Test
  @Transactional
  public void createToolDefinitionWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = toolDefinitionRepository.findAll().size();

    // Create the ToolDefinition with an existing ID
    toolDefinition.setId(1L);
    ToolDefinitionDTO toolDefinitionDTO = toolDefinitionMapper.toDto(toolDefinition);

    // An entity with an existing ID cannot be created, so this API call must fail
    restToolDefinitionMockMvc
        .perform(
            post("/api/tool-definitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(toolDefinitionDTO)))
        .andExpect(status().isBadRequest());

    // Validate the ToolDefinition in the database
    List<ToolDefinition> toolDefinitionList = toolDefinitionRepository.findAll();
    assertThat(toolDefinitionList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllToolDefinitions() throws Exception {
    // Initialize the database
    toolDefinitionRepository.saveAndFlush(toolDefinition);

    // Get all the toolDefinitionList
    restToolDefinitionMockMvc
        .perform(get("/api/tool-definitions?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(toolDefinition.getId().intValue())))
        .andExpect(jsonPath("$.[*].ident").value(hasItem(DEFAULT_IDENT.toString())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
  }

  @SuppressWarnings({"unchecked"})
  public void getAllToolDefinitionsWithEagerRelationshipsIsEnabled() throws Exception {
    ToolDefinitionResource toolDefinitionResource =
        new ToolDefinitionResource(toolDefinitionServiceMock);
    when(toolDefinitionServiceMock.findAllWithEagerRelationships(any()))
        .thenReturn(new PageImpl(new ArrayList<>()));

    MockMvc restToolDefinitionMockMvc =
        MockMvcBuilders.standaloneSetup(toolDefinitionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .build();

    restToolDefinitionMockMvc
        .perform(get("/api/tool-definitions?eagerload=true"))
        .andExpect(status().isOk());

    verify(toolDefinitionServiceMock, times(1)).findAllWithEagerRelationships(any());
  }

  @SuppressWarnings({"unchecked"})
  public void getAllToolDefinitionsWithEagerRelationshipsIsNotEnabled() throws Exception {
    ToolDefinitionResource toolDefinitionResource =
        new ToolDefinitionResource(toolDefinitionServiceMock);
    when(toolDefinitionServiceMock.findAllWithEagerRelationships(any()))
        .thenReturn(new PageImpl(new ArrayList<>()));
    MockMvc restToolDefinitionMockMvc =
        MockMvcBuilders.standaloneSetup(toolDefinitionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .build();

    restToolDefinitionMockMvc
        .perform(get("/api/tool-definitions?eagerload=true"))
        .andExpect(status().isOk());

    verify(toolDefinitionServiceMock, times(1)).findAllWithEagerRelationships(any());
  }

  @Test
  @Transactional
  public void getToolDefinition() throws Exception {
    // Initialize the database
    toolDefinitionRepository.saveAndFlush(toolDefinition);

    // Get the toolDefinition
    restToolDefinitionMockMvc
        .perform(get("/api/tool-definitions/{id}", toolDefinition.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(toolDefinition.getId().intValue()))
        .andExpect(jsonPath("$.ident").value(DEFAULT_IDENT.toString()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingToolDefinition() throws Exception {
    // Get the toolDefinition
    restToolDefinitionMockMvc
        .perform(get("/api/tool-definitions/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateToolDefinition() throws Exception {
    // Initialize the database
    toolDefinitionRepository.saveAndFlush(toolDefinition);

    int databaseSizeBeforeUpdate = toolDefinitionRepository.findAll().size();

    // Update the toolDefinition
    ToolDefinition updatedToolDefinition =
        toolDefinitionRepository.findById(toolDefinition.getId()).get();
    // Disconnect from session so that the updates on updatedToolDefinition are not directly saved
    // in db
    em.detach(updatedToolDefinition);
    updatedToolDefinition.ident(UPDATED_IDENT).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    ToolDefinitionDTO toolDefinitionDTO = toolDefinitionMapper.toDto(updatedToolDefinition);

    restToolDefinitionMockMvc
        .perform(
            put("/api/tool-definitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(toolDefinitionDTO)))
        .andExpect(status().isOk());

    // Validate the ToolDefinition in the database
    List<ToolDefinition> toolDefinitionList = toolDefinitionRepository.findAll();
    assertThat(toolDefinitionList).hasSize(databaseSizeBeforeUpdate);
    ToolDefinition testToolDefinition = toolDefinitionList.get(toolDefinitionList.size() - 1);
    assertThat(testToolDefinition.getIdent()).isEqualTo(UPDATED_IDENT);
    assertThat(testToolDefinition.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testToolDefinition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  }

  @Test
  @Transactional
  public void updateNonExistingToolDefinition() throws Exception {
    int databaseSizeBeforeUpdate = toolDefinitionRepository.findAll().size();

    // Create the ToolDefinition
    ToolDefinitionDTO toolDefinitionDTO = toolDefinitionMapper.toDto(toolDefinition);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restToolDefinitionMockMvc
        .perform(
            put("/api/tool-definitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(toolDefinitionDTO)))
        .andExpect(status().isBadRequest());

    // Validate the ToolDefinition in the database
    List<ToolDefinition> toolDefinitionList = toolDefinitionRepository.findAll();
    assertThat(toolDefinitionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteToolDefinition() throws Exception {
    // Initialize the database
    toolDefinitionRepository.saveAndFlush(toolDefinition);

    int databaseSizeBeforeDelete = toolDefinitionRepository.findAll().size();

    // Delete the toolDefinition
    restToolDefinitionMockMvc
        .perform(
            delete("/api/tool-definitions/{id}", toolDefinition.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ToolDefinition> toolDefinitionList = toolDefinitionRepository.findAll();
    assertThat(toolDefinitionList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ToolDefinition.class);
    ToolDefinition toolDefinition1 = new ToolDefinition();
    toolDefinition1.setId(1L);
    ToolDefinition toolDefinition2 = new ToolDefinition();
    toolDefinition2.setId(toolDefinition1.getId());
    assertThat(toolDefinition1).isEqualTo(toolDefinition2);
    toolDefinition2.setId(2L);
    assertThat(toolDefinition1).isNotEqualTo(toolDefinition2);
    toolDefinition1.setId(null);
    assertThat(toolDefinition1).isNotEqualTo(toolDefinition2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(ToolDefinitionDTO.class);
    ToolDefinitionDTO toolDefinitionDTO1 = new ToolDefinitionDTO();
    toolDefinitionDTO1.setId(1L);
    ToolDefinitionDTO toolDefinitionDTO2 = new ToolDefinitionDTO();
    assertThat(toolDefinitionDTO1).isNotEqualTo(toolDefinitionDTO2);
    toolDefinitionDTO2.setId(toolDefinitionDTO1.getId());
    assertThat(toolDefinitionDTO1).isEqualTo(toolDefinitionDTO2);
    toolDefinitionDTO2.setId(2L);
    assertThat(toolDefinitionDTO1).isNotEqualTo(toolDefinitionDTO2);
    toolDefinitionDTO1.setId(null);
    assertThat(toolDefinitionDTO1).isNotEqualTo(toolDefinitionDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(toolDefinitionMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(toolDefinitionMapper.fromId(null)).isNull();
  }
}
