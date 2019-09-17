package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.GlobalActivityEvent;
import br.ufpa.labes.spm.repository.GlobalActivityEventRepository;
import br.ufpa.labes.spm.service.GlobalActivityEventService;
import br.ufpa.labes.spm.service.dto.GlobalActivityEventDTO;
import br.ufpa.labes.spm.service.mapper.GlobalActivityEventMapper;
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
 * Integration tests for the {@link GlobalActivityEventResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class GlobalActivityEventResourceIT {

    @Autowired
    private GlobalActivityEventRepository globalActivityEventRepository;

    @Autowired
    private GlobalActivityEventMapper globalActivityEventMapper;

    @Autowired
    private GlobalActivityEventService globalActivityEventService;

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

    private MockMvc restGlobalActivityEventMockMvc;

    private GlobalActivityEvent globalActivityEvent;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GlobalActivityEventResource globalActivityEventResource = new GlobalActivityEventResource(globalActivityEventService);
        this.restGlobalActivityEventMockMvc = MockMvcBuilders.standaloneSetup(globalActivityEventResource)
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
    public static GlobalActivityEvent createEntity(EntityManager em) {
        GlobalActivityEvent globalActivityEvent = new GlobalActivityEvent();
        return globalActivityEvent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GlobalActivityEvent createUpdatedEntity(EntityManager em) {
        GlobalActivityEvent globalActivityEvent = new GlobalActivityEvent();
        return globalActivityEvent;
    }

    @BeforeEach
    public void initTest() {
        globalActivityEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createGlobalActivityEvent() throws Exception {
        int databaseSizeBeforeCreate = globalActivityEventRepository.findAll().size();

        // Create the GlobalActivityEvent
        GlobalActivityEventDTO globalActivityEventDTO = globalActivityEventMapper.toDto(globalActivityEvent);
        restGlobalActivityEventMockMvc.perform(post("/api/global-activity-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalActivityEventDTO)))
            .andExpect(status().isCreated());

        // Validate the GlobalActivityEvent in the database
        List<GlobalActivityEvent> globalActivityEventList = globalActivityEventRepository.findAll();
        assertThat(globalActivityEventList).hasSize(databaseSizeBeforeCreate + 1);
        GlobalActivityEvent testGlobalActivityEvent = globalActivityEventList.get(globalActivityEventList.size() - 1);
    }

    @Test
    @Transactional
    public void createGlobalActivityEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = globalActivityEventRepository.findAll().size();

        // Create the GlobalActivityEvent with an existing ID
        globalActivityEvent.setId(1L);
        GlobalActivityEventDTO globalActivityEventDTO = globalActivityEventMapper.toDto(globalActivityEvent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGlobalActivityEventMockMvc.perform(post("/api/global-activity-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalActivityEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GlobalActivityEvent in the database
        List<GlobalActivityEvent> globalActivityEventList = globalActivityEventRepository.findAll();
        assertThat(globalActivityEventList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllGlobalActivityEvents() throws Exception {
        // Initialize the database
        globalActivityEventRepository.saveAndFlush(globalActivityEvent);

        // Get all the globalActivityEventList
        restGlobalActivityEventMockMvc.perform(get("/api/global-activity-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(globalActivityEvent.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getGlobalActivityEvent() throws Exception {
        // Initialize the database
        globalActivityEventRepository.saveAndFlush(globalActivityEvent);

        // Get the globalActivityEvent
        restGlobalActivityEventMockMvc.perform(get("/api/global-activity-events/{id}", globalActivityEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(globalActivityEvent.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGlobalActivityEvent() throws Exception {
        // Get the globalActivityEvent
        restGlobalActivityEventMockMvc.perform(get("/api/global-activity-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGlobalActivityEvent() throws Exception {
        // Initialize the database
        globalActivityEventRepository.saveAndFlush(globalActivityEvent);

        int databaseSizeBeforeUpdate = globalActivityEventRepository.findAll().size();

        // Update the globalActivityEvent
        GlobalActivityEvent updatedGlobalActivityEvent = globalActivityEventRepository.findById(globalActivityEvent.getId()).get();
        // Disconnect from session so that the updates on updatedGlobalActivityEvent are not directly saved in db
        em.detach(updatedGlobalActivityEvent);
        GlobalActivityEventDTO globalActivityEventDTO = globalActivityEventMapper.toDto(updatedGlobalActivityEvent);

        restGlobalActivityEventMockMvc.perform(put("/api/global-activity-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalActivityEventDTO)))
            .andExpect(status().isOk());

        // Validate the GlobalActivityEvent in the database
        List<GlobalActivityEvent> globalActivityEventList = globalActivityEventRepository.findAll();
        assertThat(globalActivityEventList).hasSize(databaseSizeBeforeUpdate);
        GlobalActivityEvent testGlobalActivityEvent = globalActivityEventList.get(globalActivityEventList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingGlobalActivityEvent() throws Exception {
        int databaseSizeBeforeUpdate = globalActivityEventRepository.findAll().size();

        // Create the GlobalActivityEvent
        GlobalActivityEventDTO globalActivityEventDTO = globalActivityEventMapper.toDto(globalActivityEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGlobalActivityEventMockMvc.perform(put("/api/global-activity-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalActivityEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GlobalActivityEvent in the database
        List<GlobalActivityEvent> globalActivityEventList = globalActivityEventRepository.findAll();
        assertThat(globalActivityEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGlobalActivityEvent() throws Exception {
        // Initialize the database
        globalActivityEventRepository.saveAndFlush(globalActivityEvent);

        int databaseSizeBeforeDelete = globalActivityEventRepository.findAll().size();

        // Delete the globalActivityEvent
        restGlobalActivityEventMockMvc.perform(delete("/api/global-activity-events/{id}", globalActivityEvent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GlobalActivityEvent> globalActivityEventList = globalActivityEventRepository.findAll();
        assertThat(globalActivityEventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GlobalActivityEvent.class);
        GlobalActivityEvent globalActivityEvent1 = new GlobalActivityEvent();
        globalActivityEvent1.setId(1L);
        GlobalActivityEvent globalActivityEvent2 = new GlobalActivityEvent();
        globalActivityEvent2.setId(globalActivityEvent1.getId());
        assertThat(globalActivityEvent1).isEqualTo(globalActivityEvent2);
        globalActivityEvent2.setId(2L);
        assertThat(globalActivityEvent1).isNotEqualTo(globalActivityEvent2);
        globalActivityEvent1.setId(null);
        assertThat(globalActivityEvent1).isNotEqualTo(globalActivityEvent2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GlobalActivityEventDTO.class);
        GlobalActivityEventDTO globalActivityEventDTO1 = new GlobalActivityEventDTO();
        globalActivityEventDTO1.setId(1L);
        GlobalActivityEventDTO globalActivityEventDTO2 = new GlobalActivityEventDTO();
        assertThat(globalActivityEventDTO1).isNotEqualTo(globalActivityEventDTO2);
        globalActivityEventDTO2.setId(globalActivityEventDTO1.getId());
        assertThat(globalActivityEventDTO1).isEqualTo(globalActivityEventDTO2);
        globalActivityEventDTO2.setId(2L);
        assertThat(globalActivityEventDTO1).isNotEqualTo(globalActivityEventDTO2);
        globalActivityEventDTO1.setId(null);
        assertThat(globalActivityEventDTO1).isNotEqualTo(globalActivityEventDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(globalActivityEventMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(globalActivityEventMapper.fromId(null)).isNull();
    }
}
