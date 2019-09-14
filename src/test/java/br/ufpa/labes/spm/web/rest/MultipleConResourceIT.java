package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.MultipleCon;
import br.ufpa.labes.spm.repository.MultipleConRepository;
import br.ufpa.labes.spm.service.MultipleConService;
import br.ufpa.labes.spm.service.dto.MultipleConDTO;
import br.ufpa.labes.spm.service.mapper.MultipleConMapper;
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
 * Integration tests for the {@link MultipleConResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class MultipleConResourceIT {

    private static final Boolean DEFAULT_FIRED = false;
    private static final Boolean UPDATED_FIRED = true;

    @Autowired
    private MultipleConRepository multipleConRepository;

    @Autowired
    private MultipleConMapper multipleConMapper;

    @Autowired
    private MultipleConService multipleConService;

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

    private MockMvc restMultipleConMockMvc;

    private MultipleCon multipleCon;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MultipleConResource multipleConResource = new MultipleConResource(multipleConService);
        this.restMultipleConMockMvc = MockMvcBuilders.standaloneSetup(multipleConResource)
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
    public static MultipleCon createEntity(EntityManager em) {
        MultipleCon multipleCon = new MultipleCon()
            .fired(DEFAULT_FIRED);
        return multipleCon;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MultipleCon createUpdatedEntity(EntityManager em) {
        MultipleCon multipleCon = new MultipleCon()
            .fired(UPDATED_FIRED);
        return multipleCon;
    }

    @BeforeEach
    public void initTest() {
        multipleCon = createEntity(em);
    }

    @Test
    @Transactional
    public void createMultipleCon() throws Exception {
        int databaseSizeBeforeCreate = multipleConRepository.findAll().size();

        // Create the MultipleCon
        MultipleConDTO multipleConDTO = multipleConMapper.toDto(multipleCon);
        restMultipleConMockMvc.perform(post("/api/multiple-cons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(multipleConDTO)))
            .andExpect(status().isCreated());

        // Validate the MultipleCon in the database
        List<MultipleCon> multipleConList = multipleConRepository.findAll();
        assertThat(multipleConList).hasSize(databaseSizeBeforeCreate + 1);
        MultipleCon testMultipleCon = multipleConList.get(multipleConList.size() - 1);
        assertThat(testMultipleCon.isFired()).isEqualTo(DEFAULT_FIRED);
    }

    @Test
    @Transactional
    public void createMultipleConWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = multipleConRepository.findAll().size();

        // Create the MultipleCon with an existing ID
        multipleCon.setId(1L);
        MultipleConDTO multipleConDTO = multipleConMapper.toDto(multipleCon);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMultipleConMockMvc.perform(post("/api/multiple-cons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(multipleConDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MultipleCon in the database
        List<MultipleCon> multipleConList = multipleConRepository.findAll();
        assertThat(multipleConList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMultipleCons() throws Exception {
        // Initialize the database
        multipleConRepository.saveAndFlush(multipleCon);

        // Get all the multipleConList
        restMultipleConMockMvc.perform(get("/api/multiple-cons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(multipleCon.getId().intValue())))
            .andExpect(jsonPath("$.[*].fired").value(hasItem(DEFAULT_FIRED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getMultipleCon() throws Exception {
        // Initialize the database
        multipleConRepository.saveAndFlush(multipleCon);

        // Get the multipleCon
        restMultipleConMockMvc.perform(get("/api/multiple-cons/{id}", multipleCon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(multipleCon.getId().intValue()))
            .andExpect(jsonPath("$.fired").value(DEFAULT_FIRED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMultipleCon() throws Exception {
        // Get the multipleCon
        restMultipleConMockMvc.perform(get("/api/multiple-cons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMultipleCon() throws Exception {
        // Initialize the database
        multipleConRepository.saveAndFlush(multipleCon);

        int databaseSizeBeforeUpdate = multipleConRepository.findAll().size();

        // Update the multipleCon
        MultipleCon updatedMultipleCon = multipleConRepository.findById(multipleCon.getId()).get();
        // Disconnect from session so that the updates on updatedMultipleCon are not directly saved in db
        em.detach(updatedMultipleCon);
        updatedMultipleCon
            .fired(UPDATED_FIRED);
        MultipleConDTO multipleConDTO = multipleConMapper.toDto(updatedMultipleCon);

        restMultipleConMockMvc.perform(put("/api/multiple-cons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(multipleConDTO)))
            .andExpect(status().isOk());

        // Validate the MultipleCon in the database
        List<MultipleCon> multipleConList = multipleConRepository.findAll();
        assertThat(multipleConList).hasSize(databaseSizeBeforeUpdate);
        MultipleCon testMultipleCon = multipleConList.get(multipleConList.size() - 1);
        assertThat(testMultipleCon.isFired()).isEqualTo(UPDATED_FIRED);
    }

    @Test
    @Transactional
    public void updateNonExistingMultipleCon() throws Exception {
        int databaseSizeBeforeUpdate = multipleConRepository.findAll().size();

        // Create the MultipleCon
        MultipleConDTO multipleConDTO = multipleConMapper.toDto(multipleCon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMultipleConMockMvc.perform(put("/api/multiple-cons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(multipleConDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MultipleCon in the database
        List<MultipleCon> multipleConList = multipleConRepository.findAll();
        assertThat(multipleConList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMultipleCon() throws Exception {
        // Initialize the database
        multipleConRepository.saveAndFlush(multipleCon);

        int databaseSizeBeforeDelete = multipleConRepository.findAll().size();

        // Delete the multipleCon
        restMultipleConMockMvc.perform(delete("/api/multiple-cons/{id}", multipleCon.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MultipleCon> multipleConList = multipleConRepository.findAll();
        assertThat(multipleConList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MultipleCon.class);
        MultipleCon multipleCon1 = new MultipleCon();
        multipleCon1.setId(1L);
        MultipleCon multipleCon2 = new MultipleCon();
        multipleCon2.setId(multipleCon1.getId());
        assertThat(multipleCon1).isEqualTo(multipleCon2);
        multipleCon2.setId(2L);
        assertThat(multipleCon1).isNotEqualTo(multipleCon2);
        multipleCon1.setId(null);
        assertThat(multipleCon1).isNotEqualTo(multipleCon2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MultipleConDTO.class);
        MultipleConDTO multipleConDTO1 = new MultipleConDTO();
        multipleConDTO1.setId(1L);
        MultipleConDTO multipleConDTO2 = new MultipleConDTO();
        assertThat(multipleConDTO1).isNotEqualTo(multipleConDTO2);
        multipleConDTO2.setId(multipleConDTO1.getId());
        assertThat(multipleConDTO1).isEqualTo(multipleConDTO2);
        multipleConDTO2.setId(2L);
        assertThat(multipleConDTO1).isNotEqualTo(multipleConDTO2);
        multipleConDTO1.setId(null);
        assertThat(multipleConDTO1).isNotEqualTo(multipleConDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(multipleConMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(multipleConMapper.fromId(null)).isNull();
    }
}
