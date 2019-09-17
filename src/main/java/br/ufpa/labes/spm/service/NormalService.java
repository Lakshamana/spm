package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.repository.NormalRepository;
import br.ufpa.labes.spm.service.dto.NormalDTO;
import br.ufpa.labes.spm.service.mapper.NormalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link Normal}.
 */
@Service
@Transactional
public class NormalService {

    private final Logger log = LoggerFactory.getLogger(NormalService.class);

    private final NormalRepository normalRepository;

    private final NormalMapper normalMapper;

    public NormalService(NormalRepository normalRepository, NormalMapper normalMapper) {
        this.normalRepository = normalRepository;
        this.normalMapper = normalMapper;
    }

    /**
     * Save a normal.
     *
     * @param normalDTO the entity to save.
     * @return the persisted entity.
     */
    public NormalDTO save(NormalDTO normalDTO) {
        log.debug("Request to save Normal : {}", normalDTO);
        Normal normal = normalMapper.toEntity(normalDTO);
        normal = normalRepository.save(normal);
        return normalMapper.toDto(normal);
    }

    /**
     * Get all the normals.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<NormalDTO> findAll() {
        log.debug("Request to get all Normals");
        return normalRepository.findAll().stream()
            .map(normalMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the normals where TheResourceEvent is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<NormalDTO> findAllWhereTheResourceEventIsNull() {
        log.debug("Request to get all normals where TheResourceEvent is null");
        return StreamSupport
            .stream(normalRepository.findAll().spliterator(), false)
            .filter(normal -> normal.getTheResourceEvent() == null)
            .map(normalMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one normal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NormalDTO> findOne(Long id) {
        log.debug("Request to get Normal : {}", id);
        return normalRepository.findById(id)
            .map(normalMapper::toDto);
    }

    /**
     * Delete the normal by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Normal : {}", id);
        normalRepository.deleteById(id);
    }
}
