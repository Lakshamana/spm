package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ArtifactCon;
import br.ufpa.labes.spm.repository.ArtifactConRepository;
import br.ufpa.labes.spm.service.dto.ArtifactConDTO;
import br.ufpa.labes.spm.service.mapper.ArtifactConMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link ArtifactCon}.
 */
@Service
@Transactional
public class ArtifactConService {

    private final Logger log = LoggerFactory.getLogger(ArtifactConService.class);

    private final ArtifactConRepository artifactConRepository;

    private final ArtifactConMapper artifactConMapper;

    public ArtifactConService(ArtifactConRepository artifactConRepository, ArtifactConMapper artifactConMapper) {
        this.artifactConRepository = artifactConRepository;
        this.artifactConMapper = artifactConMapper;
    }

    /**
     * Save a artifactCon.
     *
     * @param artifactConDTO the entity to save.
     * @return the persisted entity.
     */
    public ArtifactConDTO save(ArtifactConDTO artifactConDTO) {
        log.debug("Request to save ArtifactCon : {}", artifactConDTO);
        ArtifactCon artifactCon = artifactConMapper.toEntity(artifactConDTO);
        artifactCon = artifactConRepository.save(artifactCon);
        return artifactConMapper.toDto(artifactCon);
    }

    /**
     * Get all the artifactCons.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ArtifactConDTO> findAll() {
        log.debug("Request to get all ArtifactCons");
        return artifactConRepository.findAllWithEagerRelationships().stream()
            .map(artifactConMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the artifactCons with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ArtifactConDTO> findAllWithEagerRelationships(Pageable pageable) {
        return artifactConRepository.findAllWithEagerRelationships(pageable).map(artifactConMapper::toDto);
    }
    


    /**
    *  Get all the artifactCons where TheConnectionSuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<ArtifactConDTO> findAllWhereTheConnectionSuperIsNull() {
        log.debug("Request to get all artifactCons where TheConnectionSuper is null");
        return StreamSupport
            .stream(artifactConRepository.findAll().spliterator(), false)
            .filter(artifactCon -> artifactCon.getTheConnectionSuper() == null)
            .map(artifactConMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one artifactCon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArtifactConDTO> findOne(Long id) {
        log.debug("Request to get ArtifactCon : {}", id);
        return artifactConRepository.findOneWithEagerRelationships(id)
            .map(artifactConMapper::toDto);
    }

    /**
     * Delete the artifactCon by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ArtifactCon : {}", id);
        artifactConRepository.deleteById(id);
    }
}
