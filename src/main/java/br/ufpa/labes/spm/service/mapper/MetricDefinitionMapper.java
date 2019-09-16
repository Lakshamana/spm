package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.MetricDefinitionDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link MetricDefinition} and its DTO {@link MetricDefinitionDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {MetricTypeMapper.class})
public interface MetricDefinitionMapper
    extends EntityMapper<MetricDefinitionDTO, MetricDefinition> {

  @Mapping(source = "theMetricType.id", target = "theMetricTypeId")
  MetricDefinitionDTO toDto(MetricDefinition metricDefinition);

  @Mapping(target = "units", ignore = true)
  @Mapping(target = "removeUnits", ignore = true)
  @Mapping(source = "theMetricTypeId", target = "theMetricType")
  @Mapping(target = "theEstimations", ignore = true)
  @Mapping(target = "removeTheEstimation", ignore = true)
  @Mapping(target = "theMetrics", ignore = true)
  @Mapping(target = "removeTheMetric", ignore = true)
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
