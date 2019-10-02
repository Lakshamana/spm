package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ArtifactEstimation;
import br.ufpa.labes.spm.repository.ArtifactEstimationRepository;
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
 * Integration tests for the {@link ArtifactEstimationResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ArtifactEstimationResourceIT {

    @Autowired
    private ArtifactEstimationRepository artifactEstimationRepository;

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

    private MockMvc restArtifactEstimationMockMvc;

    private ArtifactEstimation artifactEstimation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ArtifactEstimationResource artifactEstimationResource = new ArtifactEstimationResource(artifactEstimationRepository);
        this.restArtifactEstimationMockMvc = MockMvcBuilders.standaloneSetup(artifactEstimationResource)
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
    public static ArtifactEstimation createEntity(EntityManager em) {
        ArtifactEstimation artifactEstimation = new ArtifactEstimation();
        return artifactEstimation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArtifactEstimation createUpdatedEntity(EntityManager em) {
        ArtifactEstimation artifactEstimation = new ArtifactEstimation();
        return artifactEstimation;
    }

    @BeforeEach
    public void initTest() {
        artifactEstimation = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtifactEstimation() throws Exception {
        int databaseSizeBeforeCreate = artifactEstimationRepository.findAll().size();

        // Create the ArtifactEstimation
        restArtifactEstimationMockMvc.perform(post("/api/artifact-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artifactEstimation)))
            .andExpect(status().isCreated());

        // Validate the ArtifactEstimation in the database
        List<ArtifactEstimation> artifactEstimationList = artifactEstimationRepository.findAll();
        assertThat(artifactEstimationList).hasSize(databaseSizeBeforeCreate + 1);
        ArtifactEstimation testArtifactEstimation = artifactEstimationList.get(artifactEstimationList.size() - 1);
    }

    @Test
    @Transactional
    public void createArtifactEstimationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = artifactEstimationRepository.findAll().size();

        // Create the ArtifactEstimation with an existing ID
        artifactEstimation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtifactEstimationMockMvc.perform(post("/api/artifact-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artifactEstimation)))
            .andExpect(status().isBadRequest());

        // Validate the ArtifactEstimation in the database
        List<ArtifactEstimation> artifactEstimationList = artifactEstimationRepository.findAll();
        assertThat(artifactEstimationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllArtifactEstimations() throws Exception {
        // Initialize the database
        artifactEstimationRepository.saveAndFlush(artifactEstimation);

        // Get all the artifactEstimationList
        restArtifactEstimationMockMvc.perform(get("/api/artifact-estimations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artifactEstimation.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getArtifactEstimation() throws Exception {
        // Initialize the database
        artifactEstimationRepository.saveAndFlush(artifactEstimation);

        // Get the artifactEstimation
        restArtifactEstimationMockMvc.perform(get("/api/artifact-estimations/{id}", artifactEstimation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(artifactEstimation.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingArtifactEstimation() throws Exception {
        // Get the artifactEstimation
        restArtifactEstimationMockMvc.perform(get("/api/artifact-estimations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtifactEstimation() throws Exception {
        // Initialize the database
        artifactEstimationRepository.saveAndFlush(artifactEstimation);

        int databaseSizeBeforeUpdate = artifactEstimationRepository.findAll().size();

        // Update the artifactEstimation
        ArtifactEstimation updatedArtifactEstimation = artifactEstimationRepository.findById(artifactEstimation.getId()).get();
        // Disconnect from session so that the updates on updatedArtifactEstimation are not directly saved in db
        em.detach(updatedArtifactEstimation);

        restArtifactEstimationMockMvc.perform(put("/api/artifact-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedArtifactEstimation)))
            .andExpect(status().isOk());

        // Validate the ArtifactEstimation in the database
        List<ArtifactEstimation> artifactEstimationList = artifactEstimationRepository.findAll();
        assertThat(artifactEstimationList).hasSize(databaseSizeBeforeUpdate);
        ArtifactEstimation testArtifactEstimation = artifactEstimationList.get(artifactEstimationList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingArtifactEstimation() throws Exception {
        int databaseSizeBeforeUpdate = artifactEstimationRepository.findAll().size();

        // Create the ArtifactEstimation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtifactEstimationMockMvc.perform(put("/api/artifact-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artifactEstimation)))
            .andExpect(status().isBadRequest());

        // Validate the ArtifactEstimation in the database
        List<ArtifactEstimation> artifactEstimationList = artifactEstimationRepository.findAll();
        assertThat(artifactEstimationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArtifactEstimation() throws Exception {
        // Initialize the database
        artifactEstimationRepository.saveAndFlush(artifactEstimation);

        int databaseSizeBeforeDelete = artifactEstimationRepository.findAll().size();

        // Delete the artifactEstimation
        restArtifactEstimationMockMvc.perform(delete("/api/artifact-estimations/{id}", artifactEstimation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArtifactEstimation> artifactEstimationList = artifactEstimationRepository.findAll();
        assertThat(artifactEstimationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArtifactEstimation.class);
        ArtifactEstimation artifactEstimation1 = new ArtifactEstimation();
        artifactEstimation1.setId(1L);
        ArtifactEstimation artifactEstimation2 = new ArtifactEstimation();
        artifactEstimation2.setId(artifactEstimation1.getId());
        assertThat(artifactEstimation1).isEqualTo(artifactEstimation2);
        artifactEstimation2.setId(2L);
        assertThat(artifactEstimation1).isNotEqualTo(artifactEstimation2);
        artifactEstimation1.setId(null);
        assertThat(artifactEstimation1).isNotEqualTo(artifactEstimation2);
    }
}
