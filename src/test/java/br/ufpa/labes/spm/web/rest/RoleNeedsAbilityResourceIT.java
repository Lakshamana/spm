package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.RoleNeedsAbility;
import br.ufpa.labes.spm.repository.RoleNeedsAbilityRepository;
import br.ufpa.labes.spm.service.RoleNeedsAbilityService;
import br.ufpa.labes.spm.service.dto.RoleNeedsAbilityDTO;
import br.ufpa.labes.spm.service.mapper.RoleNeedsAbilityMapper;
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
 * Integration tests for the {@link RoleNeedsAbilityResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class RoleNeedsAbilityResourceIT {

    private static final Integer DEFAULT_DEGREE = 1;
    private static final Integer UPDATED_DEGREE = 2;
    private static final Integer SMALLER_DEGREE = 1 - 1;

    @Autowired
    private RoleNeedsAbilityRepository roleNeedsAbilityRepository;

    @Autowired
    private RoleNeedsAbilityMapper roleNeedsAbilityMapper;

    @Autowired
    private RoleNeedsAbilityService roleNeedsAbilityService;

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

    private MockMvc restRoleNeedsAbilityMockMvc;

    private RoleNeedsAbility roleNeedsAbility;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RoleNeedsAbilityResource roleNeedsAbilityResource = new RoleNeedsAbilityResource(roleNeedsAbilityService);
        this.restRoleNeedsAbilityMockMvc = MockMvcBuilders.standaloneSetup(roleNeedsAbilityResource)
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
    public static RoleNeedsAbility createEntity(EntityManager em) {
        RoleNeedsAbility roleNeedsAbility = new RoleNeedsAbility()
            .degree(DEFAULT_DEGREE);
        return roleNeedsAbility;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoleNeedsAbility createUpdatedEntity(EntityManager em) {
        RoleNeedsAbility roleNeedsAbility = new RoleNeedsAbility()
            .degree(UPDATED_DEGREE);
        return roleNeedsAbility;
    }

    @BeforeEach
    public void initTest() {
        roleNeedsAbility = createEntity(em);
    }

    @Test
    @Transactional
    public void createRoleNeedsAbility() throws Exception {
        int databaseSizeBeforeCreate = roleNeedsAbilityRepository.findAll().size();

        // Create the RoleNeedsAbility
        RoleNeedsAbilityDTO roleNeedsAbilityDTO = roleNeedsAbilityMapper.toDto(roleNeedsAbility);
        restRoleNeedsAbilityMockMvc.perform(post("/api/role-needs-abilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleNeedsAbilityDTO)))
            .andExpect(status().isCreated());

        // Validate the RoleNeedsAbility in the database
        List<RoleNeedsAbility> roleNeedsAbilityList = roleNeedsAbilityRepository.findAll();
        assertThat(roleNeedsAbilityList).hasSize(databaseSizeBeforeCreate + 1);
        RoleNeedsAbility testRoleNeedsAbility = roleNeedsAbilityList.get(roleNeedsAbilityList.size() - 1);
        assertThat(testRoleNeedsAbility.getDegree()).isEqualTo(DEFAULT_DEGREE);
    }

    @Test
    @Transactional
    public void createRoleNeedsAbilityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = roleNeedsAbilityRepository.findAll().size();

        // Create the RoleNeedsAbility with an existing ID
        roleNeedsAbility.setId(1L);
        RoleNeedsAbilityDTO roleNeedsAbilityDTO = roleNeedsAbilityMapper.toDto(roleNeedsAbility);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoleNeedsAbilityMockMvc.perform(post("/api/role-needs-abilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleNeedsAbilityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoleNeedsAbility in the database
        List<RoleNeedsAbility> roleNeedsAbilityList = roleNeedsAbilityRepository.findAll();
        assertThat(roleNeedsAbilityList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRoleNeedsAbilities() throws Exception {
        // Initialize the database
        roleNeedsAbilityRepository.saveAndFlush(roleNeedsAbility);

        // Get all the roleNeedsAbilityList
        restRoleNeedsAbilityMockMvc.perform(get("/api/role-needs-abilities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roleNeedsAbility.getId().intValue())))
            .andExpect(jsonPath("$.[*].degree").value(hasItem(DEFAULT_DEGREE)));
    }
    
    @Test
    @Transactional
    public void getRoleNeedsAbility() throws Exception {
        // Initialize the database
        roleNeedsAbilityRepository.saveAndFlush(roleNeedsAbility);

        // Get the roleNeedsAbility
        restRoleNeedsAbilityMockMvc.perform(get("/api/role-needs-abilities/{id}", roleNeedsAbility.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(roleNeedsAbility.getId().intValue()))
            .andExpect(jsonPath("$.degree").value(DEFAULT_DEGREE));
    }

    @Test
    @Transactional
    public void getNonExistingRoleNeedsAbility() throws Exception {
        // Get the roleNeedsAbility
        restRoleNeedsAbilityMockMvc.perform(get("/api/role-needs-abilities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRoleNeedsAbility() throws Exception {
        // Initialize the database
        roleNeedsAbilityRepository.saveAndFlush(roleNeedsAbility);

        int databaseSizeBeforeUpdate = roleNeedsAbilityRepository.findAll().size();

        // Update the roleNeedsAbility
        RoleNeedsAbility updatedRoleNeedsAbility = roleNeedsAbilityRepository.findById(roleNeedsAbility.getId()).get();
        // Disconnect from session so that the updates on updatedRoleNeedsAbility are not directly saved in db
        em.detach(updatedRoleNeedsAbility);
        updatedRoleNeedsAbility
            .degree(UPDATED_DEGREE);
        RoleNeedsAbilityDTO roleNeedsAbilityDTO = roleNeedsAbilityMapper.toDto(updatedRoleNeedsAbility);

        restRoleNeedsAbilityMockMvc.perform(put("/api/role-needs-abilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleNeedsAbilityDTO)))
            .andExpect(status().isOk());

        // Validate the RoleNeedsAbility in the database
        List<RoleNeedsAbility> roleNeedsAbilityList = roleNeedsAbilityRepository.findAll();
        assertThat(roleNeedsAbilityList).hasSize(databaseSizeBeforeUpdate);
        RoleNeedsAbility testRoleNeedsAbility = roleNeedsAbilityList.get(roleNeedsAbilityList.size() - 1);
        assertThat(testRoleNeedsAbility.getDegree()).isEqualTo(UPDATED_DEGREE);
    }

    @Test
    @Transactional
    public void updateNonExistingRoleNeedsAbility() throws Exception {
        int databaseSizeBeforeUpdate = roleNeedsAbilityRepository.findAll().size();

        // Create the RoleNeedsAbility
        RoleNeedsAbilityDTO roleNeedsAbilityDTO = roleNeedsAbilityMapper.toDto(roleNeedsAbility);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoleNeedsAbilityMockMvc.perform(put("/api/role-needs-abilities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(roleNeedsAbilityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RoleNeedsAbility in the database
        List<RoleNeedsAbility> roleNeedsAbilityList = roleNeedsAbilityRepository.findAll();
        assertThat(roleNeedsAbilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRoleNeedsAbility() throws Exception {
        // Initialize the database
        roleNeedsAbilityRepository.saveAndFlush(roleNeedsAbility);

        int databaseSizeBeforeDelete = roleNeedsAbilityRepository.findAll().size();

        // Delete the roleNeedsAbility
        restRoleNeedsAbilityMockMvc.perform(delete("/api/role-needs-abilities/{id}", roleNeedsAbility.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RoleNeedsAbility> roleNeedsAbilityList = roleNeedsAbilityRepository.findAll();
        assertThat(roleNeedsAbilityList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleNeedsAbility.class);
        RoleNeedsAbility roleNeedsAbility1 = new RoleNeedsAbility();
        roleNeedsAbility1.setId(1L);
        RoleNeedsAbility roleNeedsAbility2 = new RoleNeedsAbility();
        roleNeedsAbility2.setId(roleNeedsAbility1.getId());
        assertThat(roleNeedsAbility1).isEqualTo(roleNeedsAbility2);
        roleNeedsAbility2.setId(2L);
        assertThat(roleNeedsAbility1).isNotEqualTo(roleNeedsAbility2);
        roleNeedsAbility1.setId(null);
        assertThat(roleNeedsAbility1).isNotEqualTo(roleNeedsAbility2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoleNeedsAbilityDTO.class);
        RoleNeedsAbilityDTO roleNeedsAbilityDTO1 = new RoleNeedsAbilityDTO();
        roleNeedsAbilityDTO1.setId(1L);
        RoleNeedsAbilityDTO roleNeedsAbilityDTO2 = new RoleNeedsAbilityDTO();
        assertThat(roleNeedsAbilityDTO1).isNotEqualTo(roleNeedsAbilityDTO2);
        roleNeedsAbilityDTO2.setId(roleNeedsAbilityDTO1.getId());
        assertThat(roleNeedsAbilityDTO1).isEqualTo(roleNeedsAbilityDTO2);
        roleNeedsAbilityDTO2.setId(2L);
        assertThat(roleNeedsAbilityDTO1).isNotEqualTo(roleNeedsAbilityDTO2);
        roleNeedsAbilityDTO1.setId(null);
        assertThat(roleNeedsAbilityDTO1).isNotEqualTo(roleNeedsAbilityDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(roleNeedsAbilityMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(roleNeedsAbilityMapper.fromId(null)).isNull();
    }
}
