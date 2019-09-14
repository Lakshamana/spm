package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.BranchCon;
import br.ufpa.labes.spm.repository.BranchConRepository;
import br.ufpa.labes.spm.service.dto.BranchConDTO;
import br.ufpa.labes.spm.service.mapper.BranchConMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing {@link BranchCon}.
 */
@Service
@Transactional
public class BranchConService {

    private final Logger log = LoggerFactory.getLogger(BranchConService.class);

    private final BranchConRepository branchConRepository;

    private final BranchConMapper branchConMapper;

    public BranchConService(BranchConRepository branchConRepository, BranchConMapper branchConMapper) {
        this.branchConRepository = branchConRepository;
        this.branchConMapper = branchConMapper;
    }

    /**
     * Save a branchCon.
     *
     * @param branchConDTO the entity to save.
     * @return the persisted entity.
     */
    public BranchConDTO save(BranchConDTO branchConDTO) {
        log.debug("Request to save BranchCon : {}", branchConDTO);
        BranchCon branchCon = branchConMapper.toEntity(branchConDTO);
        branchCon = branchConRepository.save(branchCon);
        return branchConMapper.toDto(branchCon);
    }

    /**
     * Get all the branchCons.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BranchConDTO> findAll() {
        log.debug("Request to get all BranchCons");
        return branchConRepository.findAll().stream()
            .map(branchConMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the branchCons where TheMultipleConSuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<BranchConDTO> findAllWhereTheMultipleConSuperIsNull() {
        log.debug("Request to get all branchCons where TheMultipleConSuper is null");
        return StreamSupport
            .stream(branchConRepository.findAll().spliterator(), false)
            .filter(branchCon -> branchCon.getTheMultipleConSuper() == null)
            .map(branchConMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one branchCon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BranchConDTO> findOne(Long id) {
        log.debug("Request to get BranchCon : {}", id);
        return branchConRepository.findById(id)
            .map(branchConMapper::toDto);
    }

    /**
     * Delete the branchCon by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BranchCon : {}", id);
        branchConRepository.deleteById(id);
    }
}
