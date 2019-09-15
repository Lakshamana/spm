package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Plain;
import br.ufpa.labes.spm.repository.PlainRepository;
import br.ufpa.labes.spm.service.dto.PlainDTO;
import br.ufpa.labes.spm.service.mapper.PlainMapper;
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
 * Service Implementation for managing {@link Plain}.
 */
@Service
@Transactional
public class PlainService {

    private final Logger log = LoggerFactory.getLogger(PlainService.class);

    private final PlainRepository plainRepository;

    private final PlainMapper plainMapper;

    public PlainService(PlainRepository plainRepository, PlainMapper plainMapper) {
        this.plainRepository = plainRepository;
        this.plainMapper = plainMapper;
    }

    /**
     * Save a plain.
     *
     * @param plainDTO the entity to save.
     * @return the persisted entity.
     */
    public PlainDTO save(PlainDTO plainDTO) {
        log.debug("Request to save Plain : {}", plainDTO);
        Plain plain = plainMapper.toEntity(plainDTO);
        plain = plainRepository.save(plain);
        return plainMapper.toDto(plain);
    }

    /**
     * Get all the plainActivities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PlainDTO> findAll() {
        log.debug("Request to get all PlainActivities");
        return plainRepository.findAll().stream()
            .map(plainMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the plainActivities where TheActivitySuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PlainDTO> findAllWhereTheActivitySuperIsNull() {
        log.debug("Request to get all plainActivities where TheActivitySuper is null");
        return StreamSupport
            .stream(plainRepository.findAll().spliterator(), false)
            .filter(plain -> plain.getTheActivitySuper() == null)
            .map(plainMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one plain by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlainDTO> findOne(Long id) {
        log.debug("Request to get Plain : {}", id);
        return plainRepository.findById(id)
            .map(plainMapper::toDto);
    }

    /**
     * Delete the plain by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Plain : {}", id);
        plainRepository.deleteById(id);
    }
}
