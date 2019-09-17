package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.WorkGroupType;
import br.ufpa.labes.spm.repository.WorkGroupTypeRepository;
import br.ufpa.labes.spm.service.WorkGroupTypeService;
import br.ufpa.labes.spm.service.dto.WorkGroupTypeDTO;
import br.ufpa.labes.spm.service.mapper.WorkGroupTypeMapper;
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

/** Integration tests for the {@link WorkGroupTypeResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class WorkGroupTypeResourceIT {

  @Autowired private WorkGroupTypeRepository workGroupTypeRepository;

  @Autowired private WorkGroupTypeMapper workGroupTypeMapper;

  @Autowired private WorkGroupTypeService workGroupTypeService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restWorkGroupTypeMockMvc;

  private WorkGroupType workGroupType;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final WorkGroupTypeResource workGroupTypeResource =
        new WorkGroupTypeResource(workGroupTypeService);
    this.restWorkGroupTypeMockMvc =
        MockMvcBuilders.standaloneSetup(workGroupTypeResource)
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
  public static WorkGroupType createEntity(EntityManager em) {
    WorkGroupType workGroupType = new WorkGroupType();
    return workGroupType;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static WorkGroupType createUpdatedEntity(EntityManager em) {
    WorkGroupType workGroupType = new WorkGroupType();
    return workGroupType;
  }

  @BeforeEach
  public void initTest() {
    workGroupType = createEntity(em);
  }

  @Test
  @Transactional
  public void createWorkGroupType() throws Exception {
    int databaseSizeBeforeCreate = workGroupTypeRepository.findAll().size();

    // Create the WorkGroupType
    WorkGroupTypeDTO workGroupTypeDTO = workGroupTypeMapper.toDto(workGroupType);
    restWorkGroupTypeMockMvc
        .perform(
            post("/api/work-group-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workGroupTypeDTO)))
        .andExpect(status().isCreated());

    // Validate the WorkGroupType in the database
    List<WorkGroupType> workGroupTypeList = workGroupTypeRepository.findAll();
    assertThat(workGroupTypeList).hasSize(databaseSizeBeforeCreate + 1);
    WorkGroupType testWorkGroupType = workGroupTypeList.get(workGroupTypeList.size() - 1);
  }

  @Test
  @Transactional
  public void createWorkGroupTypeWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = workGroupTypeRepository.findAll().size();

    // Create the WorkGroupType with an existing ID
    workGroupType.setId(1L);
    WorkGroupTypeDTO workGroupTypeDTO = workGroupTypeMapper.toDto(workGroupType);

    // An entity with an existing ID cannot be created, so this API call must fail
    restWorkGroupTypeMockMvc
        .perform(
            post("/api/work-group-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workGroupTypeDTO)))
        .andExpect(status().isBadRequest());

    // Validate the WorkGroupType in the database
    List<WorkGroupType> workGroupTypeList = workGroupTypeRepository.findAll();
    assertThat(workGroupTypeList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllWorkGroupTypes() throws Exception {
    // Initialize the database
    workGroupTypeRepository.saveAndFlush(workGroupType);

    // Get all the workGroupTypeList
    restWorkGroupTypeMockMvc
        .perform(get("/api/work-group-types?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(workGroupType.getId().intValue())));
  }

  @Test
  @Transactional
  public void getWorkGroupType() throws Exception {
    // Initialize the database
    workGroupTypeRepository.saveAndFlush(workGroupType);

    // Get the workGroupType
    restWorkGroupTypeMockMvc
        .perform(get("/api/work-group-types/{id}", workGroupType.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(workGroupType.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingWorkGroupType() throws Exception {
    // Get the workGroupType
    restWorkGroupTypeMockMvc
        .perform(get("/api/work-group-types/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateWorkGroupType() throws Exception {
    // Initialize the database
    workGroupTypeRepository.saveAndFlush(workGroupType);

    int databaseSizeBeforeUpdate = workGroupTypeRepository.findAll().size();

    // Update the workGroupType
    WorkGroupType updatedWorkGroupType =
        workGroupTypeRepository.findById(workGroupType.getId()).get();
    // Disconnect from session so that the updates on updatedWorkGroupType are not directly saved in
    // db
    em.detach(updatedWorkGroupType);
    WorkGroupTypeDTO workGroupTypeDTO = workGroupTypeMapper.toDto(updatedWorkGroupType);

    restWorkGroupTypeMockMvc
        .perform(
            put("/api/work-group-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workGroupTypeDTO)))
        .andExpect(status().isOk());

    // Validate the WorkGroupType in the database
    List<WorkGroupType> workGroupTypeList = workGroupTypeRepository.findAll();
    assertThat(workGroupTypeList).hasSize(databaseSizeBeforeUpdate);
    WorkGroupType testWorkGroupType = workGroupTypeList.get(workGroupTypeList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingWorkGroupType() throws Exception {
    int databaseSizeBeforeUpdate = workGroupTypeRepository.findAll().size();

    // Create the WorkGroupType
    WorkGroupTypeDTO workGroupTypeDTO = workGroupTypeMapper.toDto(workGroupType);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restWorkGroupTypeMockMvc
        .perform(
            put("/api/work-group-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workGroupTypeDTO)))
        .andExpect(status().isBadRequest());

    // Validate the WorkGroupType in the database
    List<WorkGroupType> workGroupTypeList = workGroupTypeRepository.findAll();
    assertThat(workGroupTypeList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteWorkGroupType() throws Exception {
    // Initialize the database
    workGroupTypeRepository.saveAndFlush(workGroupType);

    int databaseSizeBeforeDelete = workGroupTypeRepository.findAll().size();

    // Delete the workGroupType
    restWorkGroupTypeMockMvc
        .perform(
            delete("/api/work-group-types/{id}", workGroupType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<WorkGroupType> workGroupTypeList = workGroupTypeRepository.findAll();
    assertThat(workGroupTypeList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(WorkGroupType.class);
    WorkGroupType workGroupType1 = new WorkGroupType();
    workGroupType1.setId(1L);
    WorkGroupType workGroupType2 = new WorkGroupType();
    workGroupType2.setId(workGroupType1.getId());
    assertThat(workGroupType1).isEqualTo(workGroupType2);
    workGroupType2.setId(2L);
    assertThat(workGroupType1).isNotEqualTo(workGroupType2);
    workGroupType1.setId(null);
    assertThat(workGroupType1).isNotEqualTo(workGroupType2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(WorkGroupTypeDTO.class);
    WorkGroupTypeDTO workGroupTypeDTO1 = new WorkGroupTypeDTO();
    workGroupTypeDTO1.setId(1L);
    WorkGroupTypeDTO workGroupTypeDTO2 = new WorkGroupTypeDTO();
    assertThat(workGroupTypeDTO1).isNotEqualTo(workGroupTypeDTO2);
    workGroupTypeDTO2.setId(workGroupTypeDTO1.getId());
    assertThat(workGroupTypeDTO1).isEqualTo(workGroupTypeDTO2);
    workGroupTypeDTO2.setId(2L);
    assertThat(workGroupTypeDTO1).isNotEqualTo(workGroupTypeDTO2);
    workGroupTypeDTO1.setId(null);
    assertThat(workGroupTypeDTO1).isNotEqualTo(workGroupTypeDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(workGroupTypeMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(workGroupTypeMapper.fromId(null)).isNull();
  }
}
