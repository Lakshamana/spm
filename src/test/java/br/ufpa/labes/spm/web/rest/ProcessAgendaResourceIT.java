package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ProcessAgenda;
import br.ufpa.labes.spm.repository.ProcessAgendaRepository;
import br.ufpa.labes.spm.service.ProcessAgendaService;
import br.ufpa.labes.spm.service.dto.ProcessAgendaDTO;
import br.ufpa.labes.spm.service.mapper.ProcessAgendaMapper;
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
 * Integration tests for the {@link ProcessAgendaResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ProcessAgendaResourceIT {

    @Autowired
    private ProcessAgendaRepository processAgendaRepository;

    @Autowired
    private ProcessAgendaMapper processAgendaMapper;

    @Autowired
    private ProcessAgendaService processAgendaService;

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

    private MockMvc restProcessAgendaMockMvc;

    private ProcessAgenda processAgenda;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProcessAgendaResource processAgendaResource = new ProcessAgendaResource(processAgendaService);
        this.restProcessAgendaMockMvc = MockMvcBuilders.standaloneSetup(processAgendaResource)
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
    public static ProcessAgenda createEntity(EntityManager em) {
        ProcessAgenda processAgenda = new ProcessAgenda();
        return processAgenda;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessAgenda createUpdatedEntity(EntityManager em) {
        ProcessAgenda processAgenda = new ProcessAgenda();
        return processAgenda;
    }

    @BeforeEach
    public void initTest() {
        processAgenda = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcessAgenda() throws Exception {
        int databaseSizeBeforeCreate = processAgendaRepository.findAll().size();

        // Create the ProcessAgenda
        ProcessAgendaDTO processAgendaDTO = processAgendaMapper.toDto(processAgenda);
        restProcessAgendaMockMvc.perform(post("/api/process-agenda")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processAgendaDTO)))
            .andExpect(status().isCreated());

        // Validate the ProcessAgenda in the database
        List<ProcessAgenda> processAgendaList = processAgendaRepository.findAll();
        assertThat(processAgendaList).hasSize(databaseSizeBeforeCreate + 1);
        ProcessAgenda testProcessAgenda = processAgendaList.get(processAgendaList.size() - 1);
    }

    @Test
    @Transactional
    public void createProcessAgendaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = processAgendaRepository.findAll().size();

        // Create the ProcessAgenda with an existing ID
        processAgenda.setId(1L);
        ProcessAgendaDTO processAgendaDTO = processAgendaMapper.toDto(processAgenda);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessAgendaMockMvc.perform(post("/api/process-agenda")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processAgendaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProcessAgenda in the database
        List<ProcessAgenda> processAgendaList = processAgendaRepository.findAll();
        assertThat(processAgendaList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProcessAgenda() throws Exception {
        // Initialize the database
        processAgendaRepository.saveAndFlush(processAgenda);

        // Get all the processAgendaList
        restProcessAgendaMockMvc.perform(get("/api/process-agenda?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processAgenda.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getProcessAgenda() throws Exception {
        // Initialize the database
        processAgendaRepository.saveAndFlush(processAgenda);

        // Get the processAgenda
        restProcessAgendaMockMvc.perform(get("/api/process-agenda/{id}", processAgenda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(processAgenda.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProcessAgenda() throws Exception {
        // Get the processAgenda
        restProcessAgendaMockMvc.perform(get("/api/process-agenda/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcessAgenda() throws Exception {
        // Initialize the database
        processAgendaRepository.saveAndFlush(processAgenda);

        int databaseSizeBeforeUpdate = processAgendaRepository.findAll().size();

        // Update the processAgenda
        ProcessAgenda updatedProcessAgenda = processAgendaRepository.findById(processAgenda.getId()).get();
        // Disconnect from session so that the updates on updatedProcessAgenda are not directly saved in db
        em.detach(updatedProcessAgenda);
        ProcessAgendaDTO processAgendaDTO = processAgendaMapper.toDto(updatedProcessAgenda);

        restProcessAgendaMockMvc.perform(put("/api/process-agenda")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processAgendaDTO)))
            .andExpect(status().isOk());

        // Validate the ProcessAgenda in the database
        List<ProcessAgenda> processAgendaList = processAgendaRepository.findAll();
        assertThat(processAgendaList).hasSize(databaseSizeBeforeUpdate);
        ProcessAgenda testProcessAgenda = processAgendaList.get(processAgendaList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingProcessAgenda() throws Exception {
        int databaseSizeBeforeUpdate = processAgendaRepository.findAll().size();

        // Create the ProcessAgenda
        ProcessAgendaDTO processAgendaDTO = processAgendaMapper.toDto(processAgenda);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessAgendaMockMvc.perform(put("/api/process-agenda")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processAgendaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProcessAgenda in the database
        List<ProcessAgenda> processAgendaList = processAgendaRepository.findAll();
        assertThat(processAgendaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProcessAgenda() throws Exception {
        // Initialize the database
        processAgendaRepository.saveAndFlush(processAgenda);

        int databaseSizeBeforeDelete = processAgendaRepository.findAll().size();

        // Delete the processAgenda
        restProcessAgendaMockMvc.perform(delete("/api/process-agenda/{id}", processAgenda.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProcessAgenda> processAgendaList = processAgendaRepository.findAll();
        assertThat(processAgendaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessAgenda.class);
        ProcessAgenda processAgenda1 = new ProcessAgenda();
        processAgenda1.setId(1L);
        ProcessAgenda processAgenda2 = new ProcessAgenda();
        processAgenda2.setId(processAgenda1.getId());
        assertThat(processAgenda1).isEqualTo(processAgenda2);
        processAgenda2.setId(2L);
        assertThat(processAgenda1).isNotEqualTo(processAgenda2);
        processAgenda1.setId(null);
        assertThat(processAgenda1).isNotEqualTo(processAgenda2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessAgendaDTO.class);
        ProcessAgendaDTO processAgendaDTO1 = new ProcessAgendaDTO();
        processAgendaDTO1.setId(1L);
        ProcessAgendaDTO processAgendaDTO2 = new ProcessAgendaDTO();
        assertThat(processAgendaDTO1).isNotEqualTo(processAgendaDTO2);
        processAgendaDTO2.setId(processAgendaDTO1.getId());
        assertThat(processAgendaDTO1).isEqualTo(processAgendaDTO2);
        processAgendaDTO2.setId(2L);
        assertThat(processAgendaDTO1).isNotEqualTo(processAgendaDTO2);
        processAgendaDTO1.setId(null);
        assertThat(processAgendaDTO1).isNotEqualTo(processAgendaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(processAgendaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(processAgendaMapper.fromId(null)).isNull();
    }
}
