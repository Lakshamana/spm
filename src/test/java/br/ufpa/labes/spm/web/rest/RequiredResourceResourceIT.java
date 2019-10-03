package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.RequiredResource;
import br.ufpa.labes.spm.repository.RequiredResourceRepository;
import br.ufpa.labes.spm.service.RequiredResourceService;
import br.ufpa.labes.spm.service.dto.RequiredResourceDTO;
import br.ufpa.labes.spm.service.mapper.RequiredResourceMapper;
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
 * Integration tests for the {@link RequiredResourceResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class RequiredResourceResourceIT {

    private static final Float DEFAULT_AMOUNT_NEEDED = 1F;
    private static final Float UPDATED_AMOUNT_NEEDED = 2F;
    private static final Float SMALLER_AMOUNT_NEEDED = 1F - 1F;

    @Autowired
    private RequiredResourceRepository requiredResourceRepository;

    @Autowired
    private RequiredResourceMapper requiredResourceMapper;

    @Autowired
    private RequiredResourceService requiredResourceService;

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

    private MockMvc restRequiredResourceMockMvc;

    private RequiredResource requiredResource;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RequiredResourceResource requiredResourceResource = new RequiredResourceResource(requiredResourceService);
        this.restRequiredResourceMockMvc = MockMvcBuilders.standaloneSetup(requiredResourceResource)
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
    public static RequiredResource createEntity(EntityManager em) {
        RequiredResource requiredResource = new RequiredResource()
            .amountNeeded(DEFAULT_AMOUNT_NEEDED);
        return requiredResource;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequiredResource createUpdatedEntity(EntityManager em) {
        RequiredResource requiredResource = new RequiredResource()
            .amountNeeded(UPDATED_AMOUNT_NEEDED);
        return requiredResource;
    }

    @BeforeEach
    public void initTest() {
        requiredResource = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequiredResource() throws Exception {
        int databaseSizeBeforeCreate = requiredResourceRepository.findAll().size();

        // Create the RequiredResource
        RequiredResourceDTO requiredResourceDTO = requiredResourceMapper.toDto(requiredResource);
        restRequiredResourceMockMvc.perform(post("/api/required-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requiredResourceDTO)))
            .andExpect(status().isCreated());

        // Validate the RequiredResource in the database
        List<RequiredResource> requiredResourceList = requiredResourceRepository.findAll();
        assertThat(requiredResourceList).hasSize(databaseSizeBeforeCreate + 1);
        RequiredResource testRequiredResource = requiredResourceList.get(requiredResourceList.size() - 1);
        assertThat(testRequiredResource.getAmountNeeded()).isEqualTo(DEFAULT_AMOUNT_NEEDED);
    }

    @Test
    @Transactional
    public void createRequiredResourceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requiredResourceRepository.findAll().size();

        // Create the RequiredResource with an existing ID
        requiredResource.setId(1L);
        RequiredResourceDTO requiredResourceDTO = requiredResourceMapper.toDto(requiredResource);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequiredResourceMockMvc.perform(post("/api/required-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requiredResourceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RequiredResource in the database
        List<RequiredResource> requiredResourceList = requiredResourceRepository.findAll();
        assertThat(requiredResourceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRequiredResources() throws Exception {
        // Initialize the database
        requiredResourceRepository.saveAndFlush(requiredResource);

        // Get all the requiredResourceList
        restRequiredResourceMockMvc.perform(get("/api/required-resources?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requiredResource.getId().intValue())))
            .andExpect(jsonPath("$.[*].amountNeeded").value(hasItem(DEFAULT_AMOUNT_NEEDED.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getRequiredResource() throws Exception {
        // Initialize the database
        requiredResourceRepository.saveAndFlush(requiredResource);

        // Get the requiredResource
        restRequiredResourceMockMvc.perform(get("/api/required-resources/{id}", requiredResource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(requiredResource.getId().intValue()))
            .andExpect(jsonPath("$.amountNeeded").value(DEFAULT_AMOUNT_NEEDED.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRequiredResource() throws Exception {
        // Get the requiredResource
        restRequiredResourceMockMvc.perform(get("/api/required-resources/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequiredResource() throws Exception {
        // Initialize the database
        requiredResourceRepository.saveAndFlush(requiredResource);

        int databaseSizeBeforeUpdate = requiredResourceRepository.findAll().size();

        // Update the requiredResource
        RequiredResource updatedRequiredResource = requiredResourceRepository.findById(requiredResource.getId()).get();
        // Disconnect from session so that the updates on updatedRequiredResource are not directly saved in db
        em.detach(updatedRequiredResource);
        updatedRequiredResource
            .amountNeeded(UPDATED_AMOUNT_NEEDED);
        RequiredResourceDTO requiredResourceDTO = requiredResourceMapper.toDto(updatedRequiredResource);

        restRequiredResourceMockMvc.perform(put("/api/required-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requiredResourceDTO)))
            .andExpect(status().isOk());

        // Validate the RequiredResource in the database
        List<RequiredResource> requiredResourceList = requiredResourceRepository.findAll();
        assertThat(requiredResourceList).hasSize(databaseSizeBeforeUpdate);
        RequiredResource testRequiredResource = requiredResourceList.get(requiredResourceList.size() - 1);
        assertThat(testRequiredResource.getAmountNeeded()).isEqualTo(UPDATED_AMOUNT_NEEDED);
    }

    @Test
    @Transactional
    public void updateNonExistingRequiredResource() throws Exception {
        int databaseSizeBeforeUpdate = requiredResourceRepository.findAll().size();

        // Create the RequiredResource
        RequiredResourceDTO requiredResourceDTO = requiredResourceMapper.toDto(requiredResource);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequiredResourceMockMvc.perform(put("/api/required-resources")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requiredResourceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RequiredResource in the database
        List<RequiredResource> requiredResourceList = requiredResourceRepository.findAll();
        assertThat(requiredResourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRequiredResource() throws Exception {
        // Initialize the database
        requiredResourceRepository.saveAndFlush(requiredResource);

        int databaseSizeBeforeDelete = requiredResourceRepository.findAll().size();

        // Delete the requiredResource
        restRequiredResourceMockMvc.perform(delete("/api/required-resources/{id}", requiredResource.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RequiredResource> requiredResourceList = requiredResourceRepository.findAll();
        assertThat(requiredResourceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequiredResource.class);
        RequiredResource requiredResource1 = new RequiredResource();
        requiredResource1.setId(1L);
        RequiredResource requiredResource2 = new RequiredResource();
        requiredResource2.setId(requiredResource1.getId());
        assertThat(requiredResource1).isEqualTo(requiredResource2);
        requiredResource2.setId(2L);
        assertThat(requiredResource1).isNotEqualTo(requiredResource2);
        requiredResource1.setId(null);
        assertThat(requiredResource1).isNotEqualTo(requiredResource2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequiredResourceDTO.class);
        RequiredResourceDTO requiredResourceDTO1 = new RequiredResourceDTO();
        requiredResourceDTO1.setId(1L);
        RequiredResourceDTO requiredResourceDTO2 = new RequiredResourceDTO();
        assertThat(requiredResourceDTO1).isNotEqualTo(requiredResourceDTO2);
        requiredResourceDTO2.setId(requiredResourceDTO1.getId());
        assertThat(requiredResourceDTO1).isEqualTo(requiredResourceDTO2);
        requiredResourceDTO2.setId(2L);
        assertThat(requiredResourceDTO1).isNotEqualTo(requiredResourceDTO2);
        requiredResourceDTO1.setId(null);
        assertThat(requiredResourceDTO1).isNotEqualTo(requiredResourceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(requiredResourceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(requiredResourceMapper.fromId(null)).isNull();
    }
}
