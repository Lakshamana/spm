package br.ufpa.labes.spm.service.impl;

import br.ufpa.labes.spm.service.SpmConfigurationService;
import br.ufpa.labes.spm.domain.SpmConfiguration;
import br.ufpa.labes.spm.repository.SpmConfigurationRepository;
import br.ufpa.labes.spm.service.dto.SpmConfigurationDTO;
import br.ufpa.labes.spm.service.mapper.SpmConfigurationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link SpmConfiguration}. */
@Service
@Transactional
public class SpmConfigurationServiceImpl implements SpmConfigurationService {

  private final Logger log = LoggerFactory.getLogger(SpmConfigurationServiceImpl.class);

  private final SpmConfigurationRepository spmConfigurationRepository;

  private final SpmConfigurationMapper spmConfigurationMapper;

  public SpmConfigurationServiceImpl(
      SpmConfigurationRepository spmConfigurationRepository,
      SpmConfigurationMapper spmConfigurationMapper) {
    this.spmConfigurationRepository = spmConfigurationRepository;
    this.spmConfigurationMapper = spmConfigurationMapper;
  }

  /**
   * Save a spmConfiguration.
   *
   * @param spmConfigurationDTO the entity to save.
   * @return the persisted entity.
   */
  @Override
  public SpmConfigurationDTO save(SpmConfigurationDTO spmConfigurationDTO) {
    log.debug("Request to save SpmConfiguration : {}", spmConfigurationDTO);
    SpmConfiguration spmConfiguration = spmConfigurationMapper.toEntity(spmConfigurationDTO);
    spmConfiguration = spmConfigurationRepository.save(spmConfiguration);
    return spmConfigurationMapper.toDto(spmConfiguration);
  }

  /**
   * Get all the spmConfigurations.
   *
   * @return the list of entities.
   */
  @Override
  @Transactional(readOnly = true)
  public List<SpmConfigurationDTO> findAll() {
    log.debug("Request to get all SpmConfigurations");
    return spmConfigurationRepository.findAll().stream()
        .map(spmConfigurationMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the spmConfigurations where Agent is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<SpmConfigurationDTO> findAllWhereAgentIsNull() {
    log.debug("Request to get all spmConfigurations where Agent is null");
    return StreamSupport.stream(spmConfigurationRepository.findAll().spliterator(), false)
        .filter(spmConfiguration -> spmConfiguration.getAgent() == null)
        .map(spmConfigurationMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one spmConfiguration by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<SpmConfigurationDTO> findOne(Long id) {
    log.debug("Request to get SpmConfiguration : {}", id);
    return spmConfigurationRepository.findById(id).map(spmConfigurationMapper::toDto);
  }

  /**
   * Delete the spmConfiguration by id.
   *
   * @param id the id of the entity.
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete SpmConfiguration : {}", id);
    spmConfigurationRepository.deleteById(id);
  }
}
