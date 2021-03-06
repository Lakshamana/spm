package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ProcessMetricDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProcessMetric} and its DTO {@link ProcessMetricDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProcessMapper.class})
public interface ProcessMetricMapper extends EntityMapper<ProcessMetricDTO, ProcessMetric> {

    @Mapping(source = "process.id", target = "processId")
    ProcessMetricDTO toDto(ProcessMetric processMetric);

    @Mapping(source = "processId", target = "process")
    ProcessMetric toEntity(ProcessMetricDTO processMetricDTO);

    default ProcessMetric fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProcessMetric processMetric = new ProcessMetric();
        processMetric.setId(id);
        return processMetric;
    }
}
