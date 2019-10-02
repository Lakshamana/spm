package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.TaskDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Task} and its DTO {@link TaskDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {AgentMapper.class, NormalMapper.class, ProcessAgendaMapper.class})
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {

  @Mapping(source = "delegatedFrom.id", target = "delegatedFromId")
  @Mapping(source = "delegatedTo.id", target = "delegatedToId")
  @Mapping(source = "theNormal.id", target = "theNormalId")
  @Mapping(source = "theProcessAgenda.id", target = "theProcessAgendaId")
  TaskDTO toDto(Task task);

  @Mapping(source = "delegatedFromId", target = "delegatedFrom")
  @Mapping(source = "delegatedToId", target = "delegatedTo")
  @Mapping(source = "theNormalId", target = "theNormal")
  @Mapping(source = "theProcessAgendaId", target = "theProcessAgenda")
  @Mapping(target = "theArtifactTasks", ignore = true)
  @Mapping(target = "removeTheArtifactTask", ignore = true)
  @Mapping(target = "theAgendaEvents", ignore = true)
  @Mapping(target = "removeTheAgendaEvent", ignore = true)
  @Mapping(target = "ocurrences", ignore = true)
  @Mapping(target = "removeOcurrence", ignore = true)
  Task toEntity(TaskDTO taskDTO);

  default Task fromId(Long id) {
    if (id == null) {
      return null;
    }
    Task task = new Task();
    task.setId(id);
    return task;
  }
}
