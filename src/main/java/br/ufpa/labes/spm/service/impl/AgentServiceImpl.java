package br.ufpa.labes.spm.service.impl;

import br.ufpa.labes.spm.service.AgentService;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.repository.AgentRepository;
import br.ufpa.labes.spm.service.dto.AgentDTO;
import br.ufpa.labes.spm.service.mapper.AgentMapper;
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
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link Agent}. */
@Service
@Transactional
public class AgentServiceImpl implements AgentService {

  private final Logger log = LoggerFactory.getLogger(AgentServiceImpl.class);

  private final AgentRepository agentRepository;

  private final AgentMapper agentMapper;

  public AgentServiceImpl(AgentRepository agentRepository, AgentMapper agentMapper) {
    this.agentRepository = agentRepository;
    this.agentMapper = agentMapper;
  }

  /**
   * Save a agent.
   *
   * @param agentDTO the entity to save.
   * @return the persisted entity.
   */
  @Override
  public AgentDTO save(AgentDTO agentDTO) {
    log.debug("Request to save Agent : {}", agentDTO);
    Agent agent = agentMapper.toEntity(agentDTO);
    agent = agentRepository.save(agent);
    return agentMapper.toDto(agent);
  }

  /**
   * Get all the agents.
   *
   * @return the list of entities.
   */
  @Override
  @Transactional(readOnly = true)
  public List<AgentDTO> findAll() {
    log.debug("Request to get all Agents");
    return agentRepository.findAllWithEagerRelationships().stream()
        .map(agentMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the agents with eager load of many-to-many relationships.
   *
   * @return the list of entities.
   */
  public Page<AgentDTO> findAllWithEagerRelationships(Pageable pageable) {
    return agentRepository.findAllWithEagerRelationships(pageable).map(agentMapper::toDto);
  }

  /**
   * Get all the agents where TheChatMessage is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<AgentDTO> findAllWhereTheChatMessageIsNull() {
    log.debug("Request to get all agents where TheChatMessage is null");
    return StreamSupport.stream(agentRepository.findAll().spliterator(), false)
        .filter(agent -> agent.getTheChatMessage() == null)
        .map(agentMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one agent by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<AgentDTO> findOne(Long id) {
    log.debug("Request to get Agent : {}", id);
    return agentRepository.findOneWithEagerRelationships(id).map(agentMapper::toDto);
  }

  /**
   * Delete the agent by id.
   *
   * @param id the id of the entity.
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Agent : {}", id);
    agentRepository.deleteById(id);
  }
}
