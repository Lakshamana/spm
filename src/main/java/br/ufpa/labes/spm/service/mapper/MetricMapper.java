package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.MetricDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Metric} and its DTO {@link MetricDTO}.
 */
@Mapper(componentModel = "spring", uses = {MetricDefinitionMapper.class, ActivityMapper.class})
public interface MetricMapper extends EntityMapper<MetricDTO, Metric> {

    @Mapping(source = "metricDefinition.id", target = "metricDefinitionId")
    @Mapping(source = "activity.id", target = "activityId")
    MetricDTO toDto(Metric metric);

    @Mapping(source = "metricDefinitionId", target = "metricDefinition")
    @Mapping(source = "activityId", target = "activity")
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
