package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Parameter;
import br.ufpa.labes.spm.repository.ParameterRepository;
import br.ufpa.labes.spm.service.ParameterService;
import br.ufpa.labes.spm.service.dto.ParameterDTO;
import br.ufpa.labes.spm.service.mapper.ParameterMapper;
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

/** Integration tests for the {@link ParameterResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ParameterResourceIT {

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  @Autowired private ParameterRepository parameterRepository;

  @Autowired private ParameterMapper parameterMapper;

  @Autowired private ParameterService parameterService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restParameterMockMvc;

  private Parameter parameter;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ParameterResource parameterResource = new ParameterResource(parameterService);
    this.restParameterMockMvc =
        MockMvcBuilders.standaloneSetup(parameterResource)
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
  public static Parameter createEntity(EntityManager em) {
    Parameter parameter = new Parameter().description(DEFAULT_DESCRIPTION);
    return parameter;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Parameter createUpdatedEntity(EntityManager em) {
    Parameter parameter = new Parameter().description(UPDATED_DESCRIPTION);
    return parameter;
  }

  @BeforeEach
  public void initTest() {
    parameter = createEntity(em);
  }

  @Test
  @Transactional
  public void createParameter() throws Exception {
    int databaseSizeBeforeCreate = parameterRepository.findAll().size();

    // Create the Parameter
    ParameterDTO parameterDTO = parameterMapper.toDto(parameter);
    restParameterMockMvc
        .perform(
            post("/api/parameters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parameterDTO)))
        .andExpect(status().isCreated());

    // Validate the Parameter in the database
    List<Parameter> parameterList = parameterRepository.findAll();
    assertThat(parameterList).hasSize(databaseSizeBeforeCreate + 1);
    Parameter testParameter = parameterList.get(parameterList.size() - 1);
    assertThat(testParameter.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
  }

  @Test
  @Transactional
  public void createParameterWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = parameterRepository.findAll().size();

    // Create the Parameter with an existing ID
    parameter.setId(1L);
    ParameterDTO parameterDTO = parameterMapper.toDto(parameter);

    // An entity with an existing ID cannot be created, so this API call must fail
    restParameterMockMvc
        .perform(
            post("/api/parameters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parameterDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Parameter in the database
    List<Parameter> parameterList = parameterRepository.findAll();
    assertThat(parameterList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllParameters() throws Exception {
    // Initialize the database
    parameterRepository.saveAndFlush(parameter);

    // Get all the parameterList
    restParameterMockMvc
        .perform(get("/api/parameters?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(parameter.getId().intValue())))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
  }

  @Test
  @Transactional
  public void getParameter() throws Exception {
    // Initialize the database
    parameterRepository.saveAndFlush(parameter);

    // Get the parameter
    restParameterMockMvc
        .perform(get("/api/parameters/{id}", parameter.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(parameter.getId().intValue()))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingParameter() throws Exception {
    // Get the parameter
    restParameterMockMvc
        .perform(get("/api/parameters/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateParameter() throws Exception {
    // Initialize the database
    parameterRepository.saveAndFlush(parameter);

    int databaseSizeBeforeUpdate = parameterRepository.findAll().size();

    // Update the parameter
    Parameter updatedParameter = parameterRepository.findById(parameter.getId()).get();
    // Disconnect from session so that the updates on updatedParameter are not directly saved in db
    em.detach(updatedParameter);
    updatedParameter.description(UPDATED_DESCRIPTION);
    ParameterDTO parameterDTO = parameterMapper.toDto(updatedParameter);

    restParameterMockMvc
        .perform(
            put("/api/parameters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parameterDTO)))
        .andExpect(status().isOk());

    // Validate the Parameter in the database
    List<Parameter> parameterList = parameterRepository.findAll();
    assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
    Parameter testParameter = parameterList.get(parameterList.size() - 1);
    assertThat(testParameter.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  }

  @Test
  @Transactional
  public void updateNonExistingParameter() throws Exception {
    int databaseSizeBeforeUpdate = parameterRepository.findAll().size();

    // Create the Parameter
    ParameterDTO parameterDTO = parameterMapper.toDto(parameter);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restParameterMockMvc
        .perform(
            put("/api/parameters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(parameterDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Parameter in the database
    List<Parameter> parameterList = parameterRepository.findAll();
    assertThat(parameterList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteParameter() throws Exception {
    // Initialize the database
    parameterRepository.saveAndFlush(parameter);

    int databaseSizeBeforeDelete = parameterRepository.findAll().size();

    // Delete the parameter
    restParameterMockMvc
        .perform(
            delete("/api/parameters/{id}", parameter.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Parameter> parameterList = parameterRepository.findAll();
    assertThat(parameterList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Parameter.class);
    Parameter parameter1 = new Parameter();
    parameter1.setId(1L);
    Parameter parameter2 = new Parameter();
    parameter2.setId(parameter1.getId());
    assertThat(parameter1).isEqualTo(parameter2);
    parameter2.setId(2L);
    assertThat(parameter1).isNotEqualTo(parameter2);
    parameter1.setId(null);
    assertThat(parameter1).isNotEqualTo(parameter2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(ParameterDTO.class);
    ParameterDTO parameterDTO1 = new ParameterDTO();
    parameterDTO1.setId(1L);
    ParameterDTO parameterDTO2 = new ParameterDTO();
    assertThat(parameterDTO1).isNotEqualTo(parameterDTO2);
    parameterDTO2.setId(parameterDTO1.getId());
    assertThat(parameterDTO1).isEqualTo(parameterDTO2);
    parameterDTO2.setId(2L);
    assertThat(parameterDTO1).isNotEqualTo(parameterDTO2);
    parameterDTO1.setId(null);
    assertThat(parameterDTO1).isNotEqualTo(parameterDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(parameterMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(parameterMapper.fromId(null)).isNull();
  }
}
