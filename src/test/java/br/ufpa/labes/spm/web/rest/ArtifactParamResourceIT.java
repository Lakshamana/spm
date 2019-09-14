package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ArtifactParam;
import br.ufpa.labes.spm.repository.ArtifactParamRepository;
import br.ufpa.labes.spm.service.ArtifactParamService;
import br.ufpa.labes.spm.service.dto.ArtifactParamDTO;
import br.ufpa.labes.spm.service.mapper.ArtifactParamMapper;
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
 * Integration tests for the {@link ArtifactParamResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ArtifactParamResourceIT {

    @Autowired
    private ArtifactParamRepository artifactParamRepository;

    @Autowired
    private ArtifactParamMapper artifactParamMapper;

    @Autowired
    private ArtifactParamService artifactParamService;

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

    private MockMvc restArtifactParamMockMvc;

    private ArtifactParam artifactParam;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ArtifactParamResource artifactParamResource = new ArtifactParamResource(artifactParamService);
        this.restArtifactParamMockMvc = MockMvcBuilders.standaloneSetup(artifactParamResource)
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
    public static ArtifactParam createEntity(EntityManager em) {
        ArtifactParam artifactParam = new ArtifactParam();
        return artifactParam;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArtifactParam createUpdatedEntity(EntityManager em) {
        ArtifactParam artifactParam = new ArtifactParam();
        return artifactParam;
    }

    @BeforeEach
    public void initTest() {
        artifactParam = createEntity(em);
    }

    @Test
    @Transactional
    public void createArtifactParam() throws Exception {
        int databaseSizeBeforeCreate = artifactParamRepository.findAll().size();

        // Create the ArtifactParam
        ArtifactParamDTO artifactParamDTO = artifactParamMapper.toDto(artifactParam);
        restArtifactParamMockMvc.perform(post("/api/artifact-params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artifactParamDTO)))
            .andExpect(status().isCreated());

        // Validate the ArtifactParam in the database
        List<ArtifactParam> artifactParamList = artifactParamRepository.findAll();
        assertThat(artifactParamList).hasSize(databaseSizeBeforeCreate + 1);
        ArtifactParam testArtifactParam = artifactParamList.get(artifactParamList.size() - 1);
    }

    @Test
    @Transactional
    public void createArtifactParamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = artifactParamRepository.findAll().size();

        // Create the ArtifactParam with an existing ID
        artifactParam.setId(1L);
        ArtifactParamDTO artifactParamDTO = artifactParamMapper.toDto(artifactParam);

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtifactParamMockMvc.perform(post("/api/artifact-params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artifactParamDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ArtifactParam in the database
        List<ArtifactParam> artifactParamList = artifactParamRepository.findAll();
        assertThat(artifactParamList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllArtifactParams() throws Exception {
        // Initialize the database
        artifactParamRepository.saveAndFlush(artifactParam);

        // Get all the artifactParamList
        restArtifactParamMockMvc.perform(get("/api/artifact-params?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artifactParam.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getArtifactParam() throws Exception {
        // Initialize the database
        artifactParamRepository.saveAndFlush(artifactParam);

        // Get the artifactParam
        restArtifactParamMockMvc.perform(get("/api/artifact-params/{id}", artifactParam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(artifactParam.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingArtifactParam() throws Exception {
        // Get the artifactParam
        restArtifactParamMockMvc.perform(get("/api/artifact-params/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateArtifactParam() throws Exception {
        // Initialize the database
        artifactParamRepository.saveAndFlush(artifactParam);

        int databaseSizeBeforeUpdate = artifactParamRepository.findAll().size();

        // Update the artifactParam
        ArtifactParam updatedArtifactParam = artifactParamRepository.findById(artifactParam.getId()).get();
        // Disconnect from session so that the updates on updatedArtifactParam are not directly saved in db
        em.detach(updatedArtifactParam);
        ArtifactParamDTO artifactParamDTO = artifactParamMapper.toDto(updatedArtifactParam);

        restArtifactParamMockMvc.perform(put("/api/artifact-params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artifactParamDTO)))
            .andExpect(status().isOk());

        // Validate the ArtifactParam in the database
        List<ArtifactParam> artifactParamList = artifactParamRepository.findAll();
        assertThat(artifactParamList).hasSize(databaseSizeBeforeUpdate);
        ArtifactParam testArtifactParam = artifactParamList.get(artifactParamList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingArtifactParam() throws Exception {
        int databaseSizeBeforeUpdate = artifactParamRepository.findAll().size();

        // Create the ArtifactParam
        ArtifactParamDTO artifactParamDTO = artifactParamMapper.toDto(artifactParam);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtifactParamMockMvc.perform(put("/api/artifact-params")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(artifactParamDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ArtifactParam in the database
        List<ArtifactParam> artifactParamList = artifactParamRepository.findAll();
        assertThat(artifactParamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteArtifactParam() throws Exception {
        // Initialize the database
        artifactParamRepository.saveAndFlush(artifactParam);

        int databaseSizeBeforeDelete = artifactParamRepository.findAll().size();

        // Delete the artifactParam
        restArtifactParamMockMvc.perform(delete("/api/artifact-params/{id}", artifactParam.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArtifactParam> artifactParamList = artifactParamRepository.findAll();
        assertThat(artifactParamList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArtifactParam.class);
        ArtifactParam artifactParam1 = new ArtifactParam();
        artifactParam1.setId(1L);
        ArtifactParam artifactParam2 = new ArtifactParam();
        artifactParam2.setId(artifactParam1.getId());
        assertThat(artifactParam1).isEqualTo(artifactParam2);
        artifactParam2.setId(2L);
        assertThat(artifactParam1).isNotEqualTo(artifactParam2);
        artifactParam1.setId(null);
        assertThat(artifactParam1).isNotEqualTo(artifactParam2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArtifactParamDTO.class);
        ArtifactParamDTO artifactParamDTO1 = new ArtifactParamDTO();
        artifactParamDTO1.setId(1L);
        ArtifactParamDTO artifactParamDTO2 = new ArtifactParamDTO();
        assertThat(artifactParamDTO1).isNotEqualTo(artifactParamDTO2);
        artifactParamDTO2.setId(artifactParamDTO1.getId());
        assertThat(artifactParamDTO1).isEqualTo(artifactParamDTO2);
        artifactParamDTO2.setId(2L);
        assertThat(artifactParamDTO1).isNotEqualTo(artifactParamDTO2);
        artifactParamDTO1.setId(null);
        assertThat(artifactParamDTO1).isNotEqualTo(artifactParamDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(artifactParamMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(artifactParamMapper.fromId(null)).isNull();
    }
}
