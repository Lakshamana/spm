package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.PrimitiveParam;
import br.ufpa.labes.spm.repository.PrimitiveParamRepository;
import br.ufpa.labes.spm.service.dto.PrimitiveParamDTO;
import br.ufpa.labes.spm.service.mapper.PrimitiveParamMapper;
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
 * Service Implementation for managing {@link PrimitiveParam}.
 */
@Service
@Transactional
public class PrimitiveParamService {

    private final Logger log = LoggerFactory.getLogger(PrimitiveParamService.class);

    private final PrimitiveParamRepository primitiveParamRepository;

    private final PrimitiveParamMapper primitiveParamMapper;

    public PrimitiveParamService(PrimitiveParamRepository primitiveParamRepository, PrimitiveParamMapper primitiveParamMapper) {
        this.primitiveParamRepository = primitiveParamRepository;
        this.primitiveParamMapper = primitiveParamMapper;
    }

    /**
     * Save a primitiveParam.
     *
     * @param primitiveParamDTO the entity to save.
     * @return the persisted entity.
     */
    public PrimitiveParamDTO save(PrimitiveParamDTO primitiveParamDTO) {
        log.debug("Request to save PrimitiveParam : {}", primitiveParamDTO);
        PrimitiveParam primitiveParam = primitiveParamMapper.toEntity(primitiveParamDTO);
        primitiveParam = primitiveParamRepository.save(primitiveParam);
        return primitiveParamMapper.toDto(primitiveParam);
    }

    /**
     * Get all the primitiveParams.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PrimitiveParamDTO> findAll() {
        log.debug("Request to get all PrimitiveParams");
        return primitiveParamRepository.findAll().stream()
            .map(primitiveParamMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the primitiveParams where TheParameterSuper is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<PrimitiveParamDTO> findAllWhereTheParameterSuperIsNull() {
        log.debug("Request to get all primitiveParams where TheParameterSuper is null");
        return StreamSupport
            .stream(primitiveParamRepository.findAll().spliterator(), false)
            .filter(primitiveParam -> primitiveParam.getTheParameterSuper() == null)
            .map(primitiveParamMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one primitiveParam by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PrimitiveParamDTO> findOne(Long id) {
        log.debug("Request to get PrimitiveParam : {}", id);
        return primitiveParamRepository.findById(id)
            .map(primitiveParamMapper::toDto);
    }

    /**
     * Delete the primitiveParam by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PrimitiveParam : {}", id);
        primitiveParamRepository.deleteById(id);
    }
}
