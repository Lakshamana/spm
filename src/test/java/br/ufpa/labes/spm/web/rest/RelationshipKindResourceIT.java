package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.RelationshipKind;
import br.ufpa.labes.spm.repository.RelationshipKindRepository;
import br.ufpa.labes.spm.service.RelationshipKindService;
import br.ufpa.labes.spm.service.dto.RelationshipKindDTO;
import br.ufpa.labes.spm.service.mapper.RelationshipKindMapper;
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
 * Integration tests for the {@link RelationshipKindResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class RelationshipKindResourceIT {

    private static final String DEFAULT_TYPE_IDENT = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_IDENT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private RelationshipKindRepository relationshipKindRepository;

    @Autowired
    private RelationshipKindMapper relationshipKindMapper;

    @Autowired
    private RelationshipKindService relationshipKindService;

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

    private MockMvc restRelationshipKindMockMvc;

    private RelationshipKind relationshipKind;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RelationshipKindResource relationshipKindResource = new RelationshipKindResource(relationshipKindService);
        this.restRelationshipKindMockMvc = MockMvcBuilders.standaloneSetup(relationshipKindResource)
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
    public static RelationshipKind createEntity(EntityManager em) {
        RelationshipKind relationshipKind = new RelationshipKind()
            .typeIdent(DEFAULT_TYPE_IDENT)
            .description(DEFAULT_DESCRIPTION);
        return relationshipKind;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RelationshipKind createUpdatedEntity(EntityManager em) {
        RelationshipKind relationshipKind = new RelationshipKind()
            .typeIdent(UPDATED_TYPE_IDENT)
            .description(UPDATED_DESCRIPTION);
        return relationshipKind;
    }

    @BeforeEach
    public void initTest() {
        relationshipKind = createEntity(em);
    }

    @Test
    @Transactional
    public void createRelationshipKind() throws Exception {
        int databaseSizeBeforeCreate = relationshipKindRepository.findAll().size();

        // Create the RelationshipKind
        RelationshipKindDTO relationshipKindDTO = relationshipKindMapper.toDto(relationshipKind);
        restRelationshipKindMockMvc.perform(post("/api/relationship-kinds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relationshipKindDTO)))
            .andExpect(status().isCreated());

        // Validate the RelationshipKind in the database
        List<RelationshipKind> relationshipKindList = relationshipKindRepository.findAll();
        assertThat(relationshipKindList).hasSize(databaseSizeBeforeCreate + 1);
        RelationshipKind testRelationshipKind = relationshipKindList.get(relationshipKindList.size() - 1);
        assertThat(testRelationshipKind.getTypeIdent()).isEqualTo(DEFAULT_TYPE_IDENT);
        assertThat(testRelationshipKind.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createRelationshipKindWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = relationshipKindRepository.findAll().size();

        // Create the RelationshipKind with an existing ID
        relationshipKind.setId(1L);
        RelationshipKindDTO relationshipKindDTO = relationshipKindMapper.toDto(relationshipKind);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRelationshipKindMockMvc.perform(post("/api/relationship-kinds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relationshipKindDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RelationshipKind in the database
        List<RelationshipKind> relationshipKindList = relationshipKindRepository.findAll();
        assertThat(relationshipKindList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRelationshipKinds() throws Exception {
        // Initialize the database
        relationshipKindRepository.saveAndFlush(relationshipKind);

        // Get all the relationshipKindList
        restRelationshipKindMockMvc.perform(get("/api/relationship-kinds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(relationshipKind.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeIdent").value(hasItem(DEFAULT_TYPE_IDENT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getRelationshipKind() throws Exception {
        // Initialize the database
        relationshipKindRepository.saveAndFlush(relationshipKind);

        // Get the relationshipKind
        restRelationshipKindMockMvc.perform(get("/api/relationship-kinds/{id}", relationshipKind.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(relationshipKind.getId().intValue()))
            .andExpect(jsonPath("$.typeIdent").value(DEFAULT_TYPE_IDENT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRelationshipKind() throws Exception {
        // Get the relationshipKind
        restRelationshipKindMockMvc.perform(get("/api/relationship-kinds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRelationshipKind() throws Exception {
        // Initialize the database
        relationshipKindRepository.saveAndFlush(relationshipKind);

        int databaseSizeBeforeUpdate = relationshipKindRepository.findAll().size();

        // Update the relationshipKind
        RelationshipKind updatedRelationshipKind = relationshipKindRepository.findById(relationshipKind.getId()).get();
        // Disconnect from session so that the updates on updatedRelationshipKind are not directly saved in db
        em.detach(updatedRelationshipKind);
        updatedRelationshipKind
            .typeIdent(UPDATED_TYPE_IDENT)
            .description(UPDATED_DESCRIPTION);
        RelationshipKindDTO relationshipKindDTO = relationshipKindMapper.toDto(updatedRelationshipKind);

        restRelationshipKindMockMvc.perform(put("/api/relationship-kinds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relationshipKindDTO)))
            .andExpect(status().isOk());

        // Validate the RelationshipKind in the database
        List<RelationshipKind> relationshipKindList = relationshipKindRepository.findAll();
        assertThat(relationshipKindList).hasSize(databaseSizeBeforeUpdate);
        RelationshipKind testRelationshipKind = relationshipKindList.get(relationshipKindList.size() - 1);
        assertThat(testRelationshipKind.getTypeIdent()).isEqualTo(UPDATED_TYPE_IDENT);
        assertThat(testRelationshipKind.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingRelationshipKind() throws Exception {
        int databaseSizeBeforeUpdate = relationshipKindRepository.findAll().size();

        // Create the RelationshipKind
        RelationshipKindDTO relationshipKindDTO = relationshipKindMapper.toDto(relationshipKind);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRelationshipKindMockMvc.perform(put("/api/relationship-kinds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(relationshipKindDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RelationshipKind in the database
        List<RelationshipKind> relationshipKindList = relationshipKindRepository.findAll();
        assertThat(relationshipKindList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRelationshipKind() throws Exception {
        // Initialize the database
        relationshipKindRepository.saveAndFlush(relationshipKind);

        int databaseSizeBeforeDelete = relationshipKindRepository.findAll().size();

        // Delete the relationshipKind
        restRelationshipKindMockMvc.perform(delete("/api/relationship-kinds/{id}", relationshipKind.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RelationshipKind> relationshipKindList = relationshipKindRepository.findAll();
        assertThat(relationshipKindList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RelationshipKind.class);
        RelationshipKind relationshipKind1 = new RelationshipKind();
        relationshipKind1.setId(1L);
        RelationshipKind relationshipKind2 = new RelationshipKind();
        relationshipKind2.setId(relationshipKind1.getId());
        assertThat(relationshipKind1).isEqualTo(relationshipKind2);
        relationshipKind2.setId(2L);
        assertThat(relationshipKind1).isNotEqualTo(relationshipKind2);
        relationshipKind1.setId(null);
        assertThat(relationshipKind1).isNotEqualTo(relationshipKind2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RelationshipKindDTO.class);
        RelationshipKindDTO relationshipKindDTO1 = new RelationshipKindDTO();
        relationshipKindDTO1.setId(1L);
        RelationshipKindDTO relationshipKindDTO2 = new RelationshipKindDTO();
        assertThat(relationshipKindDTO1).isNotEqualTo(relationshipKindDTO2);
        relationshipKindDTO2.setId(relationshipKindDTO1.getId());
        assertThat(relationshipKindDTO1).isEqualTo(relationshipKindDTO2);
        relationshipKindDTO2.setId(2L);
        assertThat(relationshipKindDTO1).isNotEqualTo(relationshipKindDTO2);
        relationshipKindDTO1.setId(null);
        assertThat(relationshipKindDTO1).isNotEqualTo(relationshipKindDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(relationshipKindMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(relationshipKindMapper.fromId(null)).isNull();
    }
}
