package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Plain;
import br.ufpa.labes.spm.repository.PlainRepository;
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

/**
 * Integration tests for the {@link PlainResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class PlainResourceIT {

    private static final String DEFAULT_REQUIREMENTS = "AAAAAAAAAA";
    private static final String UPDATED_REQUIREMENTS = "BBBBBBBBBB";

    @Autowired
    private PlainRepository plainRepository;

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

    private MockMvc restPlainMockMvc;

    private Plain plain;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlainResource plainResource = new PlainResource(plainRepository);
        this.restPlainMockMvc = MockMvcBuilders.standaloneSetup(plainResource)
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
    public static Plain createEntity(EntityManager em) {
        Plain plain = new Plain()
            .requirements(DEFAULT_REQUIREMENTS);
        return plain;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plain createUpdatedEntity(EntityManager em) {
        Plain plain = new Plain()
            .requirements(UPDATED_REQUIREMENTS);
        return plain;
    }

    @BeforeEach
    public void initTest() {
        plain = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlain() throws Exception {
        int databaseSizeBeforeCreate = plainRepository.findAll().size();

        // Create the Plain
        restPlainMockMvc.perform(post("/api/plains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plain)))
            .andExpect(status().isCreated());

        // Validate the Plain in the database
        List<Plain> plainList = plainRepository.findAll();
        assertThat(plainList).hasSize(databaseSizeBeforeCreate + 1);
        Plain testPlain = plainList.get(plainList.size() - 1);
        assertThat(testPlain.getRequirements()).isEqualTo(DEFAULT_REQUIREMENTS);
    }

    @Test
    @Transactional
    public void createPlainWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = plainRepository.findAll().size();

        // Create the Plain with an existing ID
        plain.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlainMockMvc.perform(post("/api/plains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plain)))
            .andExpect(status().isBadRequest());

        // Validate the Plain in the database
        List<Plain> plainList = plainRepository.findAll();
        assertThat(plainList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPlains() throws Exception {
        // Initialize the database
        plainRepository.saveAndFlush(plain);

        // Get all the plainList
        restPlainMockMvc.perform(get("/api/plains?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plain.getId().intValue())))
            .andExpect(jsonPath("$.[*].requirements").value(hasItem(DEFAULT_REQUIREMENTS.toString())));
    }
    
    @Test
    @Transactional
    public void getPlain() throws Exception {
        // Initialize the database
        plainRepository.saveAndFlush(plain);

        // Get the plain
        restPlainMockMvc.perform(get("/api/plains/{id}", plain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(plain.getId().intValue()))
            .andExpect(jsonPath("$.requirements").value(DEFAULT_REQUIREMENTS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlain() throws Exception {
        // Get the plain
        restPlainMockMvc.perform(get("/api/plains/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlain() throws Exception {
        // Initialize the database
        plainRepository.saveAndFlush(plain);

        int databaseSizeBeforeUpdate = plainRepository.findAll().size();

        // Update the plain
        Plain updatedPlain = plainRepository.findById(plain.getId()).get();
        // Disconnect from session so that the updates on updatedPlain are not directly saved in db
        em.detach(updatedPlain);
        updatedPlain
            .requirements(UPDATED_REQUIREMENTS);

        restPlainMockMvc.perform(put("/api/plains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlain)))
            .andExpect(status().isOk());

        // Validate the Plain in the database
        List<Plain> plainList = plainRepository.findAll();
        assertThat(plainList).hasSize(databaseSizeBeforeUpdate);
        Plain testPlain = plainList.get(plainList.size() - 1);
        assertThat(testPlain.getRequirements()).isEqualTo(UPDATED_REQUIREMENTS);
    }

    @Test
    @Transactional
    public void updateNonExistingPlain() throws Exception {
        int databaseSizeBeforeUpdate = plainRepository.findAll().size();

        // Create the Plain

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlainMockMvc.perform(put("/api/plains")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(plain)))
            .andExpect(status().isBadRequest());

        // Validate the Plain in the database
        List<Plain> plainList = plainRepository.findAll();
        assertThat(plainList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlain() throws Exception {
        // Initialize the database
        plainRepository.saveAndFlush(plain);

        int databaseSizeBeforeDelete = plainRepository.findAll().size();

        // Delete the plain
        restPlainMockMvc.perform(delete("/api/plains/{id}", plain.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plain> plainList = plainRepository.findAll();
        assertThat(plainList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plain.class);
        Plain plain1 = new Plain();
        plain1.setId(1L);
        Plain plain2 = new Plain();
        plain2.setId(plain1.getId());
        assertThat(plain1).isEqualTo(plain2);
        plain2.setId(2L);
        assertThat(plain1).isNotEqualTo(plain2);
        plain1.setId(null);
        assertThat(plain1).isNotEqualTo(plain2);
    }
}
