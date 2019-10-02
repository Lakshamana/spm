package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.EmailConfiguration;
import br.ufpa.labes.spm.repository.EmailConfigurationRepository;
import br.ufpa.labes.spm.service.dto.EmailConfigurationDTO;
import br.ufpa.labes.spm.service.mapper.EmailConfigurationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link EmailConfiguration}. */
@Service
@Transactional
public class EmailConfigurationService {

  private final Logger log = LoggerFactory.getLogger(EmailConfigurationService.class);

  private final EmailConfigurationRepository emailConfigurationRepository;

  private final EmailConfigurationMapper emailConfigurationMapper;

  public EmailConfigurationService(
      EmailConfigurationRepository emailConfigurationRepository,
      EmailConfigurationMapper emailConfigurationMapper) {
    this.emailConfigurationRepository = emailConfigurationRepository;
    this.emailConfigurationMapper = emailConfigurationMapper;
  }

  /**
   * Save a emailConfiguration.
   *
   * @param emailConfigurationDTO the entity to save.
   * @return the persisted entity.
   */
  public EmailConfigurationDTO save(EmailConfigurationDTO emailConfigurationDTO) {
    log.debug("Request to save EmailConfiguration : {}", emailConfigurationDTO);
    EmailConfiguration emailConfiguration =
        emailConfigurationMapper.toEntity(emailConfigurationDTO);
    emailConfiguration = emailConfigurationRepository.save(emailConfiguration);
    return emailConfigurationMapper.toDto(emailConfiguration);
  }

  /**
   * Get all the emailConfigurations.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<EmailConfigurationDTO> findAll() {
    log.debug("Request to get all EmailConfigurations");
    return emailConfigurationRepository.findAll().stream()
        .map(emailConfigurationMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one emailConfiguration by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<EmailConfigurationDTO> findOne(Long id) {
    log.debug("Request to get EmailConfiguration : {}", id);
    return emailConfigurationRepository.findById(id).map(emailConfigurationMapper::toDto);
  }

  /**
   * Delete the emailConfiguration by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete EmailConfiguration : {}", id);
    emailConfigurationRepository.deleteById(id);
  }
}
