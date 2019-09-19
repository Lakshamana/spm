package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.WorkWorkGroupEstimation;
import br.ufpa.labes.spm.repository.WorkWorkGroupEstimationRepository;
import br.ufpa.labes.spm.service.dto.WorkWorkGroupEstimationDTO;
import br.ufpa.labes.spm.service.mapper.WorkWorkGroupEstimationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link WorkWorkGroupEstimation}.
 */
@Service
@Transactional
public class WorkWorkGroupEstimationService {

    private final Logger log = LoggerFactory.getLogger(WorkWorkGroupEstimationService.class);

    private final WorkWorkGroupEstimationRepository workWorkGroupEstimationRepository;

    private final WorkWorkGroupEstimationMapper workWorkGroupEstimationMapper;

    public WorkWorkGroupEstimationService(WorkWorkGroupEstimationRepository workWorkGroupEstimationRepository, WorkWorkGroupEstimationMapper workWorkGroupEstimationMapper) {
        this.workWorkGroupEstimationRepository = workWorkGroupEstimationRepository;
        this.workWorkGroupEstimationMapper = workWorkGroupEstimationMapper;
    }

    /**
     * Save a workWorkGroupEstimation.
     *
     * @param workWorkGroupEstimationDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkWorkGroupEstimationDTO save(WorkWorkGroupEstimationDTO workWorkGroupEstimationDTO) {
        log.debug("Request to save WorkWorkGroupEstimation : {}", workWorkGroupEstimationDTO);
        WorkWorkGroupEstimation workWorkGroupEstimation = workWorkGroupEstimationMapper.toEntity(workWorkGroupEstimationDTO);
        workWorkGroupEstimation = workWorkGroupEstimationRepository.save(workWorkGroupEstimation);
        return workWorkGroupEstimationMapper.toDto(workWorkGroupEstimation);
    }

    /**
     * Get all the workWorkGroupEstimations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkWorkGroupEstimationDTO> findAll() {
        log.debug("Request to get all WorkWorkGroupEstimations");
        return workWorkGroupEstimationRepository.findAll().stream()
            .map(workWorkGroupEstimationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one workWorkGroupEstimation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkWorkGroupEstimationDTO> findOne(Long id) {
        log.debug("Request to get WorkWorkGroupEstimation : {}", id);
        return workWorkGroupEstimationRepository.findById(id)
            .map(workWorkGroupEstimationMapper::toDto);
    }

    /**
     * Delete the workWorkGroupEstimation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkWorkGroupEstimation : {}", id);
        workWorkGroupEstimationRepository.deleteById(id);
    }
}
