package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ReqWorkGroup;
import br.ufpa.labes.spm.repository.ReqWorkGroupRepository;
import br.ufpa.labes.spm.service.dto.ReqWorkGroupDTO;
import br.ufpa.labes.spm.service.mapper.ReqWorkGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ReqWorkGroup}.
 */
@Service
@Transactional
public class ReqWorkGroupService {

    private final Logger log = LoggerFactory.getLogger(ReqWorkGroupService.class);

    private final ReqWorkWorkGroupRepository reqWorkGroupRepository;

    private final ReqWorkWorkGroupMapper reqWorkGroupMapper;

    public ReqWorkWorkGroupService(ReqWorkWorkGroupRepository reqWorkWorkGroupRepository, ReqWorkWorkGroupMapper reqWorkGroupMapper) {
        this.reqWorkWorkGroupRepository = reqWorkGroupRepository;
        this.reqWorkWorkGroupMapper = reqWorkGroupMapper;
    }

    /**
     * Save a reqWorkGroup.
     *
     * @param reqWorkGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public ReqWorkWorkGroupDTO save(ReqWorkWorkGroupDTO reqWorkGroupDTO) {
        log.debug("Request to save ReqWorkWorkGroup : {}", reqWorkGroupDTO);
        ReqWorkWorkGroup reqWorkWorkGroup = reqWorkWorkGroupMapper.toEntity(reqWorkGroupDTO);
        reqWorkWorkGroup = reqWorkWorkGroupRepository.save(reqWorkGroup);
        return reqWorkWorkGroupMapper.toDto(reqWorkGroup);
    }

    /**
     * Get all the reqWorkGroups.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReqWorkGroupDTO> findAll() {
        log.debug("Request to get all ReqWorkGroups");
        return reqWorkGroupRepository.findAll().stream()
            .map(reqWorkGroupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one reqWorkGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReqWorkGroupDTO> findOne(Long id) {
        log.debug("Request to get ReqWorkGroup : {}", id);
        return reqWorkGroupRepository.findById(id)
            .map(reqWorkGroupMapper::toDto);
    }

    /**
     * Delete the reqWorkGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ReqWorkGroup : {}", id);
        reqWorkGroupRepository.deleteById(id);
    }
}
