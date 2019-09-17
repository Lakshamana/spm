package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Decomposed;
import br.ufpa.labes.spm.repository.DecomposedRepository;
import br.ufpa.labes.spm.service.DecomposedService;
import br.ufpa.labes.spm.service.dto.DecomposedDTO;
import br.ufpa.labes.spm.service.mapper.DecomposedMapper;
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
 * Integration tests for the {@link DecomposedResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class DecomposedResourceIT {

    @Autowired
    private DecomposedRepository decomposedRepository;

    @Autowired
    private DecomposedMapper decomposedMapper;

    @Autowired
    private DecomposedService decomposedService;

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

    private MockMvc restDecomposedMockMvc;

    private Decomposed decomposed;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DecomposedResource decomposedResource = new DecomposedResource(decomposedService);
        this.restDecomposedMockMvc = MockMvcBuilders.standaloneSetup(decomposedResource)
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
    public static Decomposed createEntity(EntityManager em) {
        Decomposed decomposed = new Decomposed();
        return decomposed;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Decomposed createUpdatedEntity(EntityManager em) {
        Decomposed decomposed = new Decomposed();
        return decomposed;
    }

    @BeforeEach
    public void initTest() {
        decomposed = createEntity(em);
    }

    @Test
    @Transactional
    public void createDecomposed() throws Exception {
        int databaseSizeBeforeCreate = decomposedRepository.findAll().size();

        // Create the Decomposed
        DecomposedDTO decomposedDTO = decomposedMapper.toDto(decomposed);
        restDecomposedMockMvc.perform(post("/api/decomposeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(decomposedDTO)))
            .andExpect(status().isCreated());

        // Validate the Decomposed in the database
        List<Decomposed> decomposedList = decomposedRepository.findAll();
        assertThat(decomposedList).hasSize(databaseSizeBeforeCreate + 1);
        Decomposed testDecomposed = decomposedList.get(decomposedList.size() - 1);
    }

    @Test
    @Transactional
    public void createDecomposedWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = decomposedRepository.findAll().size();

        // Create the Decomposed with an existing ID
        decomposed.setId(1L);
        DecomposedDTO decomposedDTO = decomposedMapper.toDto(decomposed);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDecomposedMockMvc.perform(post("/api/decomposeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(decomposedDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Decomposed in the database
        List<Decomposed> decomposedList = decomposedRepository.findAll();
        assertThat(decomposedList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDecomposeds() throws Exception {
        // Initialize the database
        decomposedRepository.saveAndFlush(decomposed);

        // Get all the decomposedList
        restDecomposedMockMvc.perform(get("/api/decomposeds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decomposed.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getDecomposed() throws Exception {
        // Initialize the database
        decomposedRepository.saveAndFlush(decomposed);

        // Get the decomposed
        restDecomposedMockMvc.perform(get("/api/decomposeds/{id}", decomposed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(decomposed.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDecomposed() throws Exception {
        // Get the decomposed
        restDecomposedMockMvc.perform(get("/api/decomposeds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDecomposed() throws Exception {
        // Initialize the database
        decomposedRepository.saveAndFlush(decomposed);

        int databaseSizeBeforeUpdate = decomposedRepository.findAll().size();

        // Update the decomposed
        Decomposed updatedDecomposed = decomposedRepository.findById(decomposed.getId()).get();
        // Disconnect from session so that the updates on updatedDecomposed are not directly saved in db
        em.detach(updatedDecomposed);
        DecomposedDTO decomposedDTO = decomposedMapper.toDto(updatedDecomposed);

        restDecomposedMockMvc.perform(put("/api/decomposeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(decomposedDTO)))
            .andExpect(status().isOk());

        // Validate the Decomposed in the database
        List<Decomposed> decomposedList = decomposedRepository.findAll();
        assertThat(decomposedList).hasSize(databaseSizeBeforeUpdate);
        Decomposed testDecomposed = decomposedList.get(decomposedList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingDecomposed() throws Exception {
        int databaseSizeBeforeUpdate = decomposedRepository.findAll().size();

        // Create the Decomposed
        DecomposedDTO decomposedDTO = decomposedMapper.toDto(decomposed);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDecomposedMockMvc.perform(put("/api/decomposeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(decomposedDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Decomposed in the database
        List<Decomposed> decomposedList = decomposedRepository.findAll();
        assertThat(decomposedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDecomposed() throws Exception {
        // Initialize the database
        decomposedRepository.saveAndFlush(decomposed);

        int databaseSizeBeforeDelete = decomposedRepository.findAll().size();

        // Delete the decomposed
        restDecomposedMockMvc.perform(delete("/api/decomposeds/{id}", decomposed.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Decomposed> decomposedList = decomposedRepository.findAll();
        assertThat(decomposedList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Decomposed.class);
        Decomposed decomposed1 = new Decomposed();
        decomposed1.setId(1L);
        Decomposed decomposed2 = new Decomposed();
        decomposed2.setId(decomposed1.getId());
        assertThat(decomposed1).isEqualTo(decomposed2);
        decomposed2.setId(2L);
        assertThat(decomposed1).isNotEqualTo(decomposed2);
        decomposed1.setId(null);
        assertThat(decomposed1).isNotEqualTo(decomposed2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DecomposedDTO.class);
        DecomposedDTO decomposedDTO1 = new DecomposedDTO();
        decomposedDTO1.setId(1L);
        DecomposedDTO decomposedDTO2 = new DecomposedDTO();
        assertThat(decomposedDTO1).isNotEqualTo(decomposedDTO2);
        decomposedDTO2.setId(decomposedDTO1.getId());
        assertThat(decomposedDTO1).isEqualTo(decomposedDTO2);
        decomposedDTO2.setId(2L);
        assertThat(decomposedDTO1).isNotEqualTo(decomposedDTO2);
        decomposedDTO1.setId(null);
        assertThat(decomposedDTO1).isNotEqualTo(decomposedDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(decomposedMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(decomposedMapper.fromId(null)).isNull();
    }
}
