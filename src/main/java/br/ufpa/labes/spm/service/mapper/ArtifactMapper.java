package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ArtifactDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Artifact} and its DTO {@link ArtifactDTO}.
 */
@Mapper(componentModel = "spring", uses = {ArtifactTypeMapper.class, VCSRepositoryMapper.class, ProjectMapper.class})
public interface ArtifactMapper extends EntityMapper<ArtifactDTO, Artifact> {

    @Mapping(source = "theArtifactType.id", target = "theArtifactTypeId")
    @Mapping(source = "derivedTo.id", target = "derivedToId")
    @Mapping(source = "possess.id", target = "possessId")
    @Mapping(source = "theRepository.id", target = "theRepositoryId")
    @Mapping(source = "theProject.id", target = "theProjectId")
    ArtifactDTO toDto(Artifact artifact);

    @Mapping(target = "theInvolvedArtifacts", ignore = true)
    @Mapping(target = "removeTheInvolvedArtifacts", ignore = true)
    @Mapping(target = "theArtifactParams", ignore = true)
    @Mapping(target = "removeTheArtifactParam", ignore = true)
    @Mapping(target = "theAutomatics", ignore = true)
    @Mapping(target = "removeTheAutomatic", ignore = true)
    @Mapping(target = "theArtifactMetrics", ignore = true)
    @Mapping(target = "removeTheArtifactMetric", ignore = true)
    @Mapping(target = "theArtifactEstimations", ignore = true)
    @Mapping(target = "removeTheArtifactEstimation", ignore = true)
    @Mapping(source = "theArtifactTypeId", target = "theArtifactType")
    @Mapping(source = "derivedToId", target = "derivedTo")
    @Mapping(source = "possessId", target = "possess")
    @Mapping(source = "theRepositoryId", target = "theRepository")
    @Mapping(source = "theProjectId", target = "theProject")
    @Mapping(target = "derivedFroms", ignore = true)
    @Mapping(target = "removeDerivedFrom", ignore = true)
    @Mapping(target = "belongsTos", ignore = true)
    @Mapping(target = "removeBelongsTo", ignore = true)
    @Mapping(target = "theArtifactTasks", ignore = true)
    @Mapping(target = "removeTheArtifactTasks", ignore = true)
    @Mapping(target = "theArtifactCons", ignore = true)
    @Mapping(target = "removeTheArtifactCon", ignore = true)
    Artifact toEntity(ArtifactDTO artifactDTO);

    default Artifact fromId(Long id) {
        if (id == null) {
            return null;
        }
        Artifact artifact = new Artifact();
        artifact.setId(id);
        return artifact;
    }
}
