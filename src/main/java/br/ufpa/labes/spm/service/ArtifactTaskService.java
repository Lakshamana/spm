package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ArtifactTask;
import br.ufpa.labes.spm.repository.ArtifactTaskRepository;
import br.ufpa.labes.spm.service.dto.ArtifactTaskDTO;
import br.ufpa.labes.spm.service.mapper.ArtifactTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ArtifactTask}.
 */
@Service
@Transactional
public class ArtifactTaskService {

    private final Logger log = LoggerFactory.getLogger(ArtifactTaskService.class);

    private final ArtifactTaskRepository artifactTaskRepository;

    private final ArtifactTaskMapper artifactTaskMapper;

    public ArtifactTaskService(ArtifactTaskRepository artifactTaskRepository, ArtifactTaskMapper artifactTaskMapper) {
        this.artifactTaskRepository = artifactTaskRepository;
        this.artifactTaskMapper = artifactTaskMapper;
    }

    /**
     * Save a artifactTask.
     *
     * @param artifactTaskDTO the entity to save.
     * @return the persisted entity.
     */
    public ArtifactTaskDTO save(ArtifactTaskDTO artifactTaskDTO) {
        log.debug("Request to save ArtifactTask : {}", artifactTaskDTO);
        ArtifactTask artifactTask = artifactTaskMapper.toEntity(artifactTaskDTO);
        artifactTask = artifactTaskRepository.save(artifactTask);
        return artifactTaskMapper.toDto(artifactTask);
    }

    /**
     * Get all the artifactTasks.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ArtifactTaskDTO> findAll() {
        log.debug("Request to get all ArtifactTasks");
        return artifactTaskRepository.findAll().stream()
            .map(artifactTaskMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one artifactTask by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArtifactTaskDTO> findOne(Long id) {
        log.debug("Request to get ArtifactTask : {}", id);
        return artifactTaskRepository.findById(id)
            .map(artifactTaskMapper::toDto);
    }

    /**
     * Delete the artifactTask by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ArtifactTask : {}", id);
        artifactTaskRepository.deleteById(id);
    }
}
