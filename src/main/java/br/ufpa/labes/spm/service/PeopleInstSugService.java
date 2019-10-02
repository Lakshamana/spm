package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.PeopleInstSug;
import br.ufpa.labes.spm.repository.PeopleInstSugRepository;
import br.ufpa.labes.spm.service.dto.PeopleInstSugDTO;
import br.ufpa.labes.spm.service.mapper.PeopleInstSugMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link PeopleInstSug}. */
@Service
@Transactional
public class PeopleInstSugService {

  private final Logger log = LoggerFactory.getLogger(PeopleInstSugService.class);

  private final PeopleInstSugRepository peopleInstSugRepository;

  private final PeopleInstSugMapper peopleInstSugMapper;

  public PeopleInstSugService(
      PeopleInstSugRepository peopleInstSugRepository, PeopleInstSugMapper peopleInstSugMapper) {
    this.peopleInstSugRepository = peopleInstSugRepository;
    this.peopleInstSugMapper = peopleInstSugMapper;
  }

  /**
   * Save a peopleInstSug.
   *
   * @param peopleInstSugDTO the entity to save.
   * @return the persisted entity.
   */
  public PeopleInstSugDTO save(PeopleInstSugDTO peopleInstSugDTO) {
    log.debug("Request to save PeopleInstSug : {}", peopleInstSugDTO);
    PeopleInstSug peopleInstSug = peopleInstSugMapper.toEntity(peopleInstSugDTO);
    peopleInstSug = peopleInstSugRepository.save(peopleInstSug);
    return peopleInstSugMapper.toDto(peopleInstSug);
  }

  /**
   * Get all the peopleInstSugs.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<PeopleInstSugDTO> findAll() {
    log.debug("Request to get all PeopleInstSugs");
    return peopleInstSugRepository.findAll().stream()
        .map(peopleInstSugMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one peopleInstSug by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<PeopleInstSugDTO> findOne(Long id) {
    log.debug("Request to get PeopleInstSug : {}", id);
    return peopleInstSugRepository.findById(id).map(peopleInstSugMapper::toDto);
  }

  /**
   * Delete the peopleInstSug by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete PeopleInstSug : {}", id);
    peopleInstSugRepository.deleteById(id);
  }
}
