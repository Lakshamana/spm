package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.WorkGroupInstSug;
import br.ufpa.labes.spm.repository.WorkGroupInstSugRepository;
import br.ufpa.labes.spm.service.WorkGroupInstSugService;
import br.ufpa.labes.spm.service.dto.WorkGroupInstSugDTO;
import br.ufpa.labes.spm.service.mapper.WorkGroupInstSugMapper;
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

/** Integration tests for the {@link WorkGroupInstSugResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class WorkGroupInstSugResourceIT {

  @Autowired private WorkGroupInstSugRepository workGroupInstSugRepository;

  @Mock private WorkGroupInstSugRepository workGroupInstSugRepositoryMock;

  @Autowired private WorkGroupInstSugMapper workGroupInstSugMapper;

  @Mock private WorkGroupInstSugService workGroupInstSugServiceMock;

  @Autowired private WorkGroupInstSugService workGroupInstSugService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restWorkGroupInstSugMockMvc;

  private WorkGroupInstSug workGroupInstSug;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final WorkGroupInstSugResource workGroupInstSugResource =
        new WorkGroupInstSugResource(workGroupInstSugService);
    this.restWorkGroupInstSugMockMvc =
        MockMvcBuilders.standaloneSetup(workGroupInstSugResource)
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
  public static WorkGroupInstSug createEntity(EntityManager em) {
    WorkGroupInstSug workGroupInstSug = new WorkGroupInstSug();
    return workGroupInstSug;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static WorkGroupInstSug createUpdatedEntity(EntityManager em) {
    WorkGroupInstSug workGroupInstSug = new WorkGroupInstSug();
    return workGroupInstSug;
  }

  @BeforeEach
  public void initTest() {
    workGroupInstSug = createEntity(em);
  }

  @Test
  @Transactional
  public void createWorkGroupInstSug() throws Exception {
    int databaseSizeBeforeCreate = workGroupInstSugRepository.findAll().size();

    // Create the WorkGroupInstSug
    WorkGroupInstSugDTO workGroupInstSugDTO = workGroupInstSugMapper.toDto(workGroupInstSug);
    restWorkGroupInstSugMockMvc
        .perform(
            post("/api/work-group-inst-sugs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workGroupInstSugDTO)))
        .andExpect(status().isCreated());

    // Validate the WorkGroupInstSug in the database
    List<WorkGroupInstSug> workGroupInstSugList = workGroupInstSugRepository.findAll();
    assertThat(workGroupInstSugList).hasSize(databaseSizeBeforeCreate + 1);
    WorkGroupInstSug testWorkGroupInstSug =
        workGroupInstSugList.get(workGroupInstSugList.size() - 1);
  }

  @Test
  @Transactional
  public void createWorkGroupInstSugWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = workGroupInstSugRepository.findAll().size();

    // Create the WorkGroupInstSug with an existing ID
    workGroupInstSug.setId(1L);
    WorkGroupInstSugDTO workGroupInstSugDTO = workGroupInstSugMapper.toDto(workGroupInstSug);

    // An entity with an existing ID cannot be created, so this API call must fail
    restWorkGroupInstSugMockMvc
        .perform(
            post("/api/work-group-inst-sugs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workGroupInstSugDTO)))
        .andExpect(status().isBadRequest());

    // Validate the WorkGroupInstSug in the database
    List<WorkGroupInstSug> workGroupInstSugList = workGroupInstSugRepository.findAll();
    assertThat(workGroupInstSugList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllWorkGroupInstSugs() throws Exception {
    // Initialize the database
    workGroupInstSugRepository.saveAndFlush(workGroupInstSug);

    // Get all the workGroupInstSugList
    restWorkGroupInstSugMockMvc
        .perform(get("/api/work-group-inst-sugs?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(workGroupInstSug.getId().intValue())));
  }

  @SuppressWarnings({"unchecked"})
  public void getAllWorkGroupInstSugsWithEagerRelationshipsIsEnabled() throws Exception {
    WorkGroupInstSugResource workGroupInstSugResource =
        new WorkGroupInstSugResource(workGroupInstSugServiceMock);
    when(workGroupInstSugServiceMock.findAllWithEagerRelationships(any()))
        .thenReturn(new PageImpl(new ArrayList<>()));

    MockMvc restWorkGroupInstSugMockMvc =
        MockMvcBuilders.standaloneSetup(workGroupInstSugResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .build();

    restWorkGroupInstSugMockMvc
        .perform(get("/api/work-group-inst-sugs?eagerload=true"))
        .andExpect(status().isOk());

    verify(workGroupInstSugServiceMock, times(1)).findAllWithEagerRelationships(any());
  }

  @SuppressWarnings({"unchecked"})
  public void getAllWorkGroupInstSugsWithEagerRelationshipsIsNotEnabled() throws Exception {
    WorkGroupInstSugResource workGroupInstSugResource =
        new WorkGroupInstSugResource(workGroupInstSugServiceMock);
    when(workGroupInstSugServiceMock.findAllWithEagerRelationships(any()))
        .thenReturn(new PageImpl(new ArrayList<>()));
    MockMvc restWorkGroupInstSugMockMvc =
        MockMvcBuilders.standaloneSetup(workGroupInstSugResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .build();

    restWorkGroupInstSugMockMvc
        .perform(get("/api/work-group-inst-sugs?eagerload=true"))
        .andExpect(status().isOk());

    verify(workGroupInstSugServiceMock, times(1)).findAllWithEagerRelationships(any());
  }

  @Test
  @Transactional
  public void getWorkGroupInstSug() throws Exception {
    // Initialize the database
    workGroupInstSugRepository.saveAndFlush(workGroupInstSug);

    // Get the workGroupInstSug
    restWorkGroupInstSugMockMvc
        .perform(get("/api/work-group-inst-sugs/{id}", workGroupInstSug.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(workGroupInstSug.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingWorkGroupInstSug() throws Exception {
    // Get the workGroupInstSug
    restWorkGroupInstSugMockMvc
        .perform(get("/api/work-group-inst-sugs/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateWorkGroupInstSug() throws Exception {
    // Initialize the database
    workGroupInstSugRepository.saveAndFlush(workGroupInstSug);

    int databaseSizeBeforeUpdate = workGroupInstSugRepository.findAll().size();

    // Update the workGroupInstSug
    WorkGroupInstSug updatedWorkGroupInstSug =
        workGroupInstSugRepository.findById(workGroupInstSug.getId()).get();
    // Disconnect from session so that the updates on updatedWorkGroupInstSug are not directly saved
    // in db
    em.detach(updatedWorkGroupInstSug);
    WorkGroupInstSugDTO workGroupInstSugDTO = workGroupInstSugMapper.toDto(updatedWorkGroupInstSug);

    restWorkGroupInstSugMockMvc
        .perform(
            put("/api/work-group-inst-sugs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workGroupInstSugDTO)))
        .andExpect(status().isOk());

    // Validate the WorkGroupInstSug in the database
    List<WorkGroupInstSug> workGroupInstSugList = workGroupInstSugRepository.findAll();
    assertThat(workGroupInstSugList).hasSize(databaseSizeBeforeUpdate);
    WorkGroupInstSug testWorkGroupInstSug =
        workGroupInstSugList.get(workGroupInstSugList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingWorkGroupInstSug() throws Exception {
    int databaseSizeBeforeUpdate = workGroupInstSugRepository.findAll().size();

    // Create the WorkGroupInstSug
    WorkGroupInstSugDTO workGroupInstSugDTO = workGroupInstSugMapper.toDto(workGroupInstSug);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restWorkGroupInstSugMockMvc
        .perform(
            put("/api/work-group-inst-sugs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(workGroupInstSugDTO)))
        .andExpect(status().isBadRequest());

    // Validate the WorkGroupInstSug in the database
    List<WorkGroupInstSug> workGroupInstSugList = workGroupInstSugRepository.findAll();
    assertThat(workGroupInstSugList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteWorkGroupInstSug() throws Exception {
    // Initialize the database
    workGroupInstSugRepository.saveAndFlush(workGroupInstSug);

    int databaseSizeBeforeDelete = workGroupInstSugRepository.findAll().size();

    // Delete the workGroupInstSug
    restWorkGroupInstSugMockMvc
        .perform(
            delete("/api/work-group-inst-sugs/{id}", workGroupInstSug.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<WorkGroupInstSug> workGroupInstSugList = workGroupInstSugRepository.findAll();
    assertThat(workGroupInstSugList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(WorkGroupInstSug.class);
    WorkGroupInstSug workGroupInstSug1 = new WorkGroupInstSug();
    workGroupInstSug1.setId(1L);
    WorkGroupInstSug workGroupInstSug2 = new WorkGroupInstSug();
    workGroupInstSug2.setId(workGroupInstSug1.getId());
    assertThat(workGroupInstSug1).isEqualTo(workGroupInstSug2);
    workGroupInstSug2.setId(2L);
    assertThat(workGroupInstSug1).isNotEqualTo(workGroupInstSug2);
    workGroupInstSug1.setId(null);
    assertThat(workGroupInstSug1).isNotEqualTo(workGroupInstSug2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(WorkGroupInstSugDTO.class);
    WorkGroupInstSugDTO workGroupInstSugDTO1 = new WorkGroupInstSugDTO();
    workGroupInstSugDTO1.setId(1L);
    WorkGroupInstSugDTO workGroupInstSugDTO2 = new WorkGroupInstSugDTO();
    assertThat(workGroupInstSugDTO1).isNotEqualTo(workGroupInstSugDTO2);
    workGroupInstSugDTO2.setId(workGroupInstSugDTO1.getId());
    assertThat(workGroupInstSugDTO1).isEqualTo(workGroupInstSugDTO2);
    workGroupInstSugDTO2.setId(2L);
    assertThat(workGroupInstSugDTO1).isNotEqualTo(workGroupInstSugDTO2);
    workGroupInstSugDTO1.setId(null);
    assertThat(workGroupInstSugDTO1).isNotEqualTo(workGroupInstSugDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(workGroupInstSugMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(workGroupInstSugMapper.fromId(null)).isNull();
  }
}
