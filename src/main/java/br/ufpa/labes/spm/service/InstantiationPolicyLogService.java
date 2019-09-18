package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.InstantiationPolicyLog;
import br.ufpa.labes.spm.repository.InstantiationPolicyLogRepository;
import br.ufpa.labes.spm.service.dto.InstantiationPolicyLogDTO;
import br.ufpa.labes.spm.service.mapper.InstantiationPolicyLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link InstantiationPolicyLog}.
 */
@Service
@Transactional
public class InstantiationPolicyLogService {

    private final Logger log = LoggerFactory.getLogger(InstantiationPolicyLogService.class);

    private final InstantiationPolicyLogRepository instantiationPolicyLogRepository;

    private final InstantiationPolicyLogMapper instantiationPolicyLogMapper;

    public InstantiationPolicyLogService(InstantiationPolicyLogRepository instantiationPolicyLogRepository, InstantiationPolicyLogMapper instantiationPolicyLogMapper) {
        this.instantiationPolicyLogRepository = instantiationPolicyLogRepository;
        this.instantiationPolicyLogMapper = instantiationPolicyLogMapper;
    }

    /**
     * Save a instantiationPolicyLog.
     *
     * @param instantiationPolicyLogDTO the entity to save.
     * @return the persisted entity.
     */
    public InstantiationPolicyLogDTO save(InstantiationPolicyLogDTO instantiationPolicyLogDTO) {
        log.debug("Request to save InstantiationPolicyLog : {}", instantiationPolicyLogDTO);
        InstantiationPolicyLog instantiationPolicyLog = instantiationPolicyLogMapper.toEntity(instantiationPolicyLogDTO);
        instantiationPolicyLog = instantiationPolicyLogRepository.save(instantiationPolicyLog);
        return instantiationPolicyLogMapper.toDto(instantiationPolicyLog);
    }

    /**
     * Get all the instantiationPolicyLogs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<InstantiationPolicyLogDTO> findAll() {
        log.debug("Request to get all InstantiationPolicyLogs");
        return instantiationPolicyLogRepository.findAll().stream()
            .map(instantiationPolicyLogMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one instantiationPolicyLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InstantiationPolicyLogDTO> findOne(Long id) {
        log.debug("Request to get InstantiationPolicyLog : {}", id);
        return instantiationPolicyLogRepository.findById(id)
            .map(instantiationPolicyLogMapper::toDto);
    }

    /**
     * Delete the instantiationPolicyLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete InstantiationPolicyLog : {}", id);
        instantiationPolicyLogRepository.deleteById(id);
    }
}
