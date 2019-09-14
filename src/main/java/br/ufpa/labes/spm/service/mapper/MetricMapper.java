package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.MetricDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Metric} and its DTO {@link MetricDTO}.
 */
@Mapper(componentModel = "spring", uses = {ActivityMetricMapper.class, AgentMetricMapper.class, ArtifactMetricMapper.class, WorkGroupMetricMapper.class, OrganizationMetricMapper.class, ProcessMetricMapper.class, ResourceMetricMapper.class, MetricDefinitionMapper.class, ActivityMapper.class})
public interface MetricMapper extends EntityMapper<MetricDTO, Metric> {

    @Mapping(source = "theActivityMetricSub.id", target = "theActivityMetricSubId")
    @Mapping(source = "theAgentMetricSub.id", target = "theAgentMetricSubId")
    @Mapping(source = "theArtifactMetricSub.id", target = "theArtifactMetricSubId")
    @Mapping(source = "theWorkGroupMetricSub.id", target = "theWorkGroupMetricSubId")
    @Mapping(source = "theOrganizationMetricSub.id", target = "theOrganizationMetricSubId")
    @Mapping(source = "theProcessMetricSub.id", target = "theProcessMetricSubId")
    @Mapping(source = "theResourceMetricSub.id", target = "theResourceMetricSubId")
    @Mapping(source = "theMetricDefinition.id", target = "theMetricDefinitionId")
    @Mapping(source = "theActivity.id", target = "theActivityId")
    MetricDTO toDto(Metric metric);

    @Mapping(source = "theActivityMetricSubId", target = "theActivityMetricSub")
    @Mapping(source = "theAgentMetricSubId", target = "theAgentMetricSub")
    @Mapping(source = "theArtifactMetricSubId", target = "theArtifactMetricSub")
    @Mapping(source = "theWorkGroupMetricSubId", target = "theWorkGroupMetricSub")
    @Mapping(source = "theOrganizationMetricSubId", target = "theOrganizationMetricSub")
    @Mapping(source = "theProcessMetricSubId", target = "theProcessMetricSub")
    @Mapping(source = "theResourceMetricSubId", target = "theResourceMetricSub")
    @Mapping(source = "theMetricDefinitionId", target = "theMetricDefinition")
    @Mapping(source = "theActivityId", target = "theActivity")
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
