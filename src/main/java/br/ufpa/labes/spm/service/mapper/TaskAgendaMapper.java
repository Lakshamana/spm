package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.TaskAgendaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaskAgenda} and its DTO {@link TaskAgendaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TaskAgendaMapper extends EntityMapper<TaskAgendaDTO, TaskAgenda> {


    @Mapping(target = "theAgent", ignore = true)
    @Mapping(target = "theProcessAgenda", ignore = true)
    @Mapping(target = "removeTheProcessAgenda", ignore = true)
    TaskAgenda toEntity(TaskAgendaDTO taskAgendaDTO);

    default TaskAgenda fromId(Long id) {
        if (id == null) {
            return null;
        }
        TaskAgenda taskAgenda = new TaskAgenda();
        taskAgenda.setId(id);
        return taskAgenda;
    }
}
