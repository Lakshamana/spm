package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ActivityEstimation;
import br.ufpa.labes.spm.repository.ActivityEstimationRepository;
import br.ufpa.labes.spm.service.ActivityEstimationService;
import br.ufpa.labes.spm.service.dto.ActivityEstimationDTO;
import br.ufpa.labes.spm.service.mapper.ActivityEstimationMapper;
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
 * Integration tests for the {@link ActivityEstimationResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ActivityEstimationResourceIT {

    @Autowired
    private ActivityEstimationRepository activityEstimationRepository;

    @Autowired
    private ActivityEstimationMapper activityEstimationMapper;

    @Autowired
    private ActivityEstimationService activityEstimationService;

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

    private MockMvc restActivityEstimationMockMvc;

    private ActivityEstimation activityEstimation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActivityEstimationResource activityEstimationResource = new ActivityEstimationResource(activityEstimationService);
        this.restActivityEstimationMockMvc = MockMvcBuilders.standaloneSetup(activityEstimationResource)
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
    public static ActivityEstimation createEntity(EntityManager em) {
        ActivityEstimation activityEstimation = new ActivityEstimation();
        return activityEstimation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivityEstimation createUpdatedEntity(EntityManager em) {
        ActivityEstimation activityEstimation = new ActivityEstimation();
        return activityEstimation;
    }

    @BeforeEach
    public void initTest() {
        activityEstimation = createEntity(em);
    }

    @Test
    @Transactional
    public void createActivityEstimation() throws Exception {
        int databaseSizeBeforeCreate = activityEstimationRepository.findAll().size();

        // Create the ActivityEstimation
        ActivityEstimationDTO activityEstimationDTO = activityEstimationMapper.toDto(activityEstimation);
        restActivityEstimationMockMvc.perform(post("/api/activity-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityEstimationDTO)))
            .andExpect(status().isCreated());

        // Validate the ActivityEstimation in the database
        List<ActivityEstimation> activityEstimationList = activityEstimationRepository.findAll();
        assertThat(activityEstimationList).hasSize(databaseSizeBeforeCreate + 1);
        ActivityEstimation testActivityEstimation = activityEstimationList.get(activityEstimationList.size() - 1);
    }

    @Test
    @Transactional
    public void createActivityEstimationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activityEstimationRepository.findAll().size();

        // Create the ActivityEstimation with an existing ID
        activityEstimation.setId(1L);
        ActivityEstimationDTO activityEstimationDTO = activityEstimationMapper.toDto(activityEstimation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityEstimationMockMvc.perform(post("/api/activity-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityEstimationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityEstimation in the database
        List<ActivityEstimation> activityEstimationList = activityEstimationRepository.findAll();
        assertThat(activityEstimationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllActivityEstimations() throws Exception {
        // Initialize the database
        activityEstimationRepository.saveAndFlush(activityEstimation);

        // Get all the activityEstimationList
        restActivityEstimationMockMvc.perform(get("/api/activity-estimations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityEstimation.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getActivityEstimation() throws Exception {
        // Initialize the database
        activityEstimationRepository.saveAndFlush(activityEstimation);

        // Get the activityEstimation
        restActivityEstimationMockMvc.perform(get("/api/activity-estimations/{id}", activityEstimation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(activityEstimation.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingActivityEstimation() throws Exception {
        // Get the activityEstimation
        restActivityEstimationMockMvc.perform(get("/api/activity-estimations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivityEstimation() throws Exception {
        // Initialize the database
        activityEstimationRepository.saveAndFlush(activityEstimation);

        int databaseSizeBeforeUpdate = activityEstimationRepository.findAll().size();

        // Update the activityEstimation
        ActivityEstimation updatedActivityEstimation = activityEstimationRepository.findById(activityEstimation.getId()).get();
        // Disconnect from session so that the updates on updatedActivityEstimation are not directly saved in db
        em.detach(updatedActivityEstimation);
        ActivityEstimationDTO activityEstimationDTO = activityEstimationMapper.toDto(updatedActivityEstimation);

        restActivityEstimationMockMvc.perform(put("/api/activity-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityEstimationDTO)))
            .andExpect(status().isOk());

        // Validate the ActivityEstimation in the database
        List<ActivityEstimation> activityEstimationList = activityEstimationRepository.findAll();
        assertThat(activityEstimationList).hasSize(databaseSizeBeforeUpdate);
        ActivityEstimation testActivityEstimation = activityEstimationList.get(activityEstimationList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingActivityEstimation() throws Exception {
        int databaseSizeBeforeUpdate = activityEstimationRepository.findAll().size();

        // Create the ActivityEstimation
        ActivityEstimationDTO activityEstimationDTO = activityEstimationMapper.toDto(activityEstimation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityEstimationMockMvc.perform(put("/api/activity-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityEstimationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityEstimation in the database
        List<ActivityEstimation> activityEstimationList = activityEstimationRepository.findAll();
        assertThat(activityEstimationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActivityEstimation() throws Exception {
        // Initialize the database
        activityEstimationRepository.saveAndFlush(activityEstimation);

        int databaseSizeBeforeDelete = activityEstimationRepository.findAll().size();

        // Delete the activityEstimation
        restActivityEstimationMockMvc.perform(delete("/api/activity-estimations/{id}", activityEstimation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ActivityEstimation> activityEstimationList = activityEstimationRepository.findAll();
        assertThat(activityEstimationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityEstimation.class);
        ActivityEstimation activityEstimation1 = new ActivityEstimation();
        activityEstimation1.setId(1L);
        ActivityEstimation activityEstimation2 = new ActivityEstimation();
        activityEstimation2.setId(activityEstimation1.getId());
        assertThat(activityEstimation1).isEqualTo(activityEstimation2);
        activityEstimation2.setId(2L);
        assertThat(activityEstimation1).isNotEqualTo(activityEstimation2);
        activityEstimation1.setId(null);
        assertThat(activityEstimation1).isNotEqualTo(activityEstimation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityEstimationDTO.class);
        ActivityEstimationDTO activityEstimationDTO1 = new ActivityEstimationDTO();
        activityEstimationDTO1.setId(1L);
        ActivityEstimationDTO activityEstimationDTO2 = new ActivityEstimationDTO();
        assertThat(activityEstimationDTO1).isNotEqualTo(activityEstimationDTO2);
        activityEstimationDTO2.setId(activityEstimationDTO1.getId());
        assertThat(activityEstimationDTO1).isEqualTo(activityEstimationDTO2);
        activityEstimationDTO2.setId(2L);
        assertThat(activityEstimationDTO1).isNotEqualTo(activityEstimationDTO2);
        activityEstimationDTO1.setId(null);
        assertThat(activityEstimationDTO1).isNotEqualTo(activityEstimationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(activityEstimationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(activityEstimationMapper.fromId(null)).isNull();
    }
}
