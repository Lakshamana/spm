package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ChatLog;
import br.ufpa.labes.spm.repository.ChatLogRepository;
import br.ufpa.labes.spm.service.dto.ChatLogDTO;
import br.ufpa.labes.spm.service.mapper.ChatLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link ChatLog}. */
@Service
@Transactional
public class ChatLogService {

  private final Logger log = LoggerFactory.getLogger(ChatLogService.class);

  private final ChatLogRepository chatLogRepository;

  private final ChatLogMapper chatLogMapper;

  public ChatLogService(ChatLogRepository chatLogRepository, ChatLogMapper chatLogMapper) {
    this.chatLogRepository = chatLogRepository;
    this.chatLogMapper = chatLogMapper;
  }

  /**
   * Save a chatLog.
   *
   * @param chatLogDTO the entity to save.
   * @return the persisted entity.
   */
  public ChatLogDTO save(ChatLogDTO chatLogDTO) {
    log.debug("Request to save ChatLog : {}", chatLogDTO);
    ChatLog chatLog = chatLogMapper.toEntity(chatLogDTO);
    chatLog = chatLogRepository.save(chatLog);
    return chatLogMapper.toDto(chatLog);
  }

  /**
   * Get all the chatLogs.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ChatLogDTO> findAll() {
    log.debug("Request to get all ChatLogs");
    return chatLogRepository.findAllWithEagerRelationships().stream()
        .map(chatLogMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the chatLogs with eager load of many-to-many relationships.
   *
   * @return the list of entities.
   */
  public Page<ChatLogDTO> findAllWithEagerRelationships(Pageable pageable) {
    return chatLogRepository.findAllWithEagerRelationships(pageable).map(chatLogMapper::toDto);
  }

  /**
   * Get one chatLog by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ChatLogDTO> findOne(Long id) {
    log.debug("Request to get ChatLog : {}", id);
    return chatLogRepository.findOneWithEagerRelationships(id).map(chatLogMapper::toDto);
  }

  /**
   * Delete the chatLog by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ChatLog : {}", id);
    chatLogRepository.deleteById(id);
  }
}
