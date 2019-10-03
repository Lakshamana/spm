package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.OrganizationEstimation;
import br.ufpa.labes.spm.repository.OrganizationEstimationRepository;
import br.ufpa.labes.spm.service.OrganizationEstimationService;
import br.ufpa.labes.spm.service.dto.OrganizationEstimationDTO;
import br.ufpa.labes.spm.service.mapper.OrganizationEstimationMapper;
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
 * Integration tests for the {@link OrganizationEstimationResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class OrganizationEstimationResourceIT {

    @Autowired
    private OrganizationEstimationRepository organizationEstimationRepository;

    @Autowired
    private OrganizationEstimationMapper organizationEstimationMapper;

    @Autowired
    private OrganizationEstimationService organizationEstimationService;

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

    private MockMvc restOrganizationEstimationMockMvc;

    private OrganizationEstimation organizationEstimation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OrganizationEstimationResource organizationEstimationResource = new OrganizationEstimationResource(organizationEstimationService);
        this.restOrganizationEstimationMockMvc = MockMvcBuilders.standaloneSetup(organizationEstimationResource)
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
    public static OrganizationEstimation createEntity(EntityManager em) {
        OrganizationEstimation organizationEstimation = new OrganizationEstimation();
        return organizationEstimation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrganizationEstimation createUpdatedEntity(EntityManager em) {
        OrganizationEstimation organizationEstimation = new OrganizationEstimation();
        return organizationEstimation;
    }

    @BeforeEach
    public void initTest() {
        organizationEstimation = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrganizationEstimation() throws Exception {
        int databaseSizeBeforeCreate = organizationEstimationRepository.findAll().size();

        // Create the OrganizationEstimation
        OrganizationEstimationDTO organizationEstimationDTO = organizationEstimationMapper.toDto(organizationEstimation);
        restOrganizationEstimationMockMvc.perform(post("/api/organization-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationEstimationDTO)))
            .andExpect(status().isCreated());

        // Validate the OrganizationEstimation in the database
        List<OrganizationEstimation> organizationEstimationList = organizationEstimationRepository.findAll();
        assertThat(organizationEstimationList).hasSize(databaseSizeBeforeCreate + 1);
        OrganizationEstimation testOrganizationEstimation = organizationEstimationList.get(organizationEstimationList.size() - 1);
    }

    @Test
    @Transactional
    public void createOrganizationEstimationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = organizationEstimationRepository.findAll().size();

        // Create the OrganizationEstimation with an existing ID
        organizationEstimation.setId(1L);
        OrganizationEstimationDTO organizationEstimationDTO = organizationEstimationMapper.toDto(organizationEstimation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrganizationEstimationMockMvc.perform(post("/api/organization-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationEstimationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrganizationEstimation in the database
        List<OrganizationEstimation> organizationEstimationList = organizationEstimationRepository.findAll();
        assertThat(organizationEstimationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOrganizationEstimations() throws Exception {
        // Initialize the database
        organizationEstimationRepository.saveAndFlush(organizationEstimation);

        // Get all the organizationEstimationList
        restOrganizationEstimationMockMvc.perform(get("/api/organization-estimations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(organizationEstimation.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getOrganizationEstimation() throws Exception {
        // Initialize the database
        organizationEstimationRepository.saveAndFlush(organizationEstimation);

        // Get the organizationEstimation
        restOrganizationEstimationMockMvc.perform(get("/api/organization-estimations/{id}", organizationEstimation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(organizationEstimation.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOrganizationEstimation() throws Exception {
        // Get the organizationEstimation
        restOrganizationEstimationMockMvc.perform(get("/api/organization-estimations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrganizationEstimation() throws Exception {
        // Initialize the database
        organizationEstimationRepository.saveAndFlush(organizationEstimation);

        int databaseSizeBeforeUpdate = organizationEstimationRepository.findAll().size();

        // Update the organizationEstimation
        OrganizationEstimation updatedOrganizationEstimation = organizationEstimationRepository.findById(organizationEstimation.getId()).get();
        // Disconnect from session so that the updates on updatedOrganizationEstimation are not directly saved in db
        em.detach(updatedOrganizationEstimation);
        OrganizationEstimationDTO organizationEstimationDTO = organizationEstimationMapper.toDto(updatedOrganizationEstimation);

        restOrganizationEstimationMockMvc.perform(put("/api/organization-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationEstimationDTO)))
            .andExpect(status().isOk());

        // Validate the OrganizationEstimation in the database
        List<OrganizationEstimation> organizationEstimationList = organizationEstimationRepository.findAll();
        assertThat(organizationEstimationList).hasSize(databaseSizeBeforeUpdate);
        OrganizationEstimation testOrganizationEstimation = organizationEstimationList.get(organizationEstimationList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingOrganizationEstimation() throws Exception {
        int databaseSizeBeforeUpdate = organizationEstimationRepository.findAll().size();

        // Create the OrganizationEstimation
        OrganizationEstimationDTO organizationEstimationDTO = organizationEstimationMapper.toDto(organizationEstimation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrganizationEstimationMockMvc.perform(put("/api/organization-estimations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(organizationEstimationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrganizationEstimation in the database
        List<OrganizationEstimation> organizationEstimationList = organizationEstimationRepository.findAll();
        assertThat(organizationEstimationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOrganizationEstimation() throws Exception {
        // Initialize the database
        organizationEstimationRepository.saveAndFlush(organizationEstimation);

        int databaseSizeBeforeDelete = organizationEstimationRepository.findAll().size();

        // Delete the organizationEstimation
        restOrganizationEstimationMockMvc.perform(delete("/api/organization-estimations/{id}", organizationEstimation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrganizationEstimation> organizationEstimationList = organizationEstimationRepository.findAll();
        assertThat(organizationEstimationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationEstimation.class);
        OrganizationEstimation organizationEstimation1 = new OrganizationEstimation();
        organizationEstimation1.setId(1L);
        OrganizationEstimation organizationEstimation2 = new OrganizationEstimation();
        organizationEstimation2.setId(organizationEstimation1.getId());
        assertThat(organizationEstimation1).isEqualTo(organizationEstimation2);
        organizationEstimation2.setId(2L);
        assertThat(organizationEstimation1).isNotEqualTo(organizationEstimation2);
        organizationEstimation1.setId(null);
        assertThat(organizationEstimation1).isNotEqualTo(organizationEstimation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganizationEstimationDTO.class);
        OrganizationEstimationDTO organizationEstimationDTO1 = new OrganizationEstimationDTO();
        organizationEstimationDTO1.setId(1L);
        OrganizationEstimationDTO organizationEstimationDTO2 = new OrganizationEstimationDTO();
        assertThat(organizationEstimationDTO1).isNotEqualTo(organizationEstimationDTO2);
        organizationEstimationDTO2.setId(organizationEstimationDTO1.getId());
        assertThat(organizationEstimationDTO1).isEqualTo(organizationEstimationDTO2);
        organizationEstimationDTO2.setId(2L);
        assertThat(organizationEstimationDTO1).isNotEqualTo(organizationEstimationDTO2);
        organizationEstimationDTO1.setId(null);
        assertThat(organizationEstimationDTO1).isNotEqualTo(organizationEstimationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(organizationEstimationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(organizationEstimationMapper.fromId(null)).isNull();
    }
}
