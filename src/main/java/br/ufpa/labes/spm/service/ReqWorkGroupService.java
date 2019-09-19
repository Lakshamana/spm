package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.ReqWorkWorkGroup;
import br.ufpa.labes.spm.repository.ReqWorkWorkGroupRepository;
import br.ufpa.labes.spm.service.dto.ReqWorkWorkGroupDTO;
import br.ufpa.labes.spm.service.mapper.ReqWorkWorkGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ReqWorkWorkGroup}.
 */
@Service
@Transactional
public class ReqWorkWorkGroupService {

    private final Logger log = LoggerFactory.getLogger(ReqWorkWorkGroupService.class);

    private final ReqWorkWorkGroupRepository reqWorkWorkGroupRepository;

    private final ReqWorkWorkGroupMapper reqWorkWorkGroupMapper;

    public ReqWorkWorkGroupService(ReqWorkWorkGroupRepository reqWorkWorkGroupRepository, ReqWorkWorkGroupMapper reqWorkWorkGroupMapper) {
        this.reqWorkWorkGroupRepository = reqWorkWorkGroupRepository;
        this.reqWorkWorkGroupMapper = reqWorkWorkGroupMapper;
    }

    /**
     * Save a reqWorkWorkGroup.
     *
     * @param reqWorkWorkGroupDTO the entity to save.
     * @return the persisted entity.
     */
    public ReqWorkWorkGroupDTO save(ReqWorkWorkGroupDTO reqWorkWorkGroupDTO) {
        log.debug("Request to save ReqWorkWorkGroup : {}", reqWorkWorkGroupDTO);
        ReqWorkWorkGroup reqWorkWorkGroup = reqWorkWorkGroupMapper.toEntity(reqWorkWorkGroupDTO);
        reqWorkWorkGroup = reqWorkWorkGroupRepository.save(reqWorkWorkGroup);
        return reqWorkWorkGroupMapper.toDto(reqWorkWorkGroup);
    }

    /**
     * Get all the reqWorkWorkGroups.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ReqWorkWorkGroupDTO> findAll() {
        log.debug("Request to get all ReqWorkWorkGroups");
        return reqWorkWorkGroupRepository.findAll().stream()
            .map(reqWorkWorkGroupMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one reqWorkWorkGroup by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReqWorkWorkGroupDTO> findOne(Long id) {
        log.debug("Request to get ReqWorkWorkGroup : {}", id);
        return reqWorkWorkGroupRepository.findById(id)
            .map(reqWorkWorkGroupMapper::toDto);
    }

    /**
     * Delete the reqWorkWorkGroup by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ReqWorkWorkGroup : {}", id);
        reqWorkWorkGroupRepository.deleteById(id);
    }
}
