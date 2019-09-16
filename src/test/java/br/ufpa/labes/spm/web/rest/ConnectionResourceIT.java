package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Connection;
import br.ufpa.labes.spm.repository.ConnectionRepository;
import br.ufpa.labes.spm.service.ConnectionService;
import br.ufpa.labes.spm.service.dto.ConnectionDTO;
import br.ufpa.labes.spm.service.mapper.ConnectionMapper;
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

/** Integration tests for the {@link ConnectionResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ConnectionResourceIT {

  private static final String DEFAULT_IDENT = "AAAAAAAAAA";
  private static final String UPDATED_IDENT = "BBBBBBBBBB";

  @Autowired private ConnectionRepository connectionRepository;

  @Autowired private ConnectionMapper connectionMapper;

  @Autowired private ConnectionService connectionService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restConnectionMockMvc;

  private Connection connection;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ConnectionResource connectionResource = new ConnectionResource(connectionService);
    this.restConnectionMockMvc =
        MockMvcBuilders.standaloneSetup(connectionResource)
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
  public static Connection createEntity(EntityManager em) {
    Connection connection = new Connection().ident(DEFAULT_IDENT);
    return connection;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Connection createUpdatedEntity(EntityManager em) {
    Connection connection = new Connection().ident(UPDATED_IDENT);
    return connection;
  }

  @BeforeEach
  public void initTest() {
    connection = createEntity(em);
  }

  @Test
  @Transactional
  public void createConnection() throws Exception {
    int databaseSizeBeforeCreate = connectionRepository.findAll().size();

    // Create the Connection
    ConnectionDTO connectionDTO = connectionMapper.toDto(connection);
    restConnectionMockMvc
        .perform(
            post("/api/connections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(connectionDTO)))
        .andExpect(status().isCreated());

    // Validate the Connection in the database
    List<Connection> connectionList = connectionRepository.findAll();
    assertThat(connectionList).hasSize(databaseSizeBeforeCreate + 1);
    Connection testConnection = connectionList.get(connectionList.size() - 1);
    assertThat(testConnection.getIdent()).isEqualTo(DEFAULT_IDENT);
  }

  @Test
  @Transactional
  public void createConnectionWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = connectionRepository.findAll().size();

    // Create the Connection with an existing ID
    connection.setId(1L);
    ConnectionDTO connectionDTO = connectionMapper.toDto(connection);

    // An entity with an existing ID cannot be created, so this API call must fail
    restConnectionMockMvc
        .perform(
            post("/api/connections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(connectionDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Connection in the database
    List<Connection> connectionList = connectionRepository.findAll();
    assertThat(connectionList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllConnections() throws Exception {
    // Initialize the database
    connectionRepository.saveAndFlush(connection);

    // Get all the connectionList
    restConnectionMockMvc
        .perform(get("/api/connections?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(connection.getId().intValue())))
        .andExpect(jsonPath("$.[*].ident").value(hasItem(DEFAULT_IDENT.toString())));
  }

  @Test
  @Transactional
  public void getConnection() throws Exception {
    // Initialize the database
    connectionRepository.saveAndFlush(connection);

    // Get the connection
    restConnectionMockMvc
        .perform(get("/api/connections/{id}", connection.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(connection.getId().intValue()))
        .andExpect(jsonPath("$.ident").value(DEFAULT_IDENT.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingConnection() throws Exception {
    // Get the connection
    restConnectionMockMvc
        .perform(get("/api/connections/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateConnection() throws Exception {
    // Initialize the database
    connectionRepository.saveAndFlush(connection);

    int databaseSizeBeforeUpdate = connectionRepository.findAll().size();

    // Update the connection
    Connection updatedConnection = connectionRepository.findById(connection.getId()).get();
    // Disconnect from session so that the updates on updatedConnection are not directly saved in db
    em.detach(updatedConnection);
    updatedConnection.ident(UPDATED_IDENT);
    ConnectionDTO connectionDTO = connectionMapper.toDto(updatedConnection);

    restConnectionMockMvc
        .perform(
            put("/api/connections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(connectionDTO)))
        .andExpect(status().isOk());

    // Validate the Connection in the database
    List<Connection> connectionList = connectionRepository.findAll();
    assertThat(connectionList).hasSize(databaseSizeBeforeUpdate);
    Connection testConnection = connectionList.get(connectionList.size() - 1);
    assertThat(testConnection.getIdent()).isEqualTo(UPDATED_IDENT);
  }

  @Test
  @Transactional
  public void updateNonExistingConnection() throws Exception {
    int databaseSizeBeforeUpdate = connectionRepository.findAll().size();

    // Create the Connection
    ConnectionDTO connectionDTO = connectionMapper.toDto(connection);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restConnectionMockMvc
        .perform(
            put("/api/connections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(connectionDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Connection in the database
    List<Connection> connectionList = connectionRepository.findAll();
    assertThat(connectionList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteConnection() throws Exception {
    // Initialize the database
    connectionRepository.saveAndFlush(connection);

    int databaseSizeBeforeDelete = connectionRepository.findAll().size();

    // Delete the connection
    restConnectionMockMvc
        .perform(
            delete("/api/connections/{id}", connection.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Connection> connectionList = connectionRepository.findAll();
    assertThat(connectionList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Connection.class);
    Connection connection1 = new Connection();
    connection1.setId(1L);
    Connection connection2 = new Connection();
    connection2.setId(connection1.getId());
    assertThat(connection1).isEqualTo(connection2);
    connection2.setId(2L);
    assertThat(connection1).isNotEqualTo(connection2);
    connection1.setId(null);
    assertThat(connection1).isNotEqualTo(connection2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(ConnectionDTO.class);
    ConnectionDTO connectionDTO1 = new ConnectionDTO();
    connectionDTO1.setId(1L);
    ConnectionDTO connectionDTO2 = new ConnectionDTO();
    assertThat(connectionDTO1).isNotEqualTo(connectionDTO2);
    connectionDTO2.setId(connectionDTO1.getId());
    assertThat(connectionDTO1).isEqualTo(connectionDTO2);
    connectionDTO2.setId(2L);
    assertThat(connectionDTO1).isNotEqualTo(connectionDTO2);
    connectionDTO1.setId(null);
    assertThat(connectionDTO1).isNotEqualTo(connectionDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(connectionMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(connectionMapper.fromId(null)).isNull();
  }
}
