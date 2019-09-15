package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.BranchCondToActivity;
import br.ufpa.labes.spm.repository.BranchCondToActivityRepository;
import br.ufpa.labes.spm.service.dto.BranchCondToActivityDTO;
import br.ufpa.labes.spm.service.mapper.BranchCondToActivityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link BranchCondToActivity}.
 */
@Service
@Transactional
public class BranchCondToActivityService {

    private final Logger log = LoggerFactory.getLogger(BranchCondToActivityService.class);

    private final BranchCondToActivityRepository branchCondToActivityRepository;

    private final BranchCondToActivityMapper branchCondToActivityMapper;

    public BranchCondToActivityService(BranchCondToActivityRepository branchCondToActivityRepository, BranchCondToActivityMapper branchCondToActivityMapper) {
        this.branchCondToActivityRepository = branchCondToActivityRepository;
        this.branchCondToActivityMapper = branchCondToActivityMapper;
    }

    /**
     * Save a branchCondToActivity.
     *
     * @param branchCondToActivityDTO the entity to save.
     * @return the persisted entity.
     */
    public BranchCondToActivityDTO save(BranchCondToActivityDTO branchCondToActivityDTO) {
        log.debug("Request to save BranchCondToActivity : {}", branchCondToActivityDTO);
        BranchCondToActivity branchCondToActivity = branchCondToActivityMapper.toEntity(branchCondToActivityDTO);
        branchCondToActivity = branchCondToActivityRepository.save(branchCondToActivity);
        return branchCondToActivityMapper.toDto(branchCondToActivity);
    }

    /**
     * Get all the branchCondToActivities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BranchCondToActivityDTO> findAll() {
        log.debug("Request to get all BranchCondToActivities");
        return branchCondToActivityRepository.findAll().stream()
            .map(branchCondToActivityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one branchCondToActivity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BranchCondToActivityDTO> findOne(Long id) {
        log.debug("Request to get BranchCondToActivity : {}", id);
        return branchCondToActivityRepository.findById(id)
            .map(branchCondToActivityMapper::toDto);
    }

    /**
     * Delete the branchCondToActivity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BranchCondToActivity : {}", id);
        branchCondToActivityRepository.deleteById(id);
    }
}
