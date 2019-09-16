package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.ChatMessage;
import br.ufpa.labes.spm.repository.ChatMessageRepository;
import br.ufpa.labes.spm.service.ChatMessageService;
import br.ufpa.labes.spm.service.dto.ChatMessageDTO;
import br.ufpa.labes.spm.service.mapper.ChatMessageMapper;
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

/** Integration tests for the {@link ChatMessageResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ChatMessageResourceIT {

  private static final String DEFAULT_IDENT = "AAAAAAAAAA";
  private static final String UPDATED_IDENT = "BBBBBBBBBB";

  private static final String DEFAULT_TEXT = "AAAAAAAAAA";
  private static final String UPDATED_TEXT = "BBBBBBBBBB";

  private static final LocalDate DEFAULT_TIMESTAMP = LocalDate.ofEpochDay(0L);
  private static final LocalDate UPDATED_TIMESTAMP = LocalDate.now(ZoneId.systemDefault());
  private static final LocalDate SMALLER_TIMESTAMP = LocalDate.ofEpochDay(-1L);

  @Autowired private ChatMessageRepository chatMessageRepository;

  @Autowired private ChatMessageMapper chatMessageMapper;

  @Autowired private ChatMessageService chatMessageService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restChatMessageMockMvc;

  private ChatMessage chatMessage;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final ChatMessageResource chatMessageResource = new ChatMessageResource(chatMessageService);
    this.restChatMessageMockMvc =
        MockMvcBuilders.standaloneSetup(chatMessageResource)
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
  public static ChatMessage createEntity(EntityManager em) {
    ChatMessage chatMessage =
        new ChatMessage().ident(DEFAULT_IDENT).text(DEFAULT_TEXT).timestamp(DEFAULT_TIMESTAMP);
    return chatMessage;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static ChatMessage createUpdatedEntity(EntityManager em) {
    ChatMessage chatMessage =
        new ChatMessage().ident(UPDATED_IDENT).text(UPDATED_TEXT).timestamp(UPDATED_TIMESTAMP);
    return chatMessage;
  }

  @BeforeEach
  public void initTest() {
    chatMessage = createEntity(em);
  }

  @Test
  @Transactional
  public void createChatMessage() throws Exception {
    int databaseSizeBeforeCreate = chatMessageRepository.findAll().size();

    // Create the ChatMessage
    ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);
    restChatMessageMockMvc
        .perform(
            post("/api/chat-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
        .andExpect(status().isCreated());

    // Validate the ChatMessage in the database
    List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
    assertThat(chatMessageList).hasSize(databaseSizeBeforeCreate + 1);
    ChatMessage testChatMessage = chatMessageList.get(chatMessageList.size() - 1);
    assertThat(testChatMessage.getIdent()).isEqualTo(DEFAULT_IDENT);
    assertThat(testChatMessage.getText()).isEqualTo(DEFAULT_TEXT);
    assertThat(testChatMessage.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
  }

  @Test
  @Transactional
  public void createChatMessageWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = chatMessageRepository.findAll().size();

    // Create the ChatMessage with an existing ID
    chatMessage.setId(1L);
    ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

    // An entity with an existing ID cannot be created, so this API call must fail
    restChatMessageMockMvc
        .perform(
            post("/api/chat-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
        .andExpect(status().isBadRequest());

    // Validate the ChatMessage in the database
    List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
    assertThat(chatMessageList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void checkTimestampIsRequired() throws Exception {
    int databaseSizeBeforeTest = chatMessageRepository.findAll().size();
    // set the field null
    chatMessage.setTimestamp(null);

    // Create the ChatMessage, which fails.
    ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

    restChatMessageMockMvc
        .perform(
            post("/api/chat-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
        .andExpect(status().isBadRequest());

    List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
    assertThat(chatMessageList).hasSize(databaseSizeBeforeTest);
  }

  @Test
  @Transactional
  public void getAllChatMessages() throws Exception {
    // Initialize the database
    chatMessageRepository.saveAndFlush(chatMessage);

    // Get all the chatMessageList
    restChatMessageMockMvc
        .perform(get("/api/chat-messages?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(chatMessage.getId().intValue())))
        .andExpect(jsonPath("$.[*].ident").value(hasItem(DEFAULT_IDENT.toString())))
        .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
        .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
  }

  @Test
  @Transactional
  public void getChatMessage() throws Exception {
    // Initialize the database
    chatMessageRepository.saveAndFlush(chatMessage);

    // Get the chatMessage
    restChatMessageMockMvc
        .perform(get("/api/chat-messages/{id}", chatMessage.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(chatMessage.getId().intValue()))
        .andExpect(jsonPath("$.ident").value(DEFAULT_IDENT.toString()))
        .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
        .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingChatMessage() throws Exception {
    // Get the chatMessage
    restChatMessageMockMvc
        .perform(get("/api/chat-messages/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateChatMessage() throws Exception {
    // Initialize the database
    chatMessageRepository.saveAndFlush(chatMessage);

    int databaseSizeBeforeUpdate = chatMessageRepository.findAll().size();

    // Update the chatMessage
    ChatMessage updatedChatMessage = chatMessageRepository.findById(chatMessage.getId()).get();
    // Disconnect from session so that the updates on updatedChatMessage are not directly saved in
    // db
    em.detach(updatedChatMessage);
    updatedChatMessage.ident(UPDATED_IDENT).text(UPDATED_TEXT).timestamp(UPDATED_TIMESTAMP);
    ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(updatedChatMessage);

    restChatMessageMockMvc
        .perform(
            put("/api/chat-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
        .andExpect(status().isOk());

    // Validate the ChatMessage in the database
    List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
    assertThat(chatMessageList).hasSize(databaseSizeBeforeUpdate);
    ChatMessage testChatMessage = chatMessageList.get(chatMessageList.size() - 1);
    assertThat(testChatMessage.getIdent()).isEqualTo(UPDATED_IDENT);
    assertThat(testChatMessage.getText()).isEqualTo(UPDATED_TEXT);
    assertThat(testChatMessage.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
  }

  @Test
  @Transactional
  public void updateNonExistingChatMessage() throws Exception {
    int databaseSizeBeforeUpdate = chatMessageRepository.findAll().size();

    // Create the ChatMessage
    ChatMessageDTO chatMessageDTO = chatMessageMapper.toDto(chatMessage);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restChatMessageMockMvc
        .perform(
            put("/api/chat-messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(chatMessageDTO)))
        .andExpect(status().isBadRequest());

    // Validate the ChatMessage in the database
    List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
    assertThat(chatMessageList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteChatMessage() throws Exception {
    // Initialize the database
    chatMessageRepository.saveAndFlush(chatMessage);

    int databaseSizeBeforeDelete = chatMessageRepository.findAll().size();

    // Delete the chatMessage
    restChatMessageMockMvc
        .perform(
            delete("/api/chat-messages/{id}", chatMessage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<ChatMessage> chatMessageList = chatMessageRepository.findAll();
    assertThat(chatMessageList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(ChatMessage.class);
    ChatMessage chatMessage1 = new ChatMessage();
    chatMessage1.setId(1L);
    ChatMessage chatMessage2 = new ChatMessage();
    chatMessage2.setId(chatMessage1.getId());
    assertThat(chatMessage1).isEqualTo(chatMessage2);
    chatMessage2.setId(2L);
    assertThat(chatMessage1).isNotEqualTo(chatMessage2);
    chatMessage1.setId(null);
    assertThat(chatMessage1).isNotEqualTo(chatMessage2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(ChatMessageDTO.class);
    ChatMessageDTO chatMessageDTO1 = new ChatMessageDTO();
    chatMessageDTO1.setId(1L);
    ChatMessageDTO chatMessageDTO2 = new ChatMessageDTO();
    assertThat(chatMessageDTO1).isNotEqualTo(chatMessageDTO2);
    chatMessageDTO2.setId(chatMessageDTO1.getId());
    assertThat(chatMessageDTO1).isEqualTo(chatMessageDTO2);
    chatMessageDTO2.setId(2L);
    assertThat(chatMessageDTO1).isNotEqualTo(chatMessageDTO2);
    chatMessageDTO1.setId(null);
    assertThat(chatMessageDTO1).isNotEqualTo(chatMessageDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(chatMessageMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(chatMessageMapper.fromId(null)).isNull();
  }
}
