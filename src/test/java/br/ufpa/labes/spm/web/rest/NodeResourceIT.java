package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Node;
import br.ufpa.labes.spm.repository.NodeRepository;
import br.ufpa.labes.spm.service.NodeService;
import br.ufpa.labes.spm.service.dto.NodeDTO;
import br.ufpa.labes.spm.service.mapper.NodeMapper;
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

/** Integration tests for the {@link NodeResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class NodeResourceIT {

  private static final String DEFAULT_IDENT = "AAAAAAAAAA";
  private static final String UPDATED_IDENT = "BBBBBBBBBB";

  private static final String DEFAULT_DATA = "AAAAAAAAAA";
  private static final String UPDATED_DATA = "BBBBBBBBBB";

  private static final String DEFAULT_SERVICE_FILE_ID = "AAAAAAAAAA";
  private static final String UPDATED_SERVICE_FILE_ID = "BBBBBBBBBB";

  @Autowired private NodeRepository nodeRepository;

  @Autowired private NodeMapper nodeMapper;

  @Autowired private NodeService nodeService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restNodeMockMvc;

  private Node node;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final NodeResource nodeResource = new NodeResource(nodeService);
    this.restNodeMockMvc =
        MockMvcBuilders.standaloneSetup(nodeResource)
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
  public static Node createEntity(EntityManager em) {
    Node node =
        new Node().ident(DEFAULT_IDENT).data(DEFAULT_DATA).serviceFileId(DEFAULT_SERVICE_FILE_ID);
    return node;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Node createUpdatedEntity(EntityManager em) {
    Node node =
        new Node().ident(UPDATED_IDENT).data(UPDATED_DATA).serviceFileId(UPDATED_SERVICE_FILE_ID);
    return node;
  }

  @BeforeEach
  public void initTest() {
    node = createEntity(em);
  }

  @Test
  @Transactional
  public void createNode() throws Exception {
    int databaseSizeBeforeCreate = nodeRepository.findAll().size();

    // Create the Node
    NodeDTO nodeDTO = nodeMapper.toDto(node);
    restNodeMockMvc
        .perform(
            post("/api/nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nodeDTO)))
        .andExpect(status().isCreated());

    // Validate the Node in the database
    List<Node> nodeList = nodeRepository.findAll();
    assertThat(nodeList).hasSize(databaseSizeBeforeCreate + 1);
    Node testNode = nodeList.get(nodeList.size() - 1);
    assertThat(testNode.getIdent()).isEqualTo(DEFAULT_IDENT);
    assertThat(testNode.getData()).isEqualTo(DEFAULT_DATA);
    assertThat(testNode.getServiceFileId()).isEqualTo(DEFAULT_SERVICE_FILE_ID);
  }

  @Test
  @Transactional
  public void createNodeWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = nodeRepository.findAll().size();

    // Create the Node with an existing ID
    node.setId(1L);
    NodeDTO nodeDTO = nodeMapper.toDto(node);

    // An entity with an existing ID cannot be created, so this API call must fail
    restNodeMockMvc
        .perform(
            post("/api/nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nodeDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Node in the database
    List<Node> nodeList = nodeRepository.findAll();
    assertThat(nodeList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllNodes() throws Exception {
    // Initialize the database
    nodeRepository.saveAndFlush(node);

    // Get all the nodeList
    restNodeMockMvc
        .perform(get("/api/nodes?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(node.getId().intValue())))
        .andExpect(jsonPath("$.[*].ident").value(hasItem(DEFAULT_IDENT.toString())))
        .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
        .andExpect(
            jsonPath("$.[*].serviceFileId").value(hasItem(DEFAULT_SERVICE_FILE_ID.toString())));
  }

  @Test
  @Transactional
  public void getNode() throws Exception {
    // Initialize the database
    nodeRepository.saveAndFlush(node);

    // Get the node
    restNodeMockMvc
        .perform(get("/api/nodes/{id}", node.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(node.getId().intValue()))
        .andExpect(jsonPath("$.ident").value(DEFAULT_IDENT.toString()))
        .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
        .andExpect(jsonPath("$.serviceFileId").value(DEFAULT_SERVICE_FILE_ID.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingNode() throws Exception {
    // Get the node
    restNodeMockMvc
        .perform(get("/api/nodes/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateNode() throws Exception {
    // Initialize the database
    nodeRepository.saveAndFlush(node);

    int databaseSizeBeforeUpdate = nodeRepository.findAll().size();

    // Update the node
    Node updatedNode = nodeRepository.findById(node.getId()).get();
    // Disconnect from session so that the updates on updatedNode are not directly saved in db
    em.detach(updatedNode);
    updatedNode.ident(UPDATED_IDENT).data(UPDATED_DATA).serviceFileId(UPDATED_SERVICE_FILE_ID);
    NodeDTO nodeDTO = nodeMapper.toDto(updatedNode);

    restNodeMockMvc
        .perform(
            put("/api/nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nodeDTO)))
        .andExpect(status().isOk());

    // Validate the Node in the database
    List<Node> nodeList = nodeRepository.findAll();
    assertThat(nodeList).hasSize(databaseSizeBeforeUpdate);
    Node testNode = nodeList.get(nodeList.size() - 1);
    assertThat(testNode.getIdent()).isEqualTo(UPDATED_IDENT);
    assertThat(testNode.getData()).isEqualTo(UPDATED_DATA);
    assertThat(testNode.getServiceFileId()).isEqualTo(UPDATED_SERVICE_FILE_ID);
  }

  @Test
  @Transactional
  public void updateNonExistingNode() throws Exception {
    int databaseSizeBeforeUpdate = nodeRepository.findAll().size();

    // Create the Node
    NodeDTO nodeDTO = nodeMapper.toDto(node);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restNodeMockMvc
        .perform(
            put("/api/nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(nodeDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Node in the database
    List<Node> nodeList = nodeRepository.findAll();
    assertThat(nodeList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteNode() throws Exception {
    // Initialize the database
    nodeRepository.saveAndFlush(node);

    int databaseSizeBeforeDelete = nodeRepository.findAll().size();

    // Delete the node
    restNodeMockMvc
        .perform(delete("/api/nodes/{id}", node.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Node> nodeList = nodeRepository.findAll();
    assertThat(nodeList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Node.class);
    Node node1 = new Node();
    node1.setId(1L);
    Node node2 = new Node();
    node2.setId(node1.getId());
    assertThat(node1).isEqualTo(node2);
    node2.setId(2L);
    assertThat(node1).isNotEqualTo(node2);
    node1.setId(null);
    assertThat(node1).isNotEqualTo(node2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(NodeDTO.class);
    NodeDTO nodeDTO1 = new NodeDTO();
    nodeDTO1.setId(1L);
    NodeDTO nodeDTO2 = new NodeDTO();
    assertThat(nodeDTO1).isNotEqualTo(nodeDTO2);
    nodeDTO2.setId(nodeDTO1.getId());
    assertThat(nodeDTO1).isEqualTo(nodeDTO2);
    nodeDTO2.setId(2L);
    assertThat(nodeDTO1).isNotEqualTo(nodeDTO2);
    nodeDTO1.setId(null);
    assertThat(nodeDTO1).isNotEqualTo(nodeDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(nodeMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(nodeMapper.fromId(null)).isNull();
  }
}
