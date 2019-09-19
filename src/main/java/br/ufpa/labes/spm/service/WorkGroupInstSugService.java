package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.WorkWorkGroupInstSug;
import br.ufpa.labes.spm.repository.WorkWorkGroupInstSugRepository;
import br.ufpa.labes.spm.service.dto.WorkWorkGroupInstSugDTO;
import br.ufpa.labes.spm.service.mapper.WorkWorkGroupInstSugMapper;
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
 * Service Implementation for managing {@link WorkWorkGroupInstSug}.
 */
@Service
@Transactional
public class WorkWorkGroupInstSugService {

    private final Logger log = LoggerFactory.getLogger(WorkWorkGroupInstSugService.class);

    private final WorkWorkGroupInstSugRepository workWorkGroupInstSugRepository;

    private final WorkWorkGroupInstSugMapper workWorkGroupInstSugMapper;

    public WorkWorkGroupInstSugService(WorkWorkGroupInstSugRepository workWorkGroupInstSugRepository, WorkWorkGroupInstSugMapper workWorkGroupInstSugMapper) {
        this.workWorkGroupInstSugRepository = workWorkGroupInstSugRepository;
        this.workWorkGroupInstSugMapper = workWorkGroupInstSugMapper;
    }

    /**
     * Save a workWorkGroupInstSug.
     *
     * @param workWorkGroupInstSugDTO the entity to save.
     * @return the persisted entity.
     */
    public WorkWorkGroupInstSugDTO save(WorkWorkGroupInstSugDTO workWorkGroupInstSugDTO) {
        log.debug("Request to save WorkWorkGroupInstSug : {}", workWorkGroupInstSugDTO);
        WorkWorkGroupInstSug workWorkGroupInstSug = workWorkGroupInstSugMapper.toEntity(workWorkGroupInstSugDTO);
        workWorkGroupInstSug = workWorkGroupInstSugRepository.save(workWorkGroupInstSug);
        return workWorkGroupInstSugMapper.toDto(workWorkGroupInstSug);
    }

    /**
     * Get all the workWorkGroupInstSugs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<WorkWorkGroupInstSugDTO> findAll() {
        log.debug("Request to get all WorkWorkGroupInstSugs");
        return workWorkGroupInstSugRepository.findAllWithEagerRelationships().stream()
            .map(workWorkGroupInstSugMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the workWorkGroupInstSugs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<WorkWorkGroupInstSugDTO> findAllWithEagerRelationships(Pageable pageable) {
        return workWorkGroupInstSugRepository.findAllWithEagerRelationships(pageable).map(workWorkGroupInstSugMapper::toDto);
    }


    /**
     * Get one workWorkGroupInstSug by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WorkWorkGroupInstSugDTO> findOne(Long id) {
        log.debug("Request to get WorkWorkGroupInstSug : {}", id);
        return workWorkGroupInstSugRepository.findOneWithEagerRelationships(id)
            .map(workWorkGroupInstSugMapper::toDto);
    }

    /**
     * Delete the workWorkGroupInstSug by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete WorkWorkGroupInstSug : {}", id);
        workWorkGroupInstSugRepository.deleteById(id);
    }
}
