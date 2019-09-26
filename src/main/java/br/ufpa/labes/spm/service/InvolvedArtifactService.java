package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.InvolvedArtifact;
import br.ufpa.labes.spm.repository.InvolvedArtifactRepository;
import br.ufpa.labes.spm.service.dto.InvolvedArtifactDTO;
import br.ufpa.labes.spm.service.mapper.InvolvedArtifactMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link InvolvedArtifact}.
 */
@Service
@Transactional
public class InvolvedArtifactService {

    private final Logger log = LoggerFactory.getLogger(InvolvedArtifactService.class);

    private final InvolvedArtifactRepository involvedArtifactRepository;

    private final InvolvedArtifactMapper involvedArtifactMapper;

    public InvolvedArtifactService(InvolvedArtifactRepository involvedArtifactRepository, InvolvedArtifactMapper involvedArtifactMapper) {
        this.involvedArtifactRepository = involvedArtifactRepository;
        this.involvedArtifactMapper = involvedArtifactMapper;
    }

    /**
     * Save a involvedArtifact.
     *
     * @param involvedArtifactDTO the entity to save.
     * @return the persisted entity.
     */
    public InvolvedArtifactDTO save(InvolvedArtifactDTO involvedArtifactDTO) {
        log.debug("Request to save InvolvedArtifact : {}", involvedArtifactDTO);
        InvolvedArtifact involvedArtifact = involvedArtifactMapper.toEntity(involvedArtifactDTO);
        involvedArtifact = involvedArtifactRepository.save(involvedArtifact);
        return involvedArtifactMapper.toDto(involvedArtifact);
    }

    /**
     * Get all the involvedArtifacts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InvolvedArtifactDTO> findAll() {
        log.debug("Request to get all InvolvedArtifacts");
        return involvedArtifactRepository.findAll().stream()
            .map(involvedArtifactMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one involvedArtifact by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InvolvedArtifactDTO> findOne(Long id) {
        log.debug("Request to get InvolvedArtifact : {}", id);
        return involvedArtifactRepository.findById(id)
            .map(involvedArtifactMapper::toDto);
    }

    /**
     * Delete the involvedArtifact by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InvolvedArtifact : {}", id);
        involvedArtifactRepository.deleteById(id);
    }
}
