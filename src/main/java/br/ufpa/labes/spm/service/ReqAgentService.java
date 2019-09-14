package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ReqAgent;
import br.ufpa.labes.spm.repository.ReqAgentRepository;
import br.ufpa.labes.spm.service.dto.ReqAgentDTO;
import br.ufpa.labes.spm.service.mapper.ReqAgentMapper;
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
 * Service Implementation for managing {@link ReqAgent}.
 */
@Service
@Transactional
public class ReqAgentService {

    private final Logger log = LoggerFactory.getLogger(ReqAgentService.class);

    private final ReqAgentRepository reqAgentRepository;

    private final ReqAgentMapper reqAgentMapper;

    public ReqAgentService(ReqAgentRepository reqAgentRepository, ReqAgentMapper reqAgentMapper) {
        this.reqAgentRepository = reqAgentRepository;
        this.reqAgentMapper = reqAgentMapper;
    }

    /**
     * Save a reqAgent.
     *
     * @param reqAgentDTO the entity to save.
     * @return the persisted entity.
     */
    public ReqAgentDTO save(ReqAgentDTO reqAgentDTO) {
        log.debug("Request to save ReqAgent : {}", reqAgentDTO);
        ReqAgent reqAgent = reqAgentMapper.toEntity(reqAgentDTO);
        reqAgent = reqAgentRepository.save(reqAgent);
        return reqAgentMapper.toDto(reqAgent);
    }

    /**
     * Get all the reqAgents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReqAgentDTO> findAll() {
        log.debug("Request to get all ReqAgents");
        return reqAgentRepository.findAll().stream()
            .map(reqAgentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the reqAgents where TheRequiredPeopleSuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<ReqAgentDTO> findAllWhereTheRequiredPeopleSuperIsNull() {
        log.debug("Request to get all reqAgents where TheRequiredPeopleSuper is null");
        return StreamSupport
            .stream(reqAgentRepository.findAll().spliterator(), false)
            .filter(reqAgent -> reqAgent.getTheRequiredPeopleSuper() == null)
            .map(reqAgentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one reqAgent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReqAgentDTO> findOne(Long id) {
        log.debug("Request to get ReqAgent : {}", id);
        return reqAgentRepository.findById(id)
            .map(reqAgentMapper::toDto);
    }

    /**
     * Delete the reqAgent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ReqAgent : {}", id);
        reqAgentRepository.deleteById(id);
    }
}
