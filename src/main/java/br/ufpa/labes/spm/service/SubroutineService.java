package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.Subroutine;
import br.ufpa.labes.spm.repository.SubroutineRepository;
import br.ufpa.labes.spm.service.dto.SubroutineDTO;
import br.ufpa.labes.spm.service.mapper.SubroutineMapper;
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
 * Service Implementation for managing {@link Subroutine}.
 */
@Service
@Transactional
public class SubroutineService {

    private final Logger log = LoggerFactory.getLogger(SubroutineService.class);

    private final SubroutineRepository subroutineRepository;

    private final SubroutineMapper subroutineMapper;

    public SubroutineService(SubroutineRepository subroutineRepository, SubroutineMapper subroutineMapper) {
        this.subroutineRepository = subroutineRepository;
        this.subroutineMapper = subroutineMapper;
    }

    /**
     * Save a subroutine.
     *
     * @param subroutineDTO the entity to save.
     * @return the persisted entity.
     */
    public SubroutineDTO save(SubroutineDTO subroutineDTO) {
        log.debug("Request to save Subroutine : {}", subroutineDTO);
        Subroutine subroutine = subroutineMapper.toEntity(subroutineDTO);
        subroutine = subroutineRepository.save(subroutine);
        return subroutineMapper.toDto(subroutine);
    }

    /**
     * Get all the subroutines.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SubroutineDTO> findAll() {
        log.debug("Request to get all Subroutines");
        return subroutineRepository.findAll().stream()
            .map(subroutineMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }



    /**
    *  Get all the subroutines where TheAutomaticActivity is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<SubroutineDTO> findAllWhereTheAutomaticActivityIsNull() {
        log.debug("Request to get all subroutines where TheAutomaticActivity is null");
        return StreamSupport
            .stream(subroutineRepository.findAll().spliterator(), false)
            .filter(subroutine -> subroutine.getTheAutomaticActivity() == null)
            .map(subroutineMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one subroutine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SubroutineDTO> findOne(Long id) {
        log.debug("Request to get Subroutine : {}", id);
        return subroutineRepository.findById(id)
            .map(subroutineMapper::toDto);
    }

    /**
     * Delete the subroutine by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Subroutine : {}", id);
        subroutineRepository.deleteById(id);
    }
}
