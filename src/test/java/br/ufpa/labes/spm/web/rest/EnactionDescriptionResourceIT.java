package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.EnactionDescription;
import br.ufpa.labes.spm.repository.EnactionDescriptionRepository;
import br.ufpa.labes.spm.service.EnactionDescriptionService;
import br.ufpa.labes.spm.service.dto.EnactionDescriptionDTO;
import br.ufpa.labes.spm.service.mapper.EnactionDescriptionMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static br.ufpa.labes.spm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EnactionDescriptionResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class EnactionDescriptionResourceIT {

    private static final LocalDate DEFAULT_ACTUAL_BEGIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTUAL_BEGIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ACTUAL_BEGIN = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_ACTUAL_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTUAL_END = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ACTUAL_END = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    @Autowired
    private EnactionDescriptionRepository enactionDescriptionRepository;

    @Autowired
    private EnactionDescriptionMapper enactionDescriptionMapper;

    @Autowired
    private EnactionDescriptionService enactionDescriptionService;

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

    private MockMvc restEnactionDescriptionMockMvc;

    private EnactionDescription enactionDescription;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EnactionDescriptionResource enactionDescriptionResource = new EnactionDescriptionResource(enactionDescriptionService);
        this.restEnactionDescriptionMockMvc = MockMvcBuilders.standaloneSetup(enactionDescriptionResource)
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
    public static EnactionDescription createEntity(EntityManager em) {
        EnactionDescription enactionDescription = new EnactionDescription()
            .actualBegin(DEFAULT_ACTUAL_BEGIN)
            .actualEnd(DEFAULT_ACTUAL_END)
            .state(DEFAULT_STATE);
        return enactionDescription;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EnactionDescription createUpdatedEntity(EntityManager em) {
        EnactionDescription enactionDescription = new EnactionDescription()
            .actualBegin(UPDATED_ACTUAL_BEGIN)
            .actualEnd(UPDATED_ACTUAL_END)
            .state(UPDATED_STATE);
        return enactionDescription;
    }

    @BeforeEach
    public void initTest() {
        enactionDescription = createEntity(em);
    }

    @Test
    @Transactional
    public void createEnactionDescription() throws Exception {
        int databaseSizeBeforeCreate = enactionDescriptionRepository.findAll().size();

        // Create the EnactionDescription
        EnactionDescriptionDTO enactionDescriptionDTO = enactionDescriptionMapper.toDto(enactionDescription);
        restEnactionDescriptionMockMvc.perform(post("/api/enaction-descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(enactionDescriptionDTO)))
            .andExpect(status().isCreated());

        // Validate the EnactionDescription in the database
        List<EnactionDescription> enactionDescriptionList = enactionDescriptionRepository.findAll();
        assertThat(enactionDescriptionList).hasSize(databaseSizeBeforeCreate + 1);
        EnactionDescription testEnactionDescription = enactionDescriptionList.get(enactionDescriptionList.size() - 1);
        assertThat(testEnactionDescription.getActualBegin()).isEqualTo(DEFAULT_ACTUAL_BEGIN);
        assertThat(testEnactionDescription.getActualEnd()).isEqualTo(DEFAULT_ACTUAL_END);
        assertThat(testEnactionDescription.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void createEnactionDescriptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = enactionDescriptionRepository.findAll().size();

        // Create the EnactionDescription with an existing ID
        enactionDescription.setId(1L);
        EnactionDescriptionDTO enactionDescriptionDTO = enactionDescriptionMapper.toDto(enactionDescription);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnactionDescriptionMockMvc.perform(post("/api/enaction-descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(enactionDescriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EnactionDescription in the database
        List<EnactionDescription> enactionDescriptionList = enactionDescriptionRepository.findAll();
        assertThat(enactionDescriptionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEnactionDescriptions() throws Exception {
        // Initialize the database
        enactionDescriptionRepository.saveAndFlush(enactionDescription);

        // Get all the enactionDescriptionList
        restEnactionDescriptionMockMvc.perform(get("/api/enaction-descriptions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enactionDescription.getId().intValue())))
            .andExpect(jsonPath("$.[*].actualBegin").value(hasItem(DEFAULT_ACTUAL_BEGIN.toString())))
            .andExpect(jsonPath("$.[*].actualEnd").value(hasItem(DEFAULT_ACTUAL_END.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }
    
    @Test
    @Transactional
    public void getEnactionDescription() throws Exception {
        // Initialize the database
        enactionDescriptionRepository.saveAndFlush(enactionDescription);

        // Get the enactionDescription
        restEnactionDescriptionMockMvc.perform(get("/api/enaction-descriptions/{id}", enactionDescription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(enactionDescription.getId().intValue()))
            .andExpect(jsonPath("$.actualBegin").value(DEFAULT_ACTUAL_BEGIN.toString()))
            .andExpect(jsonPath("$.actualEnd").value(DEFAULT_ACTUAL_END.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEnactionDescription() throws Exception {
        // Get the enactionDescription
        restEnactionDescriptionMockMvc.perform(get("/api/enaction-descriptions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEnactionDescription() throws Exception {
        // Initialize the database
        enactionDescriptionRepository.saveAndFlush(enactionDescription);

        int databaseSizeBeforeUpdate = enactionDescriptionRepository.findAll().size();

        // Update the enactionDescription
        EnactionDescription updatedEnactionDescription = enactionDescriptionRepository.findById(enactionDescription.getId()).get();
        // Disconnect from session so that the updates on updatedEnactionDescription are not directly saved in db
        em.detach(updatedEnactionDescription);
        updatedEnactionDescription
            .actualBegin(UPDATED_ACTUAL_BEGIN)
            .actualEnd(UPDATED_ACTUAL_END)
            .state(UPDATED_STATE);
        EnactionDescriptionDTO enactionDescriptionDTO = enactionDescriptionMapper.toDto(updatedEnactionDescription);

        restEnactionDescriptionMockMvc.perform(put("/api/enaction-descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(enactionDescriptionDTO)))
            .andExpect(status().isOk());

        // Validate the EnactionDescription in the database
        List<EnactionDescription> enactionDescriptionList = enactionDescriptionRepository.findAll();
        assertThat(enactionDescriptionList).hasSize(databaseSizeBeforeUpdate);
        EnactionDescription testEnactionDescription = enactionDescriptionList.get(enactionDescriptionList.size() - 1);
        assertThat(testEnactionDescription.getActualBegin()).isEqualTo(UPDATED_ACTUAL_BEGIN);
        assertThat(testEnactionDescription.getActualEnd()).isEqualTo(UPDATED_ACTUAL_END);
        assertThat(testEnactionDescription.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    public void updateNonExistingEnactionDescription() throws Exception {
        int databaseSizeBeforeUpdate = enactionDescriptionRepository.findAll().size();

        // Create the EnactionDescription
        EnactionDescriptionDTO enactionDescriptionDTO = enactionDescriptionMapper.toDto(enactionDescription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnactionDescriptionMockMvc.perform(put("/api/enaction-descriptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(enactionDescriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EnactionDescription in the database
        List<EnactionDescription> enactionDescriptionList = enactionDescriptionRepository.findAll();
        assertThat(enactionDescriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEnactionDescription() throws Exception {
        // Initialize the database
        enactionDescriptionRepository.saveAndFlush(enactionDescription);

        int databaseSizeBeforeDelete = enactionDescriptionRepository.findAll().size();

        // Delete the enactionDescription
        restEnactionDescriptionMockMvc.perform(delete("/api/enaction-descriptions/{id}", enactionDescription.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EnactionDescription> enactionDescriptionList = enactionDescriptionRepository.findAll();
        assertThat(enactionDescriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EnactionDescription.class);
        EnactionDescription enactionDescription1 = new EnactionDescription();
        enactionDescription1.setId(1L);
        EnactionDescription enactionDescription2 = new EnactionDescription();
        enactionDescription2.setId(enactionDescription1.getId());
        assertThat(enactionDescription1).isEqualTo(enactionDescription2);
        enactionDescription2.setId(2L);
        assertThat(enactionDescription1).isNotEqualTo(enactionDescription2);
        enactionDescription1.setId(null);
        assertThat(enactionDescription1).isNotEqualTo(enactionDescription2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EnactionDescriptionDTO.class);
        EnactionDescriptionDTO enactionDescriptionDTO1 = new EnactionDescriptionDTO();
        enactionDescriptionDTO1.setId(1L);
        EnactionDescriptionDTO enactionDescriptionDTO2 = new EnactionDescriptionDTO();
        assertThat(enactionDescriptionDTO1).isNotEqualTo(enactionDescriptionDTO2);
        enactionDescriptionDTO2.setId(enactionDescriptionDTO1.getId());
        assertThat(enactionDescriptionDTO1).isEqualTo(enactionDescriptionDTO2);
        enactionDescriptionDTO2.setId(2L);
        assertThat(enactionDescriptionDTO1).isNotEqualTo(enactionDescriptionDTO2);
        enactionDescriptionDTO1.setId(null);
        assertThat(enactionDescriptionDTO1).isNotEqualTo(enactionDescriptionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(enactionDescriptionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(enactionDescriptionMapper.fromId(null)).isNull();
    }
}
