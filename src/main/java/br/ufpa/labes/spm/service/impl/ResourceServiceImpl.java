package br.ufpa.labes.spm.service.impl;

import br.ufpa.labes.spm.service.ResourceService;
import br.ufpa.labes.spm.domain.Resource;
import br.ufpa.labes.spm.repository.ResourceRepository;
import br.ufpa.labes.spm.service.dto.ResourceDTO;
import br.ufpa.labes.spm.service.mapper.ResourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Resource}.
 */
@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

    private final Logger log = LoggerFactory.getLogger(ResourceServiceImpl.class);

    private final ResourceRepository resourceRepository;

    private final ResourceMapper resourceMapper;

    public ResourceServiceImpl(ResourceRepository resourceRepository, ResourceMapper resourceMapper) {
        this.resourceRepository = resourceRepository;
        this.resourceMapper = resourceMapper;
    }

    /**
     * Save a resource.
     *
     * @param resourceDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ResourceDTO save(ResourceDTO resourceDTO) {
        log.debug("Request to save Resource : {}", resourceDTO);
        Resource resource = resourceMapper.toEntity(resourceDTO);
        resource = resourceRepository.save(resource);
        return resourceMapper.toDto(resource);
    }

    /**
     * Get all the resources.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResourceDTO> findAll() {
        log.debug("Request to get all Resources");
        return resourceRepository.findAll().stream()
            .map(resourceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one resource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ResourceDTO> findOne(Long id) {
        log.debug("Request to get Resource : {}", id);
        return resourceRepository.findById(id)
            .map(resourceMapper::toDto);
    }

    /**
     * Delete the resource by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Resource : {}", id);
        resourceRepository.deleteById(id);
    }
}
