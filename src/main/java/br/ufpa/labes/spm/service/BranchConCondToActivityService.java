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

    private final BranchConCondToActivityRepository branchConCondToActivityRepository;

    private final BranchConCondToActivityMapper branchConCondToActivityMapper;

    public BranchConCondToActivityService(BranchConCondToActivityRepository branchConCondToActivityRepository, BranchConCondToActivityMapper branchConCondToActivityMapper) {
        this.branchConCondToActivityRepository = branchConCondToActivityRepository;
        this.branchConCondToActivityMapper = branchConCondToActivityMapper;
    }

    /**
     * Save a branchConCondToActivity.
     *
     * @param branchConCondToActivityDTO the entity to save.
     * @return the persisted entity.
     */
    public BranchConCondToActivityDTO save(BranchConCondToActivityDTO branchConCondToActivityDTO) {
        log.debug("Request to save BranchConCondToActivity : {}", branchConCondToActivityDTO);
        BranchConCondToActivity branchConCondToActivity = branchConCondToActivityMapper.toEntity(branchConCondToActivityDTO);
        branchConCondToActivity = branchConCondToActivityRepository.save(branchConCondToActivity);
        return branchConCondToActivityMapper.toDto(branchConCondToActivity);
    }

    /**
     * Get all the branchConCondToActivities.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BranchConCondToActivityDTO> findAll() {
        log.debug("Request to get all BranchConCondToActivities");
        return branchConCondToActivityRepository.findAll().stream()
            .map(branchConCondToActivityMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one branchConCondToActivity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BranchConCondToActivityDTO> findOne(Long id) {
        log.debug("Request to get BranchConCondToActivity : {}", id);
        return branchConCondToActivityRepository.findById(id)
            .map(branchConCondToActivityMapper::toDto);
    }

    /**
     * Delete the branchConCondToActivity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BranchConCondToActivity : {}", id);
        branchConCondToActivityRepository.deleteById(id);
    }
}
