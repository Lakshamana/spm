package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ConnectionType;
import br.ufpa.labes.spm.repository.ConnectionTypeRepository;
import br.ufpa.labes.spm.service.dto.ConnectionTypeDTO;
import br.ufpa.labes.spm.service.mapper.ConnectionTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link ConnectionType}. */
@Service
@Transactional
public class ConnectionTypeService {

  private final Logger log = LoggerFactory.getLogger(ConnectionTypeService.class);

  private final ConnectionTypeRepository connectionTypeRepository;

  private final ConnectionTypeMapper connectionTypeMapper;

  public ConnectionTypeService(
      ConnectionTypeRepository connectionTypeRepository,
      ConnectionTypeMapper connectionTypeMapper) {
    this.connectionTypeRepository = connectionTypeRepository;
    this.connectionTypeMapper = connectionTypeMapper;
  }

  /**
   * Save a connectionType.
   *
   * @param connectionTypeDTO the entity to save.
   * @return the persisted entity.
   */
  public ConnectionTypeDTO save(ConnectionTypeDTO connectionTypeDTO) {
    log.debug("Request to save ConnectionType : {}", connectionTypeDTO);
    ConnectionType connectionType = connectionTypeMapper.toEntity(connectionTypeDTO);
    connectionType = connectionTypeRepository.save(connectionType);
    return connectionTypeMapper.toDto(connectionType);
  }

  /**
   * Get all the connectionTypes.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ConnectionTypeDTO> findAll() {
    log.debug("Request to get all ConnectionTypes");
    return connectionTypeRepository.findAll().stream()
        .map(connectionTypeMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one connectionType by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ConnectionTypeDTO> findOne(Long id) {
    log.debug("Request to get ConnectionType : {}", id);
    return connectionTypeRepository.findById(id).map(connectionTypeMapper::toDto);
  }

  /**
   * Delete the connectionType by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete ConnectionType : {}", id);
    connectionTypeRepository.deleteById(id);
  }
}
