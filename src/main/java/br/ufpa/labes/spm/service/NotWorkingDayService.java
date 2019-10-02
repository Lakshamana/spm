package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.NotWorkingDay;
import br.ufpa.labes.spm.repository.NotWorkingDayRepository;
import br.ufpa.labes.spm.service.dto.NotWorkingDayDTO;
import br.ufpa.labes.spm.service.mapper.NotWorkingDayMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link NotWorkingDay}. */
@Service
@Transactional
public class NotWorkingDayService {

  private final Logger log = LoggerFactory.getLogger(NotWorkingDayService.class);

  private final NotWorkingDayRepository notWorkingDayRepository;

  private final NotWorkingDayMapper notWorkingDayMapper;

  public NotWorkingDayService(
      NotWorkingDayRepository notWorkingDayRepository, NotWorkingDayMapper notWorkingDayMapper) {
    this.notWorkingDayRepository = notWorkingDayRepository;
    this.notWorkingDayMapper = notWorkingDayMapper;
  }

  /**
   * Save a notWorkingDay.
   *
   * @param notWorkingDayDTO the entity to save.
   * @return the persisted entity.
   */
  public NotWorkingDayDTO save(NotWorkingDayDTO notWorkingDayDTO) {
    log.debug("Request to save NotWorkingDay : {}", notWorkingDayDTO);
    NotWorkingDay notWorkingDay = notWorkingDayMapper.toEntity(notWorkingDayDTO);
    notWorkingDay = notWorkingDayRepository.save(notWorkingDay);
    return notWorkingDayMapper.toDto(notWorkingDay);
  }

  /**
   * Get all the notWorkingDays.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<NotWorkingDayDTO> findAll() {
    log.debug("Request to get all NotWorkingDays");
    return notWorkingDayRepository.findAll().stream()
        .map(notWorkingDayMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one notWorkingDay by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<NotWorkingDayDTO> findOne(Long id) {
    log.debug("Request to get NotWorkingDay : {}", id);
    return notWorkingDayRepository.findById(id).map(notWorkingDayMapper::toDto);
  }

  /**
   * Delete the notWorkingDay by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete NotWorkingDay : {}", id);
    notWorkingDayRepository.deleteById(id);
  }
}
