package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Author;
import br.ufpa.labes.spm.repository.AuthorRepository;
import br.ufpa.labes.spm.service.AuthorService;
import br.ufpa.labes.spm.service.dto.AuthorDTO;
import br.ufpa.labes.spm.service.mapper.AuthorMapper;
import br.ufpa.labes.spm.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
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

/** Integration tests for the {@link AuthorResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class AuthorResourceIT {

  private static final String DEFAULT_UID = "AAAAAAAAAA";
  private static final String UPDATED_UID = "BBBBBBBBBB";

  private static final Integer DEFAULT_VERSION = 1;
  private static final Integer UPDATED_VERSION = 2;
  private static final Integer SMALLER_VERSION = 1 - 1;

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
  private static final String UPDATED_EMAIL = "BBBBBBBBBB";

  private static final String DEFAULT_INTERESTS = "AAAAAAAAAA";
  private static final String UPDATED_INTERESTS = "BBBBBBBBBB";

  private static final String DEFAULT_CITY = "AAAAAAAAAA";
  private static final String UPDATED_CITY = "BBBBBBBBBB";

  private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
  private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

  private static final String DEFAULT_PHOTO_URL = "AAAAAAAAAA";
  private static final String UPDATED_PHOTO_URL = "BBBBBBBBBB";

  @Autowired private AuthorRepository authorRepository;

  @Mock private AuthorRepository authorRepositoryMock;

  @Autowired private AuthorMapper authorMapper;

  @Mock private AuthorService authorServiceMock;

  @Autowired private AuthorService authorService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restAuthorMockMvc;

  private Author author;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final AuthorResource authorResource = new AuthorResource(authorService);
    this.restAuthorMockMvc =
        MockMvcBuilders.standaloneSetup(authorResource)
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
  public static Author createEntity(EntityManager em) {
    Author author =
        new Author()
            .uid(DEFAULT_UID)
            .version(DEFAULT_VERSION)
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .interests(DEFAULT_INTERESTS)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .photoURL(DEFAULT_PHOTO_URL);
    return author;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Author createUpdatedEntity(EntityManager em) {
    Author author =
        new Author()
            .uid(UPDATED_UID)
            .version(UPDATED_VERSION)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .interests(UPDATED_INTERESTS)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .photoURL(UPDATED_PHOTO_URL);
    return author;
  }

  @BeforeEach
  public void initTest() {
    author = createEntity(em);
  }

  @Test
  @Transactional
  public void createAuthor() throws Exception {
    int databaseSizeBeforeCreate = authorRepository.findAll().size();

    // Create the Author
    AuthorDTO authorDTO = authorMapper.toDto(author);
    restAuthorMockMvc
        .perform(
            post("/api/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(authorDTO)))
        .andExpect(status().isCreated());

    // Validate the Author in the database
    List<Author> authorList = authorRepository.findAll();
    assertThat(authorList).hasSize(databaseSizeBeforeCreate + 1);
    Author testAuthor = authorList.get(authorList.size() - 1);
    assertThat(testAuthor.getUid()).isEqualTo(DEFAULT_UID);
    assertThat(testAuthor.getVersion()).isEqualTo(DEFAULT_VERSION);
    assertThat(testAuthor.getName()).isEqualTo(DEFAULT_NAME);
    assertThat(testAuthor.getEmail()).isEqualTo(DEFAULT_EMAIL);
    assertThat(testAuthor.getInterests()).isEqualTo(DEFAULT_INTERESTS);
    assertThat(testAuthor.getCity()).isEqualTo(DEFAULT_CITY);
    assertThat(testAuthor.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    assertThat(testAuthor.getPhotoURL()).isEqualTo(DEFAULT_PHOTO_URL);
  }

  @Test
  @Transactional
  public void createAuthorWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = authorRepository.findAll().size();

    // Create the Author with an existing ID
    author.setId(1L);
    AuthorDTO authorDTO = authorMapper.toDto(author);

    // An entity with an existing ID cannot be created, so this API call must fail
    restAuthorMockMvc
        .perform(
            post("/api/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(authorDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Author in the database
    List<Author> authorList = authorRepository.findAll();
    assertThat(authorList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllAuthors() throws Exception {
    // Initialize the database
    authorRepository.saveAndFlush(author);

    // Get all the authorList
    restAuthorMockMvc
        .perform(get("/api/authors?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(author.getId().intValue())))
        .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
        .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
        .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
        .andExpect(jsonPath("$.[*].interests").value(hasItem(DEFAULT_INTERESTS.toString())))
        .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
        .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY.toString())))
        .andExpect(jsonPath("$.[*].photoURL").value(hasItem(DEFAULT_PHOTO_URL.toString())));
  }

  @SuppressWarnings({"unchecked"})
  public void getAllAuthorsWithEagerRelationshipsIsEnabled() throws Exception {
    AuthorResource authorResource = new AuthorResource(authorServiceMock);
    when(authorServiceMock.findAllWithEagerRelationships(any()))
        .thenReturn(new PageImpl(new ArrayList<>()));

    MockMvc restAuthorMockMvc =
        MockMvcBuilders.standaloneSetup(authorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .build();

    restAuthorMockMvc.perform(get("/api/authors?eagerload=true")).andExpect(status().isOk());

    verify(authorServiceMock, times(1)).findAllWithEagerRelationships(any());
  }

  @SuppressWarnings({"unchecked"})
  public void getAllAuthorsWithEagerRelationshipsIsNotEnabled() throws Exception {
    AuthorResource authorResource = new AuthorResource(authorServiceMock);
    when(authorServiceMock.findAllWithEagerRelationships(any()))
        .thenReturn(new PageImpl(new ArrayList<>()));
    MockMvc restAuthorMockMvc =
        MockMvcBuilders.standaloneSetup(authorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .build();

    restAuthorMockMvc.perform(get("/api/authors?eagerload=true")).andExpect(status().isOk());

    verify(authorServiceMock, times(1)).findAllWithEagerRelationships(any());
  }

  @Test
  @Transactional
  public void getAuthor() throws Exception {
    // Initialize the database
    authorRepository.saveAndFlush(author);

    // Get the author
    restAuthorMockMvc
        .perform(get("/api/authors/{id}", author.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(author.getId().intValue()))
        .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
        .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
        .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
        .andExpect(jsonPath("$.interests").value(DEFAULT_INTERESTS.toString()))
        .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
        .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY.toString()))
        .andExpect(jsonPath("$.photoURL").value(DEFAULT_PHOTO_URL.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingAuthor() throws Exception {
    // Get the author
    restAuthorMockMvc
        .perform(get("/api/authors/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateAuthor() throws Exception {
    // Initialize the database
    authorRepository.saveAndFlush(author);

    int databaseSizeBeforeUpdate = authorRepository.findAll().size();

    // Update the author
    Author updatedAuthor = authorRepository.findById(author.getId()).get();
    // Disconnect from session so that the updates on updatedAuthor are not directly saved in db
    em.detach(updatedAuthor);
    updatedAuthor
        .uid(UPDATED_UID)
        .version(UPDATED_VERSION)
        .name(UPDATED_NAME)
        .email(UPDATED_EMAIL)
        .interests(UPDATED_INTERESTS)
        .city(UPDATED_CITY)
        .country(UPDATED_COUNTRY)
        .photoURL(UPDATED_PHOTO_URL);
    AuthorDTO authorDTO = authorMapper.toDto(updatedAuthor);

    restAuthorMockMvc
        .perform(
            put("/api/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(authorDTO)))
        .andExpect(status().isOk());

    // Validate the Author in the database
    List<Author> authorList = authorRepository.findAll();
    assertThat(authorList).hasSize(databaseSizeBeforeUpdate);
    Author testAuthor = authorList.get(authorList.size() - 1);
    assertThat(testAuthor.getUid()).isEqualTo(UPDATED_UID);
    assertThat(testAuthor.getVersion()).isEqualTo(UPDATED_VERSION);
    assertThat(testAuthor.getName()).isEqualTo(UPDATED_NAME);
    assertThat(testAuthor.getEmail()).isEqualTo(UPDATED_EMAIL);
    assertThat(testAuthor.getInterests()).isEqualTo(UPDATED_INTERESTS);
    assertThat(testAuthor.getCity()).isEqualTo(UPDATED_CITY);
    assertThat(testAuthor.getCountry()).isEqualTo(UPDATED_COUNTRY);
    assertThat(testAuthor.getPhotoURL()).isEqualTo(UPDATED_PHOTO_URL);
  }

  @Test
  @Transactional
  public void updateNonExistingAuthor() throws Exception {
    int databaseSizeBeforeUpdate = authorRepository.findAll().size();

    // Create the Author
    AuthorDTO authorDTO = authorMapper.toDto(author);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restAuthorMockMvc
        .perform(
            put("/api/authors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(authorDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Author in the database
    List<Author> authorList = authorRepository.findAll();
    assertThat(authorList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteAuthor() throws Exception {
    // Initialize the database
    authorRepository.saveAndFlush(author);

    int databaseSizeBeforeDelete = authorRepository.findAll().size();

    // Delete the author
    restAuthorMockMvc
        .perform(delete("/api/authors/{id}", author.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Author> authorList = authorRepository.findAll();
    assertThat(authorList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Author.class);
    Author author1 = new Author();
    author1.setId(1L);
    Author author2 = new Author();
    author2.setId(author1.getId());
    assertThat(author1).isEqualTo(author2);
    author2.setId(2L);
    assertThat(author1).isNotEqualTo(author2);
    author1.setId(null);
    assertThat(author1).isNotEqualTo(author2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(AuthorDTO.class);
    AuthorDTO authorDTO1 = new AuthorDTO();
    authorDTO1.setId(1L);
    AuthorDTO authorDTO2 = new AuthorDTO();
    assertThat(authorDTO1).isNotEqualTo(authorDTO2);
    authorDTO2.setId(authorDTO1.getId());
    assertThat(authorDTO1).isEqualTo(authorDTO2);
    authorDTO2.setId(2L);
    assertThat(authorDTO1).isNotEqualTo(authorDTO2);
    authorDTO1.setId(null);
    assertThat(authorDTO1).isNotEqualTo(authorDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(authorMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(authorMapper.fromId(null)).isNull();
  }
}
