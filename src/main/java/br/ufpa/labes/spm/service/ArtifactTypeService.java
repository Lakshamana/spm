package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ArtifactType;
import br.ufpa.labes.spm.repository.ArtifactTypeRepository;
import br.ufpa.labes.spm.service.dto.ArtifactTypeDTO;
import br.ufpa.labes.spm.service.mapper.ArtifactTypeMapper;
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
 * Service Implementation for managing {@link ArtifactType}.
 */
@Service
@Transactional
public class ArtifactTypeService {

    private final Logger log = LoggerFactory.getLogger(ArtifactTypeService.class);

    private final ArtifactTypeRepository artifactTypeRepository;

    private final ArtifactTypeMapper artifactTypeMapper;

    public ArtifactTypeService(ArtifactTypeRepository artifactTypeRepository, ArtifactTypeMapper artifactTypeMapper) {
        this.artifactTypeRepository = artifactTypeRepository;
        this.artifactTypeMapper = artifactTypeMapper;
    }

    /**
     * Save a artifactType.
     *
     * @param artifactTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public ArtifactTypeDTO save(ArtifactTypeDTO artifactTypeDTO) {
        log.debug("Request to save ArtifactType : {}", artifactTypeDTO);
        ArtifactType artifactType = artifactTypeMapper.toEntity(artifactTypeDTO);
        artifactType = artifactTypeRepository.save(artifactType);
        return artifactTypeMapper.toDto(artifactType);
    }

    /**
     * Get all the artifactTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ArtifactTypeDTO> findAll() {
        log.debug("Request to get all ArtifactTypes");
        return artifactTypeRepository.findAll().stream()
            .map(artifactTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the artifactTypes where TheTypeSuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<ArtifactTypeDTO> findAllWhereTheTypeSuperIsNull() {
        log.debug("Request to get all artifactTypes where TheTypeSuper is null");
        return StreamSupport
            .stream(artifactTypeRepository.findAll().spliterator(), false)
            .filter(artifactType -> artifactType.getTheTypeSuper() == null)
            .map(artifactTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one artifactType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArtifactTypeDTO> findOne(Long id) {
        log.debug("Request to get ArtifactType : {}", id);
        return artifactTypeRepository.findById(id)
            .map(artifactTypeMapper::toDto);
    }

    /**
     * Delete the artifactType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ArtifactType : {}", id);
        artifactTypeRepository.deleteById(id);
    }
}
