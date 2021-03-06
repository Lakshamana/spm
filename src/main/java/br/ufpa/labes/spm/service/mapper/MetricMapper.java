package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.MetricDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Metric} and its DTO {@link MetricDTO}.
 */
@Mapper(componentModel = "spring", uses = {MetricDefinitionMapper.class})
public interface MetricMapper extends EntityMapper<MetricDTO, Metric> {

    @Mapping(source = "metricDefinition.id", target = "metricDefinitionId")
    MetricDTO toDto(Metric metric);

    @Mapping(source = "metricDefinitionId", target = "metricDefinition")
    Metric toEntity(MetricDTO metricDTO);

    default Metric fromId(Long id) {
        if (id == null) {
            return null;
        }
        Metric metric = new Metric();
        metric.setId(id);
        return metric;
    }
}
