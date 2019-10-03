package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.MetricTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MetricType} and its DTO {@link MetricTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MetricTypeMapper extends EntityMapper<MetricTypeDTO, MetricType> {


    @Mapping(target = "theMetricDefinitions", ignore = true)
    @Mapping(target = "removeTheMetricDefinition", ignore = true)
    MetricType toEntity(MetricTypeDTO metricTypeDTO);

    default MetricType fromId(Long id) {
        if (id == null) {
            return null;
        }
        MetricType metricType = new MetricType();
        metricType.setId(id);
        return metricType;
    }
}
