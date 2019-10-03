package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Structure;
import br.ufpa.labes.spm.repository.StructureRepository;
import br.ufpa.labes.spm.service.dto.StructureDTO;
import br.ufpa.labes.spm.service.mapper.StructureMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Structure}.
 */
@Service
@Transactional
public class StructureService {

    private final Logger log = LoggerFactory.getLogger(StructureService.class);

    private final StructureRepository structureRepository;

    private final StructureMapper structureMapper;

    public StructureService(StructureRepository structureRepository, StructureMapper structureMapper) {
        this.structureRepository = structureRepository;
        this.structureMapper = structureMapper;
    }

    /**
     * Save a structure.
     *
     * @param structureDTO the entity to save.
     * @return the persisted entity.
     */
    public StructureDTO save(StructureDTO structureDTO) {
        log.debug("Request to save Structure : {}", structureDTO);
        Structure structure = structureMapper.toEntity(structureDTO);
        structure = structureRepository.save(structure);
        return structureMapper.toDto(structure);
    }

    /**
     * Get all the structures.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<StructureDTO> findAll() {
        log.debug("Request to get all Structures");
        return structureRepository.findAll().stream()
            .map(structureMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one structure by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StructureDTO> findOne(Long id) {
        log.debug("Request to get Structure : {}", id);
        return structureRepository.findById(id)
            .map(structureMapper::toDto);
    }

    /**
     * Delete the structure by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Structure : {}", id);
        structureRepository.deleteById(id);
    }
}
