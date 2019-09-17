package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.AgentMetric;
import br.ufpa.labes.spm.repository.AgentMetricRepository;
import br.ufpa.labes.spm.service.AgentMetricService;
import br.ufpa.labes.spm.service.dto.AgentMetricDTO;
import br.ufpa.labes.spm.service.mapper.AgentMetricMapper;
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
 * Integration tests for the {@link AgentMetricResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AgentMetricResourceIT {

    @Autowired
    private AgentMetricRepository agentMetricRepository;

    @Autowired
    private AgentMetricMapper agentMetricMapper;

    @Autowired
    private AgentMetricService agentMetricService;

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

    private MockMvc restAgentMetricMockMvc;

    private AgentMetric agentMetric;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AgentMetricResource agentMetricResource = new AgentMetricResource(agentMetricService);
        this.restAgentMetricMockMvc = MockMvcBuilders.standaloneSetup(agentMetricResource)
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
    public static AgentMetric createEntity(EntityManager em) {
        AgentMetric agentMetric = new AgentMetric();
        return agentMetric;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AgentMetric createUpdatedEntity(EntityManager em) {
        AgentMetric agentMetric = new AgentMetric();
        return agentMetric;
    }

    @BeforeEach
    public void initTest() {
        agentMetric = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgentMetric() throws Exception {
        int databaseSizeBeforeCreate = agentMetricRepository.findAll().size();

        // Create the AgentMetric
        AgentMetricDTO agentMetricDTO = agentMetricMapper.toDto(agentMetric);
        restAgentMetricMockMvc.perform(post("/api/agent-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentMetricDTO)))
            .andExpect(status().isCreated());

        // Validate the AgentMetric in the database
        List<AgentMetric> agentMetricList = agentMetricRepository.findAll();
        assertThat(agentMetricList).hasSize(databaseSizeBeforeCreate + 1);
        AgentMetric testAgentMetric = agentMetricList.get(agentMetricList.size() - 1);
    }

    @Test
    @Transactional
    public void createAgentMetricWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agentMetricRepository.findAll().size();

        // Create the AgentMetric with an existing ID
        agentMetric.setId(1L);
        AgentMetricDTO agentMetricDTO = agentMetricMapper.toDto(agentMetric);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgentMetricMockMvc.perform(post("/api/agent-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentMetricDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AgentMetric in the database
        List<AgentMetric> agentMetricList = agentMetricRepository.findAll();
        assertThat(agentMetricList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAgentMetrics() throws Exception {
        // Initialize the database
        agentMetricRepository.saveAndFlush(agentMetric);

        // Get all the agentMetricList
        restAgentMetricMockMvc.perform(get("/api/agent-metrics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agentMetric.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getAgentMetric() throws Exception {
        // Initialize the database
        agentMetricRepository.saveAndFlush(agentMetric);

        // Get the agentMetric
        restAgentMetricMockMvc.perform(get("/api/agent-metrics/{id}", agentMetric.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agentMetric.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAgentMetric() throws Exception {
        // Get the agentMetric
        restAgentMetricMockMvc.perform(get("/api/agent-metrics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgentMetric() throws Exception {
        // Initialize the database
        agentMetricRepository.saveAndFlush(agentMetric);

        int databaseSizeBeforeUpdate = agentMetricRepository.findAll().size();

        // Update the agentMetric
        AgentMetric updatedAgentMetric = agentMetricRepository.findById(agentMetric.getId()).get();
        // Disconnect from session so that the updates on updatedAgentMetric are not directly saved in db
        em.detach(updatedAgentMetric);
        AgentMetricDTO agentMetricDTO = agentMetricMapper.toDto(updatedAgentMetric);

        restAgentMetricMockMvc.perform(put("/api/agent-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentMetricDTO)))
            .andExpect(status().isOk());

        // Validate the AgentMetric in the database
        List<AgentMetric> agentMetricList = agentMetricRepository.findAll();
        assertThat(agentMetricList).hasSize(databaseSizeBeforeUpdate);
        AgentMetric testAgentMetric = agentMetricList.get(agentMetricList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingAgentMetric() throws Exception {
        int databaseSizeBeforeUpdate = agentMetricRepository.findAll().size();

        // Create the AgentMetric
        AgentMetricDTO agentMetricDTO = agentMetricMapper.toDto(agentMetric);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgentMetricMockMvc.perform(put("/api/agent-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agentMetricDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AgentMetric in the database
        List<AgentMetric> agentMetricList = agentMetricRepository.findAll();
        assertThat(agentMetricList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAgentMetric() throws Exception {
        // Initialize the database
        agentMetricRepository.saveAndFlush(agentMetric);

        int databaseSizeBeforeDelete = agentMetricRepository.findAll().size();

        // Delete the agentMetric
        restAgentMetricMockMvc.perform(delete("/api/agent-metrics/{id}", agentMetric.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AgentMetric> agentMetricList = agentMetricRepository.findAll();
        assertThat(agentMetricList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentMetric.class);
        AgentMetric agentMetric1 = new AgentMetric();
        agentMetric1.setId(1L);
        AgentMetric agentMetric2 = new AgentMetric();
        agentMetric2.setId(agentMetric1.getId());
        assertThat(agentMetric1).isEqualTo(agentMetric2);
        agentMetric2.setId(2L);
        assertThat(agentMetric1).isNotEqualTo(agentMetric2);
        agentMetric1.setId(null);
        assertThat(agentMetric1).isNotEqualTo(agentMetric2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AgentMetricDTO.class);
        AgentMetricDTO agentMetricDTO1 = new AgentMetricDTO();
        agentMetricDTO1.setId(1L);
        AgentMetricDTO agentMetricDTO2 = new AgentMetricDTO();
        assertThat(agentMetricDTO1).isNotEqualTo(agentMetricDTO2);
        agentMetricDTO2.setId(agentMetricDTO1.getId());
        assertThat(agentMetricDTO1).isEqualTo(agentMetricDTO2);
        agentMetricDTO2.setId(2L);
        assertThat(agentMetricDTO1).isNotEqualTo(agentMetricDTO2);
        agentMetricDTO1.setId(null);
        assertThat(agentMetricDTO1).isNotEqualTo(agentMetricDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(agentMetricMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(agentMetricMapper.fromId(null)).isNull();
    }
}
