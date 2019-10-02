package br.ufpa.labes.spm.service.impl;

import br.ufpa.labes.spm.service.ChatMessageService;
import br.ufpa.labes.spm.domain.ChatMessage;
import br.ufpa.labes.spm.repository.ChatMessageRepository;
import br.ufpa.labes.spm.service.dto.ChatMessageDTO;
import br.ufpa.labes.spm.service.mapper.ChatMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link ChatMessage}. */
@Service
@Transactional
public class ChatMessageServiceImpl implements ChatMessageService {

  private final Logger log = LoggerFactory.getLogger(ChatMessageServiceImpl.class);

  private final ChatMessageRepository chatMessageRepository;

  private final ChatMessageMapper chatMessageMapper;

  public ChatMessageServiceImpl(
      ChatMessageRepository chatMessageRepository, ChatMessageMapper chatMessageMapper) {
    this.chatMessageRepository = chatMessageRepository;
    this.chatMessageMapper = chatMessageMapper;
  }

  /**
   * Save a chatMessage.
   *
   * @param chatMessageDTO the entity to save.
   * @return the persisted entity.
   */
  @Override
  public ChatMessageDTO save(ChatMessageDTO chatMessageDTO) {
    log.debug("Request to save ChatMessage : {}", chatMessageDTO);
    ChatMessage chatMessage = chatMessageMapper.toEntity(chatMessageDTO);
    chatMessage = chatMessageRepository.save(chatMessage);
    return chatMessageMapper.toDto(chatMessage);
  }

  /**
   * Get all the chatMessages.
   *
   * @return the list of entities.
   */
  @Override
  @Transactional(readOnly = true)
  public List<ChatMessageDTO> findAll() {
    log.debug("Request to get all ChatMessages");
    return chatMessageRepository.findAll().stream()
        .map(chatMessageMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one chatMessage by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<ChatMessageDTO> findOne(Long id) {
    log.debug("Request to get ChatMessage : {}", id);
    return chatMessageRepository.findById(id).map(chatMessageMapper::toDto);
  }

  /**
   * Delete the chatMessage by id.
   *
   * @param id the id of the entity.
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete ChatMessage : {}", id);
    chatMessageRepository.deleteById(id);
  }
}
