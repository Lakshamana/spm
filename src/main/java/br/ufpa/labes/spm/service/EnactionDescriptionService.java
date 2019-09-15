package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.EnactionDescription;
import br.ufpa.labes.spm.repository.EnactionDescriptionRepository;
import br.ufpa.labes.spm.service.dto.EnactionDescriptionDTO;
import br.ufpa.labes.spm.service.mapper.EnactionDescriptionMapper;
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
 * Service Implementation for managing {@link EnactionDescription}.
 */
@Service
@Transactional
public class EnactionDescriptionService {

    private final Logger log = LoggerFactory.getLogger(EnactionDescriptionService.class);

    private final EnactionDescriptionRepository enactionDescriptionRepository;

    private final EnactionDescriptionMapper enactionDescriptionMapper;

    public EnactionDescriptionService(EnactionDescriptionRepository enactionDescriptionRepository, EnactionDescriptionMapper enactionDescriptionMapper) {
        this.enactionDescriptionRepository = enactionDescriptionRepository;
        this.enactionDescriptionMapper = enactionDescriptionMapper;
    }

    /**
     * Save a enactionDescription.
     *
     * @param enactionDescriptionDTO the entity to save.
     * @return the persisted entity.
     */
    public EnactionDescriptionDTO save(EnactionDescriptionDTO enactionDescriptionDTO) {
        log.debug("Request to save EnactionDescription : {}", enactionDescriptionDTO);
        EnactionDescription enactionDescription = enactionDescriptionMapper.toEntity(enactionDescriptionDTO);
        enactionDescription = enactionDescriptionRepository.save(enactionDescription);
        return enactionDescriptionMapper.toDto(enactionDescription);
    }

    /**
     * Get all the enactionDescriptions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EnactionDescriptionDTO> findAll() {
        log.debug("Request to get all EnactionDescriptions");
        return enactionDescriptionRepository.findAll().stream()
            .map(enactionDescriptionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the enactionDescriptions where ThePlain is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EnactionDescriptionDTO> findAllWhereThePlainIsNull() {
        log.debug("Request to get all enactionDescriptions where ThePlain is null");
        return StreamSupport
            .stream(enactionDescriptionRepository.findAll().spliterator(), false)
            .filter(enactionDescription -> enactionDescription.getThePlain() == null)
            .map(enactionDescriptionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one enactionDescription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EnactionDescriptionDTO> findOne(Long id) {
        log.debug("Request to get EnactionDescription : {}", id);
        return enactionDescriptionRepository.findById(id)
            .map(enactionDescriptionMapper::toDto);
    }

    /**
     * Delete the enactionDescription by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EnactionDescription : {}", id);
        enactionDescriptionRepository.deleteById(id);
    }
}
