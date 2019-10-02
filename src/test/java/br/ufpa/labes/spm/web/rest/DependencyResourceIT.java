package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Dependency;
import br.ufpa.labes.spm.repository.DependencyRepository;
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
 * Integration tests for the {@link DependencyResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class DependencyResourceIT {

    private static final String DEFAULT_KIND_DEP = "AAAAAAAAAA";
    private static final String UPDATED_KIND_DEP = "BBBBBBBBBB";

    @Autowired
    private DependencyRepository dependencyRepository;

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

    private MockMvc restDependencyMockMvc;

    private Dependency dependency;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DependencyResource dependencyResource = new DependencyResource(dependencyRepository);
        this.restDependencyMockMvc = MockMvcBuilders.standaloneSetup(dependencyResource)
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
    public static Dependency createEntity(EntityManager em) {
        Dependency dependency = new Dependency()
            .kindDep(DEFAULT_KIND_DEP);
        return dependency;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dependency createUpdatedEntity(EntityManager em) {
        Dependency dependency = new Dependency()
            .kindDep(UPDATED_KIND_DEP);
        return dependency;
    }

    @BeforeEach
    public void initTest() {
        dependency = createEntity(em);
    }

    @Test
    @Transactional
    public void createDependency() throws Exception {
        int databaseSizeBeforeCreate = dependencyRepository.findAll().size();

        // Create the Dependency
        restDependencyMockMvc.perform(post("/api/dependencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dependency)))
            .andExpect(status().isCreated());

        // Validate the Dependency in the database
        List<Dependency> dependencyList = dependencyRepository.findAll();
        assertThat(dependencyList).hasSize(databaseSizeBeforeCreate + 1);
        Dependency testDependency = dependencyList.get(dependencyList.size() - 1);
        assertThat(testDependency.getKindDep()).isEqualTo(DEFAULT_KIND_DEP);
    }

    @Test
    @Transactional
    public void createDependencyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dependencyRepository.findAll().size();

        // Create the Dependency with an existing ID
        dependency.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDependencyMockMvc.perform(post("/api/dependencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dependency)))
            .andExpect(status().isBadRequest());

        // Validate the Dependency in the database
        List<Dependency> dependencyList = dependencyRepository.findAll();
        assertThat(dependencyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDependencies() throws Exception {
        // Initialize the database
        dependencyRepository.saveAndFlush(dependency);

        // Get all the dependencyList
        restDependencyMockMvc.perform(get("/api/dependencies?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dependency.getId().intValue())))
            .andExpect(jsonPath("$.[*].kindDep").value(hasItem(DEFAULT_KIND_DEP.toString())));
    }
    
    @Test
    @Transactional
    public void getDependency() throws Exception {
        // Initialize the database
        dependencyRepository.saveAndFlush(dependency);

        // Get the dependency
        restDependencyMockMvc.perform(get("/api/dependencies/{id}", dependency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dependency.getId().intValue()))
            .andExpect(jsonPath("$.kindDep").value(DEFAULT_KIND_DEP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDependency() throws Exception {
        // Get the dependency
        restDependencyMockMvc.perform(get("/api/dependencies/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDependency() throws Exception {
        // Initialize the database
        dependencyRepository.saveAndFlush(dependency);

        int databaseSizeBeforeUpdate = dependencyRepository.findAll().size();

        // Update the dependency
        Dependency updatedDependency = dependencyRepository.findById(dependency.getId()).get();
        // Disconnect from session so that the updates on updatedDependency are not directly saved in db
        em.detach(updatedDependency);
        updatedDependency
            .kindDep(UPDATED_KIND_DEP);

        restDependencyMockMvc.perform(put("/api/dependencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDependency)))
            .andExpect(status().isOk());

        // Validate the Dependency in the database
        List<Dependency> dependencyList = dependencyRepository.findAll();
        assertThat(dependencyList).hasSize(databaseSizeBeforeUpdate);
        Dependency testDependency = dependencyList.get(dependencyList.size() - 1);
        assertThat(testDependency.getKindDep()).isEqualTo(UPDATED_KIND_DEP);
    }

    @Test
    @Transactional
    public void updateNonExistingDependency() throws Exception {
        int databaseSizeBeforeUpdate = dependencyRepository.findAll().size();

        // Create the Dependency

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDependencyMockMvc.perform(put("/api/dependencies")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dependency)))
            .andExpect(status().isBadRequest());

        // Validate the Dependency in the database
        List<Dependency> dependencyList = dependencyRepository.findAll();
        assertThat(dependencyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDependency() throws Exception {
        // Initialize the database
        dependencyRepository.saveAndFlush(dependency);

        int databaseSizeBeforeDelete = dependencyRepository.findAll().size();

        // Delete the dependency
        restDependencyMockMvc.perform(delete("/api/dependencies/{id}", dependency.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dependency> dependencyList = dependencyRepository.findAll();
        assertThat(dependencyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dependency.class);
        Dependency dependency1 = new Dependency();
        dependency1.setId(1L);
        Dependency dependency2 = new Dependency();
        dependency2.setId(dependency1.getId());
        assertThat(dependency1).isEqualTo(dependency2);
        dependency2.setId(2L);
        assertThat(dependency1).isNotEqualTo(dependency2);
        dependency1.setId(null);
        assertThat(dependency1).isNotEqualTo(dependency2);
    }
}
