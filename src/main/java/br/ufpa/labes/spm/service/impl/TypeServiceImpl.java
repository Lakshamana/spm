package br.ufpa.labes.spm.service.impl;

import br.ufpa.labes.spm.service.TypeService;
import br.ufpa.labes.spm.domain.Type;
import br.ufpa.labes.spm.repository.TypeRepository;
import br.ufpa.labes.spm.service.dto.TypeDTO;
import br.ufpa.labes.spm.service.mapper.TypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link Type}. */
@Service
@Transactional
public class TypeServiceImpl implements TypeService {

  private final Logger log = LoggerFactory.getLogger(TypeServiceImpl.class);

  private final TypeRepository typeRepository;

  private final TypeMapper typeMapper;

  public TypeServiceImpl(TypeRepository typeRepository, TypeMapper typeMapper) {
    this.typeRepository = typeRepository;
    this.typeMapper = typeMapper;
  }

  /**
   * Save a type.
   *
   * @param typeDTO the entity to save.
   * @return the persisted entity.
   */
  @Override
  public TypeDTO save(TypeDTO typeDTO) {
    log.debug("Request to save Type : {}", typeDTO);
    Type type = typeMapper.toEntity(typeDTO);
    type = typeRepository.save(type);
    return typeMapper.toDto(type);
  }

  /**
   * Get all the types.
   *
   * @return the list of entities.
   */
  @Override
  @Transactional(readOnly = true)
  public List<TypeDTO> findAll() {
    log.debug("Request to get all Types");
    return typeRepository.findAll().stream()
        .map(typeMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one type by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Override
  @Transactional(readOnly = true)
  public Optional<TypeDTO> findOne(Long id) {
    log.debug("Request to get Type : {}", id);
    return typeRepository.findById(id).map(typeMapper::toDto);
  }

  /**
   * Delete the type by id.
   *
   * @param id the id of the entity.
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Type : {}", id);
    typeRepository.deleteById(id);
  }
}
