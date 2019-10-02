package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ConnectionType;
import br.ufpa.labes.spm.repository.ConnectionTypeRepository;
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

/** Integration tests for the {@link ConnectionTypeResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ConnectionTypeResourceIT {

  @Autowired private ConnectionTypeRepository connectionTypeRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restConnectionTypeMockMvc;

  private ConnectionType connectionType;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ConnectionTypeResource connectionTypeResource =
        new ConnectionTypeResource(connectionTypeRepository);
    this.restConnectionTypeMockMvc =
        MockMvcBuilders.standaloneSetup(connectionTypeResource)
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
  public static ConnectionType createEntity(EntityManager em) {
    ConnectionType connectionType = new ConnectionType();
    return connectionType;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ConnectionType createUpdatedEntity(EntityManager em) {
    ConnectionType connectionType = new ConnectionType();
    return connectionType;
  }

  @BeforeEach
  public void initTest() {
    connectionType = createEntity(em);
  }

  @Test
  @Transactional
  public void createConnectionType() throws Exception {
    int databaseSizeBeforeCreate = connectionTypeRepository.findAll().size();

    // Create the ConnectionType
    restConnectionTypeMockMvc
        .perform(
            post("/api/connection-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(connectionType)))
        .andExpect(status().isCreated());

    // Validate the ConnectionType in the database
    List<ConnectionType> connectionTypeList = connectionTypeRepository.findAll();
    assertThat(connectionTypeList).hasSize(databaseSizeBeforeCreate + 1);
    ConnectionType testConnectionType = connectionTypeList.get(connectionTypeList.size() - 1);
  }

  @Test
  @Transactional
  public void createConnectionTypeWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = connectionTypeRepository.findAll().size();

    // Create the ConnectionType with an existing ID
    connectionType.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restConnectionTypeMockMvc
        .perform(
            post("/api/connection-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(connectionType)))
        .andExpect(status().isBadRequest());

    // Validate the ConnectionType in the database
    List<ConnectionType> connectionTypeList = connectionTypeRepository.findAll();
    assertThat(connectionTypeList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllConnectionTypes() throws Exception {
    // Initialize the database
    connectionTypeRepository.saveAndFlush(connectionType);

    // Get all the connectionTypeList
    restConnectionTypeMockMvc
        .perform(get("/api/connection-types?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(connectionType.getId().intValue())));
  }

  @Test
  @Transactional
  public void getConnectionType() throws Exception {
    // Initialize the database
    connectionTypeRepository.saveAndFlush(connectionType);

    // Get the connectionType
    restConnectionTypeMockMvc
        .perform(get("/api/connection-types/{id}", connectionType.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(connectionType.getId().intValue()));
  }

  @Test
  @Transactional
  public void getNonExistingConnectionType() throws Exception {
    // Get the connectionType
    restConnectionTypeMockMvc
        .perform(get("/api/connection-types/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateConnectionType() throws Exception {
    // Initialize the database
    connectionTypeRepository.saveAndFlush(connectionType);

    int databaseSizeBeforeUpdate = connectionTypeRepository.findAll().size();

    // Update the connectionType
    ConnectionType updatedConnectionType =
        connectionTypeRepository.findById(connectionType.getId()).get();
    // Disconnect from session so that the updates on updatedConnectionType are not directly saved
    // in db
    em.detach(updatedConnectionType);

    restConnectionTypeMockMvc
        .perform(
            put("/api/connection-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedConnectionType)))
        .andExpect(status().isOk());

    // Validate the ConnectionType in the database
    List<ConnectionType> connectionTypeList = connectionTypeRepository.findAll();
    assertThat(connectionTypeList).hasSize(databaseSizeBeforeUpdate);
    ConnectionType testConnectionType = connectionTypeList.get(connectionTypeList.size() - 1);
  }

  @Test
  @Transactional
  public void updateNonExistingConnectionType() throws Exception {
    int databaseSizeBeforeUpdate = connectionTypeRepository.findAll().size();

    // Create the ConnectionType

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restConnectionTypeMockMvc
        .perform(
            put("/api/connection-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(connectionType)))
        .andExpect(status().isBadRequest());

    // Validate the ConnectionType in the database
    List<ConnectionType> connectionTypeList = connectionTypeRepository.findAll();
    assertThat(connectionTypeList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteConnectionType() throws Exception {
    // Initialize the database
    connectionTypeRepository.saveAndFlush(connectionType);

    int databaseSizeBeforeDelete = connectionTypeRepository.findAll().size();

    // Delete the connectionType
    restConnectionTypeMockMvc
        .perform(
            delete("/api/connection-types/{id}", connectionType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ConnectionType> connectionTypeList = connectionTypeRepository.findAll();
    assertThat(connectionTypeList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ConnectionType.class);
    ConnectionType connectionType1 = new ConnectionType();
    connectionType1.setId(1L);
    ConnectionType connectionType2 = new ConnectionType();
    connectionType2.setId(connectionType1.getId());
    assertThat(connectionType1).isEqualTo(connectionType2);
    connectionType2.setId(2L);
    assertThat(connectionType1).isNotEqualTo(connectionType2);
    connectionType1.setId(null);
    assertThat(connectionType1).isNotEqualTo(connectionType2);
  }
}
