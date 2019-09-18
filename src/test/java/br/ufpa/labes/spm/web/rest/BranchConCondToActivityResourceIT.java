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
    private BranchConCondToActivityRepository branchConCondToActivityRepository;

    @Autowired
    private BranchConCondToActivityMapper branchConCondToActivityMapper;

    @Autowired
    private BranchConCondToActivityService branchConCondToActivityService;

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

    private BranchConCondToActivity branchConCondToActivity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BranchConCondToActivityResource branchConCondToActivityResource = new BranchConCondToActivityResource(branchConCondToActivityService);
        this.restBranchConCondToActivityMockMvc = MockMvcBuilders.standaloneSetup(branchConCondToActivityResource)
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
        BranchConCondToActivity branchConCondToActivity = new BranchConCondToActivity();
        return branchConCondToActivity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BranchConCondToActivity createUpdatedEntity(EntityManager em) {
        BranchConCondToActivity branchConCondToActivity = new BranchConCondToActivity();
        return branchConCondToActivity;
    }

    @BeforeEach
    public void initTest() {
        branchConCondToActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createBranchConCondToActivity() throws Exception {
        int databaseSizeBeforeCreate = branchConCondToActivityRepository.findAll().size();

        // Create the BranchConCondToActivity
        BranchConCondToActivityDTO branchConCondToActivityDTO = branchConCondToActivityMapper.toDto(branchConCondToActivity);
        restBranchConCondToActivityMockMvc.perform(post("/api/branch-con-cond-to-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchConCondToActivityDTO)))
            .andExpect(status().isCreated());

        // Validate the BranchConCondToActivity in the database
        List<BranchConCondToActivity> branchConCondToActivityList = branchConCondToActivityRepository.findAll();
        assertThat(branchConCondToActivityList).hasSize(databaseSizeBeforeCreate + 1);
        BranchConCondToActivity testBranchConCondToActivity = branchConCondToActivityList.get(branchConCondToActivityList.size() - 1);
    }

    @Test
    @Transactional
    public void createBranchConCondToActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = branchConCondToActivityRepository.findAll().size();

        // Create the BranchConCondToActivity with an existing ID
        branchConCondToActivity.setId(1L);
        BranchConCondToActivityDTO branchConCondToActivityDTO = branchConCondToActivityMapper.toDto(branchConCondToActivity);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchConCondToActivityMockMvc.perform(post("/api/branch-con-cond-to-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchConCondToActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BranchConCondToActivity in the database
        List<BranchConCondToActivity> branchConCondToActivityList = branchConCondToActivityRepository.findAll();
        assertThat(branchConCondToActivityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBranchConCondToActivities() throws Exception {
        // Initialize the database
        branchConCondToActivityRepository.saveAndFlush(branchConCondToActivity);

        // Get all the branchConCondToActivityList
        restBranchConCondToActivityMockMvc.perform(get("/api/branch-con-cond-to-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branchConCondToActivity.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getBranchConCondToActivity() throws Exception {
        // Initialize the database
        branchConCondToActivityRepository.saveAndFlush(branchConCondToActivity);

        // Get the branchConCondToActivity
        restBranchConCondToActivityMockMvc.perform(get("/api/branch-con-cond-to-activities/{id}", branchConCondToActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(branchConCondToActivity.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBranchConCondToActivity() throws Exception {
        // Get the branchConCondToActivity
        restBranchConCondToActivityMockMvc.perform(get("/api/branch-con-cond-to-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBranchConCondToActivity() throws Exception {
        // Initialize the database
        branchConCondToActivityRepository.saveAndFlush(branchConCondToActivity);

        int databaseSizeBeforeUpdate = branchConCondToActivityRepository.findAll().size();

        // Update the branchConCondToActivity
        BranchConCondToActivity updatedBranchConCondToActivity = branchConCondToActivityRepository.findById(branchConCondToActivity.getId()).get();
        // Disconnect from session so that the updates on updatedBranchConCondToActivity are not directly saved in db
        em.detach(updatedBranchConCondToActivity);
        BranchConCondToActivityDTO branchConCondToActivityDTO = branchConCondToActivityMapper.toDto(updatedBranchConCondToActivity);

        restBranchConCondToActivityMockMvc.perform(put("/api/branch-con-cond-to-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchConCondToActivityDTO)))
            .andExpect(status().isOk());

        // Validate the BranchConCondToActivity in the database
        List<BranchConCondToActivity> branchConCondToActivityList = branchConCondToActivityRepository.findAll();
        assertThat(branchConCondToActivityList).hasSize(databaseSizeBeforeUpdate);
        BranchConCondToActivity testBranchConCondToActivity = branchConCondToActivityList.get(branchConCondToActivityList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingBranchConCondToActivity() throws Exception {
        int databaseSizeBeforeUpdate = branchConCondToActivityRepository.findAll().size();

        // Create the BranchConCondToActivity
        BranchConCondToActivityDTO branchConCondToActivityDTO = branchConCondToActivityMapper.toDto(branchConCondToActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchConCondToActivityMockMvc.perform(put("/api/branch-con-cond-to-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchConCondToActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BranchConCondToActivity in the database
        List<BranchConCondToActivity> branchConCondToActivityList = branchConCondToActivityRepository.findAll();
        assertThat(branchConCondToActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBranchConCondToActivity() throws Exception {
        // Initialize the database
        branchConCondToActivityRepository.saveAndFlush(branchConCondToActivity);

        int databaseSizeBeforeDelete = branchConCondToActivityRepository.findAll().size();

        // Delete the branchConCondToActivity
        restBranchConCondToActivityMockMvc.perform(delete("/api/branch-con-cond-to-activities/{id}", branchConCondToActivity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BranchConCondToActivity> branchConCondToActivityList = branchConCondToActivityRepository.findAll();
        assertThat(branchConCondToActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchConCondToActivity.class);
        BranchConCondToActivity branchConCondToActivity1 = new BranchConCondToActivity();
        branchConCondToActivity1.setId(1L);
        BranchConCondToActivity branchConCondToActivity2 = new BranchConCondToActivity();
        branchConCondToActivity2.setId(branchConCondToActivity1.getId());
        assertThat(branchConCondToActivity1).isEqualTo(branchConCondToActivity2);
        branchConCondToActivity2.setId(2L);
        assertThat(branchConCondToActivity1).isNotEqualTo(branchConCondToActivity2);
        branchConCondToActivity1.setId(null);
        assertThat(branchConCondToActivity1).isNotEqualTo(branchConCondToActivity2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchConCondToActivityDTO.class);
        BranchConCondToActivityDTO branchConCondToActivityDTO1 = new BranchConCondToActivityDTO();
        branchConCondToActivityDTO1.setId(1L);
        BranchConCondToActivityDTO branchConCondToActivityDTO2 = new BranchConCondToActivityDTO();
        assertThat(branchConCondToActivityDTO1).isNotEqualTo(branchConCondToActivityDTO2);
        branchConCondToActivityDTO2.setId(branchConCondToActivityDTO1.getId());
        assertThat(branchConCondToActivityDTO1).isEqualTo(branchConCondToActivityDTO2);
        branchConCondToActivityDTO2.setId(2L);
        assertThat(branchConCondToActivityDTO1).isNotEqualTo(branchConCondToActivityDTO2);
        branchConCondToActivityDTO1.setId(null);
        assertThat(branchConCondToActivityDTO1).isNotEqualTo(branchConCondToActivityDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(branchConCondToActivityMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(branchConCondToActivityMapper.fromId(null)).isNull();
    }
}
