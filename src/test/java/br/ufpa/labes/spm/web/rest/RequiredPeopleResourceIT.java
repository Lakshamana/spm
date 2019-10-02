package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.RequiredPeople;
import br.ufpa.labes.spm.repository.RequiredPeopleRepository;
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
 * Integration tests for the {@link RequiredPeopleResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class RequiredPeopleResourceIT {

    @Autowired
    private RequiredPeopleRepository requiredPeopleRepository;

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

    private MockMvc restRequiredPeopleMockMvc;

    private RequiredPeople requiredPeople;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RequiredPeopleResource requiredPeopleResource = new RequiredPeopleResource(requiredPeopleRepository);
        this.restRequiredPeopleMockMvc = MockMvcBuilders.standaloneSetup(requiredPeopleResource)
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
    public static RequiredPeople createEntity(EntityManager em) {
        RequiredPeople requiredPeople = new RequiredPeople();
        return requiredPeople;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RequiredPeople createUpdatedEntity(EntityManager em) {
        RequiredPeople requiredPeople = new RequiredPeople();
        return requiredPeople;
    }

    @BeforeEach
    public void initTest() {
        requiredPeople = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequiredPeople() throws Exception {
        int databaseSizeBeforeCreate = requiredPeopleRepository.findAll().size();

        // Create the RequiredPeople
        restRequiredPeopleMockMvc.perform(post("/api/required-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requiredPeople)))
            .andExpect(status().isCreated());

        // Validate the RequiredPeople in the database
        List<RequiredPeople> requiredPeopleList = requiredPeopleRepository.findAll();
        assertThat(requiredPeopleList).hasSize(databaseSizeBeforeCreate + 1);
        RequiredPeople testRequiredPeople = requiredPeopleList.get(requiredPeopleList.size() - 1);
    }

    @Test
    @Transactional
    public void createRequiredPeopleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requiredPeopleRepository.findAll().size();

        // Create the RequiredPeople with an existing ID
        requiredPeople.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequiredPeopleMockMvc.perform(post("/api/required-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requiredPeople)))
            .andExpect(status().isBadRequest());

        // Validate the RequiredPeople in the database
        List<RequiredPeople> requiredPeopleList = requiredPeopleRepository.findAll();
        assertThat(requiredPeopleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRequiredPeople() throws Exception {
        // Initialize the database
        requiredPeopleRepository.saveAndFlush(requiredPeople);

        // Get all the requiredPeopleList
        restRequiredPeopleMockMvc.perform(get("/api/required-people?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requiredPeople.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getRequiredPeople() throws Exception {
        // Initialize the database
        requiredPeopleRepository.saveAndFlush(requiredPeople);

        // Get the requiredPeople
        restRequiredPeopleMockMvc.perform(get("/api/required-people/{id}", requiredPeople.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(requiredPeople.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRequiredPeople() throws Exception {
        // Get the requiredPeople
        restRequiredPeopleMockMvc.perform(get("/api/required-people/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequiredPeople() throws Exception {
        // Initialize the database
        requiredPeopleRepository.saveAndFlush(requiredPeople);

        int databaseSizeBeforeUpdate = requiredPeopleRepository.findAll().size();

        // Update the requiredPeople
        RequiredPeople updatedRequiredPeople = requiredPeopleRepository.findById(requiredPeople.getId()).get();
        // Disconnect from session so that the updates on updatedRequiredPeople are not directly saved in db
        em.detach(updatedRequiredPeople);

        restRequiredPeopleMockMvc.perform(put("/api/required-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRequiredPeople)))
            .andExpect(status().isOk());

        // Validate the RequiredPeople in the database
        List<RequiredPeople> requiredPeopleList = requiredPeopleRepository.findAll();
        assertThat(requiredPeopleList).hasSize(databaseSizeBeforeUpdate);
        RequiredPeople testRequiredPeople = requiredPeopleList.get(requiredPeopleList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingRequiredPeople() throws Exception {
        int databaseSizeBeforeUpdate = requiredPeopleRepository.findAll().size();

        // Create the RequiredPeople

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequiredPeopleMockMvc.perform(put("/api/required-people")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requiredPeople)))
            .andExpect(status().isBadRequest());

        // Validate the RequiredPeople in the database
        List<RequiredPeople> requiredPeopleList = requiredPeopleRepository.findAll();
        assertThat(requiredPeopleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRequiredPeople() throws Exception {
        // Initialize the database
        requiredPeopleRepository.saveAndFlush(requiredPeople);

        int databaseSizeBeforeDelete = requiredPeopleRepository.findAll().size();

        // Delete the requiredPeople
        restRequiredPeopleMockMvc.perform(delete("/api/required-people/{id}", requiredPeople.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RequiredPeople> requiredPeopleList = requiredPeopleRepository.findAll();
        assertThat(requiredPeopleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequiredPeople.class);
        RequiredPeople requiredPeople1 = new RequiredPeople();
        requiredPeople1.setId(1L);
        RequiredPeople requiredPeople2 = new RequiredPeople();
        requiredPeople2.setId(requiredPeople1.getId());
        assertThat(requiredPeople1).isEqualTo(requiredPeople2);
        requiredPeople2.setId(2L);
        assertThat(requiredPeople1).isNotEqualTo(requiredPeople2);
        requiredPeople1.setId(null);
        assertThat(requiredPeople1).isNotEqualTo(requiredPeople2);
    }
}
