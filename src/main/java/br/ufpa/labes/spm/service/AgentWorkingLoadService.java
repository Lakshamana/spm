package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.AgentWorkingLoad;
import br.ufpa.labes.spm.repository.AgentWorkingLoadRepository;
import br.ufpa.labes.spm.service.dto.AgentWorkingLoadDTO;
import br.ufpa.labes.spm.service.mapper.AgentWorkingLoadMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link AgentWorkingLoad}. */
@Service
@Transactional
public class AgentWorkingLoadService {

  private final Logger log = LoggerFactory.getLogger(AgentWorkingLoadService.class);

  private final AgentWorkingLoadRepository agentWorkingLoadRepository;

  private final AgentWorkingLoadMapper agentWorkingLoadMapper;

  public AgentWorkingLoadService(
      AgentWorkingLoadRepository agentWorkingLoadRepository,
      AgentWorkingLoadMapper agentWorkingLoadMapper) {
    this.agentWorkingLoadRepository = agentWorkingLoadRepository;
    this.agentWorkingLoadMapper = agentWorkingLoadMapper;
  }

  /**
   * Save a agentWorkingLoad.
   *
   * @param agentWorkingLoadDTO the entity to save.
   * @return the persisted entity.
   */
  public AgentWorkingLoadDTO save(AgentWorkingLoadDTO agentWorkingLoadDTO) {
    log.debug("Request to save AgentWorkingLoad : {}", agentWorkingLoadDTO);
    AgentWorkingLoad agentWorkingLoad = agentWorkingLoadMapper.toEntity(agentWorkingLoadDTO);
    agentWorkingLoad = agentWorkingLoadRepository.save(agentWorkingLoad);
    return agentWorkingLoadMapper.toDto(agentWorkingLoad);
  }

  /**
   * Get all the agentWorkingLoads.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<AgentWorkingLoadDTO> findAll() {
    log.debug("Request to get all AgentWorkingLoads");
    return agentWorkingLoadRepository.findAll().stream()
        .map(agentWorkingLoadMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one agentWorkingLoad by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<AgentWorkingLoadDTO> findOne(Long id) {
    log.debug("Request to get AgentWorkingLoad : {}", id);
    return agentWorkingLoadRepository.findById(id).map(agentWorkingLoadMapper::toDto);
  }

  /**
   * Delete the agentWorkingLoad by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete AgentWorkingLoad : {}", id);
    agentWorkingLoadRepository.deleteById(id);
  }
}
