package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ChatLog;
import br.ufpa.labes.spm.repository.ChatLogRepository;
import br.ufpa.labes.spm.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.util.ArrayList;
import java.util.List;

import static br.ufpa.labes.spm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ChatLogResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ChatLogResourceIT {

    private static final String DEFAULT_LOG = "AAAAAAAAAA";
    private static final String UPDATED_LOG = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private ChatLogRepository chatLogRepository;

    @Mock
    private ChatLogRepository chatLogRepositoryMock;

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

    private MockMvc restChatLogMockMvc;

    private ChatLog chatLog;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChatLogResource chatLogResource = new ChatLogResource(chatLogRepository);
        this.restChatLogMockMvc = MockMvcBuilders.standaloneSetup(chatLogResource)
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
    public static ChatLog createEntity(EntityManager em) {
        ChatLog chatLog = new ChatLog()
            .log(DEFAULT_LOG)
            .date(DEFAULT_DATE);
        return chatLog;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ChatLog createUpdatedEntity(EntityManager em) {
        ChatLog chatLog = new ChatLog()
            .log(UPDATED_LOG)
            .date(UPDATED_DATE);
        return chatLog;
    }

    @BeforeEach
    public void initTest() {
        chatLog = createEntity(em);
    }

    @Test
    @Transactional
    public void createChatLog() throws Exception {
        int databaseSizeBeforeCreate = chatLogRepository.findAll().size();

        // Create the ChatLog
        restChatLogMockMvc.perform(post("/api/chat-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatLog)))
            .andExpect(status().isCreated());

        // Validate the ChatLog in the database
        List<ChatLog> chatLogList = chatLogRepository.findAll();
        assertThat(chatLogList).hasSize(databaseSizeBeforeCreate + 1);
        ChatLog testChatLog = chatLogList.get(chatLogList.size() - 1);
        assertThat(testChatLog.getLog()).isEqualTo(DEFAULT_LOG);
        assertThat(testChatLog.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createChatLogWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chatLogRepository.findAll().size();

        // Create the ChatLog with an existing ID
        chatLog.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatLogMockMvc.perform(post("/api/chat-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatLog)))
            .andExpect(status().isBadRequest());

        // Validate the ChatLog in the database
        List<ChatLog> chatLogList = chatLogRepository.findAll();
        assertThat(chatLogList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllChatLogs() throws Exception {
        // Initialize the database
        chatLogRepository.saveAndFlush(chatLog);

        // Get all the chatLogList
        restChatLogMockMvc.perform(get("/api/chat-logs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chatLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].log").value(hasItem(DEFAULT_LOG.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllChatLogsWithEagerRelationshipsIsEnabled() throws Exception {
        ChatLogResource chatLogResource = new ChatLogResource(chatLogRepositoryMock);
        when(chatLogRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restChatLogMockMvc = MockMvcBuilders.standaloneSetup(chatLogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restChatLogMockMvc.perform(get("/api/chat-logs?eagerload=true"))
        .andExpect(status().isOk());

        verify(chatLogRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllChatLogsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ChatLogResource chatLogResource = new ChatLogResource(chatLogRepositoryMock);
            when(chatLogRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restChatLogMockMvc = MockMvcBuilders.standaloneSetup(chatLogResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restChatLogMockMvc.perform(get("/api/chat-logs?eagerload=true"))
        .andExpect(status().isOk());

            verify(chatLogRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getChatLog() throws Exception {
        // Initialize the database
        chatLogRepository.saveAndFlush(chatLog);

        // Get the chatLog
        restChatLogMockMvc.perform(get("/api/chat-logs/{id}", chatLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chatLog.getId().intValue()))
            .andExpect(jsonPath("$.log").value(DEFAULT_LOG.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChatLog() throws Exception {
        // Get the chatLog
        restChatLogMockMvc.perform(get("/api/chat-logs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChatLog() throws Exception {
        // Initialize the database
        chatLogRepository.saveAndFlush(chatLog);

        int databaseSizeBeforeUpdate = chatLogRepository.findAll().size();

        // Update the chatLog
        ChatLog updatedChatLog = chatLogRepository.findById(chatLog.getId()).get();
        // Disconnect from session so that the updates on updatedChatLog are not directly saved in db
        em.detach(updatedChatLog);
        updatedChatLog
            .log(UPDATED_LOG)
            .date(UPDATED_DATE);

        restChatLogMockMvc.perform(put("/api/chat-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedChatLog)))
            .andExpect(status().isOk());

        // Validate the ChatLog in the database
        List<ChatLog> chatLogList = chatLogRepository.findAll();
        assertThat(chatLogList).hasSize(databaseSizeBeforeUpdate);
        ChatLog testChatLog = chatLogList.get(chatLogList.size() - 1);
        assertThat(testChatLog.getLog()).isEqualTo(UPDATED_LOG);
        assertThat(testChatLog.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingChatLog() throws Exception {
        int databaseSizeBeforeUpdate = chatLogRepository.findAll().size();

        // Create the ChatLog

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChatLogMockMvc.perform(put("/api/chat-logs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatLog)))
            .andExpect(status().isBadRequest());

        // Validate the ChatLog in the database
        List<ChatLog> chatLogList = chatLogRepository.findAll();
        assertThat(chatLogList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChatLog() throws Exception {
        // Initialize the database
        chatLogRepository.saveAndFlush(chatLog);

        int databaseSizeBeforeDelete = chatLogRepository.findAll().size();

        // Delete the chatLog
        restChatLogMockMvc.perform(delete("/api/chat-logs/{id}", chatLog.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ChatLog> chatLogList = chatLogRepository.findAll();
        assertThat(chatLogList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatLog.class);
        ChatLog chatLog1 = new ChatLog();
        chatLog1.setId(1L);
        ChatLog chatLog2 = new ChatLog();
        chatLog2.setId(chatLog1.getId());
        assertThat(chatLog1).isEqualTo(chatLog2);
        chatLog2.setId(2L);
        assertThat(chatLog1).isNotEqualTo(chatLog2);
        chatLog1.setId(null);
        assertThat(chatLog1).isNotEqualTo(chatLog2);
    }
}
