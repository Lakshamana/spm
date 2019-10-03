package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ConnectionEvent;
import br.ufpa.labes.spm.repository.ConnectionEventRepository;
import br.ufpa.labes.spm.service.ConnectionEventService;
import br.ufpa.labes.spm.service.dto.ConnectionEventDTO;
import br.ufpa.labes.spm.service.mapper.ConnectionEventMapper;
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
 * Integration tests for the {@link ConnectionEventResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ConnectionEventResourceIT {

    @Autowired
    private ConnectionEventRepository connectionEventRepository;

    @Autowired
    private ConnectionEventMapper connectionEventMapper;

    @Autowired
    private ConnectionEventService connectionEventService;

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

    private MockMvc restConnectionEventMockMvc;

    private ConnectionEvent connectionEvent;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConnectionEventResource connectionEventResource = new ConnectionEventResource(connectionEventService);
        this.restConnectionEventMockMvc = MockMvcBuilders.standaloneSetup(connectionEventResource)
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
    public static ConnectionEvent createEntity(EntityManager em) {
        ConnectionEvent connectionEvent = new ConnectionEvent();
        return connectionEvent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConnectionEvent createUpdatedEntity(EntityManager em) {
        ConnectionEvent connectionEvent = new ConnectionEvent();
        return connectionEvent;
    }

    @BeforeEach
    public void initTest() {
        connectionEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createConnectionEvent() throws Exception {
        int databaseSizeBeforeCreate = connectionEventRepository.findAll().size();

        // Create the ConnectionEvent
        ConnectionEventDTO connectionEventDTO = connectionEventMapper.toDto(connectionEvent);
        restConnectionEventMockMvc.perform(post("/api/connection-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(connectionEventDTO)))
            .andExpect(status().isCreated());

        // Validate the ConnectionEvent in the database
        List<ConnectionEvent> connectionEventList = connectionEventRepository.findAll();
        assertThat(connectionEventList).hasSize(databaseSizeBeforeCreate + 1);
        ConnectionEvent testConnectionEvent = connectionEventList.get(connectionEventList.size() - 1);
    }

    @Test
    @Transactional
    public void createConnectionEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = connectionEventRepository.findAll().size();

        // Create the ConnectionEvent with an existing ID
        connectionEvent.setId(1L);
        ConnectionEventDTO connectionEventDTO = connectionEventMapper.toDto(connectionEvent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConnectionEventMockMvc.perform(post("/api/connection-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(connectionEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConnectionEvent in the database
        List<ConnectionEvent> connectionEventList = connectionEventRepository.findAll();
        assertThat(connectionEventList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConnectionEvents() throws Exception {
        // Initialize the database
        connectionEventRepository.saveAndFlush(connectionEvent);

        // Get all the connectionEventList
        restConnectionEventMockMvc.perform(get("/api/connection-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(connectionEvent.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getConnectionEvent() throws Exception {
        // Initialize the database
        connectionEventRepository.saveAndFlush(connectionEvent);

        // Get the connectionEvent
        restConnectionEventMockMvc.perform(get("/api/connection-events/{id}", connectionEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(connectionEvent.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingConnectionEvent() throws Exception {
        // Get the connectionEvent
        restConnectionEventMockMvc.perform(get("/api/connection-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConnectionEvent() throws Exception {
        // Initialize the database
        connectionEventRepository.saveAndFlush(connectionEvent);

        int databaseSizeBeforeUpdate = connectionEventRepository.findAll().size();

        // Update the connectionEvent
        ConnectionEvent updatedConnectionEvent = connectionEventRepository.findById(connectionEvent.getId()).get();
        // Disconnect from session so that the updates on updatedConnectionEvent are not directly saved in db
        em.detach(updatedConnectionEvent);
        ConnectionEventDTO connectionEventDTO = connectionEventMapper.toDto(updatedConnectionEvent);

        restConnectionEventMockMvc.perform(put("/api/connection-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(connectionEventDTO)))
            .andExpect(status().isOk());

        // Validate the ConnectionEvent in the database
        List<ConnectionEvent> connectionEventList = connectionEventRepository.findAll();
        assertThat(connectionEventList).hasSize(databaseSizeBeforeUpdate);
        ConnectionEvent testConnectionEvent = connectionEventList.get(connectionEventList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingConnectionEvent() throws Exception {
        int databaseSizeBeforeUpdate = connectionEventRepository.findAll().size();

        // Create the ConnectionEvent
        ConnectionEventDTO connectionEventDTO = connectionEventMapper.toDto(connectionEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConnectionEventMockMvc.perform(put("/api/connection-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(connectionEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConnectionEvent in the database
        List<ConnectionEvent> connectionEventList = connectionEventRepository.findAll();
        assertThat(connectionEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConnectionEvent() throws Exception {
        // Initialize the database
        connectionEventRepository.saveAndFlush(connectionEvent);

        int databaseSizeBeforeDelete = connectionEventRepository.findAll().size();

        // Delete the connectionEvent
        restConnectionEventMockMvc.perform(delete("/api/connection-events/{id}", connectionEvent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConnectionEvent> connectionEventList = connectionEventRepository.findAll();
        assertThat(connectionEventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConnectionEvent.class);
        ConnectionEvent connectionEvent1 = new ConnectionEvent();
        connectionEvent1.setId(1L);
        ConnectionEvent connectionEvent2 = new ConnectionEvent();
        connectionEvent2.setId(connectionEvent1.getId());
        assertThat(connectionEvent1).isEqualTo(connectionEvent2);
        connectionEvent2.setId(2L);
        assertThat(connectionEvent1).isNotEqualTo(connectionEvent2);
        connectionEvent1.setId(null);
        assertThat(connectionEvent1).isNotEqualTo(connectionEvent2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConnectionEventDTO.class);
        ConnectionEventDTO connectionEventDTO1 = new ConnectionEventDTO();
        connectionEventDTO1.setId(1L);
        ConnectionEventDTO connectionEventDTO2 = new ConnectionEventDTO();
        assertThat(connectionEventDTO1).isNotEqualTo(connectionEventDTO2);
        connectionEventDTO2.setId(connectionEventDTO1.getId());
        assertThat(connectionEventDTO1).isEqualTo(connectionEventDTO2);
        connectionEventDTO2.setId(2L);
        assertThat(connectionEventDTO1).isNotEqualTo(connectionEventDTO2);
        connectionEventDTO1.setId(null);
        assertThat(connectionEventDTO1).isNotEqualTo(connectionEventDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(connectionEventMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(connectionEventMapper.fromId(null)).isNull();
    }
}
