package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Exclusive;
import br.ufpa.labes.spm.repository.ExclusiveRepository;
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
 * Integration tests for the {@link ExclusiveResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ExclusiveResourceIT {

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_OF_COST = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_OF_COST = "BBBBBBBBBB";

    @Autowired
    private ExclusiveRepository exclusiveRepository;

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

    private MockMvc restExclusiveMockMvc;

    private Exclusive exclusive;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExclusiveResource exclusiveResource = new ExclusiveResource(exclusiveRepository);
        this.restExclusiveMockMvc = MockMvcBuilders.standaloneSetup(exclusiveResource)
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
    public static Exclusive createEntity(EntityManager em) {
        Exclusive exclusive = new Exclusive()
            .state(DEFAULT_STATE)
            .unitOfCost(DEFAULT_UNIT_OF_COST);
        return exclusive;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exclusive createUpdatedEntity(EntityManager em) {
        Exclusive exclusive = new Exclusive()
            .state(UPDATED_STATE)
            .unitOfCost(UPDATED_UNIT_OF_COST);
        return exclusive;
    }

    @BeforeEach
    public void initTest() {
        exclusive = createEntity(em);
    }

    @Test
    @Transactional
    public void createExclusive() throws Exception {
        int databaseSizeBeforeCreate = exclusiveRepository.findAll().size();

        // Create the Exclusive
        restExclusiveMockMvc.perform(post("/api/exclusives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exclusive)))
            .andExpect(status().isCreated());

        // Validate the Exclusive in the database
        List<Exclusive> exclusiveList = exclusiveRepository.findAll();
        assertThat(exclusiveList).hasSize(databaseSizeBeforeCreate + 1);
        Exclusive testExclusive = exclusiveList.get(exclusiveList.size() - 1);
        assertThat(testExclusive.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testExclusive.getUnitOfCost()).isEqualTo(DEFAULT_UNIT_OF_COST);
    }

    @Test
    @Transactional
    public void createExclusiveWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = exclusiveRepository.findAll().size();

        // Create the Exclusive with an existing ID
        exclusive.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExclusiveMockMvc.perform(post("/api/exclusives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exclusive)))
            .andExpect(status().isBadRequest());

        // Validate the Exclusive in the database
        List<Exclusive> exclusiveList = exclusiveRepository.findAll();
        assertThat(exclusiveList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllExclusives() throws Exception {
        // Initialize the database
        exclusiveRepository.saveAndFlush(exclusive);

        // Get all the exclusiveList
        restExclusiveMockMvc.perform(get("/api/exclusives?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exclusive.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].unitOfCost").value(hasItem(DEFAULT_UNIT_OF_COST.toString())));
    }
    
    @Test
    @Transactional
    public void getExclusive() throws Exception {
        // Initialize the database
        exclusiveRepository.saveAndFlush(exclusive);

        // Get the exclusive
        restExclusiveMockMvc.perform(get("/api/exclusives/{id}", exclusive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(exclusive.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.unitOfCost").value(DEFAULT_UNIT_OF_COST.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExclusive() throws Exception {
        // Get the exclusive
        restExclusiveMockMvc.perform(get("/api/exclusives/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExclusive() throws Exception {
        // Initialize the database
        exclusiveRepository.saveAndFlush(exclusive);

        int databaseSizeBeforeUpdate = exclusiveRepository.findAll().size();

        // Update the exclusive
        Exclusive updatedExclusive = exclusiveRepository.findById(exclusive.getId()).get();
        // Disconnect from session so that the updates on updatedExclusive are not directly saved in db
        em.detach(updatedExclusive);
        updatedExclusive
            .state(UPDATED_STATE)
            .unitOfCost(UPDATED_UNIT_OF_COST);

        restExclusiveMockMvc.perform(put("/api/exclusives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExclusive)))
            .andExpect(status().isOk());

        // Validate the Exclusive in the database
        List<Exclusive> exclusiveList = exclusiveRepository.findAll();
        assertThat(exclusiveList).hasSize(databaseSizeBeforeUpdate);
        Exclusive testExclusive = exclusiveList.get(exclusiveList.size() - 1);
        assertThat(testExclusive.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testExclusive.getUnitOfCost()).isEqualTo(UPDATED_UNIT_OF_COST);
    }

    @Test
    @Transactional
    public void updateNonExistingExclusive() throws Exception {
        int databaseSizeBeforeUpdate = exclusiveRepository.findAll().size();

        // Create the Exclusive

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExclusiveMockMvc.perform(put("/api/exclusives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exclusive)))
            .andExpect(status().isBadRequest());

        // Validate the Exclusive in the database
        List<Exclusive> exclusiveList = exclusiveRepository.findAll();
        assertThat(exclusiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExclusive() throws Exception {
        // Initialize the database
        exclusiveRepository.saveAndFlush(exclusive);

        int databaseSizeBeforeDelete = exclusiveRepository.findAll().size();

        // Delete the exclusive
        restExclusiveMockMvc.perform(delete("/api/exclusives/{id}", exclusive.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Exclusive> exclusiveList = exclusiveRepository.findAll();
        assertThat(exclusiveList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Exclusive.class);
        Exclusive exclusive1 = new Exclusive();
        exclusive1.setId(1L);
        Exclusive exclusive2 = new Exclusive();
        exclusive2.setId(exclusive1.getId());
        assertThat(exclusive1).isEqualTo(exclusive2);
        exclusive2.setId(2L);
        assertThat(exclusive1).isNotEqualTo(exclusive2);
        exclusive1.setId(null);
        assertThat(exclusive1).isNotEqualTo(exclusive2);
    }
}
