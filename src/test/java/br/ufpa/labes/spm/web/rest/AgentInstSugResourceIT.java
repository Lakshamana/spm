package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.AgentInstSug;
import br.ufpa.labes.spm.repository.AgentInstSugRepository;
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
 * Integration tests for the {@link AgentInstSugResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AgentInstSugResourceIT {

    @Autowired
    private AgentInstSugRepository agentInstSugRepository;

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

    private MockMvc restAgentInstSugMockMvc;

    private AgentInstSug agentInstSug;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AgentInstSugResource agentInstSugResource = new AgentInstSugResource(agentInstSugRepository);
        this.restAgentInstSugMockMvc = MockMvcBuilders.standaloneSetup(agentInstSugResource)
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
    public static AgentInstSug createEntity(EntityManager em) {
        AgentInstSug agentInstSug = new AgentInstSug();
        return agentInstSug;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgentInstSug createUpdatedEntity(EntityManager em) {
        AgentInstSug agentInstSug = new AgentInstSug();
        return agentInstSug;
    }

    @BeforeEach
    public void initTest() {
        agentInstSug = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgentInstSug() throws Exception {
        int databaseSizeBeforeCreate = agentInstSugRepository.findAll().size();

        // Create the AgentInstSug
        restAgentInstSugMockMvc.perform(post("/api/agent-inst-sugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentInstSug)))
            .andExpect(status().isCreated());

        // Validate the AgentInstSug in the database
        List<AgentInstSug> agentInstSugList = agentInstSugRepository.findAll();
        assertThat(agentInstSugList).hasSize(databaseSizeBeforeCreate + 1);
        AgentInstSug testAgentInstSug = agentInstSugList.get(agentInstSugList.size() - 1);
    }

    @Test
    @Transactional
    public void createAgentInstSugWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agentInstSugRepository.findAll().size();

        // Create the AgentInstSug with an existing ID
        agentInstSug.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgentInstSugMockMvc.perform(post("/api/agent-inst-sugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentInstSug)))
            .andExpect(status().isBadRequest());

        // Validate the AgentInstSug in the database
        List<AgentInstSug> agentInstSugList = agentInstSugRepository.findAll();
        assertThat(agentInstSugList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAgentInstSugs() throws Exception {
        // Initialize the database
        agentInstSugRepository.saveAndFlush(agentInstSug);

        // Get all the agentInstSugList
        restAgentInstSugMockMvc.perform(get("/api/agent-inst-sugs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentInstSug.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getAgentInstSug() throws Exception {
        // Initialize the database
        agentInstSugRepository.saveAndFlush(agentInstSug);

        // Get the agentInstSug
        restAgentInstSugMockMvc.perform(get("/api/agent-inst-sugs/{id}", agentInstSug.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agentInstSug.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAgentInstSug() throws Exception {
        // Get the agentInstSug
        restAgentInstSugMockMvc.perform(get("/api/agent-inst-sugs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgentInstSug() throws Exception {
        // Initialize the database
        agentInstSugRepository.saveAndFlush(agentInstSug);

        int databaseSizeBeforeUpdate = agentInstSugRepository.findAll().size();

        // Update the agentInstSug
        AgentInstSug updatedAgentInstSug = agentInstSugRepository.findById(agentInstSug.getId()).get();
        // Disconnect from session so that the updates on updatedAgentInstSug are not directly saved in db
        em.detach(updatedAgentInstSug);

        restAgentInstSugMockMvc.perform(put("/api/agent-inst-sugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAgentInstSug)))
            .andExpect(status().isOk());

        // Validate the AgentInstSug in the database
        List<AgentInstSug> agentInstSugList = agentInstSugRepository.findAll();
        assertThat(agentInstSugList).hasSize(databaseSizeBeforeUpdate);
        AgentInstSug testAgentInstSug = agentInstSugList.get(agentInstSugList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingAgentInstSug() throws Exception {
        int databaseSizeBeforeUpdate = agentInstSugRepository.findAll().size();

        // Create the AgentInstSug

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgentInstSugMockMvc.perform(put("/api/agent-inst-sugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentInstSug)))
            .andExpect(status().isBadRequest());

        // Validate the AgentInstSug in the database
        List<AgentInstSug> agentInstSugList = agentInstSugRepository.findAll();
        assertThat(agentInstSugList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAgentInstSug() throws Exception {
        // Initialize the database
        agentInstSugRepository.saveAndFlush(agentInstSug);

        int databaseSizeBeforeDelete = agentInstSugRepository.findAll().size();

        // Delete the agentInstSug
        restAgentInstSugMockMvc.perform(delete("/api/agent-inst-sugs/{id}", agentInstSug.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AgentInstSug> agentInstSugList = agentInstSugRepository.findAll();
        assertThat(agentInstSugList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentInstSug.class);
        AgentInstSug agentInstSug1 = new AgentInstSug();
        agentInstSug1.setId(1L);
        AgentInstSug agentInstSug2 = new AgentInstSug();
        agentInstSug2.setId(agentInstSug1.getId());
        assertThat(agentInstSug1).isEqualTo(agentInstSug2);
        agentInstSug2.setId(2L);
        assertThat(agentInstSug1).isNotEqualTo(agentInstSug2);
        agentInstSug1.setId(null);
        assertThat(agentInstSug1).isNotEqualTo(agentInstSug2);
    }
}
