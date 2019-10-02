package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.AbilityType;
import br.ufpa.labes.spm.repository.AbilityTypeRepository;
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
 * Integration tests for the {@link AbilityTypeResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AbilityTypeResourceIT {

    @Autowired
    private AbilityTypeRepository abilityTypeRepository;

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

    private MockMvc restAbilityTypeMockMvc;

    private AbilityType abilityType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AbilityTypeResource abilityTypeResource = new AbilityTypeResource(abilityTypeRepository);
        this.restAbilityTypeMockMvc = MockMvcBuilders.standaloneSetup(abilityTypeResource)
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
    public static AbilityType createEntity(EntityManager em) {
        AbilityType abilityType = new AbilityType();
        return abilityType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AbilityType createUpdatedEntity(EntityManager em) {
        AbilityType abilityType = new AbilityType();
        return abilityType;
    }

    @BeforeEach
    public void initTest() {
        abilityType = createEntity(em);
    }

    @Test
    @Transactional
    public void createAbilityType() throws Exception {
        int databaseSizeBeforeCreate = abilityTypeRepository.findAll().size();

        // Create the AbilityType
        restAbilityTypeMockMvc.perform(post("/api/ability-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(abilityType)))
            .andExpect(status().isCreated());

        // Validate the AbilityType in the database
        List<AbilityType> abilityTypeList = abilityTypeRepository.findAll();
        assertThat(abilityTypeList).hasSize(databaseSizeBeforeCreate + 1);
        AbilityType testAbilityType = abilityTypeList.get(abilityTypeList.size() - 1);
    }

    @Test
    @Transactional
    public void createAbilityTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = abilityTypeRepository.findAll().size();

        // Create the AbilityType with an existing ID
        abilityType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAbilityTypeMockMvc.perform(post("/api/ability-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(abilityType)))
            .andExpect(status().isBadRequest());

        // Validate the AbilityType in the database
        List<AbilityType> abilityTypeList = abilityTypeRepository.findAll();
        assertThat(abilityTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAbilityTypes() throws Exception {
        // Initialize the database
        abilityTypeRepository.saveAndFlush(abilityType);

        // Get all the abilityTypeList
        restAbilityTypeMockMvc.perform(get("/api/ability-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(abilityType.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getAbilityType() throws Exception {
        // Initialize the database
        abilityTypeRepository.saveAndFlush(abilityType);

        // Get the abilityType
        restAbilityTypeMockMvc.perform(get("/api/ability-types/{id}", abilityType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(abilityType.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAbilityType() throws Exception {
        // Get the abilityType
        restAbilityTypeMockMvc.perform(get("/api/ability-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAbilityType() throws Exception {
        // Initialize the database
        abilityTypeRepository.saveAndFlush(abilityType);

        int databaseSizeBeforeUpdate = abilityTypeRepository.findAll().size();

        // Update the abilityType
        AbilityType updatedAbilityType = abilityTypeRepository.findById(abilityType.getId()).get();
        // Disconnect from session so that the updates on updatedAbilityType are not directly saved in db
        em.detach(updatedAbilityType);

        restAbilityTypeMockMvc.perform(put("/api/ability-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAbilityType)))
            .andExpect(status().isOk());

        // Validate the AbilityType in the database
        List<AbilityType> abilityTypeList = abilityTypeRepository.findAll();
        assertThat(abilityTypeList).hasSize(databaseSizeBeforeUpdate);
        AbilityType testAbilityType = abilityTypeList.get(abilityTypeList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingAbilityType() throws Exception {
        int databaseSizeBeforeUpdate = abilityTypeRepository.findAll().size();

        // Create the AbilityType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAbilityTypeMockMvc.perform(put("/api/ability-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(abilityType)))
            .andExpect(status().isBadRequest());

        // Validate the AbilityType in the database
        List<AbilityType> abilityTypeList = abilityTypeRepository.findAll();
        assertThat(abilityTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAbilityType() throws Exception {
        // Initialize the database
        abilityTypeRepository.saveAndFlush(abilityType);

        int databaseSizeBeforeDelete = abilityTypeRepository.findAll().size();

        // Delete the abilityType
        restAbilityTypeMockMvc.perform(delete("/api/ability-types/{id}", abilityType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AbilityType> abilityTypeList = abilityTypeRepository.findAll();
        assertThat(abilityTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AbilityType.class);
        AbilityType abilityType1 = new AbilityType();
        abilityType1.setId(1L);
        AbilityType abilityType2 = new AbilityType();
        abilityType2.setId(abilityType1.getId());
        assertThat(abilityType1).isEqualTo(abilityType2);
        abilityType2.setId(2L);
        assertThat(abilityType1).isNotEqualTo(abilityType2);
        abilityType1.setId(null);
        assertThat(abilityType1).isNotEqualTo(abilityType2);
    }
}
