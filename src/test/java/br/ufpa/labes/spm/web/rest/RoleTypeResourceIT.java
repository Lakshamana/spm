package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.RoleType;
import br.ufpa.labes.spm.repository.RoleTypeRepository;
import br.ufpa.labes.spm.service.RoleTypeService;
import br.ufpa.labes.spm.service.dto.RoleTypeDTO;
import br.ufpa.labes.spm.service.mapper.RoleTypeMapper;
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
 * Integration tests for the {@link RoleTypeResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class RoleTypeResourceIT {

    @Autowired
    private RoleTypeRepository roleTypeRepository;

    @Autowired
    private RoleTypeMapper roleTypeMapper;

    @Autowired
    private RoleTypeService roleTypeService;

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

    private MockMvc restRoleTypeMockMvc;

    private RoleType roleType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoleTypeResource roleTypeResource = new RoleTypeResource(roleTypeService);
        this.restRoleTypeMockMvc = MockMvcBuilders.standaloneSetup(roleTypeResource)
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
    public static RoleType createEntity(EntityManager em) {
        RoleType roleType = new RoleType();
        return roleType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoleType createUpdatedEntity(EntityManager em) {
        RoleType roleType = new RoleType();
        return roleType;
    }

    @BeforeEach
    public void initTest() {
        roleType = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoleType() throws Exception {
        int databaseSizeBeforeCreate = roleTypeRepository.findAll().size();

        // Create the RoleType
        RoleTypeDTO roleTypeDTO = roleTypeMapper.toDto(roleType);
        restRoleTypeMockMvc.perform(post("/api/role-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the RoleType in the database
        List<RoleType> roleTypeList = roleTypeRepository.findAll();
        assertThat(roleTypeList).hasSize(databaseSizeBeforeCreate + 1);
        RoleType testRoleType = roleTypeList.get(roleTypeList.size() - 1);
    }

    @Test
    @Transactional
    public void createRoleTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roleTypeRepository.findAll().size();

        // Create the RoleType with an existing ID
        roleType.setId(1L);
        RoleTypeDTO roleTypeDTO = roleTypeMapper.toDto(roleType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleTypeMockMvc.perform(post("/api/role-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoleType in the database
        List<RoleType> roleTypeList = roleTypeRepository.findAll();
        assertThat(roleTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRoleTypes() throws Exception {
        // Initialize the database
        roleTypeRepository.saveAndFlush(roleType);

        // Get all the roleTypeList
        restRoleTypeMockMvc.perform(get("/api/role-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roleType.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getRoleType() throws Exception {
        // Initialize the database
        roleTypeRepository.saveAndFlush(roleType);

        // Get the roleType
        restRoleTypeMockMvc.perform(get("/api/role-types/{id}", roleType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(roleType.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRoleType() throws Exception {
        // Get the roleType
        restRoleTypeMockMvc.perform(get("/api/role-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoleType() throws Exception {
        // Initialize the database
        roleTypeRepository.saveAndFlush(roleType);

        int databaseSizeBeforeUpdate = roleTypeRepository.findAll().size();

        // Update the roleType
        RoleType updatedRoleType = roleTypeRepository.findById(roleType.getId()).get();
        // Disconnect from session so that the updates on updatedRoleType are not directly saved in db
        em.detach(updatedRoleType);
        RoleTypeDTO roleTypeDTO = roleTypeMapper.toDto(updatedRoleType);

        restRoleTypeMockMvc.perform(put("/api/role-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleTypeDTO)))
            .andExpect(status().isOk());

        // Validate the RoleType in the database
        List<RoleType> roleTypeList = roleTypeRepository.findAll();
        assertThat(roleTypeList).hasSize(databaseSizeBeforeUpdate);
        RoleType testRoleType = roleTypeList.get(roleTypeList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingRoleType() throws Exception {
        int databaseSizeBeforeUpdate = roleTypeRepository.findAll().size();

        // Create the RoleType
        RoleTypeDTO roleTypeDTO = roleTypeMapper.toDto(roleType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleTypeMockMvc.perform(put("/api/role-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoleType in the database
        List<RoleType> roleTypeList = roleTypeRepository.findAll();
        assertThat(roleTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRoleType() throws Exception {
        // Initialize the database
        roleTypeRepository.saveAndFlush(roleType);

        int databaseSizeBeforeDelete = roleTypeRepository.findAll().size();

        // Delete the roleType
        restRoleTypeMockMvc.perform(delete("/api/role-types/{id}", roleType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RoleType> roleTypeList = roleTypeRepository.findAll();
        assertThat(roleTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleType.class);
        RoleType roleType1 = new RoleType();
        roleType1.setId(1L);
        RoleType roleType2 = new RoleType();
        roleType2.setId(roleType1.getId());
        assertThat(roleType1).isEqualTo(roleType2);
        roleType2.setId(2L);
        assertThat(roleType1).isNotEqualTo(roleType2);
        roleType1.setId(null);
        assertThat(roleType1).isNotEqualTo(roleType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleTypeDTO.class);
        RoleTypeDTO roleTypeDTO1 = new RoleTypeDTO();
        roleTypeDTO1.setId(1L);
        RoleTypeDTO roleTypeDTO2 = new RoleTypeDTO();
        assertThat(roleTypeDTO1).isNotEqualTo(roleTypeDTO2);
        roleTypeDTO2.setId(roleTypeDTO1.getId());
        assertThat(roleTypeDTO1).isEqualTo(roleTypeDTO2);
        roleTypeDTO2.setId(2L);
        assertThat(roleTypeDTO1).isNotEqualTo(roleTypeDTO2);
        roleTypeDTO1.setId(null);
        assertThat(roleTypeDTO1).isNotEqualTo(roleTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(roleTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(roleTypeMapper.fromId(null)).isNull();
    }
}
