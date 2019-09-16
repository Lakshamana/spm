package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Parameter;
import br.ufpa.labes.spm.repository.ParameterRepository;
import br.ufpa.labes.spm.service.dto.ParameterDTO;
import br.ufpa.labes.spm.service.mapper.ParameterMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link Parameter}. */
@Service
@Transactional
public class ParameterService {

  private final Logger log = LoggerFactory.getLogger(ParameterService.class);

  private final ParameterRepository parameterRepository;

  private final ParameterMapper parameterMapper;

  public ParameterService(
      ParameterRepository parameterRepository, ParameterMapper parameterMapper) {
    this.parameterRepository = parameterRepository;
    this.parameterMapper = parameterMapper;
  }

  /**
   * Save a parameter.
   *
   * @param parameterDTO the entity to save.
   * @return the persisted entity.
   */
  public ParameterDTO save(ParameterDTO parameterDTO) {
    log.debug("Request to save Parameter : {}", parameterDTO);
    Parameter parameter = parameterMapper.toEntity(parameterDTO);
    parameter = parameterRepository.save(parameter);
    return parameterMapper.toDto(parameter);
  }

  /**
   * Get all the parameters.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ParameterDTO> findAll() {
    log.debug("Request to get all Parameters");
    return parameterRepository.findAll().stream()
        .map(parameterMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one parameter by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ParameterDTO> findOne(Long id) {
    log.debug("Request to get Parameter : {}", id);
    return parameterRepository.findById(id).map(parameterMapper::toDto);
  }

  /**
   * Delete the parameter by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete Parameter : {}", id);
    parameterRepository.deleteById(id);
  }
}
