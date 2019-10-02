package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Email;
import br.ufpa.labes.spm.repository.EmailRepository;
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
 * Integration tests for the {@link EmailResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class EmailResourceIT {

    private static final String DEFAULT_EMAIL_SERVER_HOST = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_SERVER_HOST = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_SERVER_PORT = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_SERVER_PORT = "BBBBBBBBBB";

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SERVICO_TLS = false;
    private static final Boolean UPDATED_SERVICO_TLS = true;

    private static final Boolean DEFAULT_SERVICO_SSL = false;
    private static final Boolean UPDATED_SERVICO_SSL = true;

    private static final Boolean DEFAULT_TESTE = false;
    private static final Boolean UPDATED_TESTE = true;

    @Autowired
    private EmailRepository emailRepository;

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

    private MockMvc restEmailMockMvc;

    private Email email;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmailResource emailResource = new EmailResource(emailRepository);
        this.restEmailMockMvc = MockMvcBuilders.standaloneSetup(emailResource)
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
    public static Email createEntity(EntityManager em) {
        Email email = new Email()
            .emailServerHost(DEFAULT_EMAIL_SERVER_HOST)
            .emailServerPort(DEFAULT_EMAIL_SERVER_PORT)
            .userName(DEFAULT_USER_NAME)
            .password(DEFAULT_PASSWORD)
            .servicoTls(DEFAULT_SERVICO_TLS)
            .servicoSsl(DEFAULT_SERVICO_SSL)
            .teste(DEFAULT_TESTE);
        return email;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Email createUpdatedEntity(EntityManager em) {
        Email email = new Email()
            .emailServerHost(UPDATED_EMAIL_SERVER_HOST)
            .emailServerPort(UPDATED_EMAIL_SERVER_PORT)
            .userName(UPDATED_USER_NAME)
            .password(UPDATED_PASSWORD)
            .servicoTls(UPDATED_SERVICO_TLS)
            .servicoSsl(UPDATED_SERVICO_SSL)
            .teste(UPDATED_TESTE);
        return email;
    }

    @BeforeEach
    public void initTest() {
        email = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmail() throws Exception {
        int databaseSizeBeforeCreate = emailRepository.findAll().size();

        // Create the Email
        restEmailMockMvc.perform(post("/api/emails")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(email)))
            .andExpect(status().isCreated());

        // Validate the Email in the database
        List<Email> emailList = emailRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeCreate + 1);
        Email testEmail = emailList.get(emailList.size() - 1);
        assertThat(testEmail.getEmailServerHost()).isEqualTo(DEFAULT_EMAIL_SERVER_HOST);
        assertThat(testEmail.getEmailServerPort()).isEqualTo(DEFAULT_EMAIL_SERVER_PORT);
        assertThat(testEmail.getUserName()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testEmail.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testEmail.isServicoTls()).isEqualTo(DEFAULT_SERVICO_TLS);
        assertThat(testEmail.isServicoSsl()).isEqualTo(DEFAULT_SERVICO_SSL);
        assertThat(testEmail.isTeste()).isEqualTo(DEFAULT_TESTE);
    }

    @Test
    @Transactional
    public void createEmailWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = emailRepository.findAll().size();

        // Create the Email with an existing ID
        email.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailMockMvc.perform(post("/api/emails")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(email)))
            .andExpect(status().isBadRequest());

        // Validate the Email in the database
        List<Email> emailList = emailRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEmails() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);

        // Get all the emailList
        restEmailMockMvc.perform(get("/api/emails?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(email.getId().intValue())))
            .andExpect(jsonPath("$.[*].emailServerHost").value(hasItem(DEFAULT_EMAIL_SERVER_HOST.toString())))
            .andExpect(jsonPath("$.[*].emailServerPort").value(hasItem(DEFAULT_EMAIL_SERVER_PORT.toString())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].servicoTls").value(hasItem(DEFAULT_SERVICO_TLS.booleanValue())))
            .andExpect(jsonPath("$.[*].servicoSsl").value(hasItem(DEFAULT_SERVICO_SSL.booleanValue())))
            .andExpect(jsonPath("$.[*].teste").value(hasItem(DEFAULT_TESTE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getEmail() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);

        // Get the email
        restEmailMockMvc.perform(get("/api/emails/{id}", email.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(email.getId().intValue()))
            .andExpect(jsonPath("$.emailServerHost").value(DEFAULT_EMAIL_SERVER_HOST.toString()))
            .andExpect(jsonPath("$.emailServerPort").value(DEFAULT_EMAIL_SERVER_PORT.toString()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.servicoTls").value(DEFAULT_SERVICO_TLS.booleanValue()))
            .andExpect(jsonPath("$.servicoSsl").value(DEFAULT_SERVICO_SSL.booleanValue()))
            .andExpect(jsonPath("$.teste").value(DEFAULT_TESTE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingEmail() throws Exception {
        // Get the email
        restEmailMockMvc.perform(get("/api/emails/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmail() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);

        int databaseSizeBeforeUpdate = emailRepository.findAll().size();

        // Update the email
        Email updatedEmail = emailRepository.findById(email.getId()).get();
        // Disconnect from session so that the updates on updatedEmail are not directly saved in db
        em.detach(updatedEmail);
        updatedEmail
            .emailServerHost(UPDATED_EMAIL_SERVER_HOST)
            .emailServerPort(UPDATED_EMAIL_SERVER_PORT)
            .userName(UPDATED_USER_NAME)
            .password(UPDATED_PASSWORD)
            .servicoTls(UPDATED_SERVICO_TLS)
            .servicoSsl(UPDATED_SERVICO_SSL)
            .teste(UPDATED_TESTE);

        restEmailMockMvc.perform(put("/api/emails")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEmail)))
            .andExpect(status().isOk());

        // Validate the Email in the database
        List<Email> emailList = emailRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeUpdate);
        Email testEmail = emailList.get(emailList.size() - 1);
        assertThat(testEmail.getEmailServerHost()).isEqualTo(UPDATED_EMAIL_SERVER_HOST);
        assertThat(testEmail.getEmailServerPort()).isEqualTo(UPDATED_EMAIL_SERVER_PORT);
        assertThat(testEmail.getUserName()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testEmail.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testEmail.isServicoTls()).isEqualTo(UPDATED_SERVICO_TLS);
        assertThat(testEmail.isServicoSsl()).isEqualTo(UPDATED_SERVICO_SSL);
        assertThat(testEmail.isTeste()).isEqualTo(UPDATED_TESTE);
    }

    @Test
    @Transactional
    public void updateNonExistingEmail() throws Exception {
        int databaseSizeBeforeUpdate = emailRepository.findAll().size();

        // Create the Email

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailMockMvc.perform(put("/api/emails")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(email)))
            .andExpect(status().isBadRequest());

        // Validate the Email in the database
        List<Email> emailList = emailRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEmail() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);

        int databaseSizeBeforeDelete = emailRepository.findAll().size();

        // Delete the email
        restEmailMockMvc.perform(delete("/api/emails/{id}", email.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Email> emailList = emailRepository.findAll();
        assertThat(emailList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Email.class);
        Email email1 = new Email();
        email1.setId(1L);
        Email email2 = new Email();
        email2.setId(email1.getId());
        assertThat(email1).isEqualTo(email2);
        email2.setId(2L);
        assertThat(email1).isNotEqualTo(email2);
        email1.setId(null);
        assertThat(email1).isNotEqualTo(email2);
    }
}
