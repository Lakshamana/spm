package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.NotWorkingDay;
import br.ufpa.labes.spm.repository.NotWorkingDayRepository;
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
 * Integration tests for the {@link NotWorkingDayResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class NotWorkingDayResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private NotWorkingDayRepository notWorkingDayRepository;

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

    private MockMvc restNotWorkingDayMockMvc;

    private NotWorkingDay notWorkingDay;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NotWorkingDayResource notWorkingDayResource = new NotWorkingDayResource(notWorkingDayRepository);
        this.restNotWorkingDayMockMvc = MockMvcBuilders.standaloneSetup(notWorkingDayResource)
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
    public static NotWorkingDay createEntity(EntityManager em) {
        NotWorkingDay notWorkingDay = new NotWorkingDay()
            .name(DEFAULT_NAME);
        return notWorkingDay;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotWorkingDay createUpdatedEntity(EntityManager em) {
        NotWorkingDay notWorkingDay = new NotWorkingDay()
            .name(UPDATED_NAME);
        return notWorkingDay;
    }

    @BeforeEach
    public void initTest() {
        notWorkingDay = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotWorkingDay() throws Exception {
        int databaseSizeBeforeCreate = notWorkingDayRepository.findAll().size();

        // Create the NotWorkingDay
        restNotWorkingDayMockMvc.perform(post("/api/not-working-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notWorkingDay)))
            .andExpect(status().isCreated());

        // Validate the NotWorkingDay in the database
        List<NotWorkingDay> notWorkingDayList = notWorkingDayRepository.findAll();
        assertThat(notWorkingDayList).hasSize(databaseSizeBeforeCreate + 1);
        NotWorkingDay testNotWorkingDay = notWorkingDayList.get(notWorkingDayList.size() - 1);
        assertThat(testNotWorkingDay.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createNotWorkingDayWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notWorkingDayRepository.findAll().size();

        // Create the NotWorkingDay with an existing ID
        notWorkingDay.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotWorkingDayMockMvc.perform(post("/api/not-working-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notWorkingDay)))
            .andExpect(status().isBadRequest());

        // Validate the NotWorkingDay in the database
        List<NotWorkingDay> notWorkingDayList = notWorkingDayRepository.findAll();
        assertThat(notWorkingDayList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllNotWorkingDays() throws Exception {
        // Initialize the database
        notWorkingDayRepository.saveAndFlush(notWorkingDay);

        // Get all the notWorkingDayList
        restNotWorkingDayMockMvc.perform(get("/api/not-working-days?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notWorkingDay.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getNotWorkingDay() throws Exception {
        // Initialize the database
        notWorkingDayRepository.saveAndFlush(notWorkingDay);

        // Get the notWorkingDay
        restNotWorkingDayMockMvc.perform(get("/api/not-working-days/{id}", notWorkingDay.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(notWorkingDay.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNotWorkingDay() throws Exception {
        // Get the notWorkingDay
        restNotWorkingDayMockMvc.perform(get("/api/not-working-days/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotWorkingDay() throws Exception {
        // Initialize the database
        notWorkingDayRepository.saveAndFlush(notWorkingDay);

        int databaseSizeBeforeUpdate = notWorkingDayRepository.findAll().size();

        // Update the notWorkingDay
        NotWorkingDay updatedNotWorkingDay = notWorkingDayRepository.findById(notWorkingDay.getId()).get();
        // Disconnect from session so that the updates on updatedNotWorkingDay are not directly saved in db
        em.detach(updatedNotWorkingDay);
        updatedNotWorkingDay
            .name(UPDATED_NAME);

        restNotWorkingDayMockMvc.perform(put("/api/not-working-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNotWorkingDay)))
            .andExpect(status().isOk());

        // Validate the NotWorkingDay in the database
        List<NotWorkingDay> notWorkingDayList = notWorkingDayRepository.findAll();
        assertThat(notWorkingDayList).hasSize(databaseSizeBeforeUpdate);
        NotWorkingDay testNotWorkingDay = notWorkingDayList.get(notWorkingDayList.size() - 1);
        assertThat(testNotWorkingDay.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingNotWorkingDay() throws Exception {
        int databaseSizeBeforeUpdate = notWorkingDayRepository.findAll().size();

        // Create the NotWorkingDay

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotWorkingDayMockMvc.perform(put("/api/not-working-days")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(notWorkingDay)))
            .andExpect(status().isBadRequest());

        // Validate the NotWorkingDay in the database
        List<NotWorkingDay> notWorkingDayList = notWorkingDayRepository.findAll();
        assertThat(notWorkingDayList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNotWorkingDay() throws Exception {
        // Initialize the database
        notWorkingDayRepository.saveAndFlush(notWorkingDay);

        int databaseSizeBeforeDelete = notWorkingDayRepository.findAll().size();

        // Delete the notWorkingDay
        restNotWorkingDayMockMvc.perform(delete("/api/not-working-days/{id}", notWorkingDay.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NotWorkingDay> notWorkingDayList = notWorkingDayRepository.findAll();
        assertThat(notWorkingDayList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotWorkingDay.class);
        NotWorkingDay notWorkingDay1 = new NotWorkingDay();
        notWorkingDay1.setId(1L);
        NotWorkingDay notWorkingDay2 = new NotWorkingDay();
        notWorkingDay2.setId(notWorkingDay1.getId());
        assertThat(notWorkingDay1).isEqualTo(notWorkingDay2);
        notWorkingDay2.setId(2L);
        assertThat(notWorkingDay1).isNotEqualTo(notWorkingDay2);
        notWorkingDay1.setId(null);
        assertThat(notWorkingDay1).isNotEqualTo(notWorkingDay2);
    }
}
