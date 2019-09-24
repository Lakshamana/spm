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

    private final WorkWorkGroupMetricRepository WorkGroupMetricRepository;

    private final WorkWorkGroupMetricMapper WorkGroupMetricMapper;

    public WorkWorkGroupMetricService(WorkWorkGroupMetricRepository workWorkGroupMetricRepository, WorkWorkGroupMetricMapper WorkGroupMetricMapper) {
        this.workWorkGroupMetricRepository = WorkGroupMetricRepository;
        this.workWorkGroupMetricMapper = WorkGroupMetricMapper;
    }

    /**
     * Save a WorkGroupMetric.
     *
     * @param WorkGroupMetricDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkWorkGroupMetricDTO save(WorkWorkGroupMetricDTO WorkGroupMetricDTO) {
        log.debug("Request to save WorkWorkGroupMetric : {}", WorkGroupMetricDTO);
        WorkWorkGroupMetric workWorkGroupMetric = workWorkGroupMetricMapper.toEntity(WorkGroupMetricDTO);
        workWorkGroupMetric = workWorkGroupMetricRepository.save(WorkGroupMetric);
        return workWorkGroupMetricMapper.toDto(WorkGroupMetric);
    }

    /**
     * Get all the WorkGroupMetrics.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkGroupMetricDTO> findAll() {
        log.debug("Request to get all WorkGroupMetrics");
        return WorkGroupMetricRepository.findAll().stream()
            .map(WorkGroupMetricMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one WorkGroupMetric by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkGroupMetricDTO> findOne(Long id) {
        log.debug("Request to get WorkGroupMetric : {}", id);
        return WorkGroupMetricRepository.findById(id)
            .map(WorkGroupMetricMapper::toDto);
    }

    /**
     * Delete the WorkGroupMetric by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkGroupMetric : {}", id);
        WorkGroupMetricRepository.deleteById(id);
    }
}
