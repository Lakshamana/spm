package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.BranchCondToActivity;
import br.ufpa.labes.spm.repository.BranchCondToActivityRepository;
import br.ufpa.labes.spm.service.BranchCondToActivityService;
import br.ufpa.labes.spm.service.dto.BranchCondToActivityDTO;
import br.ufpa.labes.spm.service.mapper.BranchCondToActivityMapper;
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
 * Integration tests for the {@link BranchCondToActivityResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class BranchCondToActivityResourceIT {

    @Autowired
    private BranchCondToActivityRepository branchCondToActivityRepository;

    @Autowired
    private BranchCondToActivityMapper branchCondToActivityMapper;

    @Autowired
    private BranchCondToActivityService branchCondToActivityService;

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

    private MockMvc restBranchCondToActivityMockMvc;

    private BranchCondToActivity branchCondToActivity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BranchCondToActivityResource branchCondToActivityResource = new BranchCondToActivityResource(branchCondToActivityService);
        this.restBranchCondToActivityMockMvc = MockMvcBuilders.standaloneSetup(branchCondToActivityResource)
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
    public static BranchCondToActivity createEntity(EntityManager em) {
        BranchCondToActivity branchCondToActivity = new BranchCondToActivity();
        return branchCondToActivity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BranchCondToActivity createUpdatedEntity(EntityManager em) {
        BranchCondToActivity branchCondToActivity = new BranchCondToActivity();
        return branchCondToActivity;
    }

    @BeforeEach
    public void initTest() {
        branchCondToActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createBranchCondToActivity() throws Exception {
        int databaseSizeBeforeCreate = branchCondToActivityRepository.findAll().size();

        // Create the BranchCondToActivity
        BranchCondToActivityDTO branchCondToActivityDTO = branchCondToActivityMapper.toDto(branchCondToActivity);
        restBranchCondToActivityMockMvc.perform(post("/api/branch-cond-to-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchCondToActivityDTO)))
            .andExpect(status().isCreated());

        // Validate the BranchCondToActivity in the database
        List<BranchCondToActivity> branchCondToActivityList = branchCondToActivityRepository.findAll();
        assertThat(branchCondToActivityList).hasSize(databaseSizeBeforeCreate + 1);
        BranchCondToActivity testBranchCondToActivity = branchCondToActivityList.get(branchCondToActivityList.size() - 1);
    }

    @Test
    @Transactional
    public void createBranchCondToActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = branchCondToActivityRepository.findAll().size();

        // Create the BranchCondToActivity with an existing ID
        branchCondToActivity.setId(1L);
        BranchCondToActivityDTO branchCondToActivityDTO = branchCondToActivityMapper.toDto(branchCondToActivity);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchCondToActivityMockMvc.perform(post("/api/branch-cond-to-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchCondToActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BranchCondToActivity in the database
        List<BranchCondToActivity> branchCondToActivityList = branchCondToActivityRepository.findAll();
        assertThat(branchCondToActivityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBranchCondToActivities() throws Exception {
        // Initialize the database
        branchCondToActivityRepository.saveAndFlush(branchCondToActivity);

        // Get all the branchCondToActivityList
        restBranchCondToActivityMockMvc.perform(get("/api/branch-cond-to-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branchCondToActivity.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getBranchCondToActivity() throws Exception {
        // Initialize the database
        branchCondToActivityRepository.saveAndFlush(branchCondToActivity);

        // Get the branchCondToActivity
        restBranchCondToActivityMockMvc.perform(get("/api/branch-cond-to-activities/{id}", branchCondToActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(branchCondToActivity.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBranchCondToActivity() throws Exception {
        // Get the branchCondToActivity
        restBranchCondToActivityMockMvc.perform(get("/api/branch-cond-to-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBranchCondToActivity() throws Exception {
        // Initialize the database
        branchCondToActivityRepository.saveAndFlush(branchCondToActivity);

        int databaseSizeBeforeUpdate = branchCondToActivityRepository.findAll().size();

        // Update the branchCondToActivity
        BranchCondToActivity updatedBranchCondToActivity = branchCondToActivityRepository.findById(branchCondToActivity.getId()).get();
        // Disconnect from session so that the updates on updatedBranchCondToActivity are not directly saved in db
        em.detach(updatedBranchCondToActivity);
        BranchCondToActivityDTO branchCondToActivityDTO = branchCondToActivityMapper.toDto(updatedBranchCondToActivity);

        restBranchCondToActivityMockMvc.perform(put("/api/branch-cond-to-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchCondToActivityDTO)))
            .andExpect(status().isOk());

        // Validate the BranchCondToActivity in the database
        List<BranchCondToActivity> branchCondToActivityList = branchCondToActivityRepository.findAll();
        assertThat(branchCondToActivityList).hasSize(databaseSizeBeforeUpdate);
        BranchCondToActivity testBranchCondToActivity = branchCondToActivityList.get(branchCondToActivityList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingBranchCondToActivity() throws Exception {
        int databaseSizeBeforeUpdate = branchCondToActivityRepository.findAll().size();

        // Create the BranchCondToActivity
        BranchCondToActivityDTO branchCondToActivityDTO = branchCondToActivityMapper.toDto(branchCondToActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchCondToActivityMockMvc.perform(put("/api/branch-cond-to-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchCondToActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BranchCondToActivity in the database
        List<BranchCondToActivity> branchCondToActivityList = branchCondToActivityRepository.findAll();
        assertThat(branchCondToActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBranchCondToActivity() throws Exception {
        // Initialize the database
        branchCondToActivityRepository.saveAndFlush(branchCondToActivity);

        int databaseSizeBeforeDelete = branchCondToActivityRepository.findAll().size();

        // Delete the branchCondToActivity
        restBranchCondToActivityMockMvc.perform(delete("/api/branch-cond-to-activities/{id}", branchCondToActivity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BranchCondToActivity> branchCondToActivityList = branchCondToActivityRepository.findAll();
        assertThat(branchCondToActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchCondToActivity.class);
        BranchCondToActivity branchCondToActivity1 = new BranchCondToActivity();
        branchCondToActivity1.setId(1L);
        BranchCondToActivity branchCondToActivity2 = new BranchCondToActivity();
        branchCondToActivity2.setId(branchCondToActivity1.getId());
        assertThat(branchCondToActivity1).isEqualTo(branchCondToActivity2);
        branchCondToActivity2.setId(2L);
        assertThat(branchCondToActivity1).isNotEqualTo(branchCondToActivity2);
        branchCondToActivity1.setId(null);
        assertThat(branchCondToActivity1).isNotEqualTo(branchCondToActivity2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchCondToActivityDTO.class);
        BranchCondToActivityDTO branchCondToActivityDTO1 = new BranchCondToActivityDTO();
        branchCondToActivityDTO1.setId(1L);
        BranchCondToActivityDTO branchCondToActivityDTO2 = new BranchCondToActivityDTO();
        assertThat(branchCondToActivityDTO1).isNotEqualTo(branchCondToActivityDTO2);
        branchCondToActivityDTO2.setId(branchCondToActivityDTO1.getId());
        assertThat(branchCondToActivityDTO1).isEqualTo(branchCondToActivityDTO2);
        branchCondToActivityDTO2.setId(2L);
        assertThat(branchCondToActivityDTO1).isNotEqualTo(branchCondToActivityDTO2);
        branchCondToActivityDTO1.setId(null);
        assertThat(branchCondToActivityDTO1).isNotEqualTo(branchCondToActivityDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(branchCondToActivityMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(branchCondToActivityMapper.fromId(null)).isNull();
    }
}
