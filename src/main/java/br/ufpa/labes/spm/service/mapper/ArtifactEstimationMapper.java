package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ArtifactEstimationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArtifactEstimation} and its DTO {@link ArtifactEstimationDTO}.
 */
@Mapper(componentModel = "spring", uses = {ArtifactMapper.class})
public interface ArtifactEstimationMapper extends EntityMapper<ArtifactEstimationDTO, ArtifactEstimation> {

    @Mapping(source = "theArtifact.id", target = "theArtifactId")
    ArtifactEstimationDTO toDto(ArtifactEstimation artifactEstimation);

    @Mapping(source = "theArtifactId", target = "theArtifact")
    ArtifactEstimation toEntity(ArtifactEstimationDTO artifactEstimationDTO);

    default ArtifactEstimation fromId(Long id) {
        if (id == null) {
            return null;
        }
        ArtifactEstimation artifactEstimation = new ArtifactEstimation();
        artifactEstimation.setId(id);
        return artifactEstimation;
    }
}
