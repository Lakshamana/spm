package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ResourceType;
import br.ufpa.labes.spm.repository.ResourceTypeRepository;
import br.ufpa.labes.spm.service.dto.ResourceTypeDTO;
import br.ufpa.labes.spm.service.mapper.ResourceTypeMapper;
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
 * Service Implementation for managing {@link ResourceType}.
 */
@Service
@Transactional
public class ResourceTypeService {

    private final Logger log = LoggerFactory.getLogger(ResourceTypeService.class);

    private final ResourceTypeRepository resourceTypeRepository;

    private final ResourceTypeMapper resourceTypeMapper;

    public ResourceTypeService(ResourceTypeRepository resourceTypeRepository, ResourceTypeMapper resourceTypeMapper) {
        this.resourceTypeRepository = resourceTypeRepository;
        this.resourceTypeMapper = resourceTypeMapper;
    }

    /**
     * Save a resourceType.
     *
     * @param resourceTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public ResourceTypeDTO save(ResourceTypeDTO resourceTypeDTO) {
        log.debug("Request to save ResourceType : {}", resourceTypeDTO);
        ResourceType resourceType = resourceTypeMapper.toEntity(resourceTypeDTO);
        resourceType = resourceTypeRepository.save(resourceType);
        return resourceTypeMapper.toDto(resourceType);
    }

    /**
     * Get all the resourceTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ResourceTypeDTO> findAll() {
        log.debug("Request to get all ResourceTypes");
        return resourceTypeRepository.findAll().stream()
            .map(resourceTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the resourceTypes where TheTypeSuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<ResourceTypeDTO> findAllWhereTheTypeSuperIsNull() {
        log.debug("Request to get all resourceTypes where TheTypeSuper is null");
        return StreamSupport
            .stream(resourceTypeRepository.findAll().spliterator(), false)
            .filter(resourceType -> resourceType.getTheTypeSuper() == null)
            .map(resourceTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one resourceType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ResourceTypeDTO> findOne(Long id) {
        log.debug("Request to get ResourceType : {}", id);
        return resourceTypeRepository.findById(id)
            .map(resourceTypeMapper::toDto);
    }

    /**
     * Delete the resourceType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ResourceType : {}", id);
        resourceTypeRepository.deleteById(id);
    }
}
