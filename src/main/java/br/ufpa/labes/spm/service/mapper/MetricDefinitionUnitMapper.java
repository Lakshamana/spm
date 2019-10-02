package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.MetricDefinitionUnitDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link MetricDefinitionUnit} and its DTO {@link MetricDefinitionUnitDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {MetricDefinitionMapper.class})
public interface MetricDefinitionUnitMapper
    extends EntityMapper<MetricDefinitionUnitDTO, MetricDefinitionUnit> {

  @Mapping(source = "theMetricDefinition.id", target = "theMetricDefinitionId")
  MetricDefinitionUnitDTO toDto(MetricDefinitionUnit metricDefinitionUnit);

  @Mapping(source = "theMetricDefinitionId", target = "theMetricDefinition")
  MetricDefinitionUnit toEntity(MetricDefinitionUnitDTO metricDefinitionUnitDTO);

  default MetricDefinitionUnit fromId(Long id) {
    if (id == null) {
      return null;
    }
    MetricDefinitionUnit metricDefinitionUnit = new MetricDefinitionUnit();
    metricDefinitionUnit.setId(id);
    return metricDefinitionUnit;
  }
}
