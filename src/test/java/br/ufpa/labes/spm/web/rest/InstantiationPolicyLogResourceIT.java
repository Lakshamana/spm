package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.InstantiationPolicyLog;
import br.ufpa.labes.spm.repository.InstantiationPolicyLogRepository;
import br.ufpa.labes.spm.service.InstantiationPolicyLogService;
import br.ufpa.labes.spm.service.dto.InstantiationPolicyLogDTO;
import br.ufpa.labes.spm.service.mapper.InstantiationPolicyLogMapper;
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
 * Integration tests for the {@link InstantiationPolicyLogResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class InstantiationPolicyLogResourceIT {

    @Autowired
    private InstantiationPolicyLogRepository instantiationPolicyLogRepository;

    @Autowired
    private InstantiationPolicyLogMapper instantiationPolicyLogMapper;

    @Autowired
    private InstantiationPolicyLogService instantiationPolicyLogService;

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

    private MockMvc restInstantiationPolicyLogMockMvc;

    private InstantiationPolicyLog instantiationPolicyLog;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InstantiationPolicyLogResource instantiationPolicyLogResource = new InstantiationPolicyLogResource(instantiationPolicyLogService);
        this.restInstantiationPolicyLogMockMvc = MockMvcBuilders.standaloneSetup(instantiationPolicyLogResource)
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
    public static InstantiationPolicyLog createEntity(EntityManager em) {
        InstantiationPolicyLog instantiationPolicyLog = new InstantiationPolicyLog();
        return instantiationPolicyLog;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InstantiationPolicyLog createUpdatedEntity(EntityManager em) {
        InstantiationPolicyLog instantiationPolicyLog = new InstantiationPolicyLog();
        return instantiationPolicyLog;
    }

    @BeforeEach
    public void initTest() {
        instantiationPolicyLog = createEntity(em);
    }

    @Test
    @Transactional
    public void createInstantiationPolicyLog() throws Exception {
        int databaseSizeBeforeCreate = instantiationPolicyLogRepository.findAll().size();

        // Create the InstantiationPolicyLog
        InstantiationPolicyLogDTO instantiationPolicyLogDTO = instantiationPolicyLogMapper.toDto(instantiationPolicyLog);
        restInstantiationPolicyLogMockMvc.perform(post("/api/instantiation-policy-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instantiationPolicyLogDTO)))
            .andExpect(status().isCreated());

        // Validate the InstantiationPolicyLog in the database
        List<InstantiationPolicyLog> instantiationPolicyLogList = instantiationPolicyLogRepository.findAll();
        assertThat(instantiationPolicyLogList).hasSize(databaseSizeBeforeCreate + 1);
        InstantiationPolicyLog testInstantiationPolicyLog = instantiationPolicyLogList.get(instantiationPolicyLogList.size() - 1);
    }

    @Test
    @Transactional
    public void createInstantiationPolicyLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = instantiationPolicyLogRepository.findAll().size();

        // Create the InstantiationPolicyLog with an existing ID
        instantiationPolicyLog.setId(1L);
        InstantiationPolicyLogDTO instantiationPolicyLogDTO = instantiationPolicyLogMapper.toDto(instantiationPolicyLog);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInstantiationPolicyLogMockMvc.perform(post("/api/instantiation-policy-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instantiationPolicyLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InstantiationPolicyLog in the database
        List<InstantiationPolicyLog> instantiationPolicyLogList = instantiationPolicyLogRepository.findAll();
        assertThat(instantiationPolicyLogList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllInstantiationPolicyLogs() throws Exception {
        // Initialize the database
        instantiationPolicyLogRepository.saveAndFlush(instantiationPolicyLog);

        // Get all the instantiationPolicyLogList
        restInstantiationPolicyLogMockMvc.perform(get("/api/instantiation-policy-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(instantiationPolicyLog.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getInstantiationPolicyLog() throws Exception {
        // Initialize the database
        instantiationPolicyLogRepository.saveAndFlush(instantiationPolicyLog);

        // Get the instantiationPolicyLog
        restInstantiationPolicyLogMockMvc.perform(get("/api/instantiation-policy-logs/{id}", instantiationPolicyLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(instantiationPolicyLog.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInstantiationPolicyLog() throws Exception {
        // Get the instantiationPolicyLog
        restInstantiationPolicyLogMockMvc.perform(get("/api/instantiation-policy-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstantiationPolicyLog() throws Exception {
        // Initialize the database
        instantiationPolicyLogRepository.saveAndFlush(instantiationPolicyLog);

        int databaseSizeBeforeUpdate = instantiationPolicyLogRepository.findAll().size();

        // Update the instantiationPolicyLog
        InstantiationPolicyLog updatedInstantiationPolicyLog = instantiationPolicyLogRepository.findById(instantiationPolicyLog.getId()).get();
        // Disconnect from session so that the updates on updatedInstantiationPolicyLog are not directly saved in db
        em.detach(updatedInstantiationPolicyLog);
        InstantiationPolicyLogDTO instantiationPolicyLogDTO = instantiationPolicyLogMapper.toDto(updatedInstantiationPolicyLog);

        restInstantiationPolicyLogMockMvc.perform(put("/api/instantiation-policy-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instantiationPolicyLogDTO)))
            .andExpect(status().isOk());

        // Validate the InstantiationPolicyLog in the database
        List<InstantiationPolicyLog> instantiationPolicyLogList = instantiationPolicyLogRepository.findAll();
        assertThat(instantiationPolicyLogList).hasSize(databaseSizeBeforeUpdate);
        InstantiationPolicyLog testInstantiationPolicyLog = instantiationPolicyLogList.get(instantiationPolicyLogList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingInstantiationPolicyLog() throws Exception {
        int databaseSizeBeforeUpdate = instantiationPolicyLogRepository.findAll().size();

        // Create the InstantiationPolicyLog
        InstantiationPolicyLogDTO instantiationPolicyLogDTO = instantiationPolicyLogMapper.toDto(instantiationPolicyLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInstantiationPolicyLogMockMvc.perform(put("/api/instantiation-policy-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(instantiationPolicyLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InstantiationPolicyLog in the database
        List<InstantiationPolicyLog> instantiationPolicyLogList = instantiationPolicyLogRepository.findAll();
        assertThat(instantiationPolicyLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInstantiationPolicyLog() throws Exception {
        // Initialize the database
        instantiationPolicyLogRepository.saveAndFlush(instantiationPolicyLog);

        int databaseSizeBeforeDelete = instantiationPolicyLogRepository.findAll().size();

        // Delete the instantiationPolicyLog
        restInstantiationPolicyLogMockMvc.perform(delete("/api/instantiation-policy-logs/{id}", instantiationPolicyLog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InstantiationPolicyLog> instantiationPolicyLogList = instantiationPolicyLogRepository.findAll();
        assertThat(instantiationPolicyLogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InstantiationPolicyLog.class);
        InstantiationPolicyLog instantiationPolicyLog1 = new InstantiationPolicyLog();
        instantiationPolicyLog1.setId(1L);
        InstantiationPolicyLog instantiationPolicyLog2 = new InstantiationPolicyLog();
        instantiationPolicyLog2.setId(instantiationPolicyLog1.getId());
        assertThat(instantiationPolicyLog1).isEqualTo(instantiationPolicyLog2);
        instantiationPolicyLog2.setId(2L);
        assertThat(instantiationPolicyLog1).isNotEqualTo(instantiationPolicyLog2);
        instantiationPolicyLog1.setId(null);
        assertThat(instantiationPolicyLog1).isNotEqualTo(instantiationPolicyLog2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InstantiationPolicyLogDTO.class);
        InstantiationPolicyLogDTO instantiationPolicyLogDTO1 = new InstantiationPolicyLogDTO();
        instantiationPolicyLogDTO1.setId(1L);
        InstantiationPolicyLogDTO instantiationPolicyLogDTO2 = new InstantiationPolicyLogDTO();
        assertThat(instantiationPolicyLogDTO1).isNotEqualTo(instantiationPolicyLogDTO2);
        instantiationPolicyLogDTO2.setId(instantiationPolicyLogDTO1.getId());
        assertThat(instantiationPolicyLogDTO1).isEqualTo(instantiationPolicyLogDTO2);
        instantiationPolicyLogDTO2.setId(2L);
        assertThat(instantiationPolicyLogDTO1).isNotEqualTo(instantiationPolicyLogDTO2);
        instantiationPolicyLogDTO1.setId(null);
        assertThat(instantiationPolicyLogDTO1).isNotEqualTo(instantiationPolicyLogDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(instantiationPolicyLogMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(instantiationPolicyLogMapper.fromId(null)).isNull();
    }
}
