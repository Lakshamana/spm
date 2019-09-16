package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Connection;
import br.ufpa.labes.spm.repository.ConnectionRepository;
import br.ufpa.labes.spm.service.dto.ConnectionDTO;
import br.ufpa.labes.spm.service.mapper.ConnectionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/** Service Implementation for managing {@link Connection}. */
@Service
@Transactional
public class ConnectionService {

  private final Logger log = LoggerFactory.getLogger(ConnectionService.class);

  private final ConnectionRepository connectionRepository;

  private final ConnectionMapper connectionMapper;

  public ConnectionService(
      ConnectionRepository connectionRepository, ConnectionMapper connectionMapper) {
    this.connectionRepository = connectionRepository;
    this.connectionMapper = connectionMapper;
  }

  /**
   * Save a connection.
   *
   * @param connectionDTO the entity to save.
   * @return the persisted entity.
   */
  public ConnectionDTO save(ConnectionDTO connectionDTO) {
    log.debug("Request to save Connection : {}", connectionDTO);
    Connection connection = connectionMapper.toEntity(connectionDTO);
    connection = connectionRepository.save(connection);
    return connectionMapper.toDto(connection);
  }

  /**
   * Get all the connections.
   *
   * @return the list of entities.
   */
  @Transactional(readOnly = true)
  public List<ConnectionDTO> findAll() {
    log.debug("Request to get all Connections");
    return connectionRepository.findAll().stream()
        .map(connectionMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Get one connection by id.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  @Transactional(readOnly = true)
  public Optional<ConnectionDTO> findOne(Long id) {
    log.debug("Request to get Connection : {}", id);
    return connectionRepository.findById(id).map(connectionMapper::toDto);
  }

  /**
   * Delete the connection by id.
   *
   * @param id the id of the entity.
   */
  public void delete(Long id) {
    log.debug("Request to delete Connection : {}", id);
    connectionRepository.deleteById(id);
  }
}
