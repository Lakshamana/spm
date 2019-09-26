package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ResourceInstSug;
import br.ufpa.labes.spm.repository.ResourceInstSugRepository;
import br.ufpa.labes.spm.service.ResourceInstSugService;
import br.ufpa.labes.spm.service.dto.ResourceInstSugDTO;
import br.ufpa.labes.spm.service.mapper.ResourceInstSugMapper;
import br.ufpa.labes.spm.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static br.ufpa.labes.spm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ResourceInstSugResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ResourceInstSugResourceIT {

    @Autowired
    private ResourceInstSugRepository resourceInstSugRepository;

    @Mock
    private ResourceInstSugRepository resourceInstSugRepositoryMock;

    @Autowired
    private ResourceInstSugMapper resourceInstSugMapper;

    @Mock
    private ResourceInstSugService resourceInstSugServiceMock;

    @Autowired
    private ResourceInstSugService resourceInstSugService;

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

    private MockMvc restResourceInstSugMockMvc;

    private ResourceInstSug resourceInstSug;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ResourceInstSugResource resourceInstSugResource = new ResourceInstSugResource(resourceInstSugService);
        this.restResourceInstSugMockMvc = MockMvcBuilders.standaloneSetup(resourceInstSugResource)
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
    public static ResourceInstSug createEntity(EntityManager em) {
        ResourceInstSug resourceInstSug = new ResourceInstSug();
        return resourceInstSug;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceInstSug createUpdatedEntity(EntityManager em) {
        ResourceInstSug resourceInstSug = new ResourceInstSug();
        return resourceInstSug;
    }

    @BeforeEach
    public void initTest() {
        resourceInstSug = createEntity(em);
    }

    @Test
    @Transactional
    public void createResourceInstSug() throws Exception {
        int databaseSizeBeforeCreate = resourceInstSugRepository.findAll().size();

        // Create the ResourceInstSug
        ResourceInstSugDTO resourceInstSugDTO = resourceInstSugMapper.toDto(resourceInstSug);
        restResourceInstSugMockMvc.perform(post("/api/resource-inst-sugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceInstSugDTO)))
            .andExpect(status().isCreated());

        // Validate the ResourceInstSug in the database
        List<ResourceInstSug> resourceInstSugList = resourceInstSugRepository.findAll();
        assertThat(resourceInstSugList).hasSize(databaseSizeBeforeCreate + 1);
        ResourceInstSug testResourceInstSug = resourceInstSugList.get(resourceInstSugList.size() - 1);
    }

    @Test
    @Transactional
    public void createResourceInstSugWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resourceInstSugRepository.findAll().size();

        // Create the ResourceInstSug with an existing ID
        resourceInstSug.setId(1L);
        ResourceInstSugDTO resourceInstSugDTO = resourceInstSugMapper.toDto(resourceInstSug);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceInstSugMockMvc.perform(post("/api/resource-inst-sugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceInstSugDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ResourceInstSug in the database
        List<ResourceInstSug> resourceInstSugList = resourceInstSugRepository.findAll();
        assertThat(resourceInstSugList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllResourceInstSugs() throws Exception {
        // Initialize the database
        resourceInstSugRepository.saveAndFlush(resourceInstSug);

        // Get all the resourceInstSugList
        restResourceInstSugMockMvc.perform(get("/api/resource-inst-sugs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceInstSug.getId().intValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllResourceInstSugsWithEagerRelationshipsIsEnabled() throws Exception {
        ResourceInstSugResource resourceInstSugResource = new ResourceInstSugResource(resourceInstSugServiceMock);
        when(resourceInstSugServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restResourceInstSugMockMvc = MockMvcBuilders.standaloneSetup(resourceInstSugResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restResourceInstSugMockMvc.perform(get("/api/resource-inst-sugs?eagerload=true"))
        .andExpect(status().isOk());

        verify(resourceInstSugServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllResourceInstSugsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ResourceInstSugResource resourceInstSugResource = new ResourceInstSugResource(resourceInstSugServiceMock);
            when(resourceInstSugServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restResourceInstSugMockMvc = MockMvcBuilders.standaloneSetup(resourceInstSugResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restResourceInstSugMockMvc.perform(get("/api/resource-inst-sugs?eagerload=true"))
        .andExpect(status().isOk());

            verify(resourceInstSugServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getResourceInstSug() throws Exception {
        // Initialize the database
        resourceInstSugRepository.saveAndFlush(resourceInstSug);

        // Get the resourceInstSug
        restResourceInstSugMockMvc.perform(get("/api/resource-inst-sugs/{id}", resourceInstSug.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resourceInstSug.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingResourceInstSug() throws Exception {
        // Get the resourceInstSug
        restResourceInstSugMockMvc.perform(get("/api/resource-inst-sugs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResourceInstSug() throws Exception {
        // Initialize the database
        resourceInstSugRepository.saveAndFlush(resourceInstSug);

        int databaseSizeBeforeUpdate = resourceInstSugRepository.findAll().size();

        // Update the resourceInstSug
        ResourceInstSug updatedResourceInstSug = resourceInstSugRepository.findById(resourceInstSug.getId()).get();
        // Disconnect from session so that the updates on updatedResourceInstSug are not directly saved in db
        em.detach(updatedResourceInstSug);
        ResourceInstSugDTO resourceInstSugDTO = resourceInstSugMapper.toDto(updatedResourceInstSug);

        restResourceInstSugMockMvc.perform(put("/api/resource-inst-sugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceInstSugDTO)))
            .andExpect(status().isOk());

        // Validate the ResourceInstSug in the database
        List<ResourceInstSug> resourceInstSugList = resourceInstSugRepository.findAll();
        assertThat(resourceInstSugList).hasSize(databaseSizeBeforeUpdate);
        ResourceInstSug testResourceInstSug = resourceInstSugList.get(resourceInstSugList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingResourceInstSug() throws Exception {
        int databaseSizeBeforeUpdate = resourceInstSugRepository.findAll().size();

        // Create the ResourceInstSug
        ResourceInstSugDTO resourceInstSugDTO = resourceInstSugMapper.toDto(resourceInstSug);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceInstSugMockMvc.perform(put("/api/resource-inst-sugs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resourceInstSugDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ResourceInstSug in the database
        List<ResourceInstSug> resourceInstSugList = resourceInstSugRepository.findAll();
        assertThat(resourceInstSugList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteResourceInstSug() throws Exception {
        // Initialize the database
        resourceInstSugRepository.saveAndFlush(resourceInstSug);

        int databaseSizeBeforeDelete = resourceInstSugRepository.findAll().size();

        // Delete the resourceInstSug
        restResourceInstSugMockMvc.perform(delete("/api/resource-inst-sugs/{id}", resourceInstSug.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ResourceInstSug> resourceInstSugList = resourceInstSugRepository.findAll();
        assertThat(resourceInstSugList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceInstSug.class);
        ResourceInstSug resourceInstSug1 = new ResourceInstSug();
        resourceInstSug1.setId(1L);
        ResourceInstSug resourceInstSug2 = new ResourceInstSug();
        resourceInstSug2.setId(resourceInstSug1.getId());
        assertThat(resourceInstSug1).isEqualTo(resourceInstSug2);
        resourceInstSug2.setId(2L);
        assertThat(resourceInstSug1).isNotEqualTo(resourceInstSug2);
        resourceInstSug1.setId(null);
        assertThat(resourceInstSug1).isNotEqualTo(resourceInstSug2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceInstSugDTO.class);
        ResourceInstSugDTO resourceInstSugDTO1 = new ResourceInstSugDTO();
        resourceInstSugDTO1.setId(1L);
        ResourceInstSugDTO resourceInstSugDTO2 = new ResourceInstSugDTO();
        assertThat(resourceInstSugDTO1).isNotEqualTo(resourceInstSugDTO2);
        resourceInstSugDTO2.setId(resourceInstSugDTO1.getId());
        assertThat(resourceInstSugDTO1).isEqualTo(resourceInstSugDTO2);
        resourceInstSugDTO2.setId(2L);
        assertThat(resourceInstSugDTO1).isNotEqualTo(resourceInstSugDTO2);
        resourceInstSugDTO1.setId(null);
        assertThat(resourceInstSugDTO1).isNotEqualTo(resourceInstSugDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(resourceInstSugMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(resourceInstSugMapper.fromId(null)).isNull();
    }
}
