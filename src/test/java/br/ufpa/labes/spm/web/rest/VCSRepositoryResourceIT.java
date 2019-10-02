package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.VCSRepository;
import br.ufpa.labes.spm.repository.VCSRepositoryRepository;
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
 * Integration tests for the {@link VCSRepositoryResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class VCSRepositoryResourceIT {

    private static final String DEFAULT_IDENT = "AAAAAAAAAA";
    private static final String UPDATED_IDENT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTROL_VERSION_SYSTEM = "AAAAAAAAAA";
    private static final String UPDATED_CONTROL_VERSION_SYSTEM = "BBBBBBBBBB";

    private static final String DEFAULT_SERVER = "AAAAAAAAAA";
    private static final String UPDATED_SERVER = "BBBBBBBBBB";

    private static final String DEFAULT_PORT = "AAAAAAAAAA";
    private static final String UPDATED_PORT = "BBBBBBBBBB";

    private static final String DEFAULT_CONNECTION_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_CONNECTION_METHOD = "BBBBBBBBBB";

    private static final String DEFAULT_REPOSITORY = "AAAAAAAAAA";
    private static final String UPDATED_REPOSITORY = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DEFAULT_USER = false;
    private static final Boolean UPDATED_DEFAULT_USER = true;

    @Autowired
    private VCSRepositoryRepository vCSRepositoryRepository;

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

    private MockMvc restVCSRepositoryMockMvc;

    private VCSRepository vCSRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VCSRepositoryResource vCSRepositoryResource = new VCSRepositoryResource(vCSRepositoryRepository);
        this.restVCSRepositoryMockMvc = MockMvcBuilders.standaloneSetup(vCSRepositoryResource)
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
    public static VCSRepository createEntity(EntityManager em) {
        VCSRepository vCSRepository = new VCSRepository()
            .ident(DEFAULT_IDENT)
            .controlVersionSystem(DEFAULT_CONTROL_VERSION_SYSTEM)
            .server(DEFAULT_SERVER)
            .port(DEFAULT_PORT)
            .connectionMethod(DEFAULT_CONNECTION_METHOD)
            .repository(DEFAULT_REPOSITORY)
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD)
            .defaultUser(DEFAULT_DEFAULT_USER);
        return vCSRepository;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VCSRepository createUpdatedEntity(EntityManager em) {
        VCSRepository vCSRepository = new VCSRepository()
            .ident(UPDATED_IDENT)
            .controlVersionSystem(UPDATED_CONTROL_VERSION_SYSTEM)
            .server(UPDATED_SERVER)
            .port(UPDATED_PORT)
            .connectionMethod(UPDATED_CONNECTION_METHOD)
            .repository(UPDATED_REPOSITORY)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .defaultUser(UPDATED_DEFAULT_USER);
        return vCSRepository;
    }

    @BeforeEach
    public void initTest() {
        vCSRepository = createEntity(em);
    }

    @Test
    @Transactional
    public void createVCSRepository() throws Exception {
        int databaseSizeBeforeCreate = vCSRepositoryRepository.findAll().size();

        // Create the VCSRepository
        restVCSRepositoryMockMvc.perform(post("/api/vcs-repositories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vCSRepository)))
            .andExpect(status().isCreated());

        // Validate the VCSRepository in the database
        List<VCSRepository> vCSRepositoryList = vCSRepositoryRepository.findAll();
        assertThat(vCSRepositoryList).hasSize(databaseSizeBeforeCreate + 1);
        VCSRepository testVCSRepository = vCSRepositoryList.get(vCSRepositoryList.size() - 1);
        assertThat(testVCSRepository.getIdent()).isEqualTo(DEFAULT_IDENT);
        assertThat(testVCSRepository.getControlVersionSystem()).isEqualTo(DEFAULT_CONTROL_VERSION_SYSTEM);
        assertThat(testVCSRepository.getServer()).isEqualTo(DEFAULT_SERVER);
        assertThat(testVCSRepository.getPort()).isEqualTo(DEFAULT_PORT);
        assertThat(testVCSRepository.getConnectionMethod()).isEqualTo(DEFAULT_CONNECTION_METHOD);
        assertThat(testVCSRepository.getRepository()).isEqualTo(DEFAULT_REPOSITORY);
        assertThat(testVCSRepository.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testVCSRepository.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testVCSRepository.isDefaultUser()).isEqualTo(DEFAULT_DEFAULT_USER);
    }

    @Test
    @Transactional
    public void createVCSRepositoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vCSRepositoryRepository.findAll().size();

        // Create the VCSRepository with an existing ID
        vCSRepository.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVCSRepositoryMockMvc.perform(post("/api/vcs-repositories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vCSRepository)))
            .andExpect(status().isBadRequest());

        // Validate the VCSRepository in the database
        List<VCSRepository> vCSRepositoryList = vCSRepositoryRepository.findAll();
        assertThat(vCSRepositoryList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllVCSRepositories() throws Exception {
        // Initialize the database
        vCSRepositoryRepository.saveAndFlush(vCSRepository);

        // Get all the vCSRepositoryList
        restVCSRepositoryMockMvc.perform(get("/api/vcs-repositories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vCSRepository.getId().intValue())))
            .andExpect(jsonPath("$.[*].ident").value(hasItem(DEFAULT_IDENT.toString())))
            .andExpect(jsonPath("$.[*].controlVersionSystem").value(hasItem(DEFAULT_CONTROL_VERSION_SYSTEM.toString())))
            .andExpect(jsonPath("$.[*].server").value(hasItem(DEFAULT_SERVER.toString())))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT.toString())))
            .andExpect(jsonPath("$.[*].connectionMethod").value(hasItem(DEFAULT_CONNECTION_METHOD.toString())))
            .andExpect(jsonPath("$.[*].repository").value(hasItem(DEFAULT_REPOSITORY.toString())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].defaultUser").value(hasItem(DEFAULT_DEFAULT_USER.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getVCSRepository() throws Exception {
        // Initialize the database
        vCSRepositoryRepository.saveAndFlush(vCSRepository);

        // Get the vCSRepository
        restVCSRepositoryMockMvc.perform(get("/api/vcs-repositories/{id}", vCSRepository.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(vCSRepository.getId().intValue()))
            .andExpect(jsonPath("$.ident").value(DEFAULT_IDENT.toString()))
            .andExpect(jsonPath("$.controlVersionSystem").value(DEFAULT_CONTROL_VERSION_SYSTEM.toString()))
            .andExpect(jsonPath("$.server").value(DEFAULT_SERVER.toString()))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT.toString()))
            .andExpect(jsonPath("$.connectionMethod").value(DEFAULT_CONNECTION_METHOD.toString()))
            .andExpect(jsonPath("$.repository").value(DEFAULT_REPOSITORY.toString()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.defaultUser").value(DEFAULT_DEFAULT_USER.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingVCSRepository() throws Exception {
        // Get the vCSRepository
        restVCSRepositoryMockMvc.perform(get("/api/vcs-repositories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVCSRepository() throws Exception {
        // Initialize the database
        vCSRepositoryRepository.saveAndFlush(vCSRepository);

        int databaseSizeBeforeUpdate = vCSRepositoryRepository.findAll().size();

        // Update the vCSRepository
        VCSRepository updatedVCSRepository = vCSRepositoryRepository.findById(vCSRepository.getId()).get();
        // Disconnect from session so that the updates on updatedVCSRepository are not directly saved in db
        em.detach(updatedVCSRepository);
        updatedVCSRepository
            .ident(UPDATED_IDENT)
            .controlVersionSystem(UPDATED_CONTROL_VERSION_SYSTEM)
            .server(UPDATED_SERVER)
            .port(UPDATED_PORT)
            .connectionMethod(UPDATED_CONNECTION_METHOD)
            .repository(UPDATED_REPOSITORY)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .defaultUser(UPDATED_DEFAULT_USER);

        restVCSRepositoryMockMvc.perform(put("/api/vcs-repositories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVCSRepository)))
            .andExpect(status().isOk());

        // Validate the VCSRepository in the database
        List<VCSRepository> vCSRepositoryList = vCSRepositoryRepository.findAll();
        assertThat(vCSRepositoryList).hasSize(databaseSizeBeforeUpdate);
        VCSRepository testVCSRepository = vCSRepositoryList.get(vCSRepositoryList.size() - 1);
        assertThat(testVCSRepository.getIdent()).isEqualTo(UPDATED_IDENT);
        assertThat(testVCSRepository.getControlVersionSystem()).isEqualTo(UPDATED_CONTROL_VERSION_SYSTEM);
        assertThat(testVCSRepository.getServer()).isEqualTo(UPDATED_SERVER);
        assertThat(testVCSRepository.getPort()).isEqualTo(UPDATED_PORT);
        assertThat(testVCSRepository.getConnectionMethod()).isEqualTo(UPDATED_CONNECTION_METHOD);
        assertThat(testVCSRepository.getRepository()).isEqualTo(UPDATED_REPOSITORY);
        assertThat(testVCSRepository.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testVCSRepository.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testVCSRepository.isDefaultUser()).isEqualTo(UPDATED_DEFAULT_USER);
    }

    @Test
    @Transactional
    public void updateNonExistingVCSRepository() throws Exception {
        int databaseSizeBeforeUpdate = vCSRepositoryRepository.findAll().size();

        // Create the VCSRepository

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVCSRepositoryMockMvc.perform(put("/api/vcs-repositories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(vCSRepository)))
            .andExpect(status().isBadRequest());

        // Validate the VCSRepository in the database
        List<VCSRepository> vCSRepositoryList = vCSRepositoryRepository.findAll();
        assertThat(vCSRepositoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVCSRepository() throws Exception {
        // Initialize the database
        vCSRepositoryRepository.saveAndFlush(vCSRepository);

        int databaseSizeBeforeDelete = vCSRepositoryRepository.findAll().size();

        // Delete the vCSRepository
        restVCSRepositoryMockMvc.perform(delete("/api/vcs-repositories/{id}", vCSRepository.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VCSRepository> vCSRepositoryList = vCSRepositoryRepository.findAll();
        assertThat(vCSRepositoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VCSRepository.class);
        VCSRepository vCSRepository1 = new VCSRepository();
        vCSRepository1.setId(1L);
        VCSRepository vCSRepository2 = new VCSRepository();
        vCSRepository2.setId(vCSRepository1.getId());
        assertThat(vCSRepository1).isEqualTo(vCSRepository2);
        vCSRepository2.setId(2L);
        assertThat(vCSRepository1).isNotEqualTo(vCSRepository2);
        vCSRepository1.setId(null);
        assertThat(vCSRepository1).isNotEqualTo(vCSRepository2);
    }
}
