package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Task;
import br.ufpa.labes.spm.repository.TaskRepository;
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

/** Integration tests for the {@link TaskResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class TaskResourceIT {

  private static final String DEFAULT_LOCAL_STATE = "AAAAAAAAAA";
  private static final String UPDATED_LOCAL_STATE = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_BEGIN_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_BEGIN_DATE = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate SMALLER_BEGIN_DATE = LocalDate.ofEpochDay(-1L);

  private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

  private static final Float DEFAULT_WORKING_HOURS = 1F;
  private static final Float UPDATED_WORKING_HOURS = 2F;
  private static final Float SMALLER_WORKING_HOURS = 1F - 1F;

  private static final LocalDate DEFAULT_DATE_DELEGATED_TO = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_DATE_DELEGATED_TO = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate SMALLER_DATE_DELEGATED_TO = LocalDate.ofEpochDay(-1L);

  private static final LocalDate DEFAULT_DATE_DELEGATED_FROM = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_DATE_DELEGATED_FROM =
      LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate SMALLER_DATE_DELEGATED_FROM = LocalDate.ofEpochDay(-1L);

  @Autowired private TaskRepository taskRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restTaskMockMvc;

  private Task task;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final TaskResource taskResource = new TaskResource(taskRepository);
    this.restTaskMockMvc =
        MockMvcBuilders.standaloneSetup(taskResource)
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
  public static Task createEntity(EntityManager em) {
    Task task =
        new Task()
            .localState(DEFAULT_LOCAL_STATE)
            .beginDate(DEFAULT_BEGIN_DATE)
            .endDate(DEFAULT_END_DATE)
            .workingHours(DEFAULT_WORKING_HOURS)
            .dateDelegatedTo(DEFAULT_DATE_DELEGATED_TO)
            .dateDelegatedFrom(DEFAULT_DATE_DELEGATED_FROM);
    return task;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Task createUpdatedEntity(EntityManager em) {
    Task task =
        new Task()
            .localState(UPDATED_LOCAL_STATE)
            .beginDate(UPDATED_BEGIN_DATE)
            .endDate(UPDATED_END_DATE)
            .workingHours(UPDATED_WORKING_HOURS)
            .dateDelegatedTo(UPDATED_DATE_DELEGATED_TO)
            .dateDelegatedFrom(UPDATED_DATE_DELEGATED_FROM);
    return task;
  }

  @BeforeEach
  public void initTest() {
    task = createEntity(em);
  }

  @Test
  @Transactional
  public void createTask() throws Exception {
    int databaseSizeBeforeCreate = taskRepository.findAll().size();

    // Create the Task
    restTaskMockMvc
        .perform(
            post("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(task)))
        .andExpect(status().isCreated());

    // Validate the Task in the database
    List<Task> taskList = taskRepository.findAll();
    assertThat(taskList).hasSize(databaseSizeBeforeCreate + 1);
    Task testTask = taskList.get(taskList.size() - 1);
    assertThat(testTask.getLocalState()).isEqualTo(DEFAULT_LOCAL_STATE);
    assertThat(testTask.getBeginDate()).isEqualTo(DEFAULT_BEGIN_DATE);
    assertThat(testTask.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    assertThat(testTask.getWorkingHours()).isEqualTo(DEFAULT_WORKING_HOURS);
    assertThat(testTask.getDateDelegatedTo()).isEqualTo(DEFAULT_DATE_DELEGATED_TO);
    assertThat(testTask.getDateDelegatedFrom()).isEqualTo(DEFAULT_DATE_DELEGATED_FROM);
  }

  @Test
  @Transactional
  public void createTaskWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = taskRepository.findAll().size();

    // Create the Task with an existing ID
    task.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restTaskMockMvc
        .perform(
            post("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(task)))
        .andExpect(status().isBadRequest());

    // Validate the Task in the database
    List<Task> taskList = taskRepository.findAll();
    assertThat(taskList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllTasks() throws Exception {
    // Initialize the database
    taskRepository.saveAndFlush(task);

    // Get all the taskList
    restTaskMockMvc
        .perform(get("/api/tasks?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
        .andExpect(jsonPath("$.[*].localState").value(hasItem(DEFAULT_LOCAL_STATE.toString())))
        .andExpect(jsonPath("$.[*].beginDate").value(hasItem(DEFAULT_BEGIN_DATE.toString())))
        .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
        .andExpect(
            jsonPath("$.[*].workingHours").value(hasItem(DEFAULT_WORKING_HOURS.doubleValue())))
        .andExpect(
            jsonPath("$.[*].dateDelegatedTo").value(hasItem(DEFAULT_DATE_DELEGATED_TO.toString())))
        .andExpect(
            jsonPath("$.[*].dateDelegatedFrom")
                .value(hasItem(DEFAULT_DATE_DELEGATED_FROM.toString())));
  }

  @Test
  @Transactional
  public void getTask() throws Exception {
    // Initialize the database
    taskRepository.saveAndFlush(task);

    // Get the task
    restTaskMockMvc
        .perform(get("/api/tasks/{id}", task.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(task.getId().intValue()))
        .andExpect(jsonPath("$.localState").value(DEFAULT_LOCAL_STATE.toString()))
        .andExpect(jsonPath("$.beginDate").value(DEFAULT_BEGIN_DATE.toString()))
        .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
        .andExpect(jsonPath("$.workingHours").value(DEFAULT_WORKING_HOURS.doubleValue()))
        .andExpect(jsonPath("$.dateDelegatedTo").value(DEFAULT_DATE_DELEGATED_TO.toString()))
        .andExpect(jsonPath("$.dateDelegatedFrom").value(DEFAULT_DATE_DELEGATED_FROM.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingTask() throws Exception {
    // Get the task
    restTaskMockMvc
        .perform(get("/api/tasks/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateTask() throws Exception {
    // Initialize the database
    taskRepository.saveAndFlush(task);

    int databaseSizeBeforeUpdate = taskRepository.findAll().size();

    // Update the task
    Task updatedTask = taskRepository.findById(task.getId()).get();
    // Disconnect from session so that the updates on updatedTask are not directly saved in db
    em.detach(updatedTask);
    updatedTask
        .localState(UPDATED_LOCAL_STATE)
        .beginDate(UPDATED_BEGIN_DATE)
        .endDate(UPDATED_END_DATE)
        .workingHours(UPDATED_WORKING_HOURS)
        .dateDelegatedTo(UPDATED_DATE_DELEGATED_TO)
        .dateDelegatedFrom(UPDATED_DATE_DELEGATED_FROM);

    restTaskMockMvc
        .perform(
            put("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTask)))
        .andExpect(status().isOk());

    // Validate the Task in the database
    List<Task> taskList = taskRepository.findAll();
    assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    Task testTask = taskList.get(taskList.size() - 1);
    assertThat(testTask.getLocalState()).isEqualTo(UPDATED_LOCAL_STATE);
    assertThat(testTask.getBeginDate()).isEqualTo(UPDATED_BEGIN_DATE);
    assertThat(testTask.getEndDate()).isEqualTo(UPDATED_END_DATE);
    assertThat(testTask.getWorkingHours()).isEqualTo(UPDATED_WORKING_HOURS);
    assertThat(testTask.getDateDelegatedTo()).isEqualTo(UPDATED_DATE_DELEGATED_TO);
    assertThat(testTask.getDateDelegatedFrom()).isEqualTo(UPDATED_DATE_DELEGATED_FROM);
  }

  @Test
  @Transactional
  public void updateNonExistingTask() throws Exception {
    int databaseSizeBeforeUpdate = taskRepository.findAll().size();

    // Create the Task

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restTaskMockMvc
        .perform(
            put("/api/tasks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(task)))
        .andExpect(status().isBadRequest());

    // Validate the Task in the database
    List<Task> taskList = taskRepository.findAll();
    assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteTask() throws Exception {
    // Initialize the database
    taskRepository.saveAndFlush(task);

    int databaseSizeBeforeDelete = taskRepository.findAll().size();

    // Delete the task
    restTaskMockMvc
        .perform(delete("/api/tasks/{id}", task.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Task> taskList = taskRepository.findAll();
    assertThat(taskList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Task.class);
    Task task1 = new Task();
    task1.setId(1L);
    Task task2 = new Task();
    task2.setId(task1.getId());
    assertThat(task1).isEqualTo(task2);
    task2.setId(2L);
    assertThat(task1).isNotEqualTo(task2);
    task1.setId(null);
    assertThat(task1).isNotEqualTo(task2);
  }
}
