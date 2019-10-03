package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.OutOfWorkPeriod;
import br.ufpa.labes.spm.repository.OutOfWorkPeriodRepository;
import br.ufpa.labes.spm.service.dto.OutOfWorkPeriodDTO;
import br.ufpa.labes.spm.service.mapper.OutOfWorkPeriodMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link OutOfWorkPeriod}.
 */
@Service
@Transactional
public class OutOfWorkPeriodService {

    private final Logger log = LoggerFactory.getLogger(OutOfWorkPeriodService.class);

    private final OutOfWorkPeriodRepository outOfWorkPeriodRepository;

    private final OutOfWorkPeriodMapper outOfWorkPeriodMapper;

    public OutOfWorkPeriodService(OutOfWorkPeriodRepository outOfWorkPeriodRepository, OutOfWorkPeriodMapper outOfWorkPeriodMapper) {
        this.outOfWorkPeriodRepository = outOfWorkPeriodRepository;
        this.outOfWorkPeriodMapper = outOfWorkPeriodMapper;
    }

    /**
     * Save a outOfWorkPeriod.
     *
     * @param outOfWorkPeriodDTO the entity to save.
     * @return the persisted entity.
     */
    public OutOfWorkPeriodDTO save(OutOfWorkPeriodDTO outOfWorkPeriodDTO) {
        log.debug("Request to save OutOfWorkPeriod : {}", outOfWorkPeriodDTO);
        OutOfWorkPeriod outOfWorkPeriod = outOfWorkPeriodMapper.toEntity(outOfWorkPeriodDTO);
        outOfWorkPeriod = outOfWorkPeriodRepository.save(outOfWorkPeriod);
        return outOfWorkPeriodMapper.toDto(outOfWorkPeriod);
    }

    /**
     * Get all the outOfWorkPeriods.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OutOfWorkPeriodDTO> findAll() {
        log.debug("Request to get all OutOfWorkPeriods");
        return outOfWorkPeriodRepository.findAll().stream()
            .map(outOfWorkPeriodMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one outOfWorkPeriod by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OutOfWorkPeriodDTO> findOne(Long id) {
        log.debug("Request to get OutOfWorkPeriod : {}", id);
        return outOfWorkPeriodRepository.findById(id)
            .map(outOfWorkPeriodMapper::toDto);
    }

    /**
     * Delete the outOfWorkPeriod by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete OutOfWorkPeriod : {}", id);
        outOfWorkPeriodRepository.deleteById(id);
    }
}
