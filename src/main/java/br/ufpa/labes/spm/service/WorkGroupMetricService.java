package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.WorkWorkGroupMetric;
import br.ufpa.labes.spm.repository.WorkWorkGroupMetricRepository;
import br.ufpa.labes.spm.service.dto.WorkWorkGroupMetricDTO;
import br.ufpa.labes.spm.service.mapper.WorkWorkGroupMetricMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link WorkWorkGroupMetric}.
 */
@Service
@Transactional
public class WorkWorkGroupMetricService {

    private final Logger log = LoggerFactory.getLogger(WorkWorkGroupMetricService.class);

    private final WorkWorkGroupMetricRepository workWorkGroupMetricRepository;

    private final WorkWorkGroupMetricMapper workWorkGroupMetricMapper;

    public WorkWorkGroupMetricService(WorkWorkGroupMetricRepository workWorkGroupMetricRepository, WorkWorkGroupMetricMapper workWorkGroupMetricMapper) {
        this.workWorkGroupMetricRepository = workWorkGroupMetricRepository;
        this.workWorkGroupMetricMapper = workWorkGroupMetricMapper;
    }

    /**
     * Save a workWorkGroupMetric.
     *
     * @param workWorkGroupMetricDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkWorkGroupMetricDTO save(WorkWorkGroupMetricDTO workWorkGroupMetricDTO) {
        log.debug("Request to save WorkWorkGroupMetric : {}", workWorkGroupMetricDTO);
        WorkWorkGroupMetric workWorkGroupMetric = workWorkGroupMetricMapper.toEntity(workWorkGroupMetricDTO);
        workWorkGroupMetric = workWorkGroupMetricRepository.save(workWorkGroupMetric);
        return workWorkGroupMetricMapper.toDto(workWorkGroupMetric);
    }

    /**
     * Get all the workWorkGroupMetrics.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkWorkGroupMetricDTO> findAll() {
        log.debug("Request to get all WorkWorkGroupMetrics");
        return workWorkGroupMetricRepository.findAll().stream()
            .map(workWorkGroupMetricMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one workWorkGroupMetric by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkWorkGroupMetricDTO> findOne(Long id) {
        log.debug("Request to get WorkWorkGroupMetric : {}", id);
        return workWorkGroupMetricRepository.findById(id)
            .map(workWorkGroupMetricMapper::toDto);
    }

    /**
     * Delete the workWorkGroupMetric by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkWorkGroupMetric : {}", id);
        workWorkGroupMetricRepository.deleteById(id);
    }
}
