package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ProcessAgenda;
import br.ufpa.labes.spm.repository.ProcessAgendaRepository;
import br.ufpa.labes.spm.service.dto.ProcessAgendaDTO;
import br.ufpa.labes.spm.service.mapper.ProcessAgendaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link ProcessAgenda}. */
@Service
@Transactional
public class ProcessAgendaService {

  private final Logger log = LoggerFactory.getLogger(ProcessAgendaService.class);

  private final ProcessAgendaRepository processAgendaRepository;

  private final ProcessAgendaMapper processAgendaMapper;

  public ProcessAgendaService(
      ProcessAgendaRepository processAgendaRepository, ProcessAgendaMapper processAgendaMapper) {
    this.processAgendaRepository = processAgendaRepository;
    this.processAgendaMapper = processAgendaMapper;
  }

  /**
   * Save a processAgenda.
   *
   * @param processAgendaDTO the entity to save.
   * @return the persisted entity.
   */
  public ProcessAgendaDTO save(ProcessAgendaDTO processAgendaDTO) {
    log.debug("Request to save ProcessAgenda : {}", processAgendaDTO);
    ProcessAgenda processAgenda = processAgendaMapper.toEntity(processAgendaDTO);
    processAgenda = processAgendaRepository.save(processAgenda);
    return processAgendaMapper.toDto(processAgenda);
  }

  /**
   * Get all the processAgenda.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ProcessAgendaDTO> findAll() {
    log.debug("Request to get all ProcessAgenda");
    return processAgendaRepository.findAll().stream()
        .map(processAgendaMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one processAgenda by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ProcessAgendaDTO> findOne(Long id) {
    log.debug("Request to get ProcessAgenda : {}", id);
    return processAgendaRepository.findById(id).map(processAgendaMapper::toDto);
  }

  /**
   * Delete the processAgenda by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ProcessAgenda : {}", id);
    processAgendaRepository.deleteById(id);
  }
}
