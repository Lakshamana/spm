package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.BranchCondToMultipleCon;
import br.ufpa.labes.spm.repository.BranchCondToMultipleConRepository;
import br.ufpa.labes.spm.service.BranchCondToMultipleConService;
import br.ufpa.labes.spm.service.dto.BranchCondToMultipleConDTO;
import br.ufpa.labes.spm.service.mapper.BranchCondToMultipleConMapper;
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
 * Integration tests for the {@link BranchCondToMultipleConResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class BranchCondToMultipleConResourceIT {

    @Autowired
    private BranchCondToMultipleConRepository branchCondToMultipleConRepository;

    @Autowired
    private BranchCondToMultipleConMapper branchCondToMultipleConMapper;

    @Autowired
    private BranchCondToMultipleConService branchCondToMultipleConService;

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

    private MockMvc restBranchCondToMultipleConMockMvc;

    private BranchCondToMultipleCon branchCondToMultipleCon;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BranchCondToMultipleConResource branchCondToMultipleConResource = new BranchCondToMultipleConResource(branchCondToMultipleConService);
        this.restBranchCondToMultipleConMockMvc = MockMvcBuilders.standaloneSetup(branchCondToMultipleConResource)
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
    public static BranchCondToMultipleCon createEntity(EntityManager em) {
        BranchCondToMultipleCon branchCondToMultipleCon = new BranchCondToMultipleCon();
        return branchCondToMultipleCon;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BranchCondToMultipleCon createUpdatedEntity(EntityManager em) {
        BranchCondToMultipleCon branchCondToMultipleCon = new BranchCondToMultipleCon();
        return branchCondToMultipleCon;
    }

    @BeforeEach
    public void initTest() {
        branchCondToMultipleCon = createEntity(em);
    }

    @Test
    @Transactional
    public void createBranchCondToMultipleCon() throws Exception {
        int databaseSizeBeforeCreate = branchCondToMultipleConRepository.findAll().size();

        // Create the BranchCondToMultipleCon
        BranchCondToMultipleConDTO branchCondToMultipleConDTO = branchCondToMultipleConMapper.toDto(branchCondToMultipleCon);
        restBranchCondToMultipleConMockMvc.perform(post("/api/branch-cond-to-multiple-cons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchCondToMultipleConDTO)))
            .andExpect(status().isCreated());

        // Validate the BranchCondToMultipleCon in the database
        List<BranchCondToMultipleCon> branchCondToMultipleConList = branchCondToMultipleConRepository.findAll();
        assertThat(branchCondToMultipleConList).hasSize(databaseSizeBeforeCreate + 1);
        BranchCondToMultipleCon testBranchCondToMultipleCon = branchCondToMultipleConList.get(branchCondToMultipleConList.size() - 1);
    }

    @Test
    @Transactional
    public void createBranchCondToMultipleConWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = branchCondToMultipleConRepository.findAll().size();

        // Create the BranchCondToMultipleCon with an existing ID
        branchCondToMultipleCon.setId(1L);
        BranchCondToMultipleConDTO branchCondToMultipleConDTO = branchCondToMultipleConMapper.toDto(branchCondToMultipleCon);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchCondToMultipleConMockMvc.perform(post("/api/branch-cond-to-multiple-cons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchCondToMultipleConDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BranchCondToMultipleCon in the database
        List<BranchCondToMultipleCon> branchCondToMultipleConList = branchCondToMultipleConRepository.findAll();
        assertThat(branchCondToMultipleConList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBranchCondToMultipleCons() throws Exception {
        // Initialize the database
        branchCondToMultipleConRepository.saveAndFlush(branchCondToMultipleCon);

        // Get all the branchCondToMultipleConList
        restBranchCondToMultipleConMockMvc.perform(get("/api/branch-cond-to-multiple-cons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branchCondToMultipleCon.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getBranchCondToMultipleCon() throws Exception {
        // Initialize the database
        branchCondToMultipleConRepository.saveAndFlush(branchCondToMultipleCon);

        // Get the branchCondToMultipleCon
        restBranchCondToMultipleConMockMvc.perform(get("/api/branch-cond-to-multiple-cons/{id}", branchCondToMultipleCon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(branchCondToMultipleCon.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBranchCondToMultipleCon() throws Exception {
        // Get the branchCondToMultipleCon
        restBranchCondToMultipleConMockMvc.perform(get("/api/branch-cond-to-multiple-cons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBranchCondToMultipleCon() throws Exception {
        // Initialize the database
        branchCondToMultipleConRepository.saveAndFlush(branchCondToMultipleCon);

        int databaseSizeBeforeUpdate = branchCondToMultipleConRepository.findAll().size();

        // Update the branchCondToMultipleCon
        BranchCondToMultipleCon updatedBranchCondToMultipleCon = branchCondToMultipleConRepository.findById(branchCondToMultipleCon.getId()).get();
        // Disconnect from session so that the updates on updatedBranchCondToMultipleCon are not directly saved in db
        em.detach(updatedBranchCondToMultipleCon);
        BranchCondToMultipleConDTO branchCondToMultipleConDTO = branchCondToMultipleConMapper.toDto(updatedBranchCondToMultipleCon);

        restBranchCondToMultipleConMockMvc.perform(put("/api/branch-cond-to-multiple-cons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchCondToMultipleConDTO)))
            .andExpect(status().isOk());

        // Validate the BranchCondToMultipleCon in the database
        List<BranchCondToMultipleCon> branchCondToMultipleConList = branchCondToMultipleConRepository.findAll();
        assertThat(branchCondToMultipleConList).hasSize(databaseSizeBeforeUpdate);
        BranchCondToMultipleCon testBranchCondToMultipleCon = branchCondToMultipleConList.get(branchCondToMultipleConList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingBranchCondToMultipleCon() throws Exception {
        int databaseSizeBeforeUpdate = branchCondToMultipleConRepository.findAll().size();

        // Create the BranchCondToMultipleCon
        BranchCondToMultipleConDTO branchCondToMultipleConDTO = branchCondToMultipleConMapper.toDto(branchCondToMultipleCon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchCondToMultipleConMockMvc.perform(put("/api/branch-cond-to-multiple-cons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchCondToMultipleConDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BranchCondToMultipleCon in the database
        List<BranchCondToMultipleCon> branchCondToMultipleConList = branchCondToMultipleConRepository.findAll();
        assertThat(branchCondToMultipleConList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBranchCondToMultipleCon() throws Exception {
        // Initialize the database
        branchCondToMultipleConRepository.saveAndFlush(branchCondToMultipleCon);

        int databaseSizeBeforeDelete = branchCondToMultipleConRepository.findAll().size();

        // Delete the branchCondToMultipleCon
        restBranchCondToMultipleConMockMvc.perform(delete("/api/branch-cond-to-multiple-cons/{id}", branchCondToMultipleCon.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BranchCondToMultipleCon> branchCondToMultipleConList = branchCondToMultipleConRepository.findAll();
        assertThat(branchCondToMultipleConList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchCondToMultipleCon.class);
        BranchCondToMultipleCon branchCondToMultipleCon1 = new BranchCondToMultipleCon();
        branchCondToMultipleCon1.setId(1L);
        BranchCondToMultipleCon branchCondToMultipleCon2 = new BranchCondToMultipleCon();
        branchCondToMultipleCon2.setId(branchCondToMultipleCon1.getId());
        assertThat(branchCondToMultipleCon1).isEqualTo(branchCondToMultipleCon2);
        branchCondToMultipleCon2.setId(2L);
        assertThat(branchCondToMultipleCon1).isNotEqualTo(branchCondToMultipleCon2);
        branchCondToMultipleCon1.setId(null);
        assertThat(branchCondToMultipleCon1).isNotEqualTo(branchCondToMultipleCon2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchCondToMultipleConDTO.class);
        BranchCondToMultipleConDTO branchCondToMultipleConDTO1 = new BranchCondToMultipleConDTO();
        branchCondToMultipleConDTO1.setId(1L);
        BranchCondToMultipleConDTO branchCondToMultipleConDTO2 = new BranchCondToMultipleConDTO();
        assertThat(branchCondToMultipleConDTO1).isNotEqualTo(branchCondToMultipleConDTO2);
        branchCondToMultipleConDTO2.setId(branchCondToMultipleConDTO1.getId());
        assertThat(branchCondToMultipleConDTO1).isEqualTo(branchCondToMultipleConDTO2);
        branchCondToMultipleConDTO2.setId(2L);
        assertThat(branchCondToMultipleConDTO1).isNotEqualTo(branchCondToMultipleConDTO2);
        branchCondToMultipleConDTO1.setId(null);
        assertThat(branchCondToMultipleConDTO1).isNotEqualTo(branchCondToMultipleConDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(branchCondToMultipleConMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(branchCondToMultipleConMapper.fromId(null)).isNull();
    }
}
