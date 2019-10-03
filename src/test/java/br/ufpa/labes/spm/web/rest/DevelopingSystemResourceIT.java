package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.DevelopingSystem;
import br.ufpa.labes.spm.repository.DevelopingSystemRepository;
import br.ufpa.labes.spm.service.DevelopingSystemService;
import br.ufpa.labes.spm.service.dto.DevelopingSystemDTO;
import br.ufpa.labes.spm.service.mapper.DevelopingSystemMapper;
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
 * Integration tests for the {@link DevelopingSystemResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class DevelopingSystemResourceIT {

    private static final String DEFAULT_IDENT = "AAAAAAAAAA";
    private static final String UPDATED_IDENT = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private DevelopingSystemRepository developingSystemRepository;

    @Autowired
    private DevelopingSystemMapper developingSystemMapper;

    @Autowired
    private DevelopingSystemService developingSystemService;

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

    private MockMvc restDevelopingSystemMockMvc;

    private DevelopingSystem developingSystem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DevelopingSystemResource developingSystemResource = new DevelopingSystemResource(developingSystemService);
        this.restDevelopingSystemMockMvc = MockMvcBuilders.standaloneSetup(developingSystemResource)
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
    public static DevelopingSystem createEntity(EntityManager em) {
        DevelopingSystem developingSystem = new DevelopingSystem()
            .ident(DEFAULT_IDENT)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return developingSystem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DevelopingSystem createUpdatedEntity(EntityManager em) {
        DevelopingSystem developingSystem = new DevelopingSystem()
            .ident(UPDATED_IDENT)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        return developingSystem;
    }

    @BeforeEach
    public void initTest() {
        developingSystem = createEntity(em);
    }

    @Test
    @Transactional
    public void createDevelopingSystem() throws Exception {
        int databaseSizeBeforeCreate = developingSystemRepository.findAll().size();

        // Create the DevelopingSystem
        DevelopingSystemDTO developingSystemDTO = developingSystemMapper.toDto(developingSystem);
        restDevelopingSystemMockMvc.perform(post("/api/developing-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developingSystemDTO)))
            .andExpect(status().isCreated());

        // Validate the DevelopingSystem in the database
        List<DevelopingSystem> developingSystemList = developingSystemRepository.findAll();
        assertThat(developingSystemList).hasSize(databaseSizeBeforeCreate + 1);
        DevelopingSystem testDevelopingSystem = developingSystemList.get(developingSystemList.size() - 1);
        assertThat(testDevelopingSystem.getIdent()).isEqualTo(DEFAULT_IDENT);
        assertThat(testDevelopingSystem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDevelopingSystem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createDevelopingSystemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = developingSystemRepository.findAll().size();

        // Create the DevelopingSystem with an existing ID
        developingSystem.setId(1L);
        DevelopingSystemDTO developingSystemDTO = developingSystemMapper.toDto(developingSystem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDevelopingSystemMockMvc.perform(post("/api/developing-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developingSystemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DevelopingSystem in the database
        List<DevelopingSystem> developingSystemList = developingSystemRepository.findAll();
        assertThat(developingSystemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDevelopingSystems() throws Exception {
        // Initialize the database
        developingSystemRepository.saveAndFlush(developingSystem);

        // Get all the developingSystemList
        restDevelopingSystemMockMvc.perform(get("/api/developing-systems?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(developingSystem.getId().intValue())))
            .andExpect(jsonPath("$.[*].ident").value(hasItem(DEFAULT_IDENT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getDevelopingSystem() throws Exception {
        // Initialize the database
        developingSystemRepository.saveAndFlush(developingSystem);

        // Get the developingSystem
        restDevelopingSystemMockMvc.perform(get("/api/developing-systems/{id}", developingSystem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(developingSystem.getId().intValue()))
            .andExpect(jsonPath("$.ident").value(DEFAULT_IDENT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDevelopingSystem() throws Exception {
        // Get the developingSystem
        restDevelopingSystemMockMvc.perform(get("/api/developing-systems/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDevelopingSystem() throws Exception {
        // Initialize the database
        developingSystemRepository.saveAndFlush(developingSystem);

        int databaseSizeBeforeUpdate = developingSystemRepository.findAll().size();

        // Update the developingSystem
        DevelopingSystem updatedDevelopingSystem = developingSystemRepository.findById(developingSystem.getId()).get();
        // Disconnect from session so that the updates on updatedDevelopingSystem are not directly saved in db
        em.detach(updatedDevelopingSystem);
        updatedDevelopingSystem
            .ident(UPDATED_IDENT)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        DevelopingSystemDTO developingSystemDTO = developingSystemMapper.toDto(updatedDevelopingSystem);

        restDevelopingSystemMockMvc.perform(put("/api/developing-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developingSystemDTO)))
            .andExpect(status().isOk());

        // Validate the DevelopingSystem in the database
        List<DevelopingSystem> developingSystemList = developingSystemRepository.findAll();
        assertThat(developingSystemList).hasSize(databaseSizeBeforeUpdate);
        DevelopingSystem testDevelopingSystem = developingSystemList.get(developingSystemList.size() - 1);
        assertThat(testDevelopingSystem.getIdent()).isEqualTo(UPDATED_IDENT);
        assertThat(testDevelopingSystem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDevelopingSystem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingDevelopingSystem() throws Exception {
        int databaseSizeBeforeUpdate = developingSystemRepository.findAll().size();

        // Create the DevelopingSystem
        DevelopingSystemDTO developingSystemDTO = developingSystemMapper.toDto(developingSystem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDevelopingSystemMockMvc.perform(put("/api/developing-systems")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(developingSystemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DevelopingSystem in the database
        List<DevelopingSystem> developingSystemList = developingSystemRepository.findAll();
        assertThat(developingSystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDevelopingSystem() throws Exception {
        // Initialize the database
        developingSystemRepository.saveAndFlush(developingSystem);

        int databaseSizeBeforeDelete = developingSystemRepository.findAll().size();

        // Delete the developingSystem
        restDevelopingSystemMockMvc.perform(delete("/api/developing-systems/{id}", developingSystem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DevelopingSystem> developingSystemList = developingSystemRepository.findAll();
        assertThat(developingSystemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DevelopingSystem.class);
        DevelopingSystem developingSystem1 = new DevelopingSystem();
        developingSystem1.setId(1L);
        DevelopingSystem developingSystem2 = new DevelopingSystem();
        developingSystem2.setId(developingSystem1.getId());
        assertThat(developingSystem1).isEqualTo(developingSystem2);
        developingSystem2.setId(2L);
        assertThat(developingSystem1).isNotEqualTo(developingSystem2);
        developingSystem1.setId(null);
        assertThat(developingSystem1).isNotEqualTo(developingSystem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DevelopingSystemDTO.class);
        DevelopingSystemDTO developingSystemDTO1 = new DevelopingSystemDTO();
        developingSystemDTO1.setId(1L);
        DevelopingSystemDTO developingSystemDTO2 = new DevelopingSystemDTO();
        assertThat(developingSystemDTO1).isNotEqualTo(developingSystemDTO2);
        developingSystemDTO2.setId(developingSystemDTO1.getId());
        assertThat(developingSystemDTO1).isEqualTo(developingSystemDTO2);
        developingSystemDTO2.setId(2L);
        assertThat(developingSystemDTO1).isNotEqualTo(developingSystemDTO2);
        developingSystemDTO1.setId(null);
        assertThat(developingSystemDTO1).isNotEqualTo(developingSystemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(developingSystemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(developingSystemMapper.fromId(null)).isNull();
    }
}
