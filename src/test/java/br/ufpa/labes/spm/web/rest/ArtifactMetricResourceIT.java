package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ArtifactMetric;
import br.ufpa.labes.spm.repository.ArtifactMetricRepository;
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
 * Integration tests for the {@link ArtifactMetricResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ArtifactMetricResourceIT {

    @Autowired
    private ArtifactMetricRepository artifactMetricRepository;

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

    private MockMvc restArtifactMetricMockMvc;

    private ArtifactMetric artifactMetric;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ArtifactMetricResource artifactMetricResource = new ArtifactMetricResource(artifactMetricRepository);
        this.restArtifactMetricMockMvc = MockMvcBuilders.standaloneSetup(artifactMetricResource)
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
    public static ArtifactMetric createEntity(EntityManager em) {
        ArtifactMetric artifactMetric = new ArtifactMetric();
        return artifactMetric;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArtifactMetric createUpdatedEntity(EntityManager em) {
        ArtifactMetric artifactMetric = new ArtifactMetric();
        return artifactMetric;
    }

    @BeforeEach
    public void initTest() {
        artifactMetric = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtifactMetric() throws Exception {
        int databaseSizeBeforeCreate = artifactMetricRepository.findAll().size();

        // Create the ArtifactMetric
        restArtifactMetricMockMvc.perform(post("/api/artifact-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artifactMetric)))
            .andExpect(status().isCreated());

        // Validate the ArtifactMetric in the database
        List<ArtifactMetric> artifactMetricList = artifactMetricRepository.findAll();
        assertThat(artifactMetricList).hasSize(databaseSizeBeforeCreate + 1);
        ArtifactMetric testArtifactMetric = artifactMetricList.get(artifactMetricList.size() - 1);
    }

    @Test
    @Transactional
    public void createArtifactMetricWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = artifactMetricRepository.findAll().size();

        // Create the ArtifactMetric with an existing ID
        artifactMetric.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtifactMetricMockMvc.perform(post("/api/artifact-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artifactMetric)))
            .andExpect(status().isBadRequest());

        // Validate the ArtifactMetric in the database
        List<ArtifactMetric> artifactMetricList = artifactMetricRepository.findAll();
        assertThat(artifactMetricList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllArtifactMetrics() throws Exception {
        // Initialize the database
        artifactMetricRepository.saveAndFlush(artifactMetric);

        // Get all the artifactMetricList
        restArtifactMetricMockMvc.perform(get("/api/artifact-metrics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artifactMetric.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getArtifactMetric() throws Exception {
        // Initialize the database
        artifactMetricRepository.saveAndFlush(artifactMetric);

        // Get the artifactMetric
        restArtifactMetricMockMvc.perform(get("/api/artifact-metrics/{id}", artifactMetric.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(artifactMetric.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingArtifactMetric() throws Exception {
        // Get the artifactMetric
        restArtifactMetricMockMvc.perform(get("/api/artifact-metrics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtifactMetric() throws Exception {
        // Initialize the database
        artifactMetricRepository.saveAndFlush(artifactMetric);

        int databaseSizeBeforeUpdate = artifactMetricRepository.findAll().size();

        // Update the artifactMetric
        ArtifactMetric updatedArtifactMetric = artifactMetricRepository.findById(artifactMetric.getId()).get();
        // Disconnect from session so that the updates on updatedArtifactMetric are not directly saved in db
        em.detach(updatedArtifactMetric);

        restArtifactMetricMockMvc.perform(put("/api/artifact-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedArtifactMetric)))
            .andExpect(status().isOk());

        // Validate the ArtifactMetric in the database
        List<ArtifactMetric> artifactMetricList = artifactMetricRepository.findAll();
        assertThat(artifactMetricList).hasSize(databaseSizeBeforeUpdate);
        ArtifactMetric testArtifactMetric = artifactMetricList.get(artifactMetricList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingArtifactMetric() throws Exception {
        int databaseSizeBeforeUpdate = artifactMetricRepository.findAll().size();

        // Create the ArtifactMetric

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtifactMetricMockMvc.perform(put("/api/artifact-metrics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artifactMetric)))
            .andExpect(status().isBadRequest());

        // Validate the ArtifactMetric in the database
        List<ArtifactMetric> artifactMetricList = artifactMetricRepository.findAll();
        assertThat(artifactMetricList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArtifactMetric() throws Exception {
        // Initialize the database
        artifactMetricRepository.saveAndFlush(artifactMetric);

        int databaseSizeBeforeDelete = artifactMetricRepository.findAll().size();

        // Delete the artifactMetric
        restArtifactMetricMockMvc.perform(delete("/api/artifact-metrics/{id}", artifactMetric.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArtifactMetric> artifactMetricList = artifactMetricRepository.findAll();
        assertThat(artifactMetricList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArtifactMetric.class);
        ArtifactMetric artifactMetric1 = new ArtifactMetric();
        artifactMetric1.setId(1L);
        ArtifactMetric artifactMetric2 = new ArtifactMetric();
        artifactMetric2.setId(artifactMetric1.getId());
        assertThat(artifactMetric1).isEqualTo(artifactMetric2);
        artifactMetric2.setId(2L);
        assertThat(artifactMetric1).isNotEqualTo(artifactMetric2);
        artifactMetric1.setId(null);
        assertThat(artifactMetric1).isNotEqualTo(artifactMetric2);
    }
}
