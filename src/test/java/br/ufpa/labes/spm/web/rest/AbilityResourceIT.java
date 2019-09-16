package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Ability;
import br.ufpa.labes.spm.repository.AbilityRepository;
import br.ufpa.labes.spm.service.AbilityService;
import br.ufpa.labes.spm.service.dto.AbilityDTO;
import br.ufpa.labes.spm.service.mapper.AbilityMapper;
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

/** Integration tests for the {@link AbilityResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AbilityResourceIT {

  private static final String DEFAULT_IDENT = "AAAAAAAAAA";
  private static final String UPDATED_IDENT = "BBBBBBBBBB";

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  @Autowired private AbilityRepository abilityRepository;

  @Autowired private AbilityMapper abilityMapper;

  @Autowired private AbilityService abilityService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restAbilityMockMvc;

  private Ability ability;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final AbilityResource abilityResource = new AbilityResource(abilityService);
    this.restAbilityMockMvc =
        MockMvcBuilders.standaloneSetup(abilityResource)
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
  public static Ability createEntity(EntityManager em) {
    Ability ability =
        new Ability().ident(DEFAULT_IDENT).name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    return ability;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Ability createUpdatedEntity(EntityManager em) {
    Ability ability =
        new Ability().ident(UPDATED_IDENT).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    return ability;
  }

  @BeforeEach
  public void initTest() {
    ability = createEntity(em);
  }

  @Test
  @Transactional
  public void createAbility() throws Exception {
    int databaseSizeBeforeCreate = abilityRepository.findAll().size();

    // Create the Ability
    AbilityDTO abilityDTO = abilityMapper.toDto(ability);
    restAbilityMockMvc
        .perform(
            post("/api/abilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(abilityDTO)))
        .andExpect(status().isCreated());

    // Validate the Ability in the database
    List<Ability> abilityList = abilityRepository.findAll();
    assertThat(abilityList).hasSize(databaseSizeBeforeCreate + 1);
    Ability testAbility = abilityList.get(abilityList.size() - 1);
    assertThat(testAbility.getIdent()).isEqualTo(DEFAULT_IDENT);
    assertThat(testAbility.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testAbility.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
  }

  @Test
  @Transactional
  public void createAbilityWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = abilityRepository.findAll().size();

    // Create the Ability with an existing ID
    ability.setId(1L);
    AbilityDTO abilityDTO = abilityMapper.toDto(ability);

    // An entity with an existing ID cannot be created, so this API call must fail
    restAbilityMockMvc
        .perform(
            post("/api/abilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(abilityDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Ability in the database
    List<Ability> abilityList = abilityRepository.findAll();
    assertThat(abilityList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllAbilities() throws Exception {
    // Initialize the database
    abilityRepository.saveAndFlush(ability);

    // Get all the abilityList
    restAbilityMockMvc
        .perform(get("/api/abilities?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(ability.getId().intValue())))
        .andExpect(jsonPath("$.[*].ident").value(hasItem(DEFAULT_IDENT.toString())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
  }

  @Test
  @Transactional
  public void getAbility() throws Exception {
    // Initialize the database
    abilityRepository.saveAndFlush(ability);

    // Get the ability
    restAbilityMockMvc
        .perform(get("/api/abilities/{id}", ability.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(ability.getId().intValue()))
        .andExpect(jsonPath("$.ident").value(DEFAULT_IDENT.toString()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingAbility() throws Exception {
    // Get the ability
    restAbilityMockMvc
        .perform(get("/api/abilities/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateAbility() throws Exception {
    // Initialize the database
    abilityRepository.saveAndFlush(ability);

    int databaseSizeBeforeUpdate = abilityRepository.findAll().size();

    // Update the ability
    Ability updatedAbility = abilityRepository.findById(ability.getId()).get();
    // Disconnect from session so that the updates on updatedAbility are not directly saved in db
    em.detach(updatedAbility);
    updatedAbility.ident(UPDATED_IDENT).name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    AbilityDTO abilityDTO = abilityMapper.toDto(updatedAbility);

    restAbilityMockMvc
        .perform(
            put("/api/abilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(abilityDTO)))
        .andExpect(status().isOk());

    // Validate the Ability in the database
    List<Ability> abilityList = abilityRepository.findAll();
    assertThat(abilityList).hasSize(databaseSizeBeforeUpdate);
    Ability testAbility = abilityList.get(abilityList.size() - 1);
    assertThat(testAbility.getIdent()).isEqualTo(UPDATED_IDENT);
    assertThat(testAbility.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testAbility.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  }

  @Test
  @Transactional
  public void updateNonExistingAbility() throws Exception {
    int databaseSizeBeforeUpdate = abilityRepository.findAll().size();

    // Create the Ability
    AbilityDTO abilityDTO = abilityMapper.toDto(ability);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAbilityMockMvc
        .perform(
            put("/api/abilities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(abilityDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Ability in the database
    List<Ability> abilityList = abilityRepository.findAll();
    assertThat(abilityList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteAbility() throws Exception {
    // Initialize the database
    abilityRepository.saveAndFlush(ability);

    int databaseSizeBeforeDelete = abilityRepository.findAll().size();

    // Delete the ability
    restAbilityMockMvc
        .perform(
            delete("/api/abilities/{id}", ability.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Ability> abilityList = abilityRepository.findAll();
    assertThat(abilityList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Ability.class);
    Ability ability1 = new Ability();
    ability1.setId(1L);
    Ability ability2 = new Ability();
    ability2.setId(ability1.getId());
    assertThat(ability1).isEqualTo(ability2);
    ability2.setId(2L);
    assertThat(ability1).isNotEqualTo(ability2);
    ability1.setId(null);
    assertThat(ability1).isNotEqualTo(ability2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(AbilityDTO.class);
    AbilityDTO abilityDTO1 = new AbilityDTO();
    abilityDTO1.setId(1L);
    AbilityDTO abilityDTO2 = new AbilityDTO();
    assertThat(abilityDTO1).isNotEqualTo(abilityDTO2);
    abilityDTO2.setId(abilityDTO1.getId());
    assertThat(abilityDTO1).isEqualTo(abilityDTO2);
    abilityDTO2.setId(2L);
    assertThat(abilityDTO1).isNotEqualTo(abilityDTO2);
    abilityDTO1.setId(null);
    assertThat(abilityDTO1).isNotEqualTo(abilityDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(abilityMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(abilityMapper.fromId(null)).isNull();
  }
}
