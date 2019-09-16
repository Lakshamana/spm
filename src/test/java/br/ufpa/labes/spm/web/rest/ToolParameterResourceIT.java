package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ToolParameter;
import br.ufpa.labes.spm.repository.ToolParameterRepository;
import br.ufpa.labes.spm.service.ToolParameterService;
import br.ufpa.labes.spm.service.dto.ToolParameterDTO;
import br.ufpa.labes.spm.service.mapper.ToolParameterMapper;
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

/** Integration tests for the {@link ToolParameterResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ToolParameterResourceIT {

  private static final String DEFAULT_LABEL = "AAAAAAAAAA";
  private static final String UPDATED_LABEL = "BBBBBBBBBB";

  private static final String DEFAULT_SEPARATOR_SYMBOL = "AAAAAAAAAA";
  private static final String UPDATED_SEPARATOR_SYMBOL = "BBBBBBBBBB";

  @Autowired private ToolParameterRepository toolParameterRepository;

  @Autowired private ToolParameterMapper toolParameterMapper;

  @Autowired private ToolParameterService toolParameterService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restToolParameterMockMvc;

  private ToolParameter toolParameter;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ToolParameterResource toolParameterResource =
        new ToolParameterResource(toolParameterService);
    this.restToolParameterMockMvc =
        MockMvcBuilders.standaloneSetup(toolParameterResource)
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
  public static ToolParameter createEntity(EntityManager em) {
    ToolParameter toolParameter =
        new ToolParameter().label(DEFAULT_LABEL).separatorSymbol(DEFAULT_SEPARATOR_SYMBOL);
    return toolParameter;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ToolParameter createUpdatedEntity(EntityManager em) {
    ToolParameter toolParameter =
        new ToolParameter().label(UPDATED_LABEL).separatorSymbol(UPDATED_SEPARATOR_SYMBOL);
    return toolParameter;
  }

  @BeforeEach
  public void initTest() {
    toolParameter = createEntity(em);
  }

  @Test
  @Transactional
  public void createToolParameter() throws Exception {
    int databaseSizeBeforeCreate = toolParameterRepository.findAll().size();

    // Create the ToolParameter
    ToolParameterDTO toolParameterDTO = toolParameterMapper.toDto(toolParameter);
    restToolParameterMockMvc
        .perform(
            post("/api/tool-parameters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(toolParameterDTO)))
        .andExpect(status().isCreated());

    // Validate the ToolParameter in the database
    List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
    assertThat(toolParameterList).hasSize(databaseSizeBeforeCreate + 1);
    ToolParameter testToolParameter = toolParameterList.get(toolParameterList.size() - 1);
    assertThat(testToolParameter.getLabel()).isEqualTo(DEFAULT_LABEL);
    assertThat(testToolParameter.getSeparatorSymbol()).isEqualTo(DEFAULT_SEPARATOR_SYMBOL);
  }

  @Test
  @Transactional
  public void createToolParameterWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = toolParameterRepository.findAll().size();

    // Create the ToolParameter with an existing ID
    toolParameter.setId(1L);
    ToolParameterDTO toolParameterDTO = toolParameterMapper.toDto(toolParameter);

    // An entity with an existing ID cannot be created, so this API call must fail
    restToolParameterMockMvc
        .perform(
            post("/api/tool-parameters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(toolParameterDTO)))
        .andExpect(status().isBadRequest());

    // Validate the ToolParameter in the database
    List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
    assertThat(toolParameterList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllToolParameters() throws Exception {
    // Initialize the database
    toolParameterRepository.saveAndFlush(toolParameter);

    // Get all the toolParameterList
    restToolParameterMockMvc
        .perform(get("/api/tool-parameters?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(toolParameter.getId().intValue())))
        .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
        .andExpect(
            jsonPath("$.[*].separatorSymbol").value(hasItem(DEFAULT_SEPARATOR_SYMBOL.toString())));
  }

  @Test
  @Transactional
  public void getToolParameter() throws Exception {
    // Initialize the database
    toolParameterRepository.saveAndFlush(toolParameter);

    // Get the toolParameter
    restToolParameterMockMvc
        .perform(get("/api/tool-parameters/{id}", toolParameter.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(toolParameter.getId().intValue()))
        .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
        .andExpect(jsonPath("$.separatorSymbol").value(DEFAULT_SEPARATOR_SYMBOL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingToolParameter() throws Exception {
    // Get the toolParameter
    restToolParameterMockMvc
        .perform(get("/api/tool-parameters/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateToolParameter() throws Exception {
    // Initialize the database
    toolParameterRepository.saveAndFlush(toolParameter);

    int databaseSizeBeforeUpdate = toolParameterRepository.findAll().size();

    // Update the toolParameter
    ToolParameter updatedToolParameter =
        toolParameterRepository.findById(toolParameter.getId()).get();
    // Disconnect from session so that the updates on updatedToolParameter are not directly saved in
    // db
    em.detach(updatedToolParameter);
    updatedToolParameter.label(UPDATED_LABEL).separatorSymbol(UPDATED_SEPARATOR_SYMBOL);
    ToolParameterDTO toolParameterDTO = toolParameterMapper.toDto(updatedToolParameter);

    restToolParameterMockMvc
        .perform(
            put("/api/tool-parameters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(toolParameterDTO)))
        .andExpect(status().isOk());

    // Validate the ToolParameter in the database
    List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
    assertThat(toolParameterList).hasSize(databaseSizeBeforeUpdate);
    ToolParameter testToolParameter = toolParameterList.get(toolParameterList.size() - 1);
    assertThat(testToolParameter.getLabel()).isEqualTo(UPDATED_LABEL);
    assertThat(testToolParameter.getSeparatorSymbol()).isEqualTo(UPDATED_SEPARATOR_SYMBOL);
  }

  @Test
  @Transactional
  public void updateNonExistingToolParameter() throws Exception {
    int databaseSizeBeforeUpdate = toolParameterRepository.findAll().size();

    // Create the ToolParameter
    ToolParameterDTO toolParameterDTO = toolParameterMapper.toDto(toolParameter);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restToolParameterMockMvc
        .perform(
            put("/api/tool-parameters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(toolParameterDTO)))
        .andExpect(status().isBadRequest());

    // Validate the ToolParameter in the database
    List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
    assertThat(toolParameterList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteToolParameter() throws Exception {
    // Initialize the database
    toolParameterRepository.saveAndFlush(toolParameter);

    int databaseSizeBeforeDelete = toolParameterRepository.findAll().size();

    // Delete the toolParameter
    restToolParameterMockMvc
        .perform(
            delete("/api/tool-parameters/{id}", toolParameter.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ToolParameter> toolParameterList = toolParameterRepository.findAll();
    assertThat(toolParameterList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ToolParameter.class);
    ToolParameter toolParameter1 = new ToolParameter();
    toolParameter1.setId(1L);
    ToolParameter toolParameter2 = new ToolParameter();
    toolParameter2.setId(toolParameter1.getId());
    assertThat(toolParameter1).isEqualTo(toolParameter2);
    toolParameter2.setId(2L);
    assertThat(toolParameter1).isNotEqualTo(toolParameter2);
    toolParameter1.setId(null);
    assertThat(toolParameter1).isNotEqualTo(toolParameter2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(ToolParameterDTO.class);
    ToolParameterDTO toolParameterDTO1 = new ToolParameterDTO();
    toolParameterDTO1.setId(1L);
    ToolParameterDTO toolParameterDTO2 = new ToolParameterDTO();
    assertThat(toolParameterDTO1).isNotEqualTo(toolParameterDTO2);
    toolParameterDTO2.setId(toolParameterDTO1.getId());
    assertThat(toolParameterDTO1).isEqualTo(toolParameterDTO2);
    toolParameterDTO2.setId(2L);
    assertThat(toolParameterDTO1).isNotEqualTo(toolParameterDTO2);
    toolParameterDTO1.setId(null);
    assertThat(toolParameterDTO1).isNotEqualTo(toolParameterDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(toolParameterMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(toolParameterMapper.fromId(null)).isNull();
  }
}
