package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ClassMethodCall;
import br.ufpa.labes.spm.repository.ClassMethodCallRepository;
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

/** Integration tests for the {@link ClassMethodCallResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ClassMethodCallResourceIT {

  private static final String DEFAULT_IDENT = "AAAAAAAAAA";
  private static final String UPDATED_IDENT = "BBBBBBBBBB";

  private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
  private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_METHOD_NAME = "AAAAAAAAAA";
  private static final String UPDATED_METHOD_NAME = "BBBBBBBBBB";

  private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
  private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

  @Autowired private ClassMethodCallRepository classMethodCallRepository;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restClassMethodCallMockMvc;

  private ClassMethodCall classMethodCall;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ClassMethodCallResource classMethodCallResource =
        new ClassMethodCallResource(classMethodCallRepository);
    this.restClassMethodCallMockMvc =
        MockMvcBuilders.standaloneSetup(classMethodCallResource)
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
  public static ClassMethodCall createEntity(EntityManager em) {
    ClassMethodCall classMethodCall =
        new ClassMethodCall()
            .ident(DEFAULT_IDENT)
            .className(DEFAULT_CLASS_NAME)
            .methodName(DEFAULT_METHOD_NAME)
            .description(DEFAULT_DESCRIPTION);
    return classMethodCall;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ClassMethodCall createUpdatedEntity(EntityManager em) {
    ClassMethodCall classMethodCall =
        new ClassMethodCall()
            .ident(UPDATED_IDENT)
            .className(UPDATED_CLASS_NAME)
            .methodName(UPDATED_METHOD_NAME)
            .description(UPDATED_DESCRIPTION);
    return classMethodCall;
  }

  @BeforeEach
  public void initTest() {
    classMethodCall = createEntity(em);
  }

  @Test
  @Transactional
  public void createClassMethodCall() throws Exception {
    int databaseSizeBeforeCreate = classMethodCallRepository.findAll().size();

    // Create the ClassMethodCall
    restClassMethodCallMockMvc
        .perform(
            post("/api/class-method-calls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classMethodCall)))
        .andExpect(status().isCreated());

    // Validate the ClassMethodCall in the database
    List<ClassMethodCall> classMethodCallList = classMethodCallRepository.findAll();
    assertThat(classMethodCallList).hasSize(databaseSizeBeforeCreate + 1);
    ClassMethodCall testClassMethodCall = classMethodCallList.get(classMethodCallList.size() - 1);
    assertThat(testClassMethodCall.getIdent()).isEqualTo(DEFAULT_IDENT);
    assertThat(testClassMethodCall.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
    assertThat(testClassMethodCall.getMethodName()).isEqualTo(DEFAULT_METHOD_NAME);
    assertThat(testClassMethodCall.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
  }

  @Test
  @Transactional
  public void createClassMethodCallWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = classMethodCallRepository.findAll().size();

    // Create the ClassMethodCall with an existing ID
    classMethodCall.setId(1L);

    // An entity with an existing ID cannot be created, so this API call must fail
    restClassMethodCallMockMvc
        .perform(
            post("/api/class-method-calls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classMethodCall)))
        .andExpect(status().isBadRequest());

    // Validate the ClassMethodCall in the database
    List<ClassMethodCall> classMethodCallList = classMethodCallRepository.findAll();
    assertThat(classMethodCallList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllClassMethodCalls() throws Exception {
    // Initialize the database
    classMethodCallRepository.saveAndFlush(classMethodCall);

    // Get all the classMethodCallList
    restClassMethodCallMockMvc
        .perform(get("/api/class-method-calls?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(classMethodCall.getId().intValue())))
        .andExpect(jsonPath("$.[*].ident").value(hasItem(DEFAULT_IDENT.toString())))
        .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME.toString())))
        .andExpect(jsonPath("$.[*].methodName").value(hasItem(DEFAULT_METHOD_NAME.toString())))
        .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
  }

  @Test
  @Transactional
  public void getClassMethodCall() throws Exception {
    // Initialize the database
    classMethodCallRepository.saveAndFlush(classMethodCall);

    // Get the classMethodCall
    restClassMethodCallMockMvc
        .perform(get("/api/class-method-calls/{id}", classMethodCall.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(classMethodCall.getId().intValue()))
        .andExpect(jsonPath("$.ident").value(DEFAULT_IDENT.toString()))
        .andExpect(jsonPath("$.className").value(DEFAULT_CLASS_NAME.toString()))
        .andExpect(jsonPath("$.methodName").value(DEFAULT_METHOD_NAME.toString()))
        .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingClassMethodCall() throws Exception {
    // Get the classMethodCall
    restClassMethodCallMockMvc
        .perform(get("/api/class-method-calls/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateClassMethodCall() throws Exception {
    // Initialize the database
    classMethodCallRepository.saveAndFlush(classMethodCall);

    int databaseSizeBeforeUpdate = classMethodCallRepository.findAll().size();

    // Update the classMethodCall
    ClassMethodCall updatedClassMethodCall =
        classMethodCallRepository.findById(classMethodCall.getId()).get();
    // Disconnect from session so that the updates on updatedClassMethodCall are not directly saved
    // in db
    em.detach(updatedClassMethodCall);
    updatedClassMethodCall
        .ident(UPDATED_IDENT)
        .className(UPDATED_CLASS_NAME)
        .methodName(UPDATED_METHOD_NAME)
        .description(UPDATED_DESCRIPTION);

    restClassMethodCallMockMvc
        .perform(
            put("/api/class-method-calls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClassMethodCall)))
        .andExpect(status().isOk());

    // Validate the ClassMethodCall in the database
    List<ClassMethodCall> classMethodCallList = classMethodCallRepository.findAll();
    assertThat(classMethodCallList).hasSize(databaseSizeBeforeUpdate);
    ClassMethodCall testClassMethodCall = classMethodCallList.get(classMethodCallList.size() - 1);
    assertThat(testClassMethodCall.getIdent()).isEqualTo(UPDATED_IDENT);
    assertThat(testClassMethodCall.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
    assertThat(testClassMethodCall.getMethodName()).isEqualTo(UPDATED_METHOD_NAME);
    assertThat(testClassMethodCall.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
  }

  @Test
  @Transactional
  public void updateNonExistingClassMethodCall() throws Exception {
    int databaseSizeBeforeUpdate = classMethodCallRepository.findAll().size();

    // Create the ClassMethodCall

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restClassMethodCallMockMvc
        .perform(
            put("/api/class-method-calls")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(classMethodCall)))
        .andExpect(status().isBadRequest());

    // Validate the ClassMethodCall in the database
    List<ClassMethodCall> classMethodCallList = classMethodCallRepository.findAll();
    assertThat(classMethodCallList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteClassMethodCall() throws Exception {
    // Initialize the database
    classMethodCallRepository.saveAndFlush(classMethodCall);

    int databaseSizeBeforeDelete = classMethodCallRepository.findAll().size();

    // Delete the classMethodCall
    restClassMethodCallMockMvc
        .perform(
            delete("/api/class-method-calls/{id}", classMethodCall.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ClassMethodCall> classMethodCallList = classMethodCallRepository.findAll();
    assertThat(classMethodCallList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ClassMethodCall.class);
    ClassMethodCall classMethodCall1 = new ClassMethodCall();
    classMethodCall1.setId(1L);
    ClassMethodCall classMethodCall2 = new ClassMethodCall();
    classMethodCall2.setId(classMethodCall1.getId());
    assertThat(classMethodCall1).isEqualTo(classMethodCall2);
    classMethodCall2.setId(2L);
    assertThat(classMethodCall1).isNotEqualTo(classMethodCall2);
    classMethodCall1.setId(null);
    assertThat(classMethodCall1).isNotEqualTo(classMethodCall2);
  }
}
