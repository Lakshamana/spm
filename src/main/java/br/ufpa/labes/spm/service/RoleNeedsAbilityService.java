package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.RoleNeedsAbility;
import br.ufpa.labes.spm.repository.RoleNeedsAbilityRepository;
import br.ufpa.labes.spm.service.dto.RoleNeedsAbilityDTO;
import br.ufpa.labes.spm.service.mapper.RoleNeedsAbilityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link RoleNeedsAbility}.
 */
@Service
@Transactional
public class RoleNeedsAbilityService {

    private final Logger log = LoggerFactory.getLogger(RoleNeedsAbilityService.class);

    private final RoleNeedsAbilityRepository roleNeedsAbilityRepository;

    private final RoleNeedsAbilityMapper roleNeedsAbilityMapper;

    public RoleNeedsAbilityService(RoleNeedsAbilityRepository roleNeedsAbilityRepository, RoleNeedsAbilityMapper roleNeedsAbilityMapper) {
        this.roleNeedsAbilityRepository = roleNeedsAbilityRepository;
        this.roleNeedsAbilityMapper = roleNeedsAbilityMapper;
    }

    /**
     * Save a roleNeedsAbility.
     *
     * @param roleNeedsAbilityDTO the entity to save.
     * @return the persisted entity.
     */
    public RoleNeedsAbilityDTO save(RoleNeedsAbilityDTO roleNeedsAbilityDTO) {
        log.debug("Request to save RoleNeedsAbility : {}", roleNeedsAbilityDTO);
        RoleNeedsAbility roleNeedsAbility = roleNeedsAbilityMapper.toEntity(roleNeedsAbilityDTO);
        roleNeedsAbility = roleNeedsAbilityRepository.save(roleNeedsAbility);
        return roleNeedsAbilityMapper.toDto(roleNeedsAbility);
    }

    /**
     * Get all the roleNeedsAbilities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RoleNeedsAbilityDTO> findAll() {
        log.debug("Request to get all RoleNeedsAbilities");
        return roleNeedsAbilityRepository.findAll().stream()
            .map(roleNeedsAbilityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one roleNeedsAbility by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RoleNeedsAbilityDTO> findOne(Long id) {
        log.debug("Request to get RoleNeedsAbility : {}", id);
        return roleNeedsAbilityRepository.findById(id)
            .map(roleNeedsAbilityMapper::toDto);
    }

    /**
     * Delete the roleNeedsAbility by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RoleNeedsAbility : {}", id);
        roleNeedsAbilityRepository.deleteById(id);
    }
}
