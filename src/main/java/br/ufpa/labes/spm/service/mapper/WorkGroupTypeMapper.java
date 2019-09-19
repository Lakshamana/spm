package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WorkWorkGroupTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkWorkGroupType} and its DTO {@link WorkWorkGroupTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WorkWorkGroupTypeMapper extends EntityMapper<WorkWorkGroupTypeDTO, WorkWorkGroupType> {


    @Mapping(target = "theWorkWorkGroups", ignore = true)
    @Mapping(target = "removeTheWorkWorkGroup", ignore = true)
    @Mapping(target = "theReqWorkWorkGroups", ignore = true)
    @Mapping(target = "removeTheReqWorkWorkGroup", ignore = true)
    WorkWorkGroupType toEntity(WorkWorkGroupTypeDTO workWorkGroupTypeDTO);

    default WorkWorkGroupType fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkWorkGroupType workWorkGroupType = new WorkWorkGroupType();
        workWorkGroupType.setId(id);
        return workWorkGroupType;
    }
}
