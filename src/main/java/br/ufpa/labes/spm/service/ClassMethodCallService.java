package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ClassMethodCall;
import br.ufpa.labes.spm.repository.ClassMethodCallRepository;
import br.ufpa.labes.spm.service.dto.ClassMethodCallDTO;
import br.ufpa.labes.spm.service.mapper.ClassMethodCallMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link ClassMethodCall}. */
@Service
@Transactional
public class ClassMethodCallService {

  private final Logger log = LoggerFactory.getLogger(ClassMethodCallService.class);

  private final ClassMethodCallRepository classMethodCallRepository;

  private final ClassMethodCallMapper classMethodCallMapper;

  public ClassMethodCallService(
      ClassMethodCallRepository classMethodCallRepository,
      ClassMethodCallMapper classMethodCallMapper) {
    this.classMethodCallRepository = classMethodCallRepository;
    this.classMethodCallMapper = classMethodCallMapper;
  }

  /**
   * Save a classMethodCall.
   *
   * @param classMethodCallDTO the entity to save.
   * @return the persisted entity.
   */
  public ClassMethodCallDTO save(ClassMethodCallDTO classMethodCallDTO) {
    log.debug("Request to save ClassMethodCall : {}", classMethodCallDTO);
    ClassMethodCall classMethodCall = classMethodCallMapper.toEntity(classMethodCallDTO);
    classMethodCall = classMethodCallRepository.save(classMethodCall);
    return classMethodCallMapper.toDto(classMethodCall);
  }

  /**
   * Get all the classMethodCalls.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ClassMethodCallDTO> findAll() {
    log.debug("Request to get all ClassMethodCalls");
    return classMethodCallRepository.findAll().stream()
        .map(classMethodCallMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the classMethodCalls where TheSubroutineSuper is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ClassMethodCallDTO> findAllWhereTheSubroutineSuperIsNull() {
    log.debug("Request to get all classMethodCalls where TheSubroutineSuper is null");
    return StreamSupport.stream(classMethodCallRepository.findAll().spliterator(), false)
        .filter(classMethodCall -> classMethodCall.getTheSubroutineSuper() == null)
        .map(classMethodCallMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one classMethodCall by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ClassMethodCallDTO> findOne(Long id) {
    log.debug("Request to get ClassMethodCall : {}", id);
    return classMethodCallRepository.findById(id).map(classMethodCallMapper::toDto);
  }

  /**
   * Delete the classMethodCall by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ClassMethodCall : {}", id);
    classMethodCallRepository.deleteById(id);
  }
}
