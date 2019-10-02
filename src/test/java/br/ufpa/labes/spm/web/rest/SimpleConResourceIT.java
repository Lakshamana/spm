package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.SimpleCon;
import br.ufpa.labes.spm.repository.SimpleConRepository;
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
 * Integration tests for the {@link SimpleConResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class SimpleConResourceIT {

    @Autowired
    private SimpleConRepository simpleConRepository;

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

    private MockMvc restSimpleConMockMvc;

    private SimpleCon simpleCon;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SimpleConResource simpleConResource = new SimpleConResource(simpleConRepository);
        this.restSimpleConMockMvc = MockMvcBuilders.standaloneSetup(simpleConResource)
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
    public static SimpleCon createEntity(EntityManager em) {
        SimpleCon simpleCon = new SimpleCon();
        return simpleCon;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SimpleCon createUpdatedEntity(EntityManager em) {
        SimpleCon simpleCon = new SimpleCon();
        return simpleCon;
    }

    @BeforeEach
    public void initTest() {
        simpleCon = createEntity(em);
    }

    @Test
    @Transactional
    public void createSimpleCon() throws Exception {
        int databaseSizeBeforeCreate = simpleConRepository.findAll().size();

        // Create the SimpleCon
        restSimpleConMockMvc.perform(post("/api/simple-cons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(simpleCon)))
            .andExpect(status().isCreated());

        // Validate the SimpleCon in the database
        List<SimpleCon> simpleConList = simpleConRepository.findAll();
        assertThat(simpleConList).hasSize(databaseSizeBeforeCreate + 1);
        SimpleCon testSimpleCon = simpleConList.get(simpleConList.size() - 1);
    }

    @Test
    @Transactional
    public void createSimpleConWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = simpleConRepository.findAll().size();

        // Create the SimpleCon with an existing ID
        simpleCon.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSimpleConMockMvc.perform(post("/api/simple-cons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(simpleCon)))
            .andExpect(status().isBadRequest());

        // Validate the SimpleCon in the database
        List<SimpleCon> simpleConList = simpleConRepository.findAll();
        assertThat(simpleConList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSimpleCons() throws Exception {
        // Initialize the database
        simpleConRepository.saveAndFlush(simpleCon);

        // Get all the simpleConList
        restSimpleConMockMvc.perform(get("/api/simple-cons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(simpleCon.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getSimpleCon() throws Exception {
        // Initialize the database
        simpleConRepository.saveAndFlush(simpleCon);

        // Get the simpleCon
        restSimpleConMockMvc.perform(get("/api/simple-cons/{id}", simpleCon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(simpleCon.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSimpleCon() throws Exception {
        // Get the simpleCon
        restSimpleConMockMvc.perform(get("/api/simple-cons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSimpleCon() throws Exception {
        // Initialize the database
        simpleConRepository.saveAndFlush(simpleCon);

        int databaseSizeBeforeUpdate = simpleConRepository.findAll().size();

        // Update the simpleCon
        SimpleCon updatedSimpleCon = simpleConRepository.findById(simpleCon.getId()).get();
        // Disconnect from session so that the updates on updatedSimpleCon are not directly saved in db
        em.detach(updatedSimpleCon);

        restSimpleConMockMvc.perform(put("/api/simple-cons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSimpleCon)))
            .andExpect(status().isOk());

        // Validate the SimpleCon in the database
        List<SimpleCon> simpleConList = simpleConRepository.findAll();
        assertThat(simpleConList).hasSize(databaseSizeBeforeUpdate);
        SimpleCon testSimpleCon = simpleConList.get(simpleConList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingSimpleCon() throws Exception {
        int databaseSizeBeforeUpdate = simpleConRepository.findAll().size();

        // Create the SimpleCon

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSimpleConMockMvc.perform(put("/api/simple-cons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(simpleCon)))
            .andExpect(status().isBadRequest());

        // Validate the SimpleCon in the database
        List<SimpleCon> simpleConList = simpleConRepository.findAll();
        assertThat(simpleConList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSimpleCon() throws Exception {
        // Initialize the database
        simpleConRepository.saveAndFlush(simpleCon);

        int databaseSizeBeforeDelete = simpleConRepository.findAll().size();

        // Delete the simpleCon
        restSimpleConMockMvc.perform(delete("/api/simple-cons/{id}", simpleCon.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SimpleCon> simpleConList = simpleConRepository.findAll();
        assertThat(simpleConList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SimpleCon.class);
        SimpleCon simpleCon1 = new SimpleCon();
        simpleCon1.setId(1L);
        SimpleCon simpleCon2 = new SimpleCon();
        simpleCon2.setId(simpleCon1.getId());
        assertThat(simpleCon1).isEqualTo(simpleCon2);
        simpleCon2.setId(2L);
        assertThat(simpleCon1).isNotEqualTo(simpleCon2);
        simpleCon1.setId(null);
        assertThat(simpleCon1).isNotEqualTo(simpleCon2);
    }
}
