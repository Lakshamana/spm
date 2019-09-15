package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.AutomaticActivity;
import br.ufpa.labes.spm.repository.AutomaticActivityRepository;
import br.ufpa.labes.spm.service.AutomaticActivityService;
import br.ufpa.labes.spm.service.dto.AutomaticActivityDTO;
import br.ufpa.labes.spm.service.mapper.AutomaticActivityMapper;
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
 * Integration tests for the {@link AutomaticActivityResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AutomaticActivityResourceIT {

    @Autowired
    private AutomaticActivityRepository automaticActivityRepository;

    @Autowired
    private AutomaticActivityMapper automaticActivityMapper;

    @Autowired
    private AutomaticActivityService automaticActivityService;

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

    private MockMvc restAutomaticActivityMockMvc;

    private AutomaticActivity automaticActivity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AutomaticActivityResource automaticActivityResource = new AutomaticActivityResource(automaticActivityService);
        this.restAutomaticActivityMockMvc = MockMvcBuilders.standaloneSetup(automaticActivityResource)
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
    public static AutomaticActivity createEntity(EntityManager em) {
        AutomaticActivity automaticActivity = new AutomaticActivity();
        return automaticActivity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AutomaticActivity createUpdatedEntity(EntityManager em) {
        AutomaticActivity automaticActivity = new AutomaticActivity();
        return automaticActivity;
    }

    @BeforeEach
    public void initTest() {
        automaticActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createAutomaticActivity() throws Exception {
        int databaseSizeBeforeCreate = automaticActivityRepository.findAll().size();

        // Create the AutomaticActivity
        AutomaticActivityDTO automaticActivityDTO = automaticActivityMapper.toDto(automaticActivity);
        restAutomaticActivityMockMvc.perform(post("/api/automatic-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(automaticActivityDTO)))
            .andExpect(status().isCreated());

        // Validate the AutomaticActivity in the database
        List<AutomaticActivity> automaticActivityList = automaticActivityRepository.findAll();
        assertThat(automaticActivityList).hasSize(databaseSizeBeforeCreate + 1);
        AutomaticActivity testAutomaticActivity = automaticActivityList.get(automaticActivityList.size() - 1);
    }

    @Test
    @Transactional
    public void createAutomaticActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = automaticActivityRepository.findAll().size();

        // Create the AutomaticActivity with an existing ID
        automaticActivity.setId(1L);
        AutomaticActivityDTO automaticActivityDTO = automaticActivityMapper.toDto(automaticActivity);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutomaticActivityMockMvc.perform(post("/api/automatic-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(automaticActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AutomaticActivity in the database
        List<AutomaticActivity> automaticActivityList = automaticActivityRepository.findAll();
        assertThat(automaticActivityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAutomaticActivities() throws Exception {
        // Initialize the database
        automaticActivityRepository.saveAndFlush(automaticActivity);

        // Get all the automaticActivityList
        restAutomaticActivityMockMvc.perform(get("/api/automatic-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(automaticActivity.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getAutomaticActivity() throws Exception {
        // Initialize the database
        automaticActivityRepository.saveAndFlush(automaticActivity);

        // Get the automaticActivity
        restAutomaticActivityMockMvc.perform(get("/api/automatic-activities/{id}", automaticActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(automaticActivity.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAutomaticActivity() throws Exception {
        // Get the automaticActivity
        restAutomaticActivityMockMvc.perform(get("/api/automatic-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAutomaticActivity() throws Exception {
        // Initialize the database
        automaticActivityRepository.saveAndFlush(automaticActivity);

        int databaseSizeBeforeUpdate = automaticActivityRepository.findAll().size();

        // Update the automaticActivity
        AutomaticActivity updatedAutomaticActivity = automaticActivityRepository.findById(automaticActivity.getId()).get();
        // Disconnect from session so that the updates on updatedAutomaticActivity are not directly saved in db
        em.detach(updatedAutomaticActivity);
        AutomaticActivityDTO automaticActivityDTO = automaticActivityMapper.toDto(updatedAutomaticActivity);

        restAutomaticActivityMockMvc.perform(put("/api/automatic-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(automaticActivityDTO)))
            .andExpect(status().isOk());

        // Validate the AutomaticActivity in the database
        List<AutomaticActivity> automaticActivityList = automaticActivityRepository.findAll();
        assertThat(automaticActivityList).hasSize(databaseSizeBeforeUpdate);
        AutomaticActivity testAutomaticActivity = automaticActivityList.get(automaticActivityList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingAutomaticActivity() throws Exception {
        int databaseSizeBeforeUpdate = automaticActivityRepository.findAll().size();

        // Create the AutomaticActivity
        AutomaticActivityDTO automaticActivityDTO = automaticActivityMapper.toDto(automaticActivity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutomaticActivityMockMvc.perform(put("/api/automatic-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(automaticActivityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AutomaticActivity in the database
        List<AutomaticActivity> automaticActivityList = automaticActivityRepository.findAll();
        assertThat(automaticActivityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAutomaticActivity() throws Exception {
        // Initialize the database
        automaticActivityRepository.saveAndFlush(automaticActivity);

        int databaseSizeBeforeDelete = automaticActivityRepository.findAll().size();

        // Delete the automaticActivity
        restAutomaticActivityMockMvc.perform(delete("/api/automatic-activities/{id}", automaticActivity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AutomaticActivity> automaticActivityList = automaticActivityRepository.findAll();
        assertThat(automaticActivityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AutomaticActivity.class);
        AutomaticActivity automaticActivity1 = new AutomaticActivity();
        automaticActivity1.setId(1L);
        AutomaticActivity automaticActivity2 = new AutomaticActivity();
        automaticActivity2.setId(automaticActivity1.getId());
        assertThat(automaticActivity1).isEqualTo(automaticActivity2);
        automaticActivity2.setId(2L);
        assertThat(automaticActivity1).isNotEqualTo(automaticActivity2);
        automaticActivity1.setId(null);
        assertThat(automaticActivity1).isNotEqualTo(automaticActivity2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AutomaticActivityDTO.class);
        AutomaticActivityDTO automaticActivityDTO1 = new AutomaticActivityDTO();
        automaticActivityDTO1.setId(1L);
        AutomaticActivityDTO automaticActivityDTO2 = new AutomaticActivityDTO();
        assertThat(automaticActivityDTO1).isNotEqualTo(automaticActivityDTO2);
        automaticActivityDTO2.setId(automaticActivityDTO1.getId());
        assertThat(automaticActivityDTO1).isEqualTo(automaticActivityDTO2);
        automaticActivityDTO2.setId(2L);
        assertThat(automaticActivityDTO1).isNotEqualTo(automaticActivityDTO2);
        automaticActivityDTO1.setId(null);
        assertThat(automaticActivityDTO1).isNotEqualTo(automaticActivityDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(automaticActivityMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(automaticActivityMapper.fromId(null)).isNull();
    }
}
