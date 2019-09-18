package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Dependency;
import br.ufpa.labes.spm.repository.DependencyRepository;
import br.ufpa.labes.spm.service.dto.DependencyDTO;
import br.ufpa.labes.spm.service.mapper.DependencyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Dependency}.
 */
@Service
@Transactional
public class DependencyService {

    private final Logger log = LoggerFactory.getLogger(DependencyService.class);

    private final DependencyRepository dependencyRepository;

    private final DependencyMapper dependencyMapper;

    public DependencyService(DependencyRepository dependencyRepository, DependencyMapper dependencyMapper) {
        this.dependencyRepository = dependencyRepository;
        this.dependencyMapper = dependencyMapper;
    }

    /**
     * Save a dependency.
     *
     * @param dependencyDTO the entity to save.
     * @return the persisted entity.
     */
    public DependencyDTO save(DependencyDTO dependencyDTO) {
        log.debug("Request to save Dependency : {}", dependencyDTO);
        Dependency dependency = dependencyMapper.toEntity(dependencyDTO);
        dependency = dependencyRepository.save(dependency);
        return dependencyMapper.toDto(dependency);
    }

    /**
     * Get all the dependencies.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DependencyDTO> findAll() {
        log.debug("Request to get all Dependencies");
        return dependencyRepository.findAll().stream()
            .map(dependencyMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one dependency by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DependencyDTO> findOne(Long id) {
        log.debug("Request to get Dependency : {}", id);
        return dependencyRepository.findById(id)
            .map(dependencyMapper::toDto);
    }

    /**
     * Delete the dependency by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Dependency : {}", id);
        dependencyRepository.deleteById(id);
    }
}
