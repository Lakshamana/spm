package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ArtifactTaskDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link ArtifactTask} and its DTO {@link ArtifactTaskDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {ArtifactMapper.class, TaskMapper.class})
public interface ArtifactTaskMapper extends EntityMapper<ArtifactTaskDTO, ArtifactTask> {

  @Mapping(source = "theArtifact.id", target = "theArtifactId")
  @Mapping(source = "theTask.id", target = "theTaskId")
  ArtifactTaskDTO toDto(ArtifactTask artifactTask);

  @Mapping(source = "theArtifactId", target = "theArtifact")
  @Mapping(source = "theTaskId", target = "theTask")
  ArtifactTask toEntity(ArtifactTaskDTO artifactTaskDTO);

  default ArtifactTask fromId(Long id) {
    if (id == null) {
      return null;
    }
    ArtifactTask artifactTask = new ArtifactTask();
    artifactTask.setId(id);
    return artifactTask;
  }
}
