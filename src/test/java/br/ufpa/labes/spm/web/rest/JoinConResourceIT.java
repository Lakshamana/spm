package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.JoinCon;
import br.ufpa.labes.spm.repository.JoinConRepository;
import br.ufpa.labes.spm.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static br.ufpa.labes.spm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link JoinConResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class JoinConResourceIT {

    private static final String DEFAULT_KIND_JOIN = "AAAAAAAAAA";
    private static final String UPDATED_KIND_JOIN = "BBBBBBBBBB";

    @Autowired
    private JoinConRepository joinConRepository;

    @Mock
    private JoinConRepository joinConRepositoryMock;

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

    private MockMvc restJoinConMockMvc;

    private JoinCon joinCon;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final JoinConResource joinConResource = new JoinConResource(joinConRepository);
        this.restJoinConMockMvc = MockMvcBuilders.standaloneSetup(joinConResource)
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
    public static JoinCon createEntity(EntityManager em) {
        JoinCon joinCon = new JoinCon()
            .kindJoin(DEFAULT_KIND_JOIN);
        return joinCon;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JoinCon createUpdatedEntity(EntityManager em) {
        JoinCon joinCon = new JoinCon()
            .kindJoin(UPDATED_KIND_JOIN);
        return joinCon;
    }

    @BeforeEach
    public void initTest() {
        joinCon = createEntity(em);
    }

    @Test
    @Transactional
    public void createJoinCon() throws Exception {
        int databaseSizeBeforeCreate = joinConRepository.findAll().size();

        // Create the JoinCon
        restJoinConMockMvc.perform(post("/api/join-cons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(joinCon)))
            .andExpect(status().isCreated());

        // Validate the JoinCon in the database
        List<JoinCon> joinConList = joinConRepository.findAll();
        assertThat(joinConList).hasSize(databaseSizeBeforeCreate + 1);
        JoinCon testJoinCon = joinConList.get(joinConList.size() - 1);
        assertThat(testJoinCon.getKindJoin()).isEqualTo(DEFAULT_KIND_JOIN);
    }

    @Test
    @Transactional
    public void createJoinConWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = joinConRepository.findAll().size();

        // Create the JoinCon with an existing ID
        joinCon.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJoinConMockMvc.perform(post("/api/join-cons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(joinCon)))
            .andExpect(status().isBadRequest());

        // Validate the JoinCon in the database
        List<JoinCon> joinConList = joinConRepository.findAll();
        assertThat(joinConList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllJoinCons() throws Exception {
        // Initialize the database
        joinConRepository.saveAndFlush(joinCon);

        // Get all the joinConList
        restJoinConMockMvc.perform(get("/api/join-cons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(joinCon.getId().intValue())))
            .andExpect(jsonPath("$.[*].kindJoin").value(hasItem(DEFAULT_KIND_JOIN.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllJoinConsWithEagerRelationshipsIsEnabled() throws Exception {
        JoinConResource joinConResource = new JoinConResource(joinConRepositoryMock);
        when(joinConRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restJoinConMockMvc = MockMvcBuilders.standaloneSetup(joinConResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restJoinConMockMvc.perform(get("/api/join-cons?eagerload=true"))
        .andExpect(status().isOk());

        verify(joinConRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllJoinConsWithEagerRelationshipsIsNotEnabled() throws Exception {
        JoinConResource joinConResource = new JoinConResource(joinConRepositoryMock);
            when(joinConRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restJoinConMockMvc = MockMvcBuilders.standaloneSetup(joinConResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restJoinConMockMvc.perform(get("/api/join-cons?eagerload=true"))
        .andExpect(status().isOk());

            verify(joinConRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getJoinCon() throws Exception {
        // Initialize the database
        joinConRepository.saveAndFlush(joinCon);

        // Get the joinCon
        restJoinConMockMvc.perform(get("/api/join-cons/{id}", joinCon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(joinCon.getId().intValue()))
            .andExpect(jsonPath("$.kindJoin").value(DEFAULT_KIND_JOIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJoinCon() throws Exception {
        // Get the joinCon
        restJoinConMockMvc.perform(get("/api/join-cons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJoinCon() throws Exception {
        // Initialize the database
        joinConRepository.saveAndFlush(joinCon);

        int databaseSizeBeforeUpdate = joinConRepository.findAll().size();

        // Update the joinCon
        JoinCon updatedJoinCon = joinConRepository.findById(joinCon.getId()).get();
        // Disconnect from session so that the updates on updatedJoinCon are not directly saved in db
        em.detach(updatedJoinCon);
        updatedJoinCon
            .kindJoin(UPDATED_KIND_JOIN);

        restJoinConMockMvc.perform(put("/api/join-cons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJoinCon)))
            .andExpect(status().isOk());

        // Validate the JoinCon in the database
        List<JoinCon> joinConList = joinConRepository.findAll();
        assertThat(joinConList).hasSize(databaseSizeBeforeUpdate);
        JoinCon testJoinCon = joinConList.get(joinConList.size() - 1);
        assertThat(testJoinCon.getKindJoin()).isEqualTo(UPDATED_KIND_JOIN);
    }

    @Test
    @Transactional
    public void updateNonExistingJoinCon() throws Exception {
        int databaseSizeBeforeUpdate = joinConRepository.findAll().size();

        // Create the JoinCon

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJoinConMockMvc.perform(put("/api/join-cons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(joinCon)))
            .andExpect(status().isBadRequest());

        // Validate the JoinCon in the database
        List<JoinCon> joinConList = joinConRepository.findAll();
        assertThat(joinConList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteJoinCon() throws Exception {
        // Initialize the database
        joinConRepository.saveAndFlush(joinCon);

        int databaseSizeBeforeDelete = joinConRepository.findAll().size();

        // Delete the joinCon
        restJoinConMockMvc.perform(delete("/api/join-cons/{id}", joinCon.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JoinCon> joinConList = joinConRepository.findAll();
        assertThat(joinConList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JoinCon.class);
        JoinCon joinCon1 = new JoinCon();
        joinCon1.setId(1L);
        JoinCon joinCon2 = new JoinCon();
        joinCon2.setId(joinCon1.getId());
        assertThat(joinCon1).isEqualTo(joinCon2);
        joinCon2.setId(2L);
        assertThat(joinCon1).isNotEqualTo(joinCon2);
        joinCon1.setId(null);
        assertThat(joinCon1).isNotEqualTo(joinCon2);
    }
}
