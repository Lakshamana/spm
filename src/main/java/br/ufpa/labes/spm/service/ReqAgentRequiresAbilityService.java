package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ReqAgentRequiresAbility;
import br.ufpa.labes.spm.repository.ReqAgentRequiresAbilityRepository;
import br.ufpa.labes.spm.service.dto.ReqAgentRequiresAbilityDTO;
import br.ufpa.labes.spm.service.mapper.ReqAgentRequiresAbilityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link ReqAgentRequiresAbility}. */
@Service
@Transactional
public class ReqAgentRequiresAbilityService {

  private final Logger log = LoggerFactory.getLogger(ReqAgentRequiresAbilityService.class);

  private final ReqAgentRequiresAbilityRepository reqAgentRequiresAbilityRepository;

  private final ReqAgentRequiresAbilityMapper reqAgentRequiresAbilityMapper;

  public ReqAgentRequiresAbilityService(
      ReqAgentRequiresAbilityRepository reqAgentRequiresAbilityRepository,
      ReqAgentRequiresAbilityMapper reqAgentRequiresAbilityMapper) {
    this.reqAgentRequiresAbilityRepository = reqAgentRequiresAbilityRepository;
    this.reqAgentRequiresAbilityMapper = reqAgentRequiresAbilityMapper;
  }

  /**
   * Save a reqAgentRequiresAbility.
   *
   * @param reqAgentRequiresAbilityDTO the entity to save.
   * @return the persisted entity.
   */
  public ReqAgentRequiresAbilityDTO save(ReqAgentRequiresAbilityDTO reqAgentRequiresAbilityDTO) {
    log.debug("Request to save ReqAgentRequiresAbility : {}", reqAgentRequiresAbilityDTO);
    ReqAgentRequiresAbility reqAgentRequiresAbility =
        reqAgentRequiresAbilityMapper.toEntity(reqAgentRequiresAbilityDTO);
    reqAgentRequiresAbility = reqAgentRequiresAbilityRepository.save(reqAgentRequiresAbility);
    return reqAgentRequiresAbilityMapper.toDto(reqAgentRequiresAbility);
  }

  /**
   * Get all the reqAgentRequiresAbilities.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ReqAgentRequiresAbilityDTO> findAll() {
    log.debug("Request to get all ReqAgentRequiresAbilities");
    return reqAgentRequiresAbilityRepository.findAll().stream()
        .map(reqAgentRequiresAbilityMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one reqAgentRequiresAbility by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ReqAgentRequiresAbilityDTO> findOne(Long id) {
    log.debug("Request to get ReqAgentRequiresAbility : {}", id);
    return reqAgentRequiresAbilityRepository.findById(id).map(reqAgentRequiresAbilityMapper::toDto);
  }

  /**
   * Delete the reqAgentRequiresAbility by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ReqAgentRequiresAbility : {}", id);
    reqAgentRequiresAbilityRepository.deleteById(id);
  }
}
