package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.PrimitiveType;
import br.ufpa.labes.spm.repository.PrimitiveTypeRepository;
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
 * Integration tests for the {@link PrimitiveTypeResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class PrimitiveTypeResourceIT {

    private static final String DEFAULT_IDENT = "AAAAAAAAAA";
    private static final String UPDATED_IDENT = "BBBBBBBBBB";

    @Autowired
    private PrimitiveTypeRepository primitiveTypeRepository;

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

    private MockMvc restPrimitiveTypeMockMvc;

    private PrimitiveType primitiveType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PrimitiveTypeResource primitiveTypeResource = new PrimitiveTypeResource(primitiveTypeRepository);
        this.restPrimitiveTypeMockMvc = MockMvcBuilders.standaloneSetup(primitiveTypeResource)
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
    public static PrimitiveType createEntity(EntityManager em) {
        PrimitiveType primitiveType = new PrimitiveType()
            .ident(DEFAULT_IDENT);
        return primitiveType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrimitiveType createUpdatedEntity(EntityManager em) {
        PrimitiveType primitiveType = new PrimitiveType()
            .ident(UPDATED_IDENT);
        return primitiveType;
    }

    @BeforeEach
    public void initTest() {
        primitiveType = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrimitiveType() throws Exception {
        int databaseSizeBeforeCreate = primitiveTypeRepository.findAll().size();

        // Create the PrimitiveType
        restPrimitiveTypeMockMvc.perform(post("/api/primitive-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(primitiveType)))
            .andExpect(status().isCreated());

        // Validate the PrimitiveType in the database
        List<PrimitiveType> primitiveTypeList = primitiveTypeRepository.findAll();
        assertThat(primitiveTypeList).hasSize(databaseSizeBeforeCreate + 1);
        PrimitiveType testPrimitiveType = primitiveTypeList.get(primitiveTypeList.size() - 1);
        assertThat(testPrimitiveType.getIdent()).isEqualTo(DEFAULT_IDENT);
    }

    @Test
    @Transactional
    public void createPrimitiveTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = primitiveTypeRepository.findAll().size();

        // Create the PrimitiveType with an existing ID
        primitiveType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrimitiveTypeMockMvc.perform(post("/api/primitive-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(primitiveType)))
            .andExpect(status().isBadRequest());

        // Validate the PrimitiveType in the database
        List<PrimitiveType> primitiveTypeList = primitiveTypeRepository.findAll();
        assertThat(primitiveTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPrimitiveTypes() throws Exception {
        // Initialize the database
        primitiveTypeRepository.saveAndFlush(primitiveType);

        // Get all the primitiveTypeList
        restPrimitiveTypeMockMvc.perform(get("/api/primitive-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(primitiveType.getId().intValue())))
            .andExpect(jsonPath("$.[*].ident").value(hasItem(DEFAULT_IDENT.toString())));
    }
    
    @Test
    @Transactional
    public void getPrimitiveType() throws Exception {
        // Initialize the database
        primitiveTypeRepository.saveAndFlush(primitiveType);

        // Get the primitiveType
        restPrimitiveTypeMockMvc.perform(get("/api/primitive-types/{id}", primitiveType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(primitiveType.getId().intValue()))
            .andExpect(jsonPath("$.ident").value(DEFAULT_IDENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPrimitiveType() throws Exception {
        // Get the primitiveType
        restPrimitiveTypeMockMvc.perform(get("/api/primitive-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrimitiveType() throws Exception {
        // Initialize the database
        primitiveTypeRepository.saveAndFlush(primitiveType);

        int databaseSizeBeforeUpdate = primitiveTypeRepository.findAll().size();

        // Update the primitiveType
        PrimitiveType updatedPrimitiveType = primitiveTypeRepository.findById(primitiveType.getId()).get();
        // Disconnect from session so that the updates on updatedPrimitiveType are not directly saved in db
        em.detach(updatedPrimitiveType);
        updatedPrimitiveType
            .ident(UPDATED_IDENT);

        restPrimitiveTypeMockMvc.perform(put("/api/primitive-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPrimitiveType)))
            .andExpect(status().isOk());

        // Validate the PrimitiveType in the database
        List<PrimitiveType> primitiveTypeList = primitiveTypeRepository.findAll();
        assertThat(primitiveTypeList).hasSize(databaseSizeBeforeUpdate);
        PrimitiveType testPrimitiveType = primitiveTypeList.get(primitiveTypeList.size() - 1);
        assertThat(testPrimitiveType.getIdent()).isEqualTo(UPDATED_IDENT);
    }

    @Test
    @Transactional
    public void updateNonExistingPrimitiveType() throws Exception {
        int databaseSizeBeforeUpdate = primitiveTypeRepository.findAll().size();

        // Create the PrimitiveType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPrimitiveTypeMockMvc.perform(put("/api/primitive-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(primitiveType)))
            .andExpect(status().isBadRequest());

        // Validate the PrimitiveType in the database
        List<PrimitiveType> primitiveTypeList = primitiveTypeRepository.findAll();
        assertThat(primitiveTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePrimitiveType() throws Exception {
        // Initialize the database
        primitiveTypeRepository.saveAndFlush(primitiveType);

        int databaseSizeBeforeDelete = primitiveTypeRepository.findAll().size();

        // Delete the primitiveType
        restPrimitiveTypeMockMvc.perform(delete("/api/primitive-types/{id}", primitiveType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PrimitiveType> primitiveTypeList = primitiveTypeRepository.findAll();
        assertThat(primitiveTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrimitiveType.class);
        PrimitiveType primitiveType1 = new PrimitiveType();
        primitiveType1.setId(1L);
        PrimitiveType primitiveType2 = new PrimitiveType();
        primitiveType2.setId(primitiveType1.getId());
        assertThat(primitiveType1).isEqualTo(primitiveType2);
        primitiveType2.setId(2L);
        assertThat(primitiveType1).isNotEqualTo(primitiveType2);
        primitiveType1.setId(null);
        assertThat(primitiveType1).isNotEqualTo(primitiveType2);
    }
}
