package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.AuthorStat;
import br.ufpa.labes.spm.repository.AuthorStatRepository;
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
 * Integration tests for the {@link AuthorStatResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AuthorStatResourceIT {

    @Autowired
    private AuthorStatRepository authorStatRepository;

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

    private MockMvc restAuthorStatMockMvc;

    private AuthorStat authorStat;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AuthorStatResource authorStatResource = new AuthorStatResource(authorStatRepository);
        this.restAuthorStatMockMvc = MockMvcBuilders.standaloneSetup(authorStatResource)
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
    public static AuthorStat createEntity(EntityManager em) {
        AuthorStat authorStat = new AuthorStat();
        return authorStat;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuthorStat createUpdatedEntity(EntityManager em) {
        AuthorStat authorStat = new AuthorStat();
        return authorStat;
    }

    @BeforeEach
    public void initTest() {
        authorStat = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuthorStat() throws Exception {
        int databaseSizeBeforeCreate = authorStatRepository.findAll().size();

        // Create the AuthorStat
        restAuthorStatMockMvc.perform(post("/api/author-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorStat)))
            .andExpect(status().isCreated());

        // Validate the AuthorStat in the database
        List<AuthorStat> authorStatList = authorStatRepository.findAll();
        assertThat(authorStatList).hasSize(databaseSizeBeforeCreate + 1);
        AuthorStat testAuthorStat = authorStatList.get(authorStatList.size() - 1);
    }

    @Test
    @Transactional
    public void createAuthorStatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = authorStatRepository.findAll().size();

        // Create the AuthorStat with an existing ID
        authorStat.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthorStatMockMvc.perform(post("/api/author-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorStat)))
            .andExpect(status().isBadRequest());

        // Validate the AuthorStat in the database
        List<AuthorStat> authorStatList = authorStatRepository.findAll();
        assertThat(authorStatList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAuthorStats() throws Exception {
        // Initialize the database
        authorStatRepository.saveAndFlush(authorStat);

        // Get all the authorStatList
        restAuthorStatMockMvc.perform(get("/api/author-stats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authorStat.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getAuthorStat() throws Exception {
        // Initialize the database
        authorStatRepository.saveAndFlush(authorStat);

        // Get the authorStat
        restAuthorStatMockMvc.perform(get("/api/author-stats/{id}", authorStat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(authorStat.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAuthorStat() throws Exception {
        // Get the authorStat
        restAuthorStatMockMvc.perform(get("/api/author-stats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuthorStat() throws Exception {
        // Initialize the database
        authorStatRepository.saveAndFlush(authorStat);

        int databaseSizeBeforeUpdate = authorStatRepository.findAll().size();

        // Update the authorStat
        AuthorStat updatedAuthorStat = authorStatRepository.findById(authorStat.getId()).get();
        // Disconnect from session so that the updates on updatedAuthorStat are not directly saved in db
        em.detach(updatedAuthorStat);

        restAuthorStatMockMvc.perform(put("/api/author-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAuthorStat)))
            .andExpect(status().isOk());

        // Validate the AuthorStat in the database
        List<AuthorStat> authorStatList = authorStatRepository.findAll();
        assertThat(authorStatList).hasSize(databaseSizeBeforeUpdate);
        AuthorStat testAuthorStat = authorStatList.get(authorStatList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingAuthorStat() throws Exception {
        int databaseSizeBeforeUpdate = authorStatRepository.findAll().size();

        // Create the AuthorStat

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthorStatMockMvc.perform(put("/api/author-stats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorStat)))
            .andExpect(status().isBadRequest());

        // Validate the AuthorStat in the database
        List<AuthorStat> authorStatList = authorStatRepository.findAll();
        assertThat(authorStatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAuthorStat() throws Exception {
        // Initialize the database
        authorStatRepository.saveAndFlush(authorStat);

        int databaseSizeBeforeDelete = authorStatRepository.findAll().size();

        // Delete the authorStat
        restAuthorStatMockMvc.perform(delete("/api/author-stats/{id}", authorStat.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AuthorStat> authorStatList = authorStatRepository.findAll();
        assertThat(authorStatList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorStat.class);
        AuthorStat authorStat1 = new AuthorStat();
        authorStat1.setId(1L);
        AuthorStat authorStat2 = new AuthorStat();
        authorStat2.setId(authorStat1.getId());
        assertThat(authorStat1).isEqualTo(authorStat2);
        authorStat2.setId(2L);
        assertThat(authorStat1).isNotEqualTo(authorStat2);
        authorStat1.setId(null);
        assertThat(authorStat1).isNotEqualTo(authorStat2);
    }
}
