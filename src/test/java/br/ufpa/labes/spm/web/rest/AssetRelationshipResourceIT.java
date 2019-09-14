package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.AssetRelationship;
import br.ufpa.labes.spm.repository.AssetRelationshipRepository;
import br.ufpa.labes.spm.service.AssetRelationshipService;
import br.ufpa.labes.spm.service.dto.AssetRelationshipDTO;
import br.ufpa.labes.spm.service.mapper.AssetRelationshipMapper;
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
 * Integration tests for the {@link AssetRelationshipResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AssetRelationshipResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private AssetRelationshipRepository assetRelationshipRepository;

    @Autowired
    private AssetRelationshipMapper assetRelationshipMapper;

    @Autowired
    private AssetRelationshipService assetRelationshipService;

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

    private MockMvc restAssetRelationshipMockMvc;

    private AssetRelationship assetRelationship;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AssetRelationshipResource assetRelationshipResource = new AssetRelationshipResource(assetRelationshipService);
        this.restAssetRelationshipMockMvc = MockMvcBuilders.standaloneSetup(assetRelationshipResource)
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
    public static AssetRelationship createEntity(EntityManager em) {
        AssetRelationship assetRelationship = new AssetRelationship()
            .description(DEFAULT_DESCRIPTION);
        return assetRelationship;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssetRelationship createUpdatedEntity(EntityManager em) {
        AssetRelationship assetRelationship = new AssetRelationship()
            .description(UPDATED_DESCRIPTION);
        return assetRelationship;
    }

    @BeforeEach
    public void initTest() {
        assetRelationship = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssetRelationship() throws Exception {
        int databaseSizeBeforeCreate = assetRelationshipRepository.findAll().size();

        // Create the AssetRelationship
        AssetRelationshipDTO assetRelationshipDTO = assetRelationshipMapper.toDto(assetRelationship);
        restAssetRelationshipMockMvc.perform(post("/api/asset-relationships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetRelationshipDTO)))
            .andExpect(status().isCreated());

        // Validate the AssetRelationship in the database
        List<AssetRelationship> assetRelationshipList = assetRelationshipRepository.findAll();
        assertThat(assetRelationshipList).hasSize(databaseSizeBeforeCreate + 1);
        AssetRelationship testAssetRelationship = assetRelationshipList.get(assetRelationshipList.size() - 1);
        assertThat(testAssetRelationship.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createAssetRelationshipWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = assetRelationshipRepository.findAll().size();

        // Create the AssetRelationship with an existing ID
        assetRelationship.setId(1L);
        AssetRelationshipDTO assetRelationshipDTO = assetRelationshipMapper.toDto(assetRelationship);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssetRelationshipMockMvc.perform(post("/api/asset-relationships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetRelationshipDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AssetRelationship in the database
        List<AssetRelationship> assetRelationshipList = assetRelationshipRepository.findAll();
        assertThat(assetRelationshipList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAssetRelationships() throws Exception {
        // Initialize the database
        assetRelationshipRepository.saveAndFlush(assetRelationship);

        // Get all the assetRelationshipList
        restAssetRelationshipMockMvc.perform(get("/api/asset-relationships?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assetRelationship.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getAssetRelationship() throws Exception {
        // Initialize the database
        assetRelationshipRepository.saveAndFlush(assetRelationship);

        // Get the assetRelationship
        restAssetRelationshipMockMvc.perform(get("/api/asset-relationships/{id}", assetRelationship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(assetRelationship.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAssetRelationship() throws Exception {
        // Get the assetRelationship
        restAssetRelationshipMockMvc.perform(get("/api/asset-relationships/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssetRelationship() throws Exception {
        // Initialize the database
        assetRelationshipRepository.saveAndFlush(assetRelationship);

        int databaseSizeBeforeUpdate = assetRelationshipRepository.findAll().size();

        // Update the assetRelationship
        AssetRelationship updatedAssetRelationship = assetRelationshipRepository.findById(assetRelationship.getId()).get();
        // Disconnect from session so that the updates on updatedAssetRelationship are not directly saved in db
        em.detach(updatedAssetRelationship);
        updatedAssetRelationship
            .description(UPDATED_DESCRIPTION);
        AssetRelationshipDTO assetRelationshipDTO = assetRelationshipMapper.toDto(updatedAssetRelationship);

        restAssetRelationshipMockMvc.perform(put("/api/asset-relationships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetRelationshipDTO)))
            .andExpect(status().isOk());

        // Validate the AssetRelationship in the database
        List<AssetRelationship> assetRelationshipList = assetRelationshipRepository.findAll();
        assertThat(assetRelationshipList).hasSize(databaseSizeBeforeUpdate);
        AssetRelationship testAssetRelationship = assetRelationshipList.get(assetRelationshipList.size() - 1);
        assertThat(testAssetRelationship.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingAssetRelationship() throws Exception {
        int databaseSizeBeforeUpdate = assetRelationshipRepository.findAll().size();

        // Create the AssetRelationship
        AssetRelationshipDTO assetRelationshipDTO = assetRelationshipMapper.toDto(assetRelationship);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssetRelationshipMockMvc.perform(put("/api/asset-relationships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assetRelationshipDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AssetRelationship in the database
        List<AssetRelationship> assetRelationshipList = assetRelationshipRepository.findAll();
        assertThat(assetRelationshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAssetRelationship() throws Exception {
        // Initialize the database
        assetRelationshipRepository.saveAndFlush(assetRelationship);

        int databaseSizeBeforeDelete = assetRelationshipRepository.findAll().size();

        // Delete the assetRelationship
        restAssetRelationshipMockMvc.perform(delete("/api/asset-relationships/{id}", assetRelationship.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AssetRelationship> assetRelationshipList = assetRelationshipRepository.findAll();
        assertThat(assetRelationshipList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetRelationship.class);
        AssetRelationship assetRelationship1 = new AssetRelationship();
        assetRelationship1.setId(1L);
        AssetRelationship assetRelationship2 = new AssetRelationship();
        assetRelationship2.setId(assetRelationship1.getId());
        assertThat(assetRelationship1).isEqualTo(assetRelationship2);
        assetRelationship2.setId(2L);
        assertThat(assetRelationship1).isNotEqualTo(assetRelationship2);
        assetRelationship1.setId(null);
        assertThat(assetRelationship1).isNotEqualTo(assetRelationship2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssetRelationshipDTO.class);
        AssetRelationshipDTO assetRelationshipDTO1 = new AssetRelationshipDTO();
        assetRelationshipDTO1.setId(1L);
        AssetRelationshipDTO assetRelationshipDTO2 = new AssetRelationshipDTO();
        assertThat(assetRelationshipDTO1).isNotEqualTo(assetRelationshipDTO2);
        assetRelationshipDTO2.setId(assetRelationshipDTO1.getId());
        assertThat(assetRelationshipDTO1).isEqualTo(assetRelationshipDTO2);
        assetRelationshipDTO2.setId(2L);
        assertThat(assetRelationshipDTO1).isNotEqualTo(assetRelationshipDTO2);
        assetRelationshipDTO1.setId(null);
        assertThat(assetRelationshipDTO1).isNotEqualTo(assetRelationshipDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(assetRelationshipMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(assetRelationshipMapper.fromId(null)).isNull();
    }
}
