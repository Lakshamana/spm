package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.TaskAgenda;
import br.ufpa.labes.spm.repository.TaskAgendaRepository;
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

/**
 * Integration tests for the {@link TaskAgendaResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class TaskAgendaResourceIT {

    @Autowired
    private TaskAgendaRepository taskAgendaRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restTaskAgendaMockMvc;

    private TaskAgenda taskAgenda;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TaskAgendaResource taskAgendaResource = new TaskAgendaResource(taskAgendaRepository);
        this.restTaskAgendaMockMvc = MockMvcBuilders.standaloneSetup(taskAgendaResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskAgenda createEntity(EntityManager em) {
        TaskAgenda taskAgenda = new TaskAgenda();
        return taskAgenda;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TaskAgenda createUpdatedEntity(EntityManager em) {
        TaskAgenda taskAgenda = new TaskAgenda();
        return taskAgenda;
    }

    @BeforeEach
    public void initTest() {
        taskAgenda = createEntity(em);
    }

    @Test
    @Transactional
    public void createTaskAgenda() throws Exception {
        int databaseSizeBeforeCreate = taskAgendaRepository.findAll().size();

        // Create the TaskAgenda
        restTaskAgendaMockMvc.perform(post("/api/task-agenda")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskAgenda)))
            .andExpect(status().isCreated());

        // Validate the TaskAgenda in the database
        List<TaskAgenda> taskAgendaList = taskAgendaRepository.findAll();
        assertThat(taskAgendaList).hasSize(databaseSizeBeforeCreate + 1);
        TaskAgenda testTaskAgenda = taskAgendaList.get(taskAgendaList.size() - 1);
    }

    @Test
    @Transactional
    public void createTaskAgendaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskAgendaRepository.findAll().size();

        // Create the TaskAgenda with an existing ID
        taskAgenda.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskAgendaMockMvc.perform(post("/api/task-agenda")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskAgenda)))
            .andExpect(status().isBadRequest());

        // Validate the TaskAgenda in the database
        List<TaskAgenda> taskAgendaList = taskAgendaRepository.findAll();
        assertThat(taskAgendaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTaskAgenda() throws Exception {
        // Initialize the database
        taskAgendaRepository.saveAndFlush(taskAgenda);

        // Get all the taskAgendaList
        restTaskAgendaMockMvc.perform(get("/api/task-agenda?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(taskAgenda.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getTaskAgenda() throws Exception {
        // Initialize the database
        taskAgendaRepository.saveAndFlush(taskAgenda);

        // Get the taskAgenda
        restTaskAgendaMockMvc.perform(get("/api/task-agenda/{id}", taskAgenda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(taskAgenda.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTaskAgenda() throws Exception {
        // Get the taskAgenda
        restTaskAgendaMockMvc.perform(get("/api/task-agenda/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTaskAgenda() throws Exception {
        // Initialize the database
        taskAgendaRepository.saveAndFlush(taskAgenda);

        int databaseSizeBeforeUpdate = taskAgendaRepository.findAll().size();

        // Update the taskAgenda
        TaskAgenda updatedTaskAgenda = taskAgendaRepository.findById(taskAgenda.getId()).get();
        // Disconnect from session so that the updates on updatedTaskAgenda are not directly saved in db
        em.detach(updatedTaskAgenda);

        restTaskAgendaMockMvc.perform(put("/api/task-agenda")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTaskAgenda)))
            .andExpect(status().isOk());

        // Validate the TaskAgenda in the database
        List<TaskAgenda> taskAgendaList = taskAgendaRepository.findAll();
        assertThat(taskAgendaList).hasSize(databaseSizeBeforeUpdate);
        TaskAgenda testTaskAgenda = taskAgendaList.get(taskAgendaList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingTaskAgenda() throws Exception {
        int databaseSizeBeforeUpdate = taskAgendaRepository.findAll().size();

        // Create the TaskAgenda

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskAgendaMockMvc.perform(put("/api/task-agenda")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(taskAgenda)))
            .andExpect(status().isBadRequest());

        // Validate the TaskAgenda in the database
        List<TaskAgenda> taskAgendaList = taskAgendaRepository.findAll();
        assertThat(taskAgendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTaskAgenda() throws Exception {
        // Initialize the database
        taskAgendaRepository.saveAndFlush(taskAgenda);

        int databaseSizeBeforeDelete = taskAgendaRepository.findAll().size();

        // Delete the taskAgenda
        restTaskAgendaMockMvc.perform(delete("/api/task-agenda/{id}", taskAgenda.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TaskAgenda> taskAgendaList = taskAgendaRepository.findAll();
        assertThat(taskAgendaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaskAgenda.class);
        TaskAgenda taskAgenda1 = new TaskAgenda();
        taskAgenda1.setId(1L);
        TaskAgenda taskAgenda2 = new TaskAgenda();
        taskAgenda2.setId(taskAgenda1.getId());
        assertThat(taskAgenda1).isEqualTo(taskAgenda2);
        taskAgenda2.setId(2L);
        assertThat(taskAgenda1).isNotEqualTo(taskAgenda2);
        taskAgenda1.setId(null);
        assertThat(taskAgenda1).isNotEqualTo(taskAgenda2);
    }
}
