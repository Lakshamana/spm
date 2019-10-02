package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Plugin;
import br.ufpa.labes.spm.repository.PluginRepository;
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

/** Integration tests for the {@link PluginResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class PluginResourceIT {

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_DEVELOPER_NAME = "AAAAAAAAAA";
  private static final String UPDATED_DEVELOPER_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_CONFIG_FILE_PATH = "AAAAAAAAAA";
  private static final String UPDATED_CONFIG_FILE_PATH = "BBBBBBBBBB";

  @Autowired private PluginRepository pluginRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restPluginMockMvc;

  private Plugin plugin;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final PluginResource pluginResource = new PluginResource(pluginRepository);
    this.restPluginMockMvc =
        MockMvcBuilders.standaloneSetup(pluginResource)
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
  public static Plugin createEntity(EntityManager em) {
    Plugin plugin =
        new Plugin()
            .name(DEFAULT_NAME)
            .developerName(DEFAULT_DEVELOPER_NAME)
            .configFilePath(DEFAULT_CONFIG_FILE_PATH);
    return plugin;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Plugin createUpdatedEntity(EntityManager em) {
    Plugin plugin =
        new Plugin()
            .name(UPDATED_NAME)
            .developerName(UPDATED_DEVELOPER_NAME)
            .configFilePath(UPDATED_CONFIG_FILE_PATH);
    return plugin;
  }

  @BeforeEach
  public void initTest() {
    plugin = createEntity(em);
  }

  @Test
  @Transactional
  public void createPlugin() throws Exception {
    int databaseSizeBeforeCreate = pluginRepository.findAll().size();

    // Create the Plugin
    restPluginMockMvc
        .perform(
            post("/api/plugins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(plugin)))
        .andExpect(status().isCreated());

    // Validate the Plugin in the database
    List<Plugin> pluginList = pluginRepository.findAll();
    assertThat(pluginList).hasSize(databaseSizeBeforeCreate + 1);
    Plugin testPlugin = pluginList.get(pluginList.size() - 1);
    assertThat(testPlugin.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testPlugin.getDeveloperName()).isEqualTo(DEFAULT_DEVELOPER_NAME);
    assertThat(testPlugin.getConfigFilePath()).isEqualTo(DEFAULT_CONFIG_FILE_PATH);
  }

  @Test
  @Transactional
  public void createPluginWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = pluginRepository.findAll().size();

    // Create the Plugin with an existing ID
    plugin.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restPluginMockMvc
        .perform(
            post("/api/plugins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(plugin)))
        .andExpect(status().isBadRequest());

    // Validate the Plugin in the database
    List<Plugin> pluginList = pluginRepository.findAll();
    assertThat(pluginList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkDeveloperNameIsRequired() throws Exception {
    int databaseSizeBeforeTest = pluginRepository.findAll().size();
    // set the field null
    plugin.setDeveloperName(null);

    // Create the Plugin, which fails.

    restPluginMockMvc
        .perform(
            post("/api/plugins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(plugin)))
        .andExpect(status().isBadRequest());

    List<Plugin> pluginList = pluginRepository.findAll();
    assertThat(pluginList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void checkConfigFilePathIsRequired() throws Exception {
    int databaseSizeBeforeTest = pluginRepository.findAll().size();
    // set the field null
    plugin.setConfigFilePath(null);

    // Create the Plugin, which fails.

    restPluginMockMvc
        .perform(
            post("/api/plugins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(plugin)))
        .andExpect(status().isBadRequest());

    List<Plugin> pluginList = pluginRepository.findAll();
    assertThat(pluginList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllPlugins() throws Exception {
    // Initialize the database
    pluginRepository.saveAndFlush(plugin);

    // Get all the pluginList
    restPluginMockMvc
        .perform(get("/api/plugins?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(plugin.getId().intValue())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(
            jsonPath("$.[*].developerName").value(hasItem(DEFAULT_DEVELOPER_NAME.toString())))
        .andExpect(
            jsonPath("$.[*].configFilePath").value(hasItem(DEFAULT_CONFIG_FILE_PATH.toString())));
  }

  @Test
  @Transactional
  public void getPlugin() throws Exception {
    // Initialize the database
    pluginRepository.saveAndFlush(plugin);

    // Get the plugin
    restPluginMockMvc
        .perform(get("/api/plugins/{id}", plugin.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(plugin.getId().intValue()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.developerName").value(DEFAULT_DEVELOPER_NAME.toString()))
        .andExpect(jsonPath("$.configFilePath").value(DEFAULT_CONFIG_FILE_PATH.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingPlugin() throws Exception {
    // Get the plugin
    restPluginMockMvc
        .perform(get("/api/plugins/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updatePlugin() throws Exception {
    // Initialize the database
    pluginRepository.saveAndFlush(plugin);

    int databaseSizeBeforeUpdate = pluginRepository.findAll().size();

    // Update the plugin
    Plugin updatedPlugin = pluginRepository.findById(plugin.getId()).get();
    // Disconnect from session so that the updates on updatedPlugin are not directly saved in db
    em.detach(updatedPlugin);
    updatedPlugin
        .name(UPDATED_NAME)
        .developerName(UPDATED_DEVELOPER_NAME)
        .configFilePath(UPDATED_CONFIG_FILE_PATH);

    restPluginMockMvc
        .perform(
            put("/api/plugins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPlugin)))
        .andExpect(status().isOk());

    // Validate the Plugin in the database
    List<Plugin> pluginList = pluginRepository.findAll();
    assertThat(pluginList).hasSize(databaseSizeBeforeUpdate);
    Plugin testPlugin = pluginList.get(pluginList.size() - 1);
    assertThat(testPlugin.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testPlugin.getDeveloperName()).isEqualTo(UPDATED_DEVELOPER_NAME);
    assertThat(testPlugin.getConfigFilePath()).isEqualTo(UPDATED_CONFIG_FILE_PATH);
  }

  @Test
  @Transactional
  public void updateNonExistingPlugin() throws Exception {
    int databaseSizeBeforeUpdate = pluginRepository.findAll().size();

    // Create the Plugin

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restPluginMockMvc
        .perform(
            put("/api/plugins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(plugin)))
        .andExpect(status().isBadRequest());

    // Validate the Plugin in the database
    List<Plugin> pluginList = pluginRepository.findAll();
    assertThat(pluginList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deletePlugin() throws Exception {
    // Initialize the database
    pluginRepository.saveAndFlush(plugin);

    int databaseSizeBeforeDelete = pluginRepository.findAll().size();

    // Delete the plugin
    restPluginMockMvc
        .perform(delete("/api/plugins/{id}", plugin.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Plugin> pluginList = pluginRepository.findAll();
    assertThat(pluginList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Plugin.class);
    Plugin plugin1 = new Plugin();
    plugin1.setId(1L);
    Plugin plugin2 = new Plugin();
    plugin2.setId(plugin1.getId());
    assertThat(plugin1).isEqualTo(plugin2);
    plugin2.setId(2L);
    assertThat(plugin1).isNotEqualTo(plugin2);
    plugin1.setId(null);
    assertThat(plugin1).isNotEqualTo(plugin2);
  }
}
