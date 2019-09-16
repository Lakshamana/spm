package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.BranchConCondToMultipleCon;
import br.ufpa.labes.spm.repository.BranchConCondToMultipleConRepository;
import br.ufpa.labes.spm.service.dto.BranchConCondToMultipleConDTO;
import br.ufpa.labes.spm.service.mapper.BranchConCondToMultipleConMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link BranchConCondToMultipleCon}.
 */
@Service
@Transactional
public class BranchConCondToMultipleConService {

    private final Logger log = LoggerFactory.getLogger(BranchConCondToMultipleConService.class);

    private final BranchConCondToMultipleConRepository branchCondToMultipleConRepository;

    private final BranchConCondToMultipleConMapper branchCondToMultipleConMapper;

    public BranchConCondToMultipleConService(BranchConCondToMultipleConRepository branchCondToMultipleConRepository, BranchConCondToMultipleConMapper branchCondToMultipleConMapper) {
        this.branchCondToMultipleConRepository = branchCondToMultipleConRepository;
        this.branchCondToMultipleConMapper = branchCondToMultipleConMapper;
    }

    /**
     * Save a branchCondToMultipleCon.
     *
     * @param branchCondToMultipleConDTO the entity to save.
     * @return the persisted entity.
     */
    public BranchConCondToMultipleConDTO save(BranchConCondToMultipleConDTO branchCondToMultipleConDTO) {
        log.debug("Request to save BranchConCondToMultipleCon : {}", branchCondToMultipleConDTO);
        BranchConCondToMultipleCon branchCondToMultipleCon = branchCondToMultipleConMapper.toEntity(branchCondToMultipleConDTO);
        branchCondToMultipleCon = branchCondToMultipleConRepository.save(branchCondToMultipleCon);
        return branchCondToMultipleConMapper.toDto(branchCondToMultipleCon);
    }

    /**
     * Get all the branchCondToMultipleCons.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BranchConCondToMultipleConDTO> findAll() {
        log.debug("Request to get all BranchConCondToMultipleCons");
        return branchCondToMultipleConRepository.findAll().stream()
            .map(branchCondToMultipleConMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one branchCondToMultipleCon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BranchConCondToMultipleConDTO> findOne(Long id) {
        log.debug("Request to get BranchConCondToMultipleCon : {}", id);
        return branchCondToMultipleConRepository.findById(id)
            .map(branchCondToMultipleConMapper::toDto);
    }

    /**
     * Delete the branchCondToMultipleCon by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BranchConCondToMultipleCon : {}", id);
        branchCondToMultipleConRepository.deleteById(id);
    }
}