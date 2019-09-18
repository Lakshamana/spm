package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.RelationshipKind;
import br.ufpa.labes.spm.repository.RelationshipKindRepository;
import br.ufpa.labes.spm.service.dto.RelationshipKindDTO;
import br.ufpa.labes.spm.service.mapper.RelationshipKindMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link RelationshipKind}.
 */
@Service
@Transactional
public class RelationshipKindService {

    private final Logger log = LoggerFactory.getLogger(RelationshipKindService.class);

    private final RelationshipKindRepository relationshipKindRepository;

    private final RelationshipKindMapper relationshipKindMapper;

    public RelationshipKindService(RelationshipKindRepository relationshipKindRepository, RelationshipKindMapper relationshipKindMapper) {
        this.relationshipKindRepository = relationshipKindRepository;
        this.relationshipKindMapper = relationshipKindMapper;
    }

    /**
     * Save a relationshipKind.
     *
     * @param relationshipKindDTO the entity to save.
     * @return the persisted entity.
     */
    public RelationshipKindDTO save(RelationshipKindDTO relationshipKindDTO) {
        log.debug("Request to save RelationshipKind : {}", relationshipKindDTO);
        RelationshipKind relationshipKind = relationshipKindMapper.toEntity(relationshipKindDTO);
        relationshipKind = relationshipKindRepository.save(relationshipKind);
        return relationshipKindMapper.toDto(relationshipKind);
    }

    /**
     * Get all the relationshipKinds.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RelationshipKindDTO> findAll() {
        log.debug("Request to get all RelationshipKinds");
        return relationshipKindRepository.findAll().stream()
            .map(relationshipKindMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one relationshipKind by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RelationshipKindDTO> findOne(Long id) {
        log.debug("Request to get RelationshipKind : {}", id);
        return relationshipKindRepository.findById(id)
            .map(relationshipKindMapper::toDto);
    }

    /**
     * Delete the relationshipKind by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RelationshipKind : {}", id);
        relationshipKindRepository.deleteById(id);
    }
}
