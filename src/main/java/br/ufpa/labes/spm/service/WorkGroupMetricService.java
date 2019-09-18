package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.WorkGroupMetric;
import br.ufpa.labes.spm.repository.WorkGroupMetricRepository;
import br.ufpa.labes.spm.service.dto.WorkGroupMetricDTO;
import br.ufpa.labes.spm.service.mapper.WorkGroupMetricMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link WorkGroupMetric}.
 */
@Service
@Transactional
public class WorkGroupMetricService {

    private final Logger log = LoggerFactory.getLogger(WorkGroupMetricService.class);

    private final WorkGroupMetricRepository workGroupMetricRepository;

    private final WorkGroupMetricMapper workGroupMetricMapper;

    public WorkGroupMetricService(WorkGroupMetricRepository workGroupMetricRepository, WorkGroupMetricMapper workGroupMetricMapper) {
        this.workGroupMetricRepository = workGroupMetricRepository;
        this.workGroupMetricMapper = workGroupMetricMapper;
    }

    /**
     * Save a workGroupMetric.
     *
     * @param workGroupMetricDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkGroupMetricDTO save(WorkGroupMetricDTO workGroupMetricDTO) {
        log.debug("Request to save WorkGroupMetric : {}", workGroupMetricDTO);
        WorkGroupMetric workGroupMetric = workGroupMetricMapper.toEntity(workGroupMetricDTO);
        workGroupMetric = workGroupMetricRepository.save(workGroupMetric);
        return workGroupMetricMapper.toDto(workGroupMetric);
    }

    /**
     * Get all the workGroupMetrics.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkGroupMetricDTO> findAll() {
        log.debug("Request to get all WorkGroupMetrics");
        return workGroupMetricRepository.findAll().stream()
            .map(workGroupMetricMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one workGroupMetric by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkGroupMetricDTO> findOne(Long id) {
        log.debug("Request to get WorkGroupMetric : {}", id);
        return workGroupMetricRepository.findById(id)
            .map(workGroupMetricMapper::toDto);
    }

    /**
     * Delete the workGroupMetric by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkGroupMetric : {}", id);
        workGroupMetricRepository.deleteById(id);
    }
}
