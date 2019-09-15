package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.BranchCondToMultipleCon;
import br.ufpa.labes.spm.repository.BranchCondToMultipleConRepository;
import br.ufpa.labes.spm.service.dto.BranchCondToMultipleConDTO;
import br.ufpa.labes.spm.service.mapper.BranchCondToMultipleConMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link BranchCondToMultipleCon}.
 */
@Service
@Transactional
public class BranchCondToMultipleConService {

    private final Logger log = LoggerFactory.getLogger(BranchCondToMultipleConService.class);

    private final BranchCondToMultipleConRepository branchCondToMultipleConRepository;

    private final BranchCondToMultipleConMapper branchCondToMultipleConMapper;

    public BranchCondToMultipleConService(BranchCondToMultipleConRepository branchCondToMultipleConRepository, BranchCondToMultipleConMapper branchCondToMultipleConMapper) {
        this.branchCondToMultipleConRepository = branchCondToMultipleConRepository;
        this.branchCondToMultipleConMapper = branchCondToMultipleConMapper;
    }

    /**
     * Save a branchCondToMultipleCon.
     *
     * @param branchCondToMultipleConDTO the entity to save.
     * @return the persisted entity.
     */
    public BranchCondToMultipleConDTO save(BranchCondToMultipleConDTO branchCondToMultipleConDTO) {
        log.debug("Request to save BranchCondToMultipleCon : {}", branchCondToMultipleConDTO);
        BranchCondToMultipleCon branchCondToMultipleCon = branchCondToMultipleConMapper.toEntity(branchCondToMultipleConDTO);
        branchCondToMultipleCon = branchCondToMultipleConRepository.save(branchCondToMultipleCon);
        return branchCondToMultipleConMapper.toDto(branchCondToMultipleCon);
    }

    /**
     * Get all the branchCondToMultipleCons.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BranchCondToMultipleConDTO> findAll() {
        log.debug("Request to get all BranchCondToMultipleCons");
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
    public Optional<BranchCondToMultipleConDTO> findOne(Long id) {
        log.debug("Request to get BranchCondToMultipleCon : {}", id);
        return branchCondToMultipleConRepository.findById(id)
            .map(branchCondToMultipleConMapper::toDto);
    }

    /**
     * Delete the branchCondToMultipleCon by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete BranchCondToMultipleCon : {}", id);
        branchCondToMultipleConRepository.deleteById(id);
    }
}
