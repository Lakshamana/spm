package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.AgentPlaysRole;
import br.ufpa.labes.spm.repository.AgentPlaysRoleRepository;
import br.ufpa.labes.spm.service.dto.AgentPlaysRoleDTO;
import br.ufpa.labes.spm.service.mapper.AgentPlaysRoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link AgentPlaysRole}.
 */
@Service
@Transactional
public class AgentPlaysRoleService {

    private final Logger log = LoggerFactory.getLogger(AgentPlaysRoleService.class);

    private final AgentPlaysRoleRepository agentPlaysRoleRepository;

    private final AgentPlaysRoleMapper agentPlaysRoleMapper;

    public AgentPlaysRoleService(AgentPlaysRoleRepository agentPlaysRoleRepository, AgentPlaysRoleMapper agentPlaysRoleMapper) {
        this.agentPlaysRoleRepository = agentPlaysRoleRepository;
        this.agentPlaysRoleMapper = agentPlaysRoleMapper;
    }

    /**
     * Save a agentPlaysRole.
     *
     * @param agentPlaysRoleDTO the entity to save.
     * @return the persisted entity.
     */
    public AgentPlaysRoleDTO save(AgentPlaysRoleDTO agentPlaysRoleDTO) {
        log.debug("Request to save AgentPlaysRole : {}", agentPlaysRoleDTO);
        AgentPlaysRole agentPlaysRole = agentPlaysRoleMapper.toEntity(agentPlaysRoleDTO);
        agentPlaysRole = agentPlaysRoleRepository.save(agentPlaysRole);
        return agentPlaysRoleMapper.toDto(agentPlaysRole);
    }

    /**
     * Get all the agentPlaysRoles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AgentPlaysRoleDTO> findAll() {
        log.debug("Request to get all AgentPlaysRoles");
        return agentPlaysRoleRepository.findAll().stream()
            .map(agentPlaysRoleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one agentPlaysRole by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AgentPlaysRoleDTO> findOne(Long id) {
        log.debug("Request to get AgentPlaysRole : {}", id);
        return agentPlaysRoleRepository.findById(id)
            .map(agentPlaysRoleMapper::toDto);
    }

    /**
     * Delete the agentPlaysRole by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AgentPlaysRole : {}", id);
        agentPlaysRoleRepository.deleteById(id);
    }
}
