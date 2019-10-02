package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.TagStats;
import br.ufpa.labes.spm.repository.TagStatsRepository;
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
 * Integration tests for the {@link TagStatsResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class TagStatsResourceIT {

    private static final Long DEFAULT_COUNT = 1L;
    private static final Long UPDATED_COUNT = 2L;
    private static final Long SMALLER_COUNT = 1L - 1L;

    @Autowired
    private TagStatsRepository tagStatsRepository;

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

    private MockMvc restTagStatsMockMvc;

    private TagStats tagStats;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TagStatsResource tagStatsResource = new TagStatsResource(tagStatsRepository);
        this.restTagStatsMockMvc = MockMvcBuilders.standaloneSetup(tagStatsResource)
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
    public static TagStats createEntity(EntityManager em) {
        TagStats tagStats = new TagStats()
            .count(DEFAULT_COUNT);
        return tagStats;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TagStats createUpdatedEntity(EntityManager em) {
        TagStats tagStats = new TagStats()
            .count(UPDATED_COUNT);
        return tagStats;
    }

    @BeforeEach
    public void initTest() {
        tagStats = createEntity(em);
    }

    @Test
    @Transactional
    public void createTagStats() throws Exception {
        int databaseSizeBeforeCreate = tagStatsRepository.findAll().size();

        // Create the TagStats
        restTagStatsMockMvc.perform(post("/api/tag-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagStats)))
            .andExpect(status().isCreated());

        // Validate the TagStats in the database
        List<TagStats> tagStatsList = tagStatsRepository.findAll();
        assertThat(tagStatsList).hasSize(databaseSizeBeforeCreate + 1);
        TagStats testTagStats = tagStatsList.get(tagStatsList.size() - 1);
        assertThat(testTagStats.getCount()).isEqualTo(DEFAULT_COUNT);
    }

    @Test
    @Transactional
    public void createTagStatsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tagStatsRepository.findAll().size();

        // Create the TagStats with an existing ID
        tagStats.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagStatsMockMvc.perform(post("/api/tag-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagStats)))
            .andExpect(status().isBadRequest());

        // Validate the TagStats in the database
        List<TagStats> tagStatsList = tagStatsRepository.findAll();
        assertThat(tagStatsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTagStats() throws Exception {
        // Initialize the database
        tagStatsRepository.saveAndFlush(tagStats);

        // Get all the tagStatsList
        restTagStatsMockMvc.perform(get("/api/tag-stats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tagStats.getId().intValue())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT.intValue())));
    }
    
    @Test
    @Transactional
    public void getTagStats() throws Exception {
        // Initialize the database
        tagStatsRepository.saveAndFlush(tagStats);

        // Get the tagStats
        restTagStatsMockMvc.perform(get("/api/tag-stats/{id}", tagStats.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tagStats.getId().intValue()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTagStats() throws Exception {
        // Get the tagStats
        restTagStatsMockMvc.perform(get("/api/tag-stats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTagStats() throws Exception {
        // Initialize the database
        tagStatsRepository.saveAndFlush(tagStats);

        int databaseSizeBeforeUpdate = tagStatsRepository.findAll().size();

        // Update the tagStats
        TagStats updatedTagStats = tagStatsRepository.findById(tagStats.getId()).get();
        // Disconnect from session so that the updates on updatedTagStats are not directly saved in db
        em.detach(updatedTagStats);
        updatedTagStats
            .count(UPDATED_COUNT);

        restTagStatsMockMvc.perform(put("/api/tag-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTagStats)))
            .andExpect(status().isOk());

        // Validate the TagStats in the database
        List<TagStats> tagStatsList = tagStatsRepository.findAll();
        assertThat(tagStatsList).hasSize(databaseSizeBeforeUpdate);
        TagStats testTagStats = tagStatsList.get(tagStatsList.size() - 1);
        assertThat(testTagStats.getCount()).isEqualTo(UPDATED_COUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingTagStats() throws Exception {
        int databaseSizeBeforeUpdate = tagStatsRepository.findAll().size();

        // Create the TagStats

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagStatsMockMvc.perform(put("/api/tag-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagStats)))
            .andExpect(status().isBadRequest());

        // Validate the TagStats in the database
        List<TagStats> tagStatsList = tagStatsRepository.findAll();
        assertThat(tagStatsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTagStats() throws Exception {
        // Initialize the database
        tagStatsRepository.saveAndFlush(tagStats);

        int databaseSizeBeforeDelete = tagStatsRepository.findAll().size();

        // Delete the tagStats
        restTagStatsMockMvc.perform(delete("/api/tag-stats/{id}", tagStats.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TagStats> tagStatsList = tagStatsRepository.findAll();
        assertThat(tagStatsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagStats.class);
        TagStats tagStats1 = new TagStats();
        tagStats1.setId(1L);
        TagStats tagStats2 = new TagStats();
        tagStats2.setId(tagStats1.getId());
        assertThat(tagStats1).isEqualTo(tagStats2);
        tagStats2.setId(2L);
        assertThat(tagStats1).isNotEqualTo(tagStats2);
        tagStats1.setId(null);
        assertThat(tagStats1).isNotEqualTo(tagStats2);
    }
}
