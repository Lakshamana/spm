package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Role;
import br.ufpa.labes.spm.repository.RoleRepository;
import br.ufpa.labes.spm.service.RoleService;
import br.ufpa.labes.spm.service.dto.RoleDTO;
import br.ufpa.labes.spm.service.mapper.RoleMapper;
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

/** Integration tests for the {@link RoleResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class RoleResourceIT {

  private static final String DEFAULT_IDENT = "AAAAAAAAAA";
  private static final String UPDATED_IDENT = "BBBBBBBBBB";

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  @Autowired private RoleRepository roleRepository;

  @Autowired private RoleMapper roleMapper;

  @Autowired private RoleService roleService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restRoleMockMvc;

  private Role role;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final RoleResource roleResource = new RoleResource(roleService);
    this.restRoleMockMvc =
        MockMvcBuilders.standaloneSetup(roleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator)
            .build();
  }

  /**
   * Create an entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Role createEntity(EntityManager em) {
    Role role = new Role().ident(DEFAULT_IDENT).name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    return role;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Role createUpdatedEntity(EntityManager em) {
    Role role = new Role().ident(UPDATED_IDENT).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    return role;
  }

  @BeforeEach
  public void initTest() {
    role = createEntity(em);
  }

  @Test
  @Transactional
  public void createRole() throws Exception {
    int databaseSizeBeforeCreate = roleRepository.findAll().size();

    // Create the Role
    RoleDTO roleDTO = roleMapper.toDto(role);
    restRoleMockMvc
        .perform(
            post("/api/roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(roleDTO)))
        .andExpect(status().isCreated());

    // Validate the Role in the database
    List<Role> roleList = roleRepository.findAll();
    assertThat(roleList).hasSize(databaseSizeBeforeCreate + 1);
    Role testRole = roleList.get(roleList.size() - 1);
    assertThat(testRole.getIdent()).isEqualTo(DEFAULT_IDENT);
    assertThat(testRole.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testRole.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
  }

  @Test
  @Transactional
  public void createRoleWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = roleRepository.findAll().size();

    // Create the Role with an existing ID
    role.setId(1L);
    RoleDTO roleDTO = roleMapper.toDto(role);

    // An entity with an existing ID cannot be created, so this API call must fail
    restRoleMockMvc
        .perform(
            post("/api/roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(roleDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Role in the database
    List<Role> roleList = roleRepository.findAll();
    assertThat(roleList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllRoles() throws Exception {
    // Initialize the database
    roleRepository.saveAndFlush(role);

    // Get all the roleList
    restRoleMockMvc
        .perform(get("/api/roles?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(role.getId().intValue())))
        .andExpect(jsonPath("$.[*].ident").value(hasItem(DEFAULT_IDENT.toString())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
  }

  @Test
  @Transactional
  public void getRole() throws Exception {
    // Initialize the database
    roleRepository.saveAndFlush(role);

    // Get the role
    restRoleMockMvc
        .perform(get("/api/roles/{id}", role.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(role.getId().intValue()))
        .andExpect(jsonPath("$.ident").value(DEFAULT_IDENT.toString()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingRole() throws Exception {
    // Get the role
    restRoleMockMvc
        .perform(get("/api/roles/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateRole() throws Exception {
    // Initialize the database
    roleRepository.saveAndFlush(role);

    int databaseSizeBeforeUpdate = roleRepository.findAll().size();

    // Update the role
    Role updatedRole = roleRepository.findById(role.getId()).get();
    // Disconnect from session so that the updates on updatedRole are not directly saved in db
    em.detach(updatedRole);
    updatedRole.ident(UPDATED_IDENT).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    RoleDTO roleDTO = roleMapper.toDto(updatedRole);

    restRoleMockMvc
        .perform(
            put("/api/roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(roleDTO)))
        .andExpect(status().isOk());

    // Validate the Role in the database
    List<Role> roleList = roleRepository.findAll();
    assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
    Role testRole = roleList.get(roleList.size() - 1);
    assertThat(testRole.getIdent()).isEqualTo(UPDATED_IDENT);
    assertThat(testRole.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testRole.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  }

  @Test
  @Transactional
  public void updateNonExistingRole() throws Exception {
    int databaseSizeBeforeUpdate = roleRepository.findAll().size();

    // Create the Role
    RoleDTO roleDTO = roleMapper.toDto(role);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restRoleMockMvc
        .perform(
            put("/api/roles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(roleDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Role in the database
    List<Role> roleList = roleRepository.findAll();
    assertThat(roleList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteRole() throws Exception {
    // Initialize the database
    roleRepository.saveAndFlush(role);

    int databaseSizeBeforeDelete = roleRepository.findAll().size();

    // Delete the role
    restRoleMockMvc
        .perform(delete("/api/roles/{id}", role.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Role> roleList = roleRepository.findAll();
    assertThat(roleList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Role.class);
    Role role1 = new Role();
    role1.setId(1L);
    Role role2 = new Role();
    role2.setId(role1.getId());
    assertThat(role1).isEqualTo(role2);
    role2.setId(2L);
    assertThat(role1).isNotEqualTo(role2);
    role1.setId(null);
    assertThat(role1).isNotEqualTo(role2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(RoleDTO.class);
    RoleDTO roleDTO1 = new RoleDTO();
    roleDTO1.setId(1L);
    RoleDTO roleDTO2 = new RoleDTO();
    assertThat(roleDTO1).isNotEqualTo(roleDTO2);
    roleDTO2.setId(roleDTO1.getId());
    assertThat(roleDTO1).isEqualTo(roleDTO2);
    roleDTO2.setId(2L);
    assertThat(roleDTO1).isNotEqualTo(roleDTO2);
    roleDTO1.setId(null);
    assertThat(roleDTO1).isNotEqualTo(roleDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(roleMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(roleMapper.fromId(null)).isNull();
  }
}
