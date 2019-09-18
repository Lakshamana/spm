package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ActivityMetric;
import br.ufpa.labes.spm.repository.ActivityMetricRepository;
import br.ufpa.labes.spm.service.ActivityMetricService;
import br.ufpa.labes.spm.service.dto.ActivityMetricDTO;
import br.ufpa.labes.spm.service.mapper.ActivityMetricMapper;
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
 * Integration tests for the {@link ActivityMetricResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ActivityMetricResourceIT {

    @Autowired
    private ActivityMetricRepository activityMetricRepository;

    @Autowired
    private ActivityMetricMapper activityMetricMapper;

    @Autowired
    private ActivityMetricService activityMetricService;

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

    private MockMvc restActivityMetricMockMvc;

    private ActivityMetric activityMetric;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ActivityMetricResource activityMetricResource = new ActivityMetricResource(activityMetricService);
        this.restActivityMetricMockMvc = MockMvcBuilders.standaloneSetup(activityMetricResource)
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
    public static ActivityMetric createEntity(EntityManager em) {
        ActivityMetric activityMetric = new ActivityMetric();
        return activityMetric;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivityMetric createUpdatedEntity(EntityManager em) {
        ActivityMetric activityMetric = new ActivityMetric();
        return activityMetric;
    }

    @BeforeEach
    public void initTest() {
        activityMetric = createEntity(em);
    }

    @Test
    @Transactional
    public void createActivityMetric() throws Exception {
        int databaseSizeBeforeCreate = activityMetricRepository.findAll().size();

        // Create the ActivityMetric
        ActivityMetricDTO activityMetricDTO = activityMetricMapper.toDto(activityMetric);
        restActivityMetricMockMvc.perform(post("/api/activity-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityMetricDTO)))
            .andExpect(status().isCreated());

        // Validate the ActivityMetric in the database
        List<ActivityMetric> activityMetricList = activityMetricRepository.findAll();
        assertThat(activityMetricList).hasSize(databaseSizeBeforeCreate + 1);
        ActivityMetric testActivityMetric = activityMetricList.get(activityMetricList.size() - 1);
    }

    @Test
    @Transactional
    public void createActivityMetricWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = activityMetricRepository.findAll().size();

        // Create the ActivityMetric with an existing ID
        activityMetric.setId(1L);
        ActivityMetricDTO activityMetricDTO = activityMetricMapper.toDto(activityMetric);

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivityMetricMockMvc.perform(post("/api/activity-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityMetricDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityMetric in the database
        List<ActivityMetric> activityMetricList = activityMetricRepository.findAll();
        assertThat(activityMetricList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllActivityMetrics() throws Exception {
        // Initialize the database
        activityMetricRepository.saveAndFlush(activityMetric);

        // Get all the activityMetricList
        restActivityMetricMockMvc.perform(get("/api/activity-metrics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activityMetric.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getActivityMetric() throws Exception {
        // Initialize the database
        activityMetricRepository.saveAndFlush(activityMetric);

        // Get the activityMetric
        restActivityMetricMockMvc.perform(get("/api/activity-metrics/{id}", activityMetric.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(activityMetric.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingActivityMetric() throws Exception {
        // Get the activityMetric
        restActivityMetricMockMvc.perform(get("/api/activity-metrics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivityMetric() throws Exception {
        // Initialize the database
        activityMetricRepository.saveAndFlush(activityMetric);

        int databaseSizeBeforeUpdate = activityMetricRepository.findAll().size();

        // Update the activityMetric
        ActivityMetric updatedActivityMetric = activityMetricRepository.findById(activityMetric.getId()).get();
        // Disconnect from session so that the updates on updatedActivityMetric are not directly saved in db
        em.detach(updatedActivityMetric);
        ActivityMetricDTO activityMetricDTO = activityMetricMapper.toDto(updatedActivityMetric);

        restActivityMetricMockMvc.perform(put("/api/activity-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityMetricDTO)))
            .andExpect(status().isOk());

        // Validate the ActivityMetric in the database
        List<ActivityMetric> activityMetricList = activityMetricRepository.findAll();
        assertThat(activityMetricList).hasSize(databaseSizeBeforeUpdate);
        ActivityMetric testActivityMetric = activityMetricList.get(activityMetricList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingActivityMetric() throws Exception {
        int databaseSizeBeforeUpdate = activityMetricRepository.findAll().size();

        // Create the ActivityMetric
        ActivityMetricDTO activityMetricDTO = activityMetricMapper.toDto(activityMetric);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivityMetricMockMvc.perform(put("/api/activity-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(activityMetricDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ActivityMetric in the database
        List<ActivityMetric> activityMetricList = activityMetricRepository.findAll();
        assertThat(activityMetricList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteActivityMetric() throws Exception {
        // Initialize the database
        activityMetricRepository.saveAndFlush(activityMetric);

        int databaseSizeBeforeDelete = activityMetricRepository.findAll().size();

        // Delete the activityMetric
        restActivityMetricMockMvc.perform(delete("/api/activity-metrics/{id}", activityMetric.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ActivityMetric> activityMetricList = activityMetricRepository.findAll();
        assertThat(activityMetricList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityMetric.class);
        ActivityMetric activityMetric1 = new ActivityMetric();
        activityMetric1.setId(1L);
        ActivityMetric activityMetric2 = new ActivityMetric();
        activityMetric2.setId(activityMetric1.getId());
        assertThat(activityMetric1).isEqualTo(activityMetric2);
        activityMetric2.setId(2L);
        assertThat(activityMetric1).isNotEqualTo(activityMetric2);
        activityMetric1.setId(null);
        assertThat(activityMetric1).isNotEqualTo(activityMetric2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivityMetricDTO.class);
        ActivityMetricDTO activityMetricDTO1 = new ActivityMetricDTO();
        activityMetricDTO1.setId(1L);
        ActivityMetricDTO activityMetricDTO2 = new ActivityMetricDTO();
        assertThat(activityMetricDTO1).isNotEqualTo(activityMetricDTO2);
        activityMetricDTO2.setId(activityMetricDTO1.getId());
        assertThat(activityMetricDTO1).isEqualTo(activityMetricDTO2);
        activityMetricDTO2.setId(2L);
        assertThat(activityMetricDTO1).isNotEqualTo(activityMetricDTO2);
        activityMetricDTO1.setId(null);
        assertThat(activityMetricDTO1).isNotEqualTo(activityMetricDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(activityMetricMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(activityMetricMapper.fromId(null)).isNull();
    }
}
