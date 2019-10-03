package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ConnectionEvent;
import br.ufpa.labes.spm.repository.ConnectionEventRepository;
import br.ufpa.labes.spm.service.dto.ConnectionEventDTO;
import br.ufpa.labes.spm.service.mapper.ConnectionEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ConnectionEvent}.
 */
@Service
@Transactional
public class ConnectionEventService {

    private final Logger log = LoggerFactory.getLogger(ConnectionEventService.class);

    private final ConnectionEventRepository connectionEventRepository;

    private final ConnectionEventMapper connectionEventMapper;

    public ConnectionEventService(ConnectionEventRepository connectionEventRepository, ConnectionEventMapper connectionEventMapper) {
        this.connectionEventRepository = connectionEventRepository;
        this.connectionEventMapper = connectionEventMapper;
    }

    /**
     * Save a connectionEvent.
     *
     * @param connectionEventDTO the entity to save.
     * @return the persisted entity.
     */
    public ConnectionEventDTO save(ConnectionEventDTO connectionEventDTO) {
        log.debug("Request to save ConnectionEvent : {}", connectionEventDTO);
        ConnectionEvent connectionEvent = connectionEventMapper.toEntity(connectionEventDTO);
        connectionEvent = connectionEventRepository.save(connectionEvent);
        return connectionEventMapper.toDto(connectionEvent);
    }

    /**
     * Get all the connectionEvents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ConnectionEventDTO> findAll() {
        log.debug("Request to get all ConnectionEvents");
        return connectionEventRepository.findAll().stream()
            .map(connectionEventMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one connectionEvent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConnectionEventDTO> findOne(Long id) {
        log.debug("Request to get ConnectionEvent : {}", id);
        return connectionEventRepository.findById(id)
            .map(connectionEventMapper::toDto);
    }

    /**
     * Delete the connectionEvent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ConnectionEvent : {}", id);
        connectionEventRepository.deleteById(id);
    }
}
