package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.WorkGroupEstimation;
import br.ufpa.labes.spm.repository.WorkGroupEstimationRepository;
import br.ufpa.labes.spm.service.dto.WorkGroupEstimationDTO;
import br.ufpa.labes.spm.service.mapper.WorkGroupEstimationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link WorkGroupEstimation}.
 */
@Service
@Transactional
public class WorkGroupEstimationService {

    private final Logger log = LoggerFactory.getLogger(WorkGroupEstimationService.class);

    private final WorkWorkGroupEstimationRepository WorkGroupEstimationRepository;

    private final WorkWorkGroupEstimationMapper WorkGroupEstimationMapper;

    public WorkWorkGroupEstimationService(WorkWorkGroupEstimationRepository workWorkGroupEstimationRepository, WorkWorkGroupEstimationMapper WorkGroupEstimationMapper) {
        this.workWorkGroupEstimationRepository = WorkGroupEstimationRepository;
        this.workWorkGroupEstimationMapper = WorkGroupEstimationMapper;
    }

    /**
     * Save a WorkGroupEstimation.
     *
     * @param WorkGroupEstimationDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkWorkGroupEstimationDTO save(WorkWorkGroupEstimationDTO WorkGroupEstimationDTO) {
        log.debug("Request to save WorkWorkGroupEstimation : {}", WorkGroupEstimationDTO);
        WorkWorkGroupEstimation workWorkGroupEstimation = workWorkGroupEstimationMapper.toEntity(WorkGroupEstimationDTO);
        workWorkGroupEstimation = workWorkGroupEstimationRepository.save(WorkGroupEstimation);
        return workWorkGroupEstimationMapper.toDto(WorkGroupEstimation);
    }

    /**
     * Get all the WorkGroupEstimations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkGroupEstimationDTO> findAll() {
        log.debug("Request to get all WorkGroupEstimations");
        return WorkGroupEstimationRepository.findAll().stream()
            .map(WorkGroupEstimationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one WorkGroupEstimation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkGroupEstimationDTO> findOne(Long id) {
        log.debug("Request to get WorkGroupEstimation : {}", id);
        return WorkGroupEstimationRepository.findById(id)
            .map(WorkGroupEstimationMapper::toDto);
    }

    /**
     * Delete the WorkGroupEstimation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkGroupEstimation : {}", id);
        WorkGroupEstimationRepository.deleteById(id);
    }
}
