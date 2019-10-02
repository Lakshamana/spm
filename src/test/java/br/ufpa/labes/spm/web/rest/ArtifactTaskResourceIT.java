package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ArtifactTask;
import br.ufpa.labes.spm.repository.ArtifactTaskRepository;
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
 * Integration tests for the {@link ArtifactTaskResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ArtifactTaskResourceIT {

    private static final String DEFAULT_IN_WORKSPACE_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_IN_WORKSPACE_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_OUT_WORKSPACE_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_OUT_WORKSPACE_VERSION = "BBBBBBBBBB";

    @Autowired
    private ArtifactTaskRepository artifactTaskRepository;

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

    private MockMvc restArtifactTaskMockMvc;

    private ArtifactTask artifactTask;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ArtifactTaskResource artifactTaskResource = new ArtifactTaskResource(artifactTaskRepository);
        this.restArtifactTaskMockMvc = MockMvcBuilders.standaloneSetup(artifactTaskResource)
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
    public static ArtifactTask createEntity(EntityManager em) {
        ArtifactTask artifactTask = new ArtifactTask()
            .inWorkspaceVersion(DEFAULT_IN_WORKSPACE_VERSION)
            .outWorkspaceVersion(DEFAULT_OUT_WORKSPACE_VERSION);
        return artifactTask;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArtifactTask createUpdatedEntity(EntityManager em) {
        ArtifactTask artifactTask = new ArtifactTask()
            .inWorkspaceVersion(UPDATED_IN_WORKSPACE_VERSION)
            .outWorkspaceVersion(UPDATED_OUT_WORKSPACE_VERSION);
        return artifactTask;
    }

    @BeforeEach
    public void initTest() {
        artifactTask = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtifactTask() throws Exception {
        int databaseSizeBeforeCreate = artifactTaskRepository.findAll().size();

        // Create the ArtifactTask
        restArtifactTaskMockMvc.perform(post("/api/artifact-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artifactTask)))
            .andExpect(status().isCreated());

        // Validate the ArtifactTask in the database
        List<ArtifactTask> artifactTaskList = artifactTaskRepository.findAll();
        assertThat(artifactTaskList).hasSize(databaseSizeBeforeCreate + 1);
        ArtifactTask testArtifactTask = artifactTaskList.get(artifactTaskList.size() - 1);
        assertThat(testArtifactTask.getInWorkspaceVersion()).isEqualTo(DEFAULT_IN_WORKSPACE_VERSION);
        assertThat(testArtifactTask.getOutWorkspaceVersion()).isEqualTo(DEFAULT_OUT_WORKSPACE_VERSION);
    }

    @Test
    @Transactional
    public void createArtifactTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = artifactTaskRepository.findAll().size();

        // Create the ArtifactTask with an existing ID
        artifactTask.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtifactTaskMockMvc.perform(post("/api/artifact-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artifactTask)))
            .andExpect(status().isBadRequest());

        // Validate the ArtifactTask in the database
        List<ArtifactTask> artifactTaskList = artifactTaskRepository.findAll();
        assertThat(artifactTaskList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllArtifactTasks() throws Exception {
        // Initialize the database
        artifactTaskRepository.saveAndFlush(artifactTask);

        // Get all the artifactTaskList
        restArtifactTaskMockMvc.perform(get("/api/artifact-tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artifactTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].inWorkspaceVersion").value(hasItem(DEFAULT_IN_WORKSPACE_VERSION.toString())))
            .andExpect(jsonPath("$.[*].outWorkspaceVersion").value(hasItem(DEFAULT_OUT_WORKSPACE_VERSION.toString())));
    }
    
    @Test
    @Transactional
    public void getArtifactTask() throws Exception {
        // Initialize the database
        artifactTaskRepository.saveAndFlush(artifactTask);

        // Get the artifactTask
        restArtifactTaskMockMvc.perform(get("/api/artifact-tasks/{id}", artifactTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(artifactTask.getId().intValue()))
            .andExpect(jsonPath("$.inWorkspaceVersion").value(DEFAULT_IN_WORKSPACE_VERSION.toString()))
            .andExpect(jsonPath("$.outWorkspaceVersion").value(DEFAULT_OUT_WORKSPACE_VERSION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingArtifactTask() throws Exception {
        // Get the artifactTask
        restArtifactTaskMockMvc.perform(get("/api/artifact-tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtifactTask() throws Exception {
        // Initialize the database
        artifactTaskRepository.saveAndFlush(artifactTask);

        int databaseSizeBeforeUpdate = artifactTaskRepository.findAll().size();

        // Update the artifactTask
        ArtifactTask updatedArtifactTask = artifactTaskRepository.findById(artifactTask.getId()).get();
        // Disconnect from session so that the updates on updatedArtifactTask are not directly saved in db
        em.detach(updatedArtifactTask);
        updatedArtifactTask
            .inWorkspaceVersion(UPDATED_IN_WORKSPACE_VERSION)
            .outWorkspaceVersion(UPDATED_OUT_WORKSPACE_VERSION);

        restArtifactTaskMockMvc.perform(put("/api/artifact-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedArtifactTask)))
            .andExpect(status().isOk());

        // Validate the ArtifactTask in the database
        List<ArtifactTask> artifactTaskList = artifactTaskRepository.findAll();
        assertThat(artifactTaskList).hasSize(databaseSizeBeforeUpdate);
        ArtifactTask testArtifactTask = artifactTaskList.get(artifactTaskList.size() - 1);
        assertThat(testArtifactTask.getInWorkspaceVersion()).isEqualTo(UPDATED_IN_WORKSPACE_VERSION);
        assertThat(testArtifactTask.getOutWorkspaceVersion()).isEqualTo(UPDATED_OUT_WORKSPACE_VERSION);
    }

    @Test
    @Transactional
    public void updateNonExistingArtifactTask() throws Exception {
        int databaseSizeBeforeUpdate = artifactTaskRepository.findAll().size();

        // Create the ArtifactTask

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtifactTaskMockMvc.perform(put("/api/artifact-tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artifactTask)))
            .andExpect(status().isBadRequest());

        // Validate the ArtifactTask in the database
        List<ArtifactTask> artifactTaskList = artifactTaskRepository.findAll();
        assertThat(artifactTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArtifactTask() throws Exception {
        // Initialize the database
        artifactTaskRepository.saveAndFlush(artifactTask);

        int databaseSizeBeforeDelete = artifactTaskRepository.findAll().size();

        // Delete the artifactTask
        restArtifactTaskMockMvc.perform(delete("/api/artifact-tasks/{id}", artifactTask.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArtifactTask> artifactTaskList = artifactTaskRepository.findAll();
        assertThat(artifactTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArtifactTask.class);
        ArtifactTask artifactTask1 = new ArtifactTask();
        artifactTask1.setId(1L);
        ArtifactTask artifactTask2 = new ArtifactTask();
        artifactTask2.setId(artifactTask1.getId());
        assertThat(artifactTask1).isEqualTo(artifactTask2);
        artifactTask2.setId(2L);
        assertThat(artifactTask1).isNotEqualTo(artifactTask2);
        artifactTask1.setId(null);
        assertThat(artifactTask1).isNotEqualTo(artifactTask2);
    }
}
