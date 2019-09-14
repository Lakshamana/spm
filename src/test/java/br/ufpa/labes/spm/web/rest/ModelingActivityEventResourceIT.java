package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ModelingActivityEvent;
import br.ufpa.labes.spm.repository.ModelingActivityEventRepository;
import br.ufpa.labes.spm.service.ModelingActivityEventService;
import br.ufpa.labes.spm.service.dto.ModelingActivityEventDTO;
import br.ufpa.labes.spm.service.mapper.ModelingActivityEventMapper;
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
 * Integration tests for the {@link ModelingActivityEventResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ModelingActivityEventResourceIT {

    @Autowired
    private ModelingActivityEventRepository modelingActivityEventRepository;

    @Autowired
    private ModelingActivityEventMapper modelingActivityEventMapper;

    @Autowired
    private ModelingActivityEventService modelingActivityEventService;

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

    private MockMvc restModelingActivityEventMockMvc;

    private ModelingActivityEvent modelingActivityEvent;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ModelingActivityEventResource modelingActivityEventResource = new ModelingActivityEventResource(modelingActivityEventService);
        this.restModelingActivityEventMockMvc = MockMvcBuilders.standaloneSetup(modelingActivityEventResource)
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
    public static ModelingActivityEvent createEntity(EntityManager em) {
        ModelingActivityEvent modelingActivityEvent = new ModelingActivityEvent();
        return modelingActivityEvent;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModelingActivityEvent createUpdatedEntity(EntityManager em) {
        ModelingActivityEvent modelingActivityEvent = new ModelingActivityEvent();
        return modelingActivityEvent;
    }

    @BeforeEach
    public void initTest() {
        modelingActivityEvent = createEntity(em);
    }

    @Test
    @Transactional
    public void createModelingActivityEvent() throws Exception {
        int databaseSizeBeforeCreate = modelingActivityEventRepository.findAll().size();

        // Create the ModelingActivityEvent
        ModelingActivityEventDTO modelingActivityEventDTO = modelingActivityEventMapper.toDto(modelingActivityEvent);
        restModelingActivityEventMockMvc.perform(post("/api/modeling-activity-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelingActivityEventDTO)))
            .andExpect(status().isCreated());

        // Validate the ModelingActivityEvent in the database
        List<ModelingActivityEvent> modelingActivityEventList = modelingActivityEventRepository.findAll();
        assertThat(modelingActivityEventList).hasSize(databaseSizeBeforeCreate + 1);
        ModelingActivityEvent testModelingActivityEvent = modelingActivityEventList.get(modelingActivityEventList.size() - 1);
    }

    @Test
    @Transactional
    public void createModelingActivityEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = modelingActivityEventRepository.findAll().size();

        // Create the ModelingActivityEvent with an existing ID
        modelingActivityEvent.setId(1L);
        ModelingActivityEventDTO modelingActivityEventDTO = modelingActivityEventMapper.toDto(modelingActivityEvent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModelingActivityEventMockMvc.perform(post("/api/modeling-activity-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelingActivityEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ModelingActivityEvent in the database
        List<ModelingActivityEvent> modelingActivityEventList = modelingActivityEventRepository.findAll();
        assertThat(modelingActivityEventList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllModelingActivityEvents() throws Exception {
        // Initialize the database
        modelingActivityEventRepository.saveAndFlush(modelingActivityEvent);

        // Get all the modelingActivityEventList
        restModelingActivityEventMockMvc.perform(get("/api/modeling-activity-events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modelingActivityEvent.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getModelingActivityEvent() throws Exception {
        // Initialize the database
        modelingActivityEventRepository.saveAndFlush(modelingActivityEvent);

        // Get the modelingActivityEvent
        restModelingActivityEventMockMvc.perform(get("/api/modeling-activity-events/{id}", modelingActivityEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(modelingActivityEvent.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingModelingActivityEvent() throws Exception {
        // Get the modelingActivityEvent
        restModelingActivityEventMockMvc.perform(get("/api/modeling-activity-events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModelingActivityEvent() throws Exception {
        // Initialize the database
        modelingActivityEventRepository.saveAndFlush(modelingActivityEvent);

        int databaseSizeBeforeUpdate = modelingActivityEventRepository.findAll().size();

        // Update the modelingActivityEvent
        ModelingActivityEvent updatedModelingActivityEvent = modelingActivityEventRepository.findById(modelingActivityEvent.getId()).get();
        // Disconnect from session so that the updates on updatedModelingActivityEvent are not directly saved in db
        em.detach(updatedModelingActivityEvent);
        ModelingActivityEventDTO modelingActivityEventDTO = modelingActivityEventMapper.toDto(updatedModelingActivityEvent);

        restModelingActivityEventMockMvc.perform(put("/api/modeling-activity-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelingActivityEventDTO)))
            .andExpect(status().isOk());

        // Validate the ModelingActivityEvent in the database
        List<ModelingActivityEvent> modelingActivityEventList = modelingActivityEventRepository.findAll();
        assertThat(modelingActivityEventList).hasSize(databaseSizeBeforeUpdate);
        ModelingActivityEvent testModelingActivityEvent = modelingActivityEventList.get(modelingActivityEventList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingModelingActivityEvent() throws Exception {
        int databaseSizeBeforeUpdate = modelingActivityEventRepository.findAll().size();

        // Create the ModelingActivityEvent
        ModelingActivityEventDTO modelingActivityEventDTO = modelingActivityEventMapper.toDto(modelingActivityEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModelingActivityEventMockMvc.perform(put("/api/modeling-activity-events")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelingActivityEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ModelingActivityEvent in the database
        List<ModelingActivityEvent> modelingActivityEventList = modelingActivityEventRepository.findAll();
        assertThat(modelingActivityEventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteModelingActivityEvent() throws Exception {
        // Initialize the database
        modelingActivityEventRepository.saveAndFlush(modelingActivityEvent);

        int databaseSizeBeforeDelete = modelingActivityEventRepository.findAll().size();

        // Delete the modelingActivityEvent
        restModelingActivityEventMockMvc.perform(delete("/api/modeling-activity-events/{id}", modelingActivityEvent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ModelingActivityEvent> modelingActivityEventList = modelingActivityEventRepository.findAll();
        assertThat(modelingActivityEventList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModelingActivityEvent.class);
        ModelingActivityEvent modelingActivityEvent1 = new ModelingActivityEvent();
        modelingActivityEvent1.setId(1L);
        ModelingActivityEvent modelingActivityEvent2 = new ModelingActivityEvent();
        modelingActivityEvent2.setId(modelingActivityEvent1.getId());
        assertThat(modelingActivityEvent1).isEqualTo(modelingActivityEvent2);
        modelingActivityEvent2.setId(2L);
        assertThat(modelingActivityEvent1).isNotEqualTo(modelingActivityEvent2);
        modelingActivityEvent1.setId(null);
        assertThat(modelingActivityEvent1).isNotEqualTo(modelingActivityEvent2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModelingActivityEventDTO.class);
        ModelingActivityEventDTO modelingActivityEventDTO1 = new ModelingActivityEventDTO();
        modelingActivityEventDTO1.setId(1L);
        ModelingActivityEventDTO modelingActivityEventDTO2 = new ModelingActivityEventDTO();
        assertThat(modelingActivityEventDTO1).isNotEqualTo(modelingActivityEventDTO2);
        modelingActivityEventDTO2.setId(modelingActivityEventDTO1.getId());
        assertThat(modelingActivityEventDTO1).isEqualTo(modelingActivityEventDTO2);
        modelingActivityEventDTO2.setId(2L);
        assertThat(modelingActivityEventDTO1).isNotEqualTo(modelingActivityEventDTO2);
        modelingActivityEventDTO1.setId(null);
        assertThat(modelingActivityEventDTO1).isNotEqualTo(modelingActivityEventDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(modelingActivityEventMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(modelingActivityEventMapper.fromId(null)).isNull();
    }
}
