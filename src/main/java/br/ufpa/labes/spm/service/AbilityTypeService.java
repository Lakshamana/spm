package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.AbilityType;
import br.ufpa.labes.spm.repository.AbilityTypeRepository;
import br.ufpa.labes.spm.service.dto.AbilityTypeDTO;
import br.ufpa.labes.spm.service.mapper.AbilityTypeMapper;
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
 * Service Implementation for managing {@link AbilityType}.
 */
@Service
@Transactional
public class AbilityTypeService {

    private final Logger log = LoggerFactory.getLogger(AbilityTypeService.class);

    private final AbilityTypeRepository abilityTypeRepository;

    private final AbilityTypeMapper abilityTypeMapper;

    public AbilityTypeService(AbilityTypeRepository abilityTypeRepository, AbilityTypeMapper abilityTypeMapper) {
        this.abilityTypeRepository = abilityTypeRepository;
        this.abilityTypeMapper = abilityTypeMapper;
    }

    /**
     * Save a abilityType.
     *
     * @param abilityTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public AbilityTypeDTO save(AbilityTypeDTO abilityTypeDTO) {
        log.debug("Request to save AbilityType : {}", abilityTypeDTO);
        AbilityType abilityType = abilityTypeMapper.toEntity(abilityTypeDTO);
        abilityType = abilityTypeRepository.save(abilityType);
        return abilityTypeMapper.toDto(abilityType);
    }

    /**
     * Get all the abilityTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AbilityTypeDTO> findAll() {
        log.debug("Request to get all AbilityTypes");
        return abilityTypeRepository.findAll().stream()
            .map(abilityTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the abilityTypes where TheTypeSuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<AbilityTypeDTO> findAllWhereTheTypeSuperIsNull() {
        log.debug("Request to get all abilityTypes where TheTypeSuper is null");
        return StreamSupport
            .stream(abilityTypeRepository.findAll().spliterator(), false)
            .filter(abilityType -> abilityType.getTheTypeSuper() == null)
            .map(abilityTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one abilityType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AbilityTypeDTO> findOne(Long id) {
        log.debug("Request to get AbilityType : {}", id);
        return abilityTypeRepository.findById(id)
            .map(abilityTypeMapper::toDto);
    }

    /**
     * Delete the abilityType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AbilityType : {}", id);
        abilityTypeRepository.deleteById(id);
    }
}
