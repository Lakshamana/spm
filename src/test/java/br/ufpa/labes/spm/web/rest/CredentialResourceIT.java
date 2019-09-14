package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Credential;
import br.ufpa.labes.spm.repository.CredentialRepository;
import br.ufpa.labes.spm.service.CredentialService;
import br.ufpa.labes.spm.service.dto.CredentialDTO;
import br.ufpa.labes.spm.service.mapper.CredentialMapper;
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

import br.ufpa.labes.spm.domain.enumeration.OperationEnum;
/**
 * Integration tests for the {@link CredentialResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class CredentialResourceIT {

    private static final String DEFAULT_CLASS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_UID = "AAAAAAAAAA";
    private static final String UPDATED_UID = "BBBBBBBBBB";

    private static final OperationEnum DEFAULT_OPERATION_ENUM = OperationEnum.CREATE;
    private static final OperationEnum UPDATED_OPERATION_ENUM = OperationEnum.DELETE;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private CredentialMapper credentialMapper;

    @Autowired
    private CredentialService credentialService;

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

    private MockMvc restCredentialMockMvc;

    private Credential credential;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CredentialResource credentialResource = new CredentialResource(credentialService);
        this.restCredentialMockMvc = MockMvcBuilders.standaloneSetup(credentialResource)
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
    public static Credential createEntity(EntityManager em) {
        Credential credential = new Credential()
            .className(DEFAULT_CLASS_NAME)
            .uid(DEFAULT_UID)
            .operationEnum(DEFAULT_OPERATION_ENUM);
        return credential;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Credential createUpdatedEntity(EntityManager em) {
        Credential credential = new Credential()
            .className(UPDATED_CLASS_NAME)
            .uid(UPDATED_UID)
            .operationEnum(UPDATED_OPERATION_ENUM);
        return credential;
    }

    @BeforeEach
    public void initTest() {
        credential = createEntity(em);
    }

    @Test
    @Transactional
    public void createCredential() throws Exception {
        int databaseSizeBeforeCreate = credentialRepository.findAll().size();

        // Create the Credential
        CredentialDTO credentialDTO = credentialMapper.toDto(credential);
        restCredentialMockMvc.perform(post("/api/credentials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(credentialDTO)))
            .andExpect(status().isCreated());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeCreate + 1);
        Credential testCredential = credentialList.get(credentialList.size() - 1);
        assertThat(testCredential.getClassName()).isEqualTo(DEFAULT_CLASS_NAME);
        assertThat(testCredential.getUid()).isEqualTo(DEFAULT_UID);
        assertThat(testCredential.getOperationEnum()).isEqualTo(DEFAULT_OPERATION_ENUM);
    }

    @Test
    @Transactional
    public void createCredentialWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = credentialRepository.findAll().size();

        // Create the Credential with an existing ID
        credential.setId(1L);
        CredentialDTO credentialDTO = credentialMapper.toDto(credential);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCredentialMockMvc.perform(post("/api/credentials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(credentialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCredentials() throws Exception {
        // Initialize the database
        credentialRepository.saveAndFlush(credential);

        // Get all the credentialList
        restCredentialMockMvc.perform(get("/api/credentials?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(credential.getId().intValue())))
            .andExpect(jsonPath("$.[*].className").value(hasItem(DEFAULT_CLASS_NAME.toString())))
            .andExpect(jsonPath("$.[*].uid").value(hasItem(DEFAULT_UID.toString())))
            .andExpect(jsonPath("$.[*].operationEnum").value(hasItem(DEFAULT_OPERATION_ENUM.toString())));
    }
    
    @Test
    @Transactional
    public void getCredential() throws Exception {
        // Initialize the database
        credentialRepository.saveAndFlush(credential);

        // Get the credential
        restCredentialMockMvc.perform(get("/api/credentials/{id}", credential.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(credential.getId().intValue()))
            .andExpect(jsonPath("$.className").value(DEFAULT_CLASS_NAME.toString()))
            .andExpect(jsonPath("$.uid").value(DEFAULT_UID.toString()))
            .andExpect(jsonPath("$.operationEnum").value(DEFAULT_OPERATION_ENUM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCredential() throws Exception {
        // Get the credential
        restCredentialMockMvc.perform(get("/api/credentials/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCredential() throws Exception {
        // Initialize the database
        credentialRepository.saveAndFlush(credential);

        int databaseSizeBeforeUpdate = credentialRepository.findAll().size();

        // Update the credential
        Credential updatedCredential = credentialRepository.findById(credential.getId()).get();
        // Disconnect from session so that the updates on updatedCredential are not directly saved in db
        em.detach(updatedCredential);
        updatedCredential
            .className(UPDATED_CLASS_NAME)
            .uid(UPDATED_UID)
            .operationEnum(UPDATED_OPERATION_ENUM);
        CredentialDTO credentialDTO = credentialMapper.toDto(updatedCredential);

        restCredentialMockMvc.perform(put("/api/credentials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(credentialDTO)))
            .andExpect(status().isOk());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeUpdate);
        Credential testCredential = credentialList.get(credentialList.size() - 1);
        assertThat(testCredential.getClassName()).isEqualTo(UPDATED_CLASS_NAME);
        assertThat(testCredential.getUid()).isEqualTo(UPDATED_UID);
        assertThat(testCredential.getOperationEnum()).isEqualTo(UPDATED_OPERATION_ENUM);
    }

    @Test
    @Transactional
    public void updateNonExistingCredential() throws Exception {
        int databaseSizeBeforeUpdate = credentialRepository.findAll().size();

        // Create the Credential
        CredentialDTO credentialDTO = credentialMapper.toDto(credential);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCredentialMockMvc.perform(put("/api/credentials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(credentialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Credential in the database
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCredential() throws Exception {
        // Initialize the database
        credentialRepository.saveAndFlush(credential);

        int databaseSizeBeforeDelete = credentialRepository.findAll().size();

        // Delete the credential
        restCredentialMockMvc.perform(delete("/api/credentials/{id}", credential.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Credential> credentialList = credentialRepository.findAll();
        assertThat(credentialList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Credential.class);
        Credential credential1 = new Credential();
        credential1.setId(1L);
        Credential credential2 = new Credential();
        credential2.setId(credential1.getId());
        assertThat(credential1).isEqualTo(credential2);
        credential2.setId(2L);
        assertThat(credential1).isNotEqualTo(credential2);
        credential1.setId(null);
        assertThat(credential1).isNotEqualTo(credential2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CredentialDTO.class);
        CredentialDTO credentialDTO1 = new CredentialDTO();
        credentialDTO1.setId(1L);
        CredentialDTO credentialDTO2 = new CredentialDTO();
        assertThat(credentialDTO1).isNotEqualTo(credentialDTO2);
        credentialDTO2.setId(credentialDTO1.getId());
        assertThat(credentialDTO1).isEqualTo(credentialDTO2);
        credentialDTO2.setId(2L);
        assertThat(credentialDTO1).isNotEqualTo(credentialDTO2);
        credentialDTO1.setId(null);
        assertThat(credentialDTO1).isNotEqualTo(credentialDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(credentialMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(credentialMapper.fromId(null)).isNull();
    }
}
