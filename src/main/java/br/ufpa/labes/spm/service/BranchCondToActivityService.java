package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.BranchConCondToActivity;
import br.ufpa.labes.spm.repository.BranchConCondToActivityRepository;
import br.ufpa.labes.spm.service.dto.BranchConCondToActivityDTO;
import br.ufpa.labes.spm.service.mapper.BranchConCondToActivityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link BranchConCondToActivity}.
 */
@Service
@Transactional
public class BranchConCondToActivityService {

    private final Logger log = LoggerFactory.getLogger(BranchConCondToActivityService.class);

    private final BranchConCondToActivityRepository branchCondToActivityRepository;

    private final BranchConCondToActivityMapper branchCondToActivityMapper;

    public BranchConCondToActivityService(BranchConCondToActivityRepository branchCondToActivityRepository, BranchConCondToActivityMapper branchCondToActivityMapper) {
        this.branchCondToActivityRepository = branchCondToActivityRepository;
        this.branchCondToActivityMapper = branchCondToActivityMapper;
    }

    /**
     * Save a branchCondToActivity.
     *
     * @param branchCondToActivityDTO the entity to save.
     * @return the persisted entity.
     */
    public BranchConCondToActivityDTO save(BranchConCondToActivityDTO branchCondToActivityDTO) {
        log.debug("Request to save BranchConCondToActivity : {}", branchCondToActivityDTO);
        BranchConCondToActivity branchCondToActivity = branchCondToActivityMapper.toEntity(branchCondToActivityDTO);
        branchCondToActivity = branchCondToActivityRepository.save(branchCondToActivity);
        return branchCondToActivityMapper.toDto(branchCondToActivity);
    }

    /**
     * Get all the branchCondToActivities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BranchConCondToActivityDTO> findAll() {
        log.debug("Request to get all BranchConCondToActivities");
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
    public Optional<BranchConCondToActivityDTO> findOne(Long id) {
        log.debug("Request to get BranchConCondToActivity : {}", id);
        return branchCondToActivityRepository.findById(id)
            .map(branchCondToActivityMapper::toDto);
    }

    /**
     * Delete the branchCondToActivity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BranchConCondToActivity : {}", id);
        branchCondToActivityRepository.deleteById(id);
    }
}