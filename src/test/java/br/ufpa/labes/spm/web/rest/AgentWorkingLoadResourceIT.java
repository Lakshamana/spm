package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.AgentWorkingLoad;
import br.ufpa.labes.spm.repository.AgentWorkingLoadRepository;
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

/**
 * Integration tests for the {@link AgentWorkingLoadResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AgentWorkingLoadResourceIT {

    private static final LocalDate DEFAULT_BEGIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BEGIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_BEGIN = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END = LocalDate.ofEpochDay(-1L);

    @Autowired
    private AgentWorkingLoadRepository agentWorkingLoadRepository;

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

    private MockMvc restAgentWorkingLoadMockMvc;

    private AgentWorkingLoad agentWorkingLoad;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AgentWorkingLoadResource agentWorkingLoadResource = new AgentWorkingLoadResource(agentWorkingLoadRepository);
        this.restAgentWorkingLoadMockMvc = MockMvcBuilders.standaloneSetup(agentWorkingLoadResource)
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
    public static AgentWorkingLoad createEntity(EntityManager em) {
        AgentWorkingLoad agentWorkingLoad = new AgentWorkingLoad()
            .begin(DEFAULT_BEGIN)
            .end(DEFAULT_END);
        return agentWorkingLoad;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgentWorkingLoad createUpdatedEntity(EntityManager em) {
        AgentWorkingLoad agentWorkingLoad = new AgentWorkingLoad()
            .begin(UPDATED_BEGIN)
            .end(UPDATED_END);
        return agentWorkingLoad;
    }

    @BeforeEach
    public void initTest() {
        agentWorkingLoad = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgentWorkingLoad() throws Exception {
        int databaseSizeBeforeCreate = agentWorkingLoadRepository.findAll().size();

        // Create the AgentWorkingLoad
        restAgentWorkingLoadMockMvc.perform(post("/api/agent-working-loads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentWorkingLoad)))
            .andExpect(status().isCreated());

        // Validate the AgentWorkingLoad in the database
        List<AgentWorkingLoad> agentWorkingLoadList = agentWorkingLoadRepository.findAll();
        assertThat(agentWorkingLoadList).hasSize(databaseSizeBeforeCreate + 1);
        AgentWorkingLoad testAgentWorkingLoad = agentWorkingLoadList.get(agentWorkingLoadList.size() - 1);
        assertThat(testAgentWorkingLoad.getBegin()).isEqualTo(DEFAULT_BEGIN);
        assertThat(testAgentWorkingLoad.getEnd()).isEqualTo(DEFAULT_END);
    }

    @Test
    @Transactional
    public void createAgentWorkingLoadWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agentWorkingLoadRepository.findAll().size();

        // Create the AgentWorkingLoad with an existing ID
        agentWorkingLoad.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgentWorkingLoadMockMvc.perform(post("/api/agent-working-loads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentWorkingLoad)))
            .andExpect(status().isBadRequest());

        // Validate the AgentWorkingLoad in the database
        List<AgentWorkingLoad> agentWorkingLoadList = agentWorkingLoadRepository.findAll();
        assertThat(agentWorkingLoadList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAgentWorkingLoads() throws Exception {
        // Initialize the database
        agentWorkingLoadRepository.saveAndFlush(agentWorkingLoad);

        // Get all the agentWorkingLoadList
        restAgentWorkingLoadMockMvc.perform(get("/api/agent-working-loads?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentWorkingLoad.getId().intValue())))
            .andExpect(jsonPath("$.[*].begin").value(hasItem(DEFAULT_BEGIN.toString())))
            .andExpect(jsonPath("$.[*].end").value(hasItem(DEFAULT_END.toString())));
    }
    
    @Test
    @Transactional
    public void getAgentWorkingLoad() throws Exception {
        // Initialize the database
        agentWorkingLoadRepository.saveAndFlush(agentWorkingLoad);

        // Get the agentWorkingLoad
        restAgentWorkingLoadMockMvc.perform(get("/api/agent-working-loads/{id}", agentWorkingLoad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agentWorkingLoad.getId().intValue()))
            .andExpect(jsonPath("$.begin").value(DEFAULT_BEGIN.toString()))
            .andExpect(jsonPath("$.end").value(DEFAULT_END.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAgentWorkingLoad() throws Exception {
        // Get the agentWorkingLoad
        restAgentWorkingLoadMockMvc.perform(get("/api/agent-working-loads/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgentWorkingLoad() throws Exception {
        // Initialize the database
        agentWorkingLoadRepository.saveAndFlush(agentWorkingLoad);

        int databaseSizeBeforeUpdate = agentWorkingLoadRepository.findAll().size();

        // Update the agentWorkingLoad
        AgentWorkingLoad updatedAgentWorkingLoad = agentWorkingLoadRepository.findById(agentWorkingLoad.getId()).get();
        // Disconnect from session so that the updates on updatedAgentWorkingLoad are not directly saved in db
        em.detach(updatedAgentWorkingLoad);
        updatedAgentWorkingLoad
            .begin(UPDATED_BEGIN)
            .end(UPDATED_END);

        restAgentWorkingLoadMockMvc.perform(put("/api/agent-working-loads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAgentWorkingLoad)))
            .andExpect(status().isOk());

        // Validate the AgentWorkingLoad in the database
        List<AgentWorkingLoad> agentWorkingLoadList = agentWorkingLoadRepository.findAll();
        assertThat(agentWorkingLoadList).hasSize(databaseSizeBeforeUpdate);
        AgentWorkingLoad testAgentWorkingLoad = agentWorkingLoadList.get(agentWorkingLoadList.size() - 1);
        assertThat(testAgentWorkingLoad.getBegin()).isEqualTo(UPDATED_BEGIN);
        assertThat(testAgentWorkingLoad.getEnd()).isEqualTo(UPDATED_END);
    }

    @Test
    @Transactional
    public void updateNonExistingAgentWorkingLoad() throws Exception {
        int databaseSizeBeforeUpdate = agentWorkingLoadRepository.findAll().size();

        // Create the AgentWorkingLoad

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgentWorkingLoadMockMvc.perform(put("/api/agent-working-loads")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentWorkingLoad)))
            .andExpect(status().isBadRequest());

        // Validate the AgentWorkingLoad in the database
        List<AgentWorkingLoad> agentWorkingLoadList = agentWorkingLoadRepository.findAll();
        assertThat(agentWorkingLoadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAgentWorkingLoad() throws Exception {
        // Initialize the database
        agentWorkingLoadRepository.saveAndFlush(agentWorkingLoad);

        int databaseSizeBeforeDelete = agentWorkingLoadRepository.findAll().size();

        // Delete the agentWorkingLoad
        restAgentWorkingLoadMockMvc.perform(delete("/api/agent-working-loads/{id}", agentWorkingLoad.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AgentWorkingLoad> agentWorkingLoadList = agentWorkingLoadRepository.findAll();
        assertThat(agentWorkingLoadList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentWorkingLoad.class);
        AgentWorkingLoad agentWorkingLoad1 = new AgentWorkingLoad();
        agentWorkingLoad1.setId(1L);
        AgentWorkingLoad agentWorkingLoad2 = new AgentWorkingLoad();
        agentWorkingLoad2.setId(agentWorkingLoad1.getId());
        assertThat(agentWorkingLoad1).isEqualTo(agentWorkingLoad2);
        agentWorkingLoad2.setId(2L);
        assertThat(agentWorkingLoad1).isNotEqualTo(agentWorkingLoad2);
        agentWorkingLoad1.setId(null);
        assertThat(agentWorkingLoad1).isNotEqualTo(agentWorkingLoad2);
    }
}
