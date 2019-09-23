package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WorkGroupTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkWorkGroupType} and its DTO {@link WorkGroupTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WorkWorkGroupTypeMapper extends EntityMapper<WorkWorkGroupTypeDTO, WorkGroupType> {


    @Mapping(target = "theWorkGroups", ignore = true)
    @Mapping(target = "removeTheWorkGroup", ignore = true)
    @Mapping(target = "theReqWorkGroups", ignore = true)
    @Mapping(target = "removeTheReqWorkGroup", ignore = true)
    WorkWorkGroupType toEntity(WorkWorkGroupTypeDTO WorkGroupTypeDTO);

    default WorkGroupType fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkWorkGroupType workWorkGroupType = new WorkGroupType();
        WorkGroupType.setId(id);
        return WorkGroupType;
    }
}
