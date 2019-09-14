package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ProcessModelEventDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProcessModelEvent} and its DTO {@link ProcessModelEventDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProcessModelMapper.class})
public interface ProcessModelEventMapper extends EntityMapper<ProcessModelEventDTO, ProcessModelEvent> {

    @Mapping(source = "theProcessModel.id", target = "theProcessModelId")
    ProcessModelEventDTO toDto(ProcessModelEvent processModelEvent);

    @Mapping(source = "theProcessModelId", target = "theProcessModel")
    @Mapping(target = "theEventSuper", ignore = true)
    @Mapping(target = "theCatalogEventToProcessModels", ignore = true)
    @Mapping(target = "removeTheCatalogEventToProcessModel", ignore = true)
    ProcessModelEvent toEntity(ProcessModelEventDTO processModelEventDTO);

    default ProcessModelEvent fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProcessModelEvent processModelEvent = new ProcessModelEvent();
        processModelEvent.setId(id);
        return processModelEvent;
    }
}
