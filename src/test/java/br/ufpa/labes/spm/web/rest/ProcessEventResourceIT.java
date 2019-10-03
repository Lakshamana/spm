package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ProcessEvent;
import br.ufpa.labes.spm.repository.ProcessEventRepository;
import br.ufpa.labes.spm.service.ProcessEventService;
import br.ufpa.labes.spm.service.dto.ProcessEventDTO;
import br.ufpa.labes.spm.service.mapper.ProcessEventMapper;
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
 * Integration tests for the {@link ProcessEventResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ProcessEventResourceIT {

    @Autowired
    private ProcessEventRepository processEventRepository;

    @Autowired
    private ProcessEventMapper processEventMapper;

    @Autowired
    private ProcessEventService processEventService;

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

    private MockMvc restProcessEventMockMvc;

    private ProcessEvent processEvent;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProcessEventResource processEventResource = new ProcessEventResource(processEventService);
        this.restProcessEventMockMvc = MockMvcBuilders.standaloneSetup(processEventResource)
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
    public static ProcessEvent createEntity(EntityManager em) {
        ProcessEvent processEvent = new ProcessEvent();
        return processEvent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProcessEvent createUpdatedEntity(EntityManager em) {
        ProcessEvent processEvent = new ProcessEvent();
        return processEvent;
    }

    @BeforeEach
    public void initTest() {
        processEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createProcessEvent() throws Exception {
        int databaseSizeBeforeCreate = processEventRepository.findAll().size();

        // Create the ProcessEvent
        ProcessEventDTO processEventDTO = processEventMapper.toDto(processEvent);
        restProcessEventMockMvc.perform(post("/api/process-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processEventDTO)))
            .andExpect(status().isCreated());

        // Validate the ProcessEvent in the database
        List<ProcessEvent> processEventList = processEventRepository.findAll();
        assertThat(processEventList).hasSize(databaseSizeBeforeCreate + 1);
        ProcessEvent testProcessEvent = processEventList.get(processEventList.size() - 1);
    }

    @Test
    @Transactional
    public void createProcessEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = processEventRepository.findAll().size();

        // Create the ProcessEvent with an existing ID
        processEvent.setId(1L);
        ProcessEventDTO processEventDTO = processEventMapper.toDto(processEvent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProcessEventMockMvc.perform(post("/api/process-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProcessEvent in the database
        List<ProcessEvent> processEventList = processEventRepository.findAll();
        assertThat(processEventList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProcessEvents() throws Exception {
        // Initialize the database
        processEventRepository.saveAndFlush(processEvent);

        // Get all the processEventList
        restProcessEventMockMvc.perform(get("/api/process-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(processEvent.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getProcessEvent() throws Exception {
        // Initialize the database
        processEventRepository.saveAndFlush(processEvent);

        // Get the processEvent
        restProcessEventMockMvc.perform(get("/api/process-events/{id}", processEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(processEvent.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProcessEvent() throws Exception {
        // Get the processEvent
        restProcessEventMockMvc.perform(get("/api/process-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProcessEvent() throws Exception {
        // Initialize the database
        processEventRepository.saveAndFlush(processEvent);

        int databaseSizeBeforeUpdate = processEventRepository.findAll().size();

        // Update the processEvent
        ProcessEvent updatedProcessEvent = processEventRepository.findById(processEvent.getId()).get();
        // Disconnect from session so that the updates on updatedProcessEvent are not directly saved in db
        em.detach(updatedProcessEvent);
        ProcessEventDTO processEventDTO = processEventMapper.toDto(updatedProcessEvent);

        restProcessEventMockMvc.perform(put("/api/process-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processEventDTO)))
            .andExpect(status().isOk());

        // Validate the ProcessEvent in the database
        List<ProcessEvent> processEventList = processEventRepository.findAll();
        assertThat(processEventList).hasSize(databaseSizeBeforeUpdate);
        ProcessEvent testProcessEvent = processEventList.get(processEventList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingProcessEvent() throws Exception {
        int databaseSizeBeforeUpdate = processEventRepository.findAll().size();

        // Create the ProcessEvent
        ProcessEventDTO processEventDTO = processEventMapper.toDto(processEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProcessEventMockMvc.perform(put("/api/process-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(processEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProcessEvent in the database
        List<ProcessEvent> processEventList = processEventRepository.findAll();
        assertThat(processEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProcessEvent() throws Exception {
        // Initialize the database
        processEventRepository.saveAndFlush(processEvent);

        int databaseSizeBeforeDelete = processEventRepository.findAll().size();

        // Delete the processEvent
        restProcessEventMockMvc.perform(delete("/api/process-events/{id}", processEvent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProcessEvent> processEventList = processEventRepository.findAll();
        assertThat(processEventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessEvent.class);
        ProcessEvent processEvent1 = new ProcessEvent();
        processEvent1.setId(1L);
        ProcessEvent processEvent2 = new ProcessEvent();
        processEvent2.setId(processEvent1.getId());
        assertThat(processEvent1).isEqualTo(processEvent2);
        processEvent2.setId(2L);
        assertThat(processEvent1).isNotEqualTo(processEvent2);
        processEvent1.setId(null);
        assertThat(processEvent1).isNotEqualTo(processEvent2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProcessEventDTO.class);
        ProcessEventDTO processEventDTO1 = new ProcessEventDTO();
        processEventDTO1.setId(1L);
        ProcessEventDTO processEventDTO2 = new ProcessEventDTO();
        assertThat(processEventDTO1).isNotEqualTo(processEventDTO2);
        processEventDTO2.setId(processEventDTO1.getId());
        assertThat(processEventDTO1).isEqualTo(processEventDTO2);
        processEventDTO2.setId(2L);
        assertThat(processEventDTO1).isNotEqualTo(processEventDTO2);
        processEventDTO1.setId(null);
        assertThat(processEventDTO1).isNotEqualTo(processEventDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(processEventMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(processEventMapper.fromId(null)).isNull();
    }
}
