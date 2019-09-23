package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.WorkGroupInstSug;
import br.ufpa.labes.spm.repository.WorkGroupInstSugRepository;
import br.ufpa.labes.spm.service.dto.WorkGroupInstSugDTO;
import br.ufpa.labes.spm.service.mapper.WorkGroupInstSugMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link WorkGroupInstSug}.
 */
@Service
@Transactional
public class WorkGroupInstSugService {

    private final Logger log = LoggerFactory.getLogger(WorkGroupInstSugService.class);

    private final WorkWorkGroupInstSugRepository WorkGroupInstSugRepository;

    private final WorkWorkGroupInstSugMapper WorkGroupInstSugMapper;

    public WorkWorkGroupInstSugService(WorkWorkGroupInstSugRepository workWorkGroupInstSugRepository, WorkWorkGroupInstSugMapper WorkGroupInstSugMapper) {
        this.workWorkGroupInstSugRepository = WorkGroupInstSugRepository;
        this.workWorkGroupInstSugMapper = WorkGroupInstSugMapper;
    }

    /**
     * Save a WorkGroupInstSug.
     *
     * @param WorkGroupInstSugDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkWorkGroupInstSugDTO save(WorkWorkGroupInstSugDTO WorkGroupInstSugDTO) {
        log.debug("Request to save WorkWorkGroupInstSug : {}", WorkGroupInstSugDTO);
        WorkWorkGroupInstSug workWorkGroupInstSug = workWorkGroupInstSugMapper.toEntity(WorkGroupInstSugDTO);
        workWorkGroupInstSug = workWorkGroupInstSugRepository.save(WorkGroupInstSug);
        return workWorkGroupInstSugMapper.toDto(WorkGroupInstSug);
    }

    /**
     * Get all the WorkGroupInstSugs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkGroupInstSugDTO> findAll() {
        log.debug("Request to get all WorkGroupInstSugs");
        return WorkGroupInstSugRepository.findAllWithEagerRelationships().stream()
            .map(WorkGroupInstSugMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the WorkGroupInstSugs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<WorkGroupInstSugDTO> findAllWithEagerRelationships(Pageable pageable) {
        return workWorkGroupInstSugRepository.findAllWithEagerRelationships(pageable).map(WorkGroupInstSugMapper::toDto);
    }


    /**
     * Get one WorkGroupInstSug by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkGroupInstSugDTO> findOne(Long id) {
        log.debug("Request to get WorkGroupInstSug : {}", id);
        return WorkGroupInstSugRepository.findOneWithEagerRelationships(id)
            .map(WorkGroupInstSugMapper::toDto);
    }

    /**
     * Delete the WorkGroupInstSug by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkGroupInstSug : {}", id);
        WorkGroupInstSugRepository.deleteById(id);
    }
}
