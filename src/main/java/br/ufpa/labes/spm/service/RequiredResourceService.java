package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.RequiredResource;
import br.ufpa.labes.spm.repository.RequiredResourceRepository;
import br.ufpa.labes.spm.service.dto.RequiredResourceDTO;
import br.ufpa.labes.spm.service.mapper.RequiredResourceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link RequiredResource}.
 */
@Service
@Transactional
public class RequiredResourceService {

    private final Logger log = LoggerFactory.getLogger(RequiredResourceService.class);

    private final RequiredResourceRepository requiredResourceRepository;

    private final RequiredResourceMapper requiredResourceMapper;

    public RequiredResourceService(RequiredResourceRepository requiredResourceRepository, RequiredResourceMapper requiredResourceMapper) {
        this.requiredResourceRepository = requiredResourceRepository;
        this.requiredResourceMapper = requiredResourceMapper;
    }

    /**
     * Save a requiredResource.
     *
     * @param requiredResourceDTO the entity to save.
     * @return the persisted entity.
     */
    public RequiredResourceDTO save(RequiredResourceDTO requiredResourceDTO) {
        log.debug("Request to save RequiredResource : {}", requiredResourceDTO);
        RequiredResource requiredResource = requiredResourceMapper.toEntity(requiredResourceDTO);
        requiredResource = requiredResourceRepository.save(requiredResource);
        return requiredResourceMapper.toDto(requiredResource);
    }

    /**
     * Get all the requiredResources.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RequiredResourceDTO> findAll() {
        log.debug("Request to get all RequiredResources");
        return requiredResourceRepository.findAll().stream()
            .map(requiredResourceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one requiredResource by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RequiredResourceDTO> findOne(Long id) {
        log.debug("Request to get RequiredResource : {}", id);
        return requiredResourceRepository.findById(id)
            .map(requiredResourceMapper::toDto);
    }

    /**
     * Delete the requiredResource by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RequiredResource : {}", id);
        requiredResourceRepository.deleteById(id);
    }
}
