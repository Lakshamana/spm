package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.EmailConfiguration;
import br.ufpa.labes.spm.repository.EmailConfigurationRepository;
import br.ufpa.labes.spm.service.EmailConfigurationService;
import br.ufpa.labes.spm.service.dto.EmailConfigurationDTO;
import br.ufpa.labes.spm.service.mapper.EmailConfigurationMapper;
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

import br.ufpa.labes.spm.domain.enumeration.EmailSecurityLevels;
import br.ufpa.labes.spm.domain.enumeration.EmailNotificationConfig;
import br.ufpa.labes.spm.domain.enumeration.EmailProcessStatusNotifications;
/**
 * Integration tests for the {@link EmailConfigurationResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class EmailConfigurationResourceIT {

    private static final Boolean DEFAULT_PROCESS_FINISHED = false;
    private static final Boolean UPDATED_PROCESS_FINISHED = true;

    private static final Boolean DEFAULT_FIRST_ACT_STARTED = false;
    private static final Boolean UPDATED_FIRST_ACT_STARTED = true;

    private static final Boolean DEFAULT_CONSUMABLE_RESOURCE_AMOUNT = false;
    private static final Boolean UPDATED_CONSUMABLE_RESOURCE_AMOUNT = true;

    private static final Boolean DEFAULT_ACTIVITY_INSTANTIED = false;
    private static final Boolean UPDATED_ACTIVITY_INSTANTIED = true;

    private static final Boolean DEFAULT_TASK_DELEGATED = false;
    private static final Boolean UPDATED_TASK_DELEGATED = true;

    private static final EmailSecurityLevels DEFAULT_SECURITY_LEVELS = EmailSecurityLevels.WITHOUT_SECURITY;
    private static final EmailSecurityLevels UPDATED_SECURITY_LEVELS = EmailSecurityLevels.TLS_IF_AVAILABLE;

    private static final EmailNotificationConfig DEFAULT_NOTIFICATION_CONFIG = EmailNotificationConfig.NOTIFY_ALL_MANAGERS;
    private static final EmailNotificationConfig UPDATED_NOTIFICATION_CONFIG = EmailNotificationConfig.NOTIFY_SPECIFIC_MANAGERS;

    private static final EmailProcessStatusNotifications DEFAULT_PROCESS_NOTIFICATIONS = EmailProcessStatusNotifications.PROCESS_HAS_FINISHED;
    private static final EmailProcessStatusNotifications UPDATED_PROCESS_NOTIFICATIONS = EmailProcessStatusNotifications.FIRST_ACTIVITY_HAS_STARTED;

    @Autowired
    private EmailConfigurationRepository emailConfigurationRepository;

    @Autowired
    private EmailConfigurationMapper emailConfigurationMapper;

    @Autowired
    private EmailConfigurationService emailConfigurationService;

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

    private MockMvc restEmailConfigurationMockMvc;

    private EmailConfiguration emailConfiguration;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EmailConfigurationResource emailConfigurationResource = new EmailConfigurationResource(emailConfigurationService);
        this.restEmailConfigurationMockMvc = MockMvcBuilders.standaloneSetup(emailConfigurationResource)
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
    public static EmailConfiguration createEntity(EntityManager em) {
        EmailConfiguration emailConfiguration = new EmailConfiguration()
            .processFinished(DEFAULT_PROCESS_FINISHED)
            .firstActStarted(DEFAULT_FIRST_ACT_STARTED)
            .consumableResourceAmount(DEFAULT_CONSUMABLE_RESOURCE_AMOUNT)
            .activityInstantied(DEFAULT_ACTIVITY_INSTANTIED)
            .taskDelegated(DEFAULT_TASK_DELEGATED)
            .securityLevels(DEFAULT_SECURITY_LEVELS)
            .notificationConfig(DEFAULT_NOTIFICATION_CONFIG)
            .processNotifications(DEFAULT_PROCESS_NOTIFICATIONS);
        return emailConfiguration;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailConfiguration createUpdatedEntity(EntityManager em) {
        EmailConfiguration emailConfiguration = new EmailConfiguration()
            .processFinished(UPDATED_PROCESS_FINISHED)
            .firstActStarted(UPDATED_FIRST_ACT_STARTED)
            .consumableResourceAmount(UPDATED_CONSUMABLE_RESOURCE_AMOUNT)
            .activityInstantied(UPDATED_ACTIVITY_INSTANTIED)
            .taskDelegated(UPDATED_TASK_DELEGATED)
            .securityLevels(UPDATED_SECURITY_LEVELS)
            .notificationConfig(UPDATED_NOTIFICATION_CONFIG)
            .processNotifications(UPDATED_PROCESS_NOTIFICATIONS);
        return emailConfiguration;
    }

    @BeforeEach
    public void initTest() {
        emailConfiguration = createEntity(em);
    }

    @Test
    @Transactional
    public void createEmailConfiguration() throws Exception {
        int databaseSizeBeforeCreate = emailConfigurationRepository.findAll().size();

        // Create the EmailConfiguration
        EmailConfigurationDTO emailConfigurationDTO = emailConfigurationMapper.toDto(emailConfiguration);
        restEmailConfigurationMockMvc.perform(post("/api/email-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailConfigurationDTO)))
            .andExpect(status().isCreated());

        // Validate the EmailConfiguration in the database
        List<EmailConfiguration> emailConfigurationList = emailConfigurationRepository.findAll();
        assertThat(emailConfigurationList).hasSize(databaseSizeBeforeCreate + 1);
        EmailConfiguration testEmailConfiguration = emailConfigurationList.get(emailConfigurationList.size() - 1);
        assertThat(testEmailConfiguration.isProcessFinished()).isEqualTo(DEFAULT_PROCESS_FINISHED);
        assertThat(testEmailConfiguration.isFirstActStarted()).isEqualTo(DEFAULT_FIRST_ACT_STARTED);
        assertThat(testEmailConfiguration.isConsumableResourceAmount()).isEqualTo(DEFAULT_CONSUMABLE_RESOURCE_AMOUNT);
        assertThat(testEmailConfiguration.isActivityInstantied()).isEqualTo(DEFAULT_ACTIVITY_INSTANTIED);
        assertThat(testEmailConfiguration.isTaskDelegated()).isEqualTo(DEFAULT_TASK_DELEGATED);
        assertThat(testEmailConfiguration.getSecurityLevels()).isEqualTo(DEFAULT_SECURITY_LEVELS);
        assertThat(testEmailConfiguration.getNotificationConfig()).isEqualTo(DEFAULT_NOTIFICATION_CONFIG);
        assertThat(testEmailConfiguration.getProcessNotifications()).isEqualTo(DEFAULT_PROCESS_NOTIFICATIONS);
    }

    @Test
    @Transactional
    public void createEmailConfigurationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = emailConfigurationRepository.findAll().size();

        // Create the EmailConfiguration with an existing ID
        emailConfiguration.setId(1L);
        EmailConfigurationDTO emailConfigurationDTO = emailConfigurationMapper.toDto(emailConfiguration);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailConfigurationMockMvc.perform(post("/api/email-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailConfigurationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmailConfiguration in the database
        List<EmailConfiguration> emailConfigurationList = emailConfigurationRepository.findAll();
        assertThat(emailConfigurationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEmailConfigurations() throws Exception {
        // Initialize the database
        emailConfigurationRepository.saveAndFlush(emailConfiguration);

        // Get all the emailConfigurationList
        restEmailConfigurationMockMvc.perform(get("/api/email-configurations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].processFinished").value(hasItem(DEFAULT_PROCESS_FINISHED.booleanValue())))
            .andExpect(jsonPath("$.[*].firstActStarted").value(hasItem(DEFAULT_FIRST_ACT_STARTED.booleanValue())))
            .andExpect(jsonPath("$.[*].consumableResourceAmount").value(hasItem(DEFAULT_CONSUMABLE_RESOURCE_AMOUNT.booleanValue())))
            .andExpect(jsonPath("$.[*].activityInstantied").value(hasItem(DEFAULT_ACTIVITY_INSTANTIED.booleanValue())))
            .andExpect(jsonPath("$.[*].taskDelegated").value(hasItem(DEFAULT_TASK_DELEGATED.booleanValue())))
            .andExpect(jsonPath("$.[*].securityLevels").value(hasItem(DEFAULT_SECURITY_LEVELS.toString())))
            .andExpect(jsonPath("$.[*].notificationConfig").value(hasItem(DEFAULT_NOTIFICATION_CONFIG.toString())))
            .andExpect(jsonPath("$.[*].processNotifications").value(hasItem(DEFAULT_PROCESS_NOTIFICATIONS.toString())));
    }
    
    @Test
    @Transactional
    public void getEmailConfiguration() throws Exception {
        // Initialize the database
        emailConfigurationRepository.saveAndFlush(emailConfiguration);

        // Get the emailConfiguration
        restEmailConfigurationMockMvc.perform(get("/api/email-configurations/{id}", emailConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(emailConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.processFinished").value(DEFAULT_PROCESS_FINISHED.booleanValue()))
            .andExpect(jsonPath("$.firstActStarted").value(DEFAULT_FIRST_ACT_STARTED.booleanValue()))
            .andExpect(jsonPath("$.consumableResourceAmount").value(DEFAULT_CONSUMABLE_RESOURCE_AMOUNT.booleanValue()))
            .andExpect(jsonPath("$.activityInstantied").value(DEFAULT_ACTIVITY_INSTANTIED.booleanValue()))
            .andExpect(jsonPath("$.taskDelegated").value(DEFAULT_TASK_DELEGATED.booleanValue()))
            .andExpect(jsonPath("$.securityLevels").value(DEFAULT_SECURITY_LEVELS.toString()))
            .andExpect(jsonPath("$.notificationConfig").value(DEFAULT_NOTIFICATION_CONFIG.toString()))
            .andExpect(jsonPath("$.processNotifications").value(DEFAULT_PROCESS_NOTIFICATIONS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmailConfiguration() throws Exception {
        // Get the emailConfiguration
        restEmailConfigurationMockMvc.perform(get("/api/email-configurations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmailConfiguration() throws Exception {
        // Initialize the database
        emailConfigurationRepository.saveAndFlush(emailConfiguration);

        int databaseSizeBeforeUpdate = emailConfigurationRepository.findAll().size();

        // Update the emailConfiguration
        EmailConfiguration updatedEmailConfiguration = emailConfigurationRepository.findById(emailConfiguration.getId()).get();
        // Disconnect from session so that the updates on updatedEmailConfiguration are not directly saved in db
        em.detach(updatedEmailConfiguration);
        updatedEmailConfiguration
            .processFinished(UPDATED_PROCESS_FINISHED)
            .firstActStarted(UPDATED_FIRST_ACT_STARTED)
            .consumableResourceAmount(UPDATED_CONSUMABLE_RESOURCE_AMOUNT)
            .activityInstantied(UPDATED_ACTIVITY_INSTANTIED)
            .taskDelegated(UPDATED_TASK_DELEGATED)
            .securityLevels(UPDATED_SECURITY_LEVELS)
            .notificationConfig(UPDATED_NOTIFICATION_CONFIG)
            .processNotifications(UPDATED_PROCESS_NOTIFICATIONS);
        EmailConfigurationDTO emailConfigurationDTO = emailConfigurationMapper.toDto(updatedEmailConfiguration);

        restEmailConfigurationMockMvc.perform(put("/api/email-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailConfigurationDTO)))
            .andExpect(status().isOk());

        // Validate the EmailConfiguration in the database
        List<EmailConfiguration> emailConfigurationList = emailConfigurationRepository.findAll();
        assertThat(emailConfigurationList).hasSize(databaseSizeBeforeUpdate);
        EmailConfiguration testEmailConfiguration = emailConfigurationList.get(emailConfigurationList.size() - 1);
        assertThat(testEmailConfiguration.isProcessFinished()).isEqualTo(UPDATED_PROCESS_FINISHED);
        assertThat(testEmailConfiguration.isFirstActStarted()).isEqualTo(UPDATED_FIRST_ACT_STARTED);
        assertThat(testEmailConfiguration.isConsumableResourceAmount()).isEqualTo(UPDATED_CONSUMABLE_RESOURCE_AMOUNT);
        assertThat(testEmailConfiguration.isActivityInstantied()).isEqualTo(UPDATED_ACTIVITY_INSTANTIED);
        assertThat(testEmailConfiguration.isTaskDelegated()).isEqualTo(UPDATED_TASK_DELEGATED);
        assertThat(testEmailConfiguration.getSecurityLevels()).isEqualTo(UPDATED_SECURITY_LEVELS);
        assertThat(testEmailConfiguration.getNotificationConfig()).isEqualTo(UPDATED_NOTIFICATION_CONFIG);
        assertThat(testEmailConfiguration.getProcessNotifications()).isEqualTo(UPDATED_PROCESS_NOTIFICATIONS);
    }

    @Test
    @Transactional
    public void updateNonExistingEmailConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = emailConfigurationRepository.findAll().size();

        // Create the EmailConfiguration
        EmailConfigurationDTO emailConfigurationDTO = emailConfigurationMapper.toDto(emailConfiguration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailConfigurationMockMvc.perform(put("/api/email-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(emailConfigurationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmailConfiguration in the database
        List<EmailConfiguration> emailConfigurationList = emailConfigurationRepository.findAll();
        assertThat(emailConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEmailConfiguration() throws Exception {
        // Initialize the database
        emailConfigurationRepository.saveAndFlush(emailConfiguration);

        int databaseSizeBeforeDelete = emailConfigurationRepository.findAll().size();

        // Delete the emailConfiguration
        restEmailConfigurationMockMvc.perform(delete("/api/email-configurations/{id}", emailConfiguration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EmailConfiguration> emailConfigurationList = emailConfigurationRepository.findAll();
        assertThat(emailConfigurationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailConfiguration.class);
        EmailConfiguration emailConfiguration1 = new EmailConfiguration();
        emailConfiguration1.setId(1L);
        EmailConfiguration emailConfiguration2 = new EmailConfiguration();
        emailConfiguration2.setId(emailConfiguration1.getId());
        assertThat(emailConfiguration1).isEqualTo(emailConfiguration2);
        emailConfiguration2.setId(2L);
        assertThat(emailConfiguration1).isNotEqualTo(emailConfiguration2);
        emailConfiguration1.setId(null);
        assertThat(emailConfiguration1).isNotEqualTo(emailConfiguration2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmailConfigurationDTO.class);
        EmailConfigurationDTO emailConfigurationDTO1 = new EmailConfigurationDTO();
        emailConfigurationDTO1.setId(1L);
        EmailConfigurationDTO emailConfigurationDTO2 = new EmailConfigurationDTO();
        assertThat(emailConfigurationDTO1).isNotEqualTo(emailConfigurationDTO2);
        emailConfigurationDTO2.setId(emailConfigurationDTO1.getId());
        assertThat(emailConfigurationDTO1).isEqualTo(emailConfigurationDTO2);
        emailConfigurationDTO2.setId(2L);
        assertThat(emailConfigurationDTO1).isNotEqualTo(emailConfigurationDTO2);
        emailConfigurationDTO1.setId(null);
        assertThat(emailConfigurationDTO1).isNotEqualTo(emailConfigurationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(emailConfigurationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(emailConfigurationMapper.fromId(null)).isNull();
    }
}
