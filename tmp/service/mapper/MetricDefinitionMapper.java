package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.MetricDefinitionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MetricDefinition} and its DTO {@link MetricDefinitionDTO}.
 */
@Mapper(componentModel = "spring", uses = {MetricTypeMapper.class})
public interface MetricDefinitionMapper extends EntityMapper<MetricDefinitionDTO, MetricDefinition> {

    @Mapping(source = "metricType.id", target = "metricTypeId")
    MetricDefinitionDTO toDto(MetricDefinition metricDefinition);

    @Mapping(target = "units", ignore = true)
    @Mapping(target = "removeUnits", ignore = true)
    @Mapping(source = "metricTypeId", target = "metricType")
    @Mapping(target = "estimations", ignore = true)
    @Mapping(target = "removeEstimation", ignore = true)
    @Mapping(target = "metrics", ignore = true)
    @Mapping(target = "removeMetric", ignore = true)
    MetricDefinition toEntity(MetricDefinitionDTO metricDefinitionDTO);

    default MetricDefinition fromId(Long id) {
        if (id == null) {
            return null;
        }
        MetricDefinition metricDefinition = new MetricDefinition();
        metricDefinition.setId(id);
        return metricDefinition;
    }
}
