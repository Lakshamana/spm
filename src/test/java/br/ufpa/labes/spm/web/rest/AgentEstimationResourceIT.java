package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.AgentEstimation;
import br.ufpa.labes.spm.repository.AgentEstimationRepository;
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
 * Integration tests for the {@link AgentEstimationResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AgentEstimationResourceIT {

    @Autowired
    private AgentEstimationRepository agentEstimationRepository;

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

    private MockMvc restAgentEstimationMockMvc;

    private AgentEstimation agentEstimation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AgentEstimationResource agentEstimationResource = new AgentEstimationResource(agentEstimationRepository);
        this.restAgentEstimationMockMvc = MockMvcBuilders.standaloneSetup(agentEstimationResource)
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
    public static AgentEstimation createEntity(EntityManager em) {
        AgentEstimation agentEstimation = new AgentEstimation();
        return agentEstimation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgentEstimation createUpdatedEntity(EntityManager em) {
        AgentEstimation agentEstimation = new AgentEstimation();
        return agentEstimation;
    }

    @BeforeEach
    public void initTest() {
        agentEstimation = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgentEstimation() throws Exception {
        int databaseSizeBeforeCreate = agentEstimationRepository.findAll().size();

        // Create the AgentEstimation
        restAgentEstimationMockMvc.perform(post("/api/agent-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentEstimation)))
            .andExpect(status().isCreated());

        // Validate the AgentEstimation in the database
        List<AgentEstimation> agentEstimationList = agentEstimationRepository.findAll();
        assertThat(agentEstimationList).hasSize(databaseSizeBeforeCreate + 1);
        AgentEstimation testAgentEstimation = agentEstimationList.get(agentEstimationList.size() - 1);
    }

    @Test
    @Transactional
    public void createAgentEstimationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agentEstimationRepository.findAll().size();

        // Create the AgentEstimation with an existing ID
        agentEstimation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgentEstimationMockMvc.perform(post("/api/agent-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentEstimation)))
            .andExpect(status().isBadRequest());

        // Validate the AgentEstimation in the database
        List<AgentEstimation> agentEstimationList = agentEstimationRepository.findAll();
        assertThat(agentEstimationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAgentEstimations() throws Exception {
        // Initialize the database
        agentEstimationRepository.saveAndFlush(agentEstimation);

        // Get all the agentEstimationList
        restAgentEstimationMockMvc.perform(get("/api/agent-estimations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentEstimation.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getAgentEstimation() throws Exception {
        // Initialize the database
        agentEstimationRepository.saveAndFlush(agentEstimation);

        // Get the agentEstimation
        restAgentEstimationMockMvc.perform(get("/api/agent-estimations/{id}", agentEstimation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agentEstimation.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAgentEstimation() throws Exception {
        // Get the agentEstimation
        restAgentEstimationMockMvc.perform(get("/api/agent-estimations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgentEstimation() throws Exception {
        // Initialize the database
        agentEstimationRepository.saveAndFlush(agentEstimation);

        int databaseSizeBeforeUpdate = agentEstimationRepository.findAll().size();

        // Update the agentEstimation
        AgentEstimation updatedAgentEstimation = agentEstimationRepository.findById(agentEstimation.getId()).get();
        // Disconnect from session so that the updates on updatedAgentEstimation are not directly saved in db
        em.detach(updatedAgentEstimation);

        restAgentEstimationMockMvc.perform(put("/api/agent-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAgentEstimation)))
            .andExpect(status().isOk());

        // Validate the AgentEstimation in the database
        List<AgentEstimation> agentEstimationList = agentEstimationRepository.findAll();
        assertThat(agentEstimationList).hasSize(databaseSizeBeforeUpdate);
        AgentEstimation testAgentEstimation = agentEstimationList.get(agentEstimationList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingAgentEstimation() throws Exception {
        int databaseSizeBeforeUpdate = agentEstimationRepository.findAll().size();

        // Create the AgentEstimation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgentEstimationMockMvc.perform(put("/api/agent-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentEstimation)))
            .andExpect(status().isBadRequest());

        // Validate the AgentEstimation in the database
        List<AgentEstimation> agentEstimationList = agentEstimationRepository.findAll();
        assertThat(agentEstimationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAgentEstimation() throws Exception {
        // Initialize the database
        agentEstimationRepository.saveAndFlush(agentEstimation);

        int databaseSizeBeforeDelete = agentEstimationRepository.findAll().size();

        // Delete the agentEstimation
        restAgentEstimationMockMvc.perform(delete("/api/agent-estimations/{id}", agentEstimation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AgentEstimation> agentEstimationList = agentEstimationRepository.findAll();
        assertThat(agentEstimationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentEstimation.class);
        AgentEstimation agentEstimation1 = new AgentEstimation();
        agentEstimation1.setId(1L);
        AgentEstimation agentEstimation2 = new AgentEstimation();
        agentEstimation2.setId(agentEstimation1.getId());
        assertThat(agentEstimation1).isEqualTo(agentEstimation2);
        agentEstimation2.setId(2L);
        assertThat(agentEstimation1).isNotEqualTo(agentEstimation2);
        agentEstimation1.setId(null);
        assertThat(agentEstimation1).isNotEqualTo(agentEstimation2);
    }
}
