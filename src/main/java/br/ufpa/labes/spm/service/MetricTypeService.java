package br.ufpa.labes.spm.service;

import br.ufpa.labes.spm.domain.MetricType;
import br.ufpa.labes.spm.repository.MetricTypeRepository;
import br.ufpa.labes.spm.service.dto.MetricTypeDTO;
import br.ufpa.labes.spm.service.mapper.MetricTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MetricType}.
 */
@Service
@Transactional
public class MetricTypeService {

    private final Logger log = LoggerFactory.getLogger(MetricTypeService.class);

    private final MetricTypeRepository metricTypeRepository;

    private final MetricTypeMapper metricTypeMapper;

    public MetricTypeService(MetricTypeRepository metricTypeRepository, MetricTypeMapper metricTypeMapper) {
        this.metricTypeRepository = metricTypeRepository;
        this.metricTypeMapper = metricTypeMapper;
    }

    /**
     * Save a metricType.
     *
     * @param metricTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public MetricTypeDTO save(MetricTypeDTO metricTypeDTO) {
        log.debug("Request to save MetricType : {}", metricTypeDTO);
        MetricType metricType = metricTypeMapper.toEntity(metricTypeDTO);
        metricType = metricTypeRepository.save(metricType);
        return metricTypeMapper.toDto(metricType);
    }

    /**
     * Get all the metricTypes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MetricTypeDTO> findAll() {
        log.debug("Request to get all MetricTypes");
        return metricTypeRepository.findAll().stream()
            .map(metricTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one metricType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MetricTypeDTO> findOne(Long id) {
        log.debug("Request to get MetricType : {}", id);
        return metricTypeRepository.findById(id)
            .map(metricTypeMapper::toDto);
    }

    /**
     * Delete the metricType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MetricType : {}", id);
        metricTypeRepository.deleteById(id);
    }
}
