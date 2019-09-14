package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.PlainActivity;
import br.ufpa.labes.spm.repository.PlainActivityRepository;
import br.ufpa.labes.spm.service.PlainActivityService;
import br.ufpa.labes.spm.service.dto.PlainActivityDTO;
import br.ufpa.labes.spm.service.mapper.PlainActivityMapper;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static br.ufpa.labes.spm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.ufpa.labes.spm.domain.enumeration.PlainActivityStatus;
/**
 * Integration tests for the {@link PlainActivityResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class PlainActivityResourceIT {

    private static final String DEFAULT_REQUIREMENTS = "AAAAAAAAAA";
    private static final String UPDATED_REQUIREMENTS = "BBBBBBBBBB";

    private static final PlainActivityStatus DEFAULT_PLAIN_ACTIVITY_STATUS = PlainActivityStatus.WAITING;
    private static final PlainActivityStatus UPDATED_PLAIN_ACTIVITY_STATUS = PlainActivityStatus.READY;

    private static final Boolean DEFAULT_AUTOMATIC = false;
    private static final Boolean UPDATED_AUTOMATIC = true;

    @Autowired
    private PlainActivityRepository plainActivityRepository;

    @Autowired
    private PlainActivityMapper plainActivityMapper;

    @Autowired
    private PlainActivityService plainActivityService;

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

    private MockMvc restPlainActivityMockMvc;

    private PlainActivity plainActivity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlainActivityResource plainActivityResource = new PlainActivityResource(plainActivityService);
        this.restPlainActivityMockMvc = MockMvcBuilders.standaloneSetup(plainActivityResource)
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
    public static PlainActivity createEntity(EntityManager em) {
        PlainActivity plainActivity = new PlainActivity()
            .requirements(DEFAULT_REQUIREMENTS)
            .plainActivityStatus(DEFAULT_PLAIN_ACTIVITY_STATUS)
            .automatic(DEFAULT_AUTOMATIC);
        return plainActivity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlainActivity createUpdatedEntity(EntityManager em) {
        PlainActivity plainActivity = new PlainActivity()
            .requirements(UPDATED_REQUIREMENTS)
            .plainActivityStatus(UPDATED_PLAIN_ACTIVITY_STATUS)
            .automatic(UPDATED_AUTOMATIC);
        return plainActivity;
    }

    @BeforeEach
    public void initTest() {
        plainActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlainActivity() throws Exception {
        int databaseSizeBeforeCreate = plainActivityRepository.findAll().size();

        // Create the PlainActivity
        PlainActivityDTO plainActivityDTO = plainActivityMapper.toDto(plainActivity);
        restPlainActivityMockMvc.perform(post("/api/plain-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plainActivityDTO)))
            .andExpect(status().isCreated());

        // Validate the PlainActivity in the database
        List<PlainActivity> plainActivityList = plainActivityRepository.findAll();
        assertThat(plainActivityList).hasSize(databaseSizeBeforeCreate + 1);
        PlainActivity testPlainActivity = plainActivityList.get(plainActivityList.size() - 1);
        assertThat(testPlainActivity.getRequirements()).isEqualTo(DEFAULT_REQUIREMENTS);
        assertThat(testPlainActivity.getPlainActivityStatus()).isEqualTo(DEFAULT_PLAIN_ACTIVITY_STATUS);
        assertThat(testPlainActivity.isAutomatic()).isEqualTo(DEFAULT_AUTOMATIC);
    }

    @Test
    @Transactional
    public void createPlainActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = plainActivityRepository.findAll().size();

        // Create the PlainActivity with an existing ID
        plainActivity.setId(1L);
        PlainActivityDTO plainActivityDTO = plainActivityMapper.toDto(plainActivity);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlainActivityMockMvc.perform(post("/api/plain-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plainActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlainActivity in the database
        List<PlainActivity> plainActivityList = plainActivityRepository.findAll();
        assertThat(plainActivityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPlainActivities() throws Exception {
        // Initialize the database
        plainActivityRepository.saveAndFlush(plainActivity);

        // Get all the plainActivityList
        restPlainActivityMockMvc.perform(get("/api/plain-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plainActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].requirements").value(hasItem(DEFAULT_REQUIREMENTS.toString())))
            .andExpect(jsonPath("$.[*].plainActivityStatus").value(hasItem(DEFAULT_PLAIN_ACTIVITY_STATUS.toString())))
            .andExpect(jsonPath("$.[*].automatic").value(hasItem(DEFAULT_AUTOMATIC.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPlainActivity() throws Exception {
        // Initialize the database
        plainActivityRepository.saveAndFlush(plainActivity);

        // Get the plainActivity
        restPlainActivityMockMvc.perform(get("/api/plain-activities/{id}", plainActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(plainActivity.getId().intValue()))
            .andExpect(jsonPath("$.requirements").value(DEFAULT_REQUIREMENTS.toString()))
            .andExpect(jsonPath("$.plainActivityStatus").value(DEFAULT_PLAIN_ACTIVITY_STATUS.toString()))
            .andExpect(jsonPath("$.automatic").value(DEFAULT_AUTOMATIC.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPlainActivity() throws Exception {
        // Get the plainActivity
        restPlainActivityMockMvc.perform(get("/api/plain-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlainActivity() throws Exception {
        // Initialize the database
        plainActivityRepository.saveAndFlush(plainActivity);

        int databaseSizeBeforeUpdate = plainActivityRepository.findAll().size();

        // Update the plainActivity
        PlainActivity updatedPlainActivity = plainActivityRepository.findById(plainActivity.getId()).get();
        // Disconnect from session so that the updates on updatedPlainActivity are not directly saved in db
        em.detach(updatedPlainActivity);
        updatedPlainActivity
            .requirements(UPDATED_REQUIREMENTS)
            .plainActivityStatus(UPDATED_PLAIN_ACTIVITY_STATUS)
            .automatic(UPDATED_AUTOMATIC);
        PlainActivityDTO plainActivityDTO = plainActivityMapper.toDto(updatedPlainActivity);

        restPlainActivityMockMvc.perform(put("/api/plain-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plainActivityDTO)))
            .andExpect(status().isOk());

        // Validate the PlainActivity in the database
        List<PlainActivity> plainActivityList = plainActivityRepository.findAll();
        assertThat(plainActivityList).hasSize(databaseSizeBeforeUpdate);
        PlainActivity testPlainActivity = plainActivityList.get(plainActivityList.size() - 1);
        assertThat(testPlainActivity.getRequirements()).isEqualTo(UPDATED_REQUIREMENTS);
        assertThat(testPlainActivity.getPlainActivityStatus()).isEqualTo(UPDATED_PLAIN_ACTIVITY_STATUS);
        assertThat(testPlainActivity.isAutomatic()).isEqualTo(UPDATED_AUTOMATIC);
    }

    @Test
    @Transactional
    public void updateNonExistingPlainActivity() throws Exception {
        int databaseSizeBeforeUpdate = plainActivityRepository.findAll().size();

        // Create the PlainActivity
        PlainActivityDTO plainActivityDTO = plainActivityMapper.toDto(plainActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlainActivityMockMvc.perform(put("/api/plain-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plainActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlainActivity in the database
        List<PlainActivity> plainActivityList = plainActivityRepository.findAll();
        assertThat(plainActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlainActivity() throws Exception {
        // Initialize the database
        plainActivityRepository.saveAndFlush(plainActivity);

        int databaseSizeBeforeDelete = plainActivityRepository.findAll().size();

        // Delete the plainActivity
        restPlainActivityMockMvc.perform(delete("/api/plain-activities/{id}", plainActivity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PlainActivity> plainActivityList = plainActivityRepository.findAll();
        assertThat(plainActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlainActivity.class);
        PlainActivity plainActivity1 = new PlainActivity();
        plainActivity1.setId(1L);
        PlainActivity plainActivity2 = new PlainActivity();
        plainActivity2.setId(plainActivity1.getId());
        assertThat(plainActivity1).isEqualTo(plainActivity2);
        plainActivity2.setId(2L);
        assertThat(plainActivity1).isNotEqualTo(plainActivity2);
        plainActivity1.setId(null);
        assertThat(plainActivity1).isNotEqualTo(plainActivity2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlainActivityDTO.class);
        PlainActivityDTO plainActivityDTO1 = new PlainActivityDTO();
        plainActivityDTO1.setId(1L);
        PlainActivityDTO plainActivityDTO2 = new PlainActivityDTO();
        assertThat(plainActivityDTO1).isNotEqualTo(plainActivityDTO2);
        plainActivityDTO2.setId(plainActivityDTO1.getId());
        assertThat(plainActivityDTO1).isEqualTo(plainActivityDTO2);
        plainActivityDTO2.setId(2L);
        assertThat(plainActivityDTO1).isNotEqualTo(plainActivityDTO2);
        plainActivityDTO1.setId(null);
        assertThat(plainActivityDTO1).isNotEqualTo(plainActivityDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(plainActivityMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(plainActivityMapper.fromId(null)).isNull();
    }
}
