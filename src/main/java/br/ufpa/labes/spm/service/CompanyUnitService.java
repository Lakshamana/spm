package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.CompanyUnit;
import br.ufpa.labes.spm.repository.CompanyUnitRepository;
import br.ufpa.labes.spm.service.dto.CompanyUnitDTO;
import br.ufpa.labes.spm.service.mapper.CompanyUnitMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CompanyUnit}.
 */
@Service
@Transactional
public class CompanyUnitService {

    private final Logger log = LoggerFactory.getLogger(CompanyUnitService.class);

    private final CompanyUnitRepository companyUnitRepository;

    private final CompanyUnitMapper companyUnitMapper;

    public CompanyUnitService(CompanyUnitRepository companyUnitRepository, CompanyUnitMapper companyUnitMapper) {
        this.companyUnitRepository = companyUnitRepository;
        this.companyUnitMapper = companyUnitMapper;
    }

    /**
     * Save a companyUnit.
     *
     * @param companyUnitDTO the entity to save.
     * @return the persisted entity.
     */
    public CompanyUnitDTO save(CompanyUnitDTO companyUnitDTO) {
        log.debug("Request to save CompanyUnit : {}", companyUnitDTO);
        CompanyUnit companyUnit = companyUnitMapper.toEntity(companyUnitDTO);
        companyUnit = companyUnitRepository.save(companyUnit);
        return companyUnitMapper.toDto(companyUnit);
    }

    /**
     * Get all the companyUnits.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CompanyUnitDTO> findAll() {
        log.debug("Request to get all CompanyUnits");
        return companyUnitRepository.findAll().stream()
            .map(companyUnitMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one companyUnit by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompanyUnitDTO> findOne(Long id) {
        log.debug("Request to get CompanyUnit : {}", id);
        return companyUnitRepository.findById(id)
            .map(companyUnitMapper::toDto);
    }

    /**
     * Delete the companyUnit by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CompanyUnit : {}", id);
        companyUnitRepository.deleteById(id);
    }
}
