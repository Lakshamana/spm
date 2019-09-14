package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.DecomposedActivity;
import br.ufpa.labes.spm.repository.DecomposedActivityRepository;
import br.ufpa.labes.spm.service.DecomposedActivityService;
import br.ufpa.labes.spm.service.dto.DecomposedActivityDTO;
import br.ufpa.labes.spm.service.mapper.DecomposedActivityMapper;
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
 * Integration tests for the {@link DecomposedActivityResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class DecomposedActivityResourceIT {

    @Autowired
    private DecomposedActivityRepository decomposedActivityRepository;

    @Autowired
    private DecomposedActivityMapper decomposedActivityMapper;

    @Autowired
    private DecomposedActivityService decomposedActivityService;

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

    private MockMvc restDecomposedActivityMockMvc;

    private DecomposedActivity decomposedActivity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DecomposedActivityResource decomposedActivityResource = new DecomposedActivityResource(decomposedActivityService);
        this.restDecomposedActivityMockMvc = MockMvcBuilders.standaloneSetup(decomposedActivityResource)
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
    public static DecomposedActivity createEntity(EntityManager em) {
        DecomposedActivity decomposedActivity = new DecomposedActivity();
        return decomposedActivity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DecomposedActivity createUpdatedEntity(EntityManager em) {
        DecomposedActivity decomposedActivity = new DecomposedActivity();
        return decomposedActivity;
    }

    @BeforeEach
    public void initTest() {
        decomposedActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createDecomposedActivity() throws Exception {
        int databaseSizeBeforeCreate = decomposedActivityRepository.findAll().size();

        // Create the DecomposedActivity
        DecomposedActivityDTO decomposedActivityDTO = decomposedActivityMapper.toDto(decomposedActivity);
        restDecomposedActivityMockMvc.perform(post("/api/decomposed-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(decomposedActivityDTO)))
            .andExpect(status().isCreated());

        // Validate the DecomposedActivity in the database
        List<DecomposedActivity> decomposedActivityList = decomposedActivityRepository.findAll();
        assertThat(decomposedActivityList).hasSize(databaseSizeBeforeCreate + 1);
        DecomposedActivity testDecomposedActivity = decomposedActivityList.get(decomposedActivityList.size() - 1);
    }

    @Test
    @Transactional
    public void createDecomposedActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = decomposedActivityRepository.findAll().size();

        // Create the DecomposedActivity with an existing ID
        decomposedActivity.setId(1L);
        DecomposedActivityDTO decomposedActivityDTO = decomposedActivityMapper.toDto(decomposedActivity);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDecomposedActivityMockMvc.perform(post("/api/decomposed-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(decomposedActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DecomposedActivity in the database
        List<DecomposedActivity> decomposedActivityList = decomposedActivityRepository.findAll();
        assertThat(decomposedActivityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDecomposedActivities() throws Exception {
        // Initialize the database
        decomposedActivityRepository.saveAndFlush(decomposedActivity);

        // Get all the decomposedActivityList
        restDecomposedActivityMockMvc.perform(get("/api/decomposed-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decomposedActivity.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getDecomposedActivity() throws Exception {
        // Initialize the database
        decomposedActivityRepository.saveAndFlush(decomposedActivity);

        // Get the decomposedActivity
        restDecomposedActivityMockMvc.perform(get("/api/decomposed-activities/{id}", decomposedActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(decomposedActivity.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDecomposedActivity() throws Exception {
        // Get the decomposedActivity
        restDecomposedActivityMockMvc.perform(get("/api/decomposed-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDecomposedActivity() throws Exception {
        // Initialize the database
        decomposedActivityRepository.saveAndFlush(decomposedActivity);

        int databaseSizeBeforeUpdate = decomposedActivityRepository.findAll().size();

        // Update the decomposedActivity
        DecomposedActivity updatedDecomposedActivity = decomposedActivityRepository.findById(decomposedActivity.getId()).get();
        // Disconnect from session so that the updates on updatedDecomposedActivity are not directly saved in db
        em.detach(updatedDecomposedActivity);
        DecomposedActivityDTO decomposedActivityDTO = decomposedActivityMapper.toDto(updatedDecomposedActivity);

        restDecomposedActivityMockMvc.perform(put("/api/decomposed-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(decomposedActivityDTO)))
            .andExpect(status().isOk());

        // Validate the DecomposedActivity in the database
        List<DecomposedActivity> decomposedActivityList = decomposedActivityRepository.findAll();
        assertThat(decomposedActivityList).hasSize(databaseSizeBeforeUpdate);
        DecomposedActivity testDecomposedActivity = decomposedActivityList.get(decomposedActivityList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingDecomposedActivity() throws Exception {
        int databaseSizeBeforeUpdate = decomposedActivityRepository.findAll().size();

        // Create the DecomposedActivity
        DecomposedActivityDTO decomposedActivityDTO = decomposedActivityMapper.toDto(decomposedActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDecomposedActivityMockMvc.perform(put("/api/decomposed-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(decomposedActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DecomposedActivity in the database
        List<DecomposedActivity> decomposedActivityList = decomposedActivityRepository.findAll();
        assertThat(decomposedActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDecomposedActivity() throws Exception {
        // Initialize the database
        decomposedActivityRepository.saveAndFlush(decomposedActivity);

        int databaseSizeBeforeDelete = decomposedActivityRepository.findAll().size();

        // Delete the decomposedActivity
        restDecomposedActivityMockMvc.perform(delete("/api/decomposed-activities/{id}", decomposedActivity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DecomposedActivity> decomposedActivityList = decomposedActivityRepository.findAll();
        assertThat(decomposedActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DecomposedActivity.class);
        DecomposedActivity decomposedActivity1 = new DecomposedActivity();
        decomposedActivity1.setId(1L);
        DecomposedActivity decomposedActivity2 = new DecomposedActivity();
        decomposedActivity2.setId(decomposedActivity1.getId());
        assertThat(decomposedActivity1).isEqualTo(decomposedActivity2);
        decomposedActivity2.setId(2L);
        assertThat(decomposedActivity1).isNotEqualTo(decomposedActivity2);
        decomposedActivity1.setId(null);
        assertThat(decomposedActivity1).isNotEqualTo(decomposedActivity2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DecomposedActivityDTO.class);
        DecomposedActivityDTO decomposedActivityDTO1 = new DecomposedActivityDTO();
        decomposedActivityDTO1.setId(1L);
        DecomposedActivityDTO decomposedActivityDTO2 = new DecomposedActivityDTO();
        assertThat(decomposedActivityDTO1).isNotEqualTo(decomposedActivityDTO2);
        decomposedActivityDTO2.setId(decomposedActivityDTO1.getId());
        assertThat(decomposedActivityDTO1).isEqualTo(decomposedActivityDTO2);
        decomposedActivityDTO2.setId(2L);
        assertThat(decomposedActivityDTO1).isNotEqualTo(decomposedActivityDTO2);
        decomposedActivityDTO1.setId(null);
        assertThat(decomposedActivityDTO1).isNotEqualTo(decomposedActivityDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(decomposedActivityMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(decomposedActivityMapper.fromId(null)).isNull();
    }
}
