package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.BranchConCondToActivity;
import br.ufpa.labes.spm.repository.BranchConCondToActivityRepository;
import br.ufpa.labes.spm.service.BranchConCondToActivityService;
import br.ufpa.labes.spm.service.dto.BranchConCondToActivityDTO;
import br.ufpa.labes.spm.service.mapper.BranchConCondToActivityMapper;
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
 * Integration tests for the {@link BranchConCondToActivityResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class BranchConCondToActivityResourceIT {

    @Autowired
    private BranchConCondToActivityRepository branchCondToActivityRepository;

    @Autowired
    private BranchConCondToActivityMapper branchCondToActivityMapper;

    @Autowired
    private BranchConCondToActivityService branchCondToActivityService;

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

    private MockMvc restBranchConCondToActivityMockMvc;

    private BranchConCondToActivity branchCondToActivity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BranchConCondToActivityResource branchCondToActivityResource = new BranchConCondToActivityResource(branchCondToActivityService);
        this.restBranchConCondToActivityMockMvc = MockMvcBuilders.standaloneSetup(branchCondToActivityResource)
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
    public static BranchConCondToActivity createEntity(EntityManager em) {
        BranchConCondToActivity branchCondToActivity = new BranchConCondToActivity();
        return branchCondToActivity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BranchConCondToActivity createUpdatedEntity(EntityManager em) {
        BranchConCondToActivity branchCondToActivity = new BranchConCondToActivity();
        return branchCondToActivity;
    }

    @BeforeEach
    public void initTest() {
        branchCondToActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createBranchConCondToActivity() throws Exception {
        int databaseSizeBeforeCreate = branchCondToActivityRepository.findAll().size();

        // Create the BranchConCondToActivity
        BranchConCondToActivityDTO branchCondToActivityDTO = branchCondToActivityMapper.toDto(branchCondToActivity);
        restBranchConCondToActivityMockMvc.perform(post("/api/branch-cond-to-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchCondToActivityDTO)))
            .andExpect(status().isCreated());

        // Validate the BranchConCondToActivity in the database
        List<BranchConCondToActivity> branchCondToActivityList = branchCondToActivityRepository.findAll();
        assertThat(branchCondToActivityList).hasSize(databaseSizeBeforeCreate + 1);
        BranchConCondToActivity testBranchConCondToActivity = branchCondToActivityList.get(branchCondToActivityList.size() - 1);
    }

    @Test
    @Transactional
    public void createBranchConCondToActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = branchCondToActivityRepository.findAll().size();

        // Create the BranchConCondToActivity with an existing ID
        branchCondToActivity.setId(1L);
        BranchConCondToActivityDTO branchCondToActivityDTO = branchCondToActivityMapper.toDto(branchCondToActivity);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchConCondToActivityMockMvc.perform(post("/api/branch-cond-to-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchCondToActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BranchConCondToActivity in the database
        List<BranchConCondToActivity> branchCondToActivityList = branchCondToActivityRepository.findAll();
        assertThat(branchCondToActivityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBranchConCondToActivities() throws Exception {
        // Initialize the database
        branchCondToActivityRepository.saveAndFlush(branchCondToActivity);

        // Get all the branchCondToActivityList
        restBranchConCondToActivityMockMvc.perform(get("/api/branch-cond-to-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branchCondToActivity.getId().intValue())));
    }

    @Test
    @Transactional
    public void getBranchConCondToActivity() throws Exception {
        // Initialize the database
        branchCondToActivityRepository.saveAndFlush(branchCondToActivity);

        // Get the branchCondToActivity
        restBranchConCondToActivityMockMvc.perform(get("/api/branch-cond-to-activities/{id}", branchCondToActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(branchCondToActivity.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBranchConCondToActivity() throws Exception {
        // Get the branchCondToActivity
        restBranchConCondToActivityMockMvc.perform(get("/api/branch-cond-to-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBranchConCondToActivity() throws Exception {
        // Initialize the database
        branchCondToActivityRepository.saveAndFlush(branchCondToActivity);

        int databaseSizeBeforeUpdate = branchCondToActivityRepository.findAll().size();

        // Update the branchCondToActivity
        BranchConCondToActivity updatedBranchConCondToActivity = branchCondToActivityRepository.findById(branchCondToActivity.getId()).get();
        // Disconnect from session so that the updates on updatedBranchConCondToActivity are not directly saved in db
        em.detach(updatedBranchConCondToActivity);
        BranchConCondToActivityDTO branchCondToActivityDTO = branchCondToActivityMapper.toDto(updatedBranchConCondToActivity);

        restBranchConCondToActivityMockMvc.perform(put("/api/branch-cond-to-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchCondToActivityDTO)))
            .andExpect(status().isOk());

        // Validate the BranchConCondToActivity in the database
        List<BranchConCondToActivity> branchCondToActivityList = branchCondToActivityRepository.findAll();
        assertThat(branchCondToActivityList).hasSize(databaseSizeBeforeUpdate);
        BranchConCondToActivity testBranchConCondToActivity = branchCondToActivityList.get(branchCondToActivityList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingBranchConCondToActivity() throws Exception {
        int databaseSizeBeforeUpdate = branchCondToActivityRepository.findAll().size();

        // Create the BranchConCondToActivity
        BranchConCondToActivityDTO branchCondToActivityDTO = branchCondToActivityMapper.toDto(branchCondToActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchConCondToActivityMockMvc.perform(put("/api/branch-cond-to-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchCondToActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BranchConCondToActivity in the database
        List<BranchConCondToActivity> branchCondToActivityList = branchCondToActivityRepository.findAll();
        assertThat(branchCondToActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBranchConCondToActivity() throws Exception {
        // Initialize the database
        branchCondToActivityRepository.saveAndFlush(branchCondToActivity);

        int databaseSizeBeforeDelete = branchCondToActivityRepository.findAll().size();

        // Delete the branchCondToActivity
        restBranchConCondToActivityMockMvc.perform(delete("/api/branch-cond-to-activities/{id}", branchCondToActivity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BranchConCondToActivity> branchCondToActivityList = branchCondToActivityRepository.findAll();
        assertThat(branchCondToActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchConCondToActivity.class);
        BranchConCondToActivity branchCondToActivity1 = new BranchConCondToActivity();
        branchCondToActivity1.setId(1L);
        BranchConCondToActivity branchCondToActivity2 = new BranchConCondToActivity();
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
        TestUtil.equalsVerifier(BranchConCondToActivityDTO.class);
        BranchConCondToActivityDTO branchCondToActivityDTO1 = new BranchConCondToActivityDTO();
        branchCondToActivityDTO1.setId(1L);
        BranchConCondToActivityDTO branchCondToActivityDTO2 = new BranchConCondToActivityDTO();
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
