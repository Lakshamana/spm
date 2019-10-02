package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.GraphicCoordinate;
import br.ufpa.labes.spm.repository.GraphicCoordinateRepository;
import br.ufpa.labes.spm.service.dto.GraphicCoordinateDTO;
import br.ufpa.labes.spm.service.mapper.GraphicCoordinateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** Service Implementation for managing {@link GraphicCoordinate}. */
@Service
@Transactional
public class GraphicCoordinateService {

  private final Logger log = LoggerFactory.getLogger(GraphicCoordinateService.class);

  private final GraphicCoordinateRepository graphicCoordinateRepository;

  private final GraphicCoordinateMapper graphicCoordinateMapper;

  public GraphicCoordinateService(
      GraphicCoordinateRepository graphicCoordinateRepository,
      GraphicCoordinateMapper graphicCoordinateMapper) {
    this.graphicCoordinateRepository = graphicCoordinateRepository;
    this.graphicCoordinateMapper = graphicCoordinateMapper;
  }

  /**
   * Save a graphicCoordinate.
   *
   * @param graphicCoordinateDTO the entity to save.
   * @return the persisted entity.
   */
  public GraphicCoordinateDTO save(GraphicCoordinateDTO graphicCoordinateDTO) {
    log.debug("Request to save GraphicCoordinate : {}", graphicCoordinateDTO);
    GraphicCoordinate graphicCoordinate = graphicCoordinateMapper.toEntity(graphicCoordinateDTO);
    graphicCoordinate = graphicCoordinateRepository.save(graphicCoordinate);
    return graphicCoordinateMapper.toDto(graphicCoordinate);
  }

  /**
   * Get all the graphicCoordinates.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<GraphicCoordinateDTO> findAll() {
    log.debug("Request to get all GraphicCoordinates");
    return graphicCoordinateRepository.findAll().stream()
        .map(graphicCoordinateMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get all the graphicCoordinates where TheObjectReference is {@code null}.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<GraphicCoordinateDTO> findAllWhereTheObjectReferenceIsNull() {
    log.debug("Request to get all graphicCoordinates where TheObjectReference is null");
    return StreamSupport.stream(graphicCoordinateRepository.findAll().spliterator(), false)
        .filter(graphicCoordinate -> graphicCoordinate.getTheObjectReference() == null)
        .map(graphicCoordinateMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one graphicCoordinate by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<GraphicCoordinateDTO> findOne(Long id) {
    log.debug("Request to get GraphicCoordinate : {}", id);
    return graphicCoordinateRepository.findById(id).map(graphicCoordinateMapper::toDto);
  }

  /**
   * Delete the graphicCoordinate by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete GraphicCoordinate : {}", id);
    graphicCoordinateRepository.deleteById(id);
  }
}
