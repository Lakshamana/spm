package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.PrimitiveType;
import br.ufpa.labes.spm.repository.PrimitiveTypeRepository;
import br.ufpa.labes.spm.service.dto.PrimitiveTypeDTO;
import br.ufpa.labes.spm.service.mapper.PrimitiveTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PrimitiveType}.
 */
@Service
@Transactional
public class PrimitiveTypeService {

    private final Logger log = LoggerFactory.getLogger(PrimitiveTypeService.class);

    private final PrimitiveTypeRepository primitiveTypeRepository;

    private final PrimitiveTypeMapper primitiveTypeMapper;

    public PrimitiveTypeService(PrimitiveTypeRepository primitiveTypeRepository, PrimitiveTypeMapper primitiveTypeMapper) {
        this.primitiveTypeRepository = primitiveTypeRepository;
        this.primitiveTypeMapper = primitiveTypeMapper;
    }

    /**
     * Save a primitiveType.
     *
     * @param primitiveTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public PrimitiveTypeDTO save(PrimitiveTypeDTO primitiveTypeDTO) {
        log.debug("Request to save PrimitiveType : {}", primitiveTypeDTO);
        PrimitiveType primitiveType = primitiveTypeMapper.toEntity(primitiveTypeDTO);
        primitiveType = primitiveTypeRepository.save(primitiveType);
        return primitiveTypeMapper.toDto(primitiveType);
    }

    /**
     * Get all the primitiveTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PrimitiveTypeDTO> findAll() {
        log.debug("Request to get all PrimitiveTypes");
        return primitiveTypeRepository.findAll().stream()
            .map(primitiveTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one primitiveType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PrimitiveTypeDTO> findOne(Long id) {
        log.debug("Request to get PrimitiveType : {}", id);
        return primitiveTypeRepository.findById(id)
            .map(primitiveTypeMapper::toDto);
    }

    /**
     * Delete the primitiveType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PrimitiveType : {}", id);
        primitiveTypeRepository.deleteById(id);
    }
}
