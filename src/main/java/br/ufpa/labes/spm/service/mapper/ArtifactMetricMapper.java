package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ArtifactMetricDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArtifactMetric} and its DTO {@link ArtifactMetricDTO}.
 */
@Mapper(componentModel = "spring", uses = {ArtifactMapper.class})
public interface ArtifactMetricMapper extends EntityMapper<ArtifactMetricDTO, ArtifactMetric> {

    @Mapping(source = "theArtifact.id", target = "theArtifactId")
    ArtifactMetricDTO toDto(ArtifactMetric artifactMetric);

    @Mapping(target = "theMetricSuper", ignore = true)
    @Mapping(source = "theArtifactId", target = "theArtifact")
    ArtifactMetric toEntity(ArtifactMetricDTO artifactMetricDTO);

    default ArtifactMetric fromId(Long id) {
        if (id == null) {
            return null;
        }
        ArtifactMetric artifactMetric = new ArtifactMetric();
        artifactMetric.setId(id);
        return artifactMetric;
    }
}
