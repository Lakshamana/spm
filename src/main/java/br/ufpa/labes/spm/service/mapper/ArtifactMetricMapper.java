package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ArtifactMetricDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link ArtifactMetric} and its DTO {@link ArtifactMetricDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {ArtifactMapper.class})
public interface ArtifactMetricMapper extends EntityMapper<ArtifactMetricDTO, ArtifactMetric> {

  @Mapping(source = "artifact.id", target = "artifactId")
  ArtifactMetricDTO toDto(ArtifactMetric artifactMetric);

  @Mapping(source = "artifactId", target = "artifact")
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
