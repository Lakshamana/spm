package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ProcessAgendaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProcessAgenda} and its DTO {@link ProcessAgendaDTO}.
 */
@Mapper(componentModel = "spring", uses = {TaskAgendaMapper.class, ProcessMapper.class})
public interface ProcessAgendaMapper extends EntityMapper<ProcessAgendaDTO, ProcessAgenda> {

    @Mapping(source = "theTaskAgenda.id", target = "theTaskAgendaId")
    @Mapping(source = "theProcess.id", target = "theProcessId")
    ProcessAgendaDTO toDto(ProcessAgenda processAgenda);

    @Mapping(source = "theTaskAgendaId", target = "theTaskAgenda")
    @Mapping(source = "theProcessId", target = "theProcess")
    @Mapping(target = "theTasks", ignore = true)
    @Mapping(target = "removeTheTask", ignore = true)
    ProcessAgenda toEntity(ProcessAgendaDTO processAgendaDTO);

    default ProcessAgenda fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProcessAgenda processAgenda = new ProcessAgenda();
        processAgenda.setId(id);
        return processAgenda;
    }
}
