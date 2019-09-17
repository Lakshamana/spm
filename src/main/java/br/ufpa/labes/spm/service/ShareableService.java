package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Shareable;
import br.ufpa.labes.spm.repository.ShareableRepository;
import br.ufpa.labes.spm.service.dto.ShareableDTO;
import br.ufpa.labes.spm.service.mapper.ShareableMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Shareable}.
 */
@Service
@Transactional
public class ShareableService {

    private final Logger log = LoggerFactory.getLogger(ShareableService.class);

    private final ShareableRepository shareableRepository;

    private final ShareableMapper shareableMapper;

    public ShareableService(ShareableRepository shareableRepository, ShareableMapper shareableMapper) {
        this.shareableRepository = shareableRepository;
        this.shareableMapper = shareableMapper;
    }

    /**
     * Save a shareable.
     *
     * @param shareableDTO the entity to save.
     * @return the persisted entity.
     */
    public ShareableDTO save(ShareableDTO shareableDTO) {
        log.debug("Request to save Shareable : {}", shareableDTO);
        Shareable shareable = shareableMapper.toEntity(shareableDTO);
        shareable = shareableRepository.save(shareable);
        return shareableMapper.toDto(shareable);
    }

    /**
     * Get all the shareables.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ShareableDTO> findAll() {
        log.debug("Request to get all Shareables");
        return shareableRepository.findAll().stream()
            .map(shareableMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one shareable by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShareableDTO> findOne(Long id) {
        log.debug("Request to get Shareable : {}", id);
        return shareableRepository.findById(id)
            .map(shareableMapper::toDto);
    }

    /**
     * Delete the shareable by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Shareable : {}", id);
        shareableRepository.deleteById(id);
    }
}
