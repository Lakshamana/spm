package br.ufpa.labes.spm.service.impl;

import br.ufpa.labes.spm.service.AbilityService;
import br.ufpa.labes.spm.domain.Ability;
import br.ufpa.labes.spm.repository.AbilityRepository;
import br.ufpa.labes.spm.service.dto.AbilityDTO;
import br.ufpa.labes.spm.service.mapper.AbilityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Ability}.
 */
@Service
@Transactional
public class AbilityServiceImpl implements AbilityService {

    private final Logger log = LoggerFactory.getLogger(AbilityServiceImpl.class);

    private final AbilityRepository abilityRepository;

    private final AbilityMapper abilityMapper;

    public AbilityServiceImpl(AbilityRepository abilityRepository, AbilityMapper abilityMapper) {
        this.abilityRepository = abilityRepository;
        this.abilityMapper = abilityMapper;
    }

    /**
     * Save a ability.
     *
     * @param abilityDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AbilityDTO save(AbilityDTO abilityDTO) {
        log.debug("Request to save Ability : {}", abilityDTO);
        Ability ability = abilityMapper.toEntity(abilityDTO);
        ability = abilityRepository.save(ability);
        return abilityMapper.toDto(ability);
    }

    /**
     * Get all the abilities.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<AbilityDTO> findAll() {
        log.debug("Request to get all Abilities");
        return abilityRepository.findAll().stream()
            .map(abilityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one ability by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AbilityDTO> findOne(Long id) {
        log.debug("Request to get Ability : {}", id);
        return abilityRepository.findById(id)
            .map(abilityMapper::toDto);
    }

    /**
     * Delete the ability by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ability : {}", id);
        abilityRepository.deleteById(id);
    }
}
