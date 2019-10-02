package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.AgentInstSuggestionToAgent;
import br.ufpa.labes.spm.repository.AgentInstSuggestionToAgentRepository;
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
 * Integration tests for the {@link AgentInstSuggestionToAgentResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AgentInstSuggestionToAgentResourceIT {

    private static final Float DEFAULT_ORDER_CRITERIA_RESULT = 1F;
    private static final Float UPDATED_ORDER_CRITERIA_RESULT = 2F;
    private static final Float SMALLER_ORDER_CRITERIA_RESULT = 1F - 1F;

    @Autowired
    private AgentInstSuggestionToAgentRepository agentInstSuggestionToAgentRepository;

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

    private MockMvc restAgentInstSuggestionToAgentMockMvc;

    private AgentInstSuggestionToAgent agentInstSuggestionToAgent;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AgentInstSuggestionToAgentResource agentInstSuggestionToAgentResource = new AgentInstSuggestionToAgentResource(agentInstSuggestionToAgentRepository);
        this.restAgentInstSuggestionToAgentMockMvc = MockMvcBuilders.standaloneSetup(agentInstSuggestionToAgentResource)
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
    public static AgentInstSuggestionToAgent createEntity(EntityManager em) {
        AgentInstSuggestionToAgent agentInstSuggestionToAgent = new AgentInstSuggestionToAgent()
            .orderCriteriaResult(DEFAULT_ORDER_CRITERIA_RESULT);
        return agentInstSuggestionToAgent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgentInstSuggestionToAgent createUpdatedEntity(EntityManager em) {
        AgentInstSuggestionToAgent agentInstSuggestionToAgent = new AgentInstSuggestionToAgent()
            .orderCriteriaResult(UPDATED_ORDER_CRITERIA_RESULT);
        return agentInstSuggestionToAgent;
    }

    @BeforeEach
    public void initTest() {
        agentInstSuggestionToAgent = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgentInstSuggestionToAgent() throws Exception {
        int databaseSizeBeforeCreate = agentInstSuggestionToAgentRepository.findAll().size();

        // Create the AgentInstSuggestionToAgent
        restAgentInstSuggestionToAgentMockMvc.perform(post("/api/agent-inst-suggestion-to-agents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentInstSuggestionToAgent)))
            .andExpect(status().isCreated());

        // Validate the AgentInstSuggestionToAgent in the database
        List<AgentInstSuggestionToAgent> agentInstSuggestionToAgentList = agentInstSuggestionToAgentRepository.findAll();
        assertThat(agentInstSuggestionToAgentList).hasSize(databaseSizeBeforeCreate + 1);
        AgentInstSuggestionToAgent testAgentInstSuggestionToAgent = agentInstSuggestionToAgentList.get(agentInstSuggestionToAgentList.size() - 1);
        assertThat(testAgentInstSuggestionToAgent.getOrderCriteriaResult()).isEqualTo(DEFAULT_ORDER_CRITERIA_RESULT);
    }

    @Test
    @Transactional
    public void createAgentInstSuggestionToAgentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agentInstSuggestionToAgentRepository.findAll().size();

        // Create the AgentInstSuggestionToAgent with an existing ID
        agentInstSuggestionToAgent.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgentInstSuggestionToAgentMockMvc.perform(post("/api/agent-inst-suggestion-to-agents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentInstSuggestionToAgent)))
            .andExpect(status().isBadRequest());

        // Validate the AgentInstSuggestionToAgent in the database
        List<AgentInstSuggestionToAgent> agentInstSuggestionToAgentList = agentInstSuggestionToAgentRepository.findAll();
        assertThat(agentInstSuggestionToAgentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAgentInstSuggestionToAgents() throws Exception {
        // Initialize the database
        agentInstSuggestionToAgentRepository.saveAndFlush(agentInstSuggestionToAgent);

        // Get all the agentInstSuggestionToAgentList
        restAgentInstSuggestionToAgentMockMvc.perform(get("/api/agent-inst-suggestion-to-agents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentInstSuggestionToAgent.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderCriteriaResult").value(hasItem(DEFAULT_ORDER_CRITERIA_RESULT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getAgentInstSuggestionToAgent() throws Exception {
        // Initialize the database
        agentInstSuggestionToAgentRepository.saveAndFlush(agentInstSuggestionToAgent);

        // Get the agentInstSuggestionToAgent
        restAgentInstSuggestionToAgentMockMvc.perform(get("/api/agent-inst-suggestion-to-agents/{id}", agentInstSuggestionToAgent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agentInstSuggestionToAgent.getId().intValue()))
            .andExpect(jsonPath("$.orderCriteriaResult").value(DEFAULT_ORDER_CRITERIA_RESULT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAgentInstSuggestionToAgent() throws Exception {
        // Get the agentInstSuggestionToAgent
        restAgentInstSuggestionToAgentMockMvc.perform(get("/api/agent-inst-suggestion-to-agents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgentInstSuggestionToAgent() throws Exception {
        // Initialize the database
        agentInstSuggestionToAgentRepository.saveAndFlush(agentInstSuggestionToAgent);

        int databaseSizeBeforeUpdate = agentInstSuggestionToAgentRepository.findAll().size();

        // Update the agentInstSuggestionToAgent
        AgentInstSuggestionToAgent updatedAgentInstSuggestionToAgent = agentInstSuggestionToAgentRepository.findById(agentInstSuggestionToAgent.getId()).get();
        // Disconnect from session so that the updates on updatedAgentInstSuggestionToAgent are not directly saved in db
        em.detach(updatedAgentInstSuggestionToAgent);
        updatedAgentInstSuggestionToAgent
            .orderCriteriaResult(UPDATED_ORDER_CRITERIA_RESULT);

        restAgentInstSuggestionToAgentMockMvc.perform(put("/api/agent-inst-suggestion-to-agents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAgentInstSuggestionToAgent)))
            .andExpect(status().isOk());

        // Validate the AgentInstSuggestionToAgent in the database
        List<AgentInstSuggestionToAgent> agentInstSuggestionToAgentList = agentInstSuggestionToAgentRepository.findAll();
        assertThat(agentInstSuggestionToAgentList).hasSize(databaseSizeBeforeUpdate);
        AgentInstSuggestionToAgent testAgentInstSuggestionToAgent = agentInstSuggestionToAgentList.get(agentInstSuggestionToAgentList.size() - 1);
        assertThat(testAgentInstSuggestionToAgent.getOrderCriteriaResult()).isEqualTo(UPDATED_ORDER_CRITERIA_RESULT);
    }

    @Test
    @Transactional
    public void updateNonExistingAgentInstSuggestionToAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentInstSuggestionToAgentRepository.findAll().size();

        // Create the AgentInstSuggestionToAgent

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgentInstSuggestionToAgentMockMvc.perform(put("/api/agent-inst-suggestion-to-agents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentInstSuggestionToAgent)))
            .andExpect(status().isBadRequest());

        // Validate the AgentInstSuggestionToAgent in the database
        List<AgentInstSuggestionToAgent> agentInstSuggestionToAgentList = agentInstSuggestionToAgentRepository.findAll();
        assertThat(agentInstSuggestionToAgentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAgentInstSuggestionToAgent() throws Exception {
        // Initialize the database
        agentInstSuggestionToAgentRepository.saveAndFlush(agentInstSuggestionToAgent);

        int databaseSizeBeforeDelete = agentInstSuggestionToAgentRepository.findAll().size();

        // Delete the agentInstSuggestionToAgent
        restAgentInstSuggestionToAgentMockMvc.perform(delete("/api/agent-inst-suggestion-to-agents/{id}", agentInstSuggestionToAgent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AgentInstSuggestionToAgent> agentInstSuggestionToAgentList = agentInstSuggestionToAgentRepository.findAll();
        assertThat(agentInstSuggestionToAgentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentInstSuggestionToAgent.class);
        AgentInstSuggestionToAgent agentInstSuggestionToAgent1 = new AgentInstSuggestionToAgent();
        agentInstSuggestionToAgent1.setId(1L);
        AgentInstSuggestionToAgent agentInstSuggestionToAgent2 = new AgentInstSuggestionToAgent();
        agentInstSuggestionToAgent2.setId(agentInstSuggestionToAgent1.getId());
        assertThat(agentInstSuggestionToAgent1).isEqualTo(agentInstSuggestionToAgent2);
        agentInstSuggestionToAgent2.setId(2L);
        assertThat(agentInstSuggestionToAgent1).isNotEqualTo(agentInstSuggestionToAgent2);
        agentInstSuggestionToAgent1.setId(null);
        assertThat(agentInstSuggestionToAgent1).isNotEqualTo(agentInstSuggestionToAgent2);
    }
}
