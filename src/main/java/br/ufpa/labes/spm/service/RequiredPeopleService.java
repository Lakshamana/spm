package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.RequiredPeople;
import br.ufpa.labes.spm.repository.RequiredPeopleRepository;
import br.ufpa.labes.spm.service.dto.RequiredPeopleDTO;
import br.ufpa.labes.spm.service.mapper.RequiredPeopleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link RequiredPeople}. */
@Service
@Transactional
public class RequiredPeopleService {

  private final Logger log = LoggerFactory.getLogger(RequiredPeopleService.class);

  private final RequiredPeopleRepository requiredPeopleRepository;

  private final RequiredPeopleMapper requiredPeopleMapper;

  public RequiredPeopleService(
      RequiredPeopleRepository requiredPeopleRepository,
      RequiredPeopleMapper requiredPeopleMapper) {
    this.requiredPeopleRepository = requiredPeopleRepository;
    this.requiredPeopleMapper = requiredPeopleMapper;
  }

  /**
   * Save a requiredPeople.
   *
   * @param requiredPeopleDTO the entity to save.
   * @return the persisted entity.
   */
  public RequiredPeopleDTO save(RequiredPeopleDTO requiredPeopleDTO) {
    log.debug("Request to save RequiredPeople : {}", requiredPeopleDTO);
    RequiredPeople requiredPeople = requiredPeopleMapper.toEntity(requiredPeopleDTO);
    requiredPeople = requiredPeopleRepository.save(requiredPeople);
    return requiredPeopleMapper.toDto(requiredPeople);
  }

  /**
   * Get all the requiredPeople.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<RequiredPeopleDTO> findAll() {
    log.debug("Request to get all RequiredPeople");
    return requiredPeopleRepository.findAll().stream()
        .map(requiredPeopleMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one requiredPeople by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<RequiredPeopleDTO> findOne(Long id) {
    log.debug("Request to get RequiredPeople : {}", id);
    return requiredPeopleRepository.findById(id).map(requiredPeopleMapper::toDto);
  }

  /**
   * Delete the requiredPeople by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete RequiredPeople : {}", id);
    requiredPeopleRepository.deleteById(id);
  }
}
