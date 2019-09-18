package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.WebAPSEEObject;
import br.ufpa.labes.spm.repository.WebAPSEEObjectRepository;
import br.ufpa.labes.spm.service.WebAPSEEObjectService;
import br.ufpa.labes.spm.service.dto.WebAPSEEObjectDTO;
import br.ufpa.labes.spm.service.mapper.WebAPSEEObjectMapper;
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

import br.ufpa.labes.spm.domain.enumeration.WebAPSEEObjectType;
/**
 * Integration tests for the {@link WebAPSEEObjectResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class WebAPSEEObjectResourceIT {

    private static final Long DEFAULT_THE_REFERRED_OID = 1L;
    private static final Long UPDATED_THE_REFERRED_OID = 2L;
    private static final Long SMALLER_THE_REFERRED_OID = 1L - 1L;

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final WebAPSEEObjectType DEFAULT_WEB_APSEE_OBJECT_TYPE = WebAPSEEObjectType.ACTIVITY;
    private static final WebAPSEEObjectType UPDATED_WEB_APSEE_OBJECT_TYPE = WebAPSEEObjectType.REQ_AGENT;

    @Autowired
    private WebAPSEEObjectRepository webAPSEEObjectRepository;

    @Autowired
    private WebAPSEEObjectMapper webAPSEEObjectMapper;

    @Autowired
    private WebAPSEEObjectService webAPSEEObjectService;

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

    private MockMvc restWebAPSEEObjectMockMvc;

    private WebAPSEEObject webAPSEEObject;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WebAPSEEObjectResource webAPSEEObjectResource = new WebAPSEEObjectResource(webAPSEEObjectService);
        this.restWebAPSEEObjectMockMvc = MockMvcBuilders.standaloneSetup(webAPSEEObjectResource)
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
    public static WebAPSEEObject createEntity(EntityManager em) {
        WebAPSEEObject webAPSEEObject = new WebAPSEEObject()
            .theReferredOid(DEFAULT_THE_REFERRED_OID)
            .className(DEFAULT_CLASS_NAME)
            .webAPSEEObjectType(DEFAULT_WEB_APSEE_OBJECT_TYPE);
        return webAPSEEObject;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WebAPSEEObject createUpdatedEntity(EntityManager em) {
        WebAPSEEObject webAPSEEObject = new WebAPSEEObject()
            .theReferredOid(UPDATED_THE_REFERRED_OID)
            .className(UPDATED_CLASS_NAME)
            .webAPSEEObjectType(UPDATED_WEB_APSEE_OBJECT_TYPE);
        return webAPSEEObject;
    }

    @BeforeEach
    public void initTest() {
        webAPSEEObject = createEntity(em);
    }

    @Test
    @Transactional
    public void createWebAPSEEObject() throws Exception {
        int databaseSizeBeforeCreate = webAPSEEObjectRepository.findAll().size();

        // Create the WebAPSEEObject
        WebAPSEEObjectDTO webAPSEEObjectDTO = webAPSEEObjectMapper.toDto(webAPSEEObject);
        restWebAPSEEObjectMockMvc.perform(post("/api/web-apsee-objects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(webAPSEEObjectDTO)))
            .andExpect(status().isCreated());

        // Validate the WebAPSEEObject in the database
        List<WebAPSEEObject> webAPSEEObjectList = webAPSEEObjectRepository.findAll();
        assertThat(webAPSEEObjectList).hasSize(databaseSizeBeforeCreate + 1);
        WebAPSEEObject testWebAPSEEObject = webAPSEEObjectList.get(webAPSEEObjectList.size() - 1);
        assertThat(testWebAPSEEObject.getTheReferredOid()).isEqualTo(DEFAULT_THE_REFERRED_OID);
        assertThat(testWebAPSEEObject.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
        assertThat(testWebAPSEEObject.getWebAPSEEObjectType()).isEqualTo(DEFAULT_WEB_APSEE_OBJECT_TYPE);
    }

    @Test
    @Transactional
    public void createWebAPSEEObjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = webAPSEEObjectRepository.findAll().size();

        // Create the WebAPSEEObject with an existing ID
        webAPSEEObject.setId(1L);
        WebAPSEEObjectDTO webAPSEEObjectDTO = webAPSEEObjectMapper.toDto(webAPSEEObject);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWebAPSEEObjectMockMvc.perform(post("/api/web-apsee-objects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(webAPSEEObjectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WebAPSEEObject in the database
        List<WebAPSEEObject> webAPSEEObjectList = webAPSEEObjectRepository.findAll();
        assertThat(webAPSEEObjectList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTheReferredOidIsRequired() throws Exception {
        int databaseSizeBeforeTest = webAPSEEObjectRepository.findAll().size();
        // set the field null
        webAPSEEObject.setTheReferredOid(null);

        // Create the WebAPSEEObject, which fails.
        WebAPSEEObjectDTO webAPSEEObjectDTO = webAPSEEObjectMapper.toDto(webAPSEEObject);

        restWebAPSEEObjectMockMvc.perform(post("/api/web-apsee-objects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(webAPSEEObjectDTO)))
            .andExpect(status().isBadRequest());

        List<WebAPSEEObject> webAPSEEObjectList = webAPSEEObjectRepository.findAll();
        assertThat(webAPSEEObjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClassNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = webAPSEEObjectRepository.findAll().size();
        // set the field null
        webAPSEEObject.setClassName(null);

        // Create the WebAPSEEObject, which fails.
        WebAPSEEObjectDTO webAPSEEObjectDTO = webAPSEEObjectMapper.toDto(webAPSEEObject);

        restWebAPSEEObjectMockMvc.perform(post("/api/web-apsee-objects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(webAPSEEObjectDTO)))
            .andExpect(status().isBadRequest());

        List<WebAPSEEObject> webAPSEEObjectList = webAPSEEObjectRepository.findAll();
        assertThat(webAPSEEObjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWebAPSEEObjects() throws Exception {
        // Initialize the database
        webAPSEEObjectRepository.saveAndFlush(webAPSEEObject);

        // Get all the webAPSEEObjectList
        restWebAPSEEObjectMockMvc.perform(get("/api/web-apsee-objects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(webAPSEEObject.getId().intValue())))
            .andExpect(jsonPath("$.[*].theReferredOid").value(hasItem(DEFAULT_THE_REFERRED_OID.intValue())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME.toString())))
            .andExpect(jsonPath("$.[*].webAPSEEObjectType").value(hasItem(DEFAULT_WEB_APSEE_OBJECT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getWebAPSEEObject() throws Exception {
        // Initialize the database
        webAPSEEObjectRepository.saveAndFlush(webAPSEEObject);

        // Get the webAPSEEObject
        restWebAPSEEObjectMockMvc.perform(get("/api/web-apsee-objects/{id}", webAPSEEObject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(webAPSEEObject.getId().intValue()))
            .andExpect(jsonPath("$.theReferredOid").value(DEFAULT_THE_REFERRED_OID.intValue()))
            .andExpect(jsonPath("$.className").value(DEFAULT_CLASS_NAME.toString()))
            .andExpect(jsonPath("$.webAPSEEObjectType").value(DEFAULT_WEB_APSEE_OBJECT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingWebAPSEEObject() throws Exception {
        // Get the webAPSEEObject
        restWebAPSEEObjectMockMvc.perform(get("/api/web-apsee-objects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWebAPSEEObject() throws Exception {
        // Initialize the database
        webAPSEEObjectRepository.saveAndFlush(webAPSEEObject);

        int databaseSizeBeforeUpdate = webAPSEEObjectRepository.findAll().size();

        // Update the webAPSEEObject
        WebAPSEEObject updatedWebAPSEEObject = webAPSEEObjectRepository.findById(webAPSEEObject.getId()).get();
        // Disconnect from session so that the updates on updatedWebAPSEEObject are not directly saved in db
        em.detach(updatedWebAPSEEObject);
        updatedWebAPSEEObject
            .theReferredOid(UPDATED_THE_REFERRED_OID)
            .className(UPDATED_CLASS_NAME)
            .webAPSEEObjectType(UPDATED_WEB_APSEE_OBJECT_TYPE);
        WebAPSEEObjectDTO webAPSEEObjectDTO = webAPSEEObjectMapper.toDto(updatedWebAPSEEObject);

        restWebAPSEEObjectMockMvc.perform(put("/api/web-apsee-objects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(webAPSEEObjectDTO)))
            .andExpect(status().isOk());

        // Validate the WebAPSEEObject in the database
        List<WebAPSEEObject> webAPSEEObjectList = webAPSEEObjectRepository.findAll();
        assertThat(webAPSEEObjectList).hasSize(databaseSizeBeforeUpdate);
        WebAPSEEObject testWebAPSEEObject = webAPSEEObjectList.get(webAPSEEObjectList.size() - 1);
        assertThat(testWebAPSEEObject.getTheReferredOid()).isEqualTo(UPDATED_THE_REFERRED_OID);
        assertThat(testWebAPSEEObject.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testWebAPSEEObject.getWebAPSEEObjectType()).isEqualTo(UPDATED_WEB_APSEE_OBJECT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingWebAPSEEObject() throws Exception {
        int databaseSizeBeforeUpdate = webAPSEEObjectRepository.findAll().size();

        // Create the WebAPSEEObject
        WebAPSEEObjectDTO webAPSEEObjectDTO = webAPSEEObjectMapper.toDto(webAPSEEObject);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWebAPSEEObjectMockMvc.perform(put("/api/web-apsee-objects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(webAPSEEObjectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WebAPSEEObject in the database
        List<WebAPSEEObject> webAPSEEObjectList = webAPSEEObjectRepository.findAll();
        assertThat(webAPSEEObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWebAPSEEObject() throws Exception {
        // Initialize the database
        webAPSEEObjectRepository.saveAndFlush(webAPSEEObject);

        int databaseSizeBeforeDelete = webAPSEEObjectRepository.findAll().size();

        // Delete the webAPSEEObject
        restWebAPSEEObjectMockMvc.perform(delete("/api/web-apsee-objects/{id}", webAPSEEObject.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WebAPSEEObject> webAPSEEObjectList = webAPSEEObjectRepository.findAll();
        assertThat(webAPSEEObjectList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WebAPSEEObject.class);
        WebAPSEEObject webAPSEEObject1 = new WebAPSEEObject();
        webAPSEEObject1.setId(1L);
        WebAPSEEObject webAPSEEObject2 = new WebAPSEEObject();
        webAPSEEObject2.setId(webAPSEEObject1.getId());
        assertThat(webAPSEEObject1).isEqualTo(webAPSEEObject2);
        webAPSEEObject2.setId(2L);
        assertThat(webAPSEEObject1).isNotEqualTo(webAPSEEObject2);
        webAPSEEObject1.setId(null);
        assertThat(webAPSEEObject1).isNotEqualTo(webAPSEEObject2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WebAPSEEObjectDTO.class);
        WebAPSEEObjectDTO webAPSEEObjectDTO1 = new WebAPSEEObjectDTO();
        webAPSEEObjectDTO1.setId(1L);
        WebAPSEEObjectDTO webAPSEEObjectDTO2 = new WebAPSEEObjectDTO();
        assertThat(webAPSEEObjectDTO1).isNotEqualTo(webAPSEEObjectDTO2);
        webAPSEEObjectDTO2.setId(webAPSEEObjectDTO1.getId());
        assertThat(webAPSEEObjectDTO1).isEqualTo(webAPSEEObjectDTO2);
        webAPSEEObjectDTO2.setId(2L);
        assertThat(webAPSEEObjectDTO1).isNotEqualTo(webAPSEEObjectDTO2);
        webAPSEEObjectDTO1.setId(null);
        assertThat(webAPSEEObjectDTO1).isNotEqualTo(webAPSEEObjectDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(webAPSEEObjectMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(webAPSEEObjectMapper.fromId(null)).isNull();
    }
}
