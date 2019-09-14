package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.EstimationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Estimation} and its DTO {@link EstimationDTO}.
 */
@Mapper(componentModel = "spring", uses = {ActivityEstimationMapper.class, AgentEstimationMapper.class, ArtifactEstimationMapper.class, WorkGroupEstimationMapper.class, OrganizationEstimationMapper.class, ProcessEstimationMapper.class, ResourceEstimationMapper.class, MetricDefinitionMapper.class})
public interface EstimationMapper extends EntityMapper<EstimationDTO, Estimation> {

    @Mapping(source = "theActivityEstimationSub.id", target = "theActivityEstimationSubId")
    @Mapping(source = "theAgentEstimationSub.id", target = "theAgentEstimationSubId")
    @Mapping(source = "theArtifactEstimationSub.id", target = "theArtifactEstimationSubId")
    @Mapping(source = "theWorkGroupEstimationSub.id", target = "theWorkGroupEstimationSubId")
    @Mapping(source = "theOrganizationEstimationSub.id", target = "theOrganizationEstimationSubId")
    @Mapping(source = "theProcessEstimationSub.id", target = "theProcessEstimationSubId")
    @Mapping(source = "theResourceEstimationSub.id", target = "theResourceEstimationSubId")
    @Mapping(source = "theMetricDefinition.id", target = "theMetricDefinitionId")
    EstimationDTO toDto(Estimation estimation);

    @Mapping(source = "theActivityEstimationSubId", target = "theActivityEstimationSub")
    @Mapping(source = "theAgentEstimationSubId", target = "theAgentEstimationSub")
    @Mapping(source = "theArtifactEstimationSubId", target = "theArtifactEstimationSub")
    @Mapping(source = "theWorkGroupEstimationSubId", target = "theWorkGroupEstimationSub")
    @Mapping(source = "theOrganizationEstimationSubId", target = "theOrganizationEstimationSub")
    @Mapping(source = "theProcessEstimationSubId", target = "theProcessEstimationSub")
    @Mapping(source = "theResourceEstimationSubId", target = "theResourceEstimationSub")
    @Mapping(source = "theMetricDefinitionId", target = "theMetricDefinition")
    Estimation toEntity(EstimationDTO estimationDTO);

    default Estimation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Estimation estimation = new Estimation();
        estimation.setId(id);
        return estimation;
    }
}
