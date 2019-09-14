package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.MultipleCon;
import br.ufpa.labes.spm.repository.MultipleConRepository;
import br.ufpa.labes.spm.service.dto.MultipleConDTO;
import br.ufpa.labes.spm.service.mapper.MultipleConMapper;
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
 * Service Implementation for managing {@link MultipleCon}.
 */
@Service
@Transactional
public class MultipleConService {

    private final Logger log = LoggerFactory.getLogger(MultipleConService.class);

    private final MultipleConRepository multipleConRepository;

    private final MultipleConMapper multipleConMapper;

    public MultipleConService(MultipleConRepository multipleConRepository, MultipleConMapper multipleConMapper) {
        this.multipleConRepository = multipleConRepository;
        this.multipleConMapper = multipleConMapper;
    }

    /**
     * Save a multipleCon.
     *
     * @param multipleConDTO the entity to save.
     * @return the persisted entity.
     */
    public MultipleConDTO save(MultipleConDTO multipleConDTO) {
        log.debug("Request to save MultipleCon : {}", multipleConDTO);
        MultipleCon multipleCon = multipleConMapper.toEntity(multipleConDTO);
        multipleCon = multipleConRepository.save(multipleCon);
        return multipleConMapper.toDto(multipleCon);
    }

    /**
     * Get all the multipleCons.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MultipleConDTO> findAll() {
        log.debug("Request to get all MultipleCons");
        return multipleConRepository.findAll().stream()
            .map(multipleConMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the multipleCons where TheConnectionSuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<MultipleConDTO> findAllWhereTheConnectionSuperIsNull() {
        log.debug("Request to get all multipleCons where TheConnectionSuper is null");
        return StreamSupport
            .stream(multipleConRepository.findAll().spliterator(), false)
            .filter(multipleCon -> multipleCon.getTheConnectionSuper() == null)
            .map(multipleConMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one multipleCon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MultipleConDTO> findOne(Long id) {
        log.debug("Request to get MultipleCon : {}", id);
        return multipleConRepository.findById(id)
            .map(multipleConMapper::toDto);
    }

    /**
     * Delete the multipleCon by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MultipleCon : {}", id);
        multipleConRepository.deleteById(id);
    }
}
