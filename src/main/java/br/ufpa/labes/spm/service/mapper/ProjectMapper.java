package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ProjectDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {ProcessMapper.class, DevelopingSystemMapper.class, CalendarMapper.class})
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {

  @Mapping(source = "processRefered.id", target = "processReferedId")
  @Mapping(source = "theSystem.id", target = "theSystemId")
  @Mapping(source = "theCalendar.id", target = "theCalendarId")
  ProjectDTO toDto(Project project);

  @Mapping(target = "finalArtifacts", ignore = true)
  @Mapping(target = "removeFinalArtifacts", ignore = true)
  @Mapping(source = "processReferedId", target = "processRefered")
  @Mapping(source = "theSystemId", target = "theSystem")
  @Mapping(source = "theCalendarId", target = "theCalendar")
  Project toEntity(ProjectDTO projectDTO);

  default Project fromId(Long id) {
    if (id == null) {
      return null;
    }
    Project project = new Project();
    project.setId(id);
    return project;
  }
}
