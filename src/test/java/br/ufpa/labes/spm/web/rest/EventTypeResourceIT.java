package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.EventType;
import br.ufpa.labes.spm.repository.EventTypeRepository;
import br.ufpa.labes.spm.service.EventTypeService;
import br.ufpa.labes.spm.service.dto.EventTypeDTO;
import br.ufpa.labes.spm.service.mapper.EventTypeMapper;
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
 * Integration tests for the {@link EventTypeResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class EventTypeResourceIT {

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Autowired
    private EventTypeMapper eventTypeMapper;

    @Autowired
    private EventTypeService eventTypeService;

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

    private MockMvc restEventTypeMockMvc;

    private EventType eventType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EventTypeResource eventTypeResource = new EventTypeResource(eventTypeService);
        this.restEventTypeMockMvc = MockMvcBuilders.standaloneSetup(eventTypeResource)
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
    public static EventType createEntity(EntityManager em) {
        EventType eventType = new EventType();
        return eventType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventType createUpdatedEntity(EntityManager em) {
        EventType eventType = new EventType();
        return eventType;
    }

    @BeforeEach
    public void initTest() {
        eventType = createEntity(em);
    }

    @Test
    @Transactional
    public void createEventType() throws Exception {
        int databaseSizeBeforeCreate = eventTypeRepository.findAll().size();

        // Create the EventType
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);
        restEventTypeMockMvc.perform(post("/api/event-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeCreate + 1);
        EventType testEventType = eventTypeList.get(eventTypeList.size() - 1);
    }

    @Test
    @Transactional
    public void createEventTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventTypeRepository.findAll().size();

        // Create the EventType with an existing ID
        eventType.setId(1L);
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventTypeMockMvc.perform(post("/api/event-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEventTypes() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get all the eventTypeList
        restEventTypeMockMvc.perform(get("/api/event-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventType.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getEventType() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        // Get the eventType
        restEventTypeMockMvc.perform(get("/api/event-types/{id}", eventType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eventType.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEventType() throws Exception {
        // Get the eventType
        restEventTypeMockMvc.perform(get("/api/event-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEventType() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();

        // Update the eventType
        EventType updatedEventType = eventTypeRepository.findById(eventType.getId()).get();
        // Disconnect from session so that the updates on updatedEventType are not directly saved in db
        em.detach(updatedEventType);
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(updatedEventType);

        restEventTypeMockMvc.perform(put("/api/event-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventTypeDTO)))
            .andExpect(status().isOk());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
        EventType testEventType = eventTypeList.get(eventTypeList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingEventType() throws Exception {
        int databaseSizeBeforeUpdate = eventTypeRepository.findAll().size();

        // Create the EventType
        EventTypeDTO eventTypeDTO = eventTypeMapper.toDto(eventType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventTypeMockMvc.perform(put("/api/event-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eventTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EventType in the database
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEventType() throws Exception {
        // Initialize the database
        eventTypeRepository.saveAndFlush(eventType);

        int databaseSizeBeforeDelete = eventTypeRepository.findAll().size();

        // Delete the eventType
        restEventTypeMockMvc.perform(delete("/api/event-types/{id}", eventType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventType> eventTypeList = eventTypeRepository.findAll();
        assertThat(eventTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventType.class);
        EventType eventType1 = new EventType();
        eventType1.setId(1L);
        EventType eventType2 = new EventType();
        eventType2.setId(eventType1.getId());
        assertThat(eventType1).isEqualTo(eventType2);
        eventType2.setId(2L);
        assertThat(eventType1).isNotEqualTo(eventType2);
        eventType1.setId(null);
        assertThat(eventType1).isNotEqualTo(eventType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventTypeDTO.class);
        EventTypeDTO eventTypeDTO1 = new EventTypeDTO();
        eventTypeDTO1.setId(1L);
        EventTypeDTO eventTypeDTO2 = new EventTypeDTO();
        assertThat(eventTypeDTO1).isNotEqualTo(eventTypeDTO2);
        eventTypeDTO2.setId(eventTypeDTO1.getId());
        assertThat(eventTypeDTO1).isEqualTo(eventTypeDTO2);
        eventTypeDTO2.setId(2L);
        assertThat(eventTypeDTO1).isNotEqualTo(eventTypeDTO2);
        eventTypeDTO1.setId(null);
        assertThat(eventTypeDTO1).isNotEqualTo(eventTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(eventTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(eventTypeMapper.fromId(null)).isNull();
    }
}
