package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.RoleType;
import br.ufpa.labes.spm.repository.RoleTypeRepository;
import br.ufpa.labes.spm.service.dto.RoleTypeDTO;
import br.ufpa.labes.spm.service.mapper.RoleTypeMapper;
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
 * Service Implementation for managing {@link RoleType}.
 */
@Service
@Transactional
public class RoleTypeService {

    private final Logger log = LoggerFactory.getLogger(RoleTypeService.class);

    private final RoleTypeRepository roleTypeRepository;

    private final RoleTypeMapper roleTypeMapper;

    public RoleTypeService(RoleTypeRepository roleTypeRepository, RoleTypeMapper roleTypeMapper) {
        this.roleTypeRepository = roleTypeRepository;
        this.roleTypeMapper = roleTypeMapper;
    }

    /**
     * Save a roleType.
     *
     * @param roleTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public RoleTypeDTO save(RoleTypeDTO roleTypeDTO) {
        log.debug("Request to save RoleType : {}", roleTypeDTO);
        RoleType roleType = roleTypeMapper.toEntity(roleTypeDTO);
        roleType = roleTypeRepository.save(roleType);
        return roleTypeMapper.toDto(roleType);
    }

    /**
     * Get all the roleTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RoleTypeDTO> findAll() {
        log.debug("Request to get all RoleTypes");
        return roleTypeRepository.findAll().stream()
            .map(roleTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the roleTypes where TheTypeSuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<RoleTypeDTO> findAllWhereTheTypeSuperIsNull() {
        log.debug("Request to get all roleTypes where TheTypeSuper is null");
        return StreamSupport
            .stream(roleTypeRepository.findAll().spliterator(), false)
            .filter(roleType -> roleType.getTheTypeSuper() == null)
            .map(roleTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one roleType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RoleTypeDTO> findOne(Long id) {
        log.debug("Request to get RoleType : {}", id);
        return roleTypeRepository.findById(id)
            .map(roleTypeMapper::toDto);
    }

    /**
     * Delete the roleType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RoleType : {}", id);
        roleTypeRepository.deleteById(id);
    }
}
