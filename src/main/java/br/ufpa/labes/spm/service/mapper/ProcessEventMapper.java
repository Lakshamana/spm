package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ProcessEventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProcessEvent} and its DTO {@link ProcessEventDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProcessMapper.class})
public interface ProcessEventMapper extends EntityMapper<ProcessEventDTO, ProcessEvent> {

    @Mapping(source = "theProcess.id", target = "theProcessId")
    ProcessEventDTO toDto(ProcessEvent processEvent);

    @Mapping(source = "theProcessId", target = "theProcess")
    @Mapping(target = "theEventSuper", ignore = true)
    @Mapping(target = "theCatalogEventToProcesses", ignore = true)
    @Mapping(target = "removeTheCatalogEventToProcess", ignore = true)
    ProcessEvent toEntity(ProcessEventDTO processEventDTO);

    default ProcessEvent fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProcessEvent processEvent = new ProcessEvent();
        processEvent.setId(id);
        return processEvent;
    }
}
