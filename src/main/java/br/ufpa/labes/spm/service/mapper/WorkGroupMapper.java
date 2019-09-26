package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WorkGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkGroup} and its DTO {@link WorkGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {WorkGroupTypeMapper.class})
public interface WorkGroupMapper extends EntityMapper<WorkGroupDTO, WorkGroup> {

    @Mapping(source = "theGroupType.id", target = "theGroupTypeId")
    @Mapping(source = "superGroup.id", target = "superGroupId")
    WorkGroupDTO toDto(WorkGroup workGroup);

    @Mapping(target = "theReqGroups", ignore = true)
    @Mapping(target = "removeTheReqGroup", ignore = true)
    @Mapping(target = "theWorkGroupMetrics", ignore = true)
    @Mapping(target = "removeTheWorkGroupMetric", ignore = true)
    @Mapping(target = "theWorkGroupEstimations", ignore = true)
    @Mapping(target = "removeTheWorkGroupEstimation", ignore = true)
    @Mapping(source = "theGroupTypeId", target = "theGroupType")
    @Mapping(source = "superGroupId", target = "superGroup")
    @Mapping(target = "subGroups", ignore = true)
    @Mapping(target = "removeSubGroups", ignore = true)
    @Mapping(target = "theWorkGroupInstSugs", ignore = true)
    @Mapping(target = "removeTheWorkGroupInstSug", ignore = true)
    @Mapping(target = "theAgents", ignore = true)
    @Mapping(target = "removeTheAgent", ignore = true)
    @Mapping(target = "theWorkGroupInstSugs", ignore = true)
    @Mapping(target = "removeTheWorkGroupInstSug", ignore = true)
    @Mapping(target = "theWorkGroupInstSugs", ignore = true)
    @Mapping(target = "removeTheWorkGroupInstSug", ignore = true)
    WorkGroup toEntity(WorkGroupDTO workGroupDTO);

    default WorkGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkGroup workGroup = new WorkGroup();
        workGroup.setId(id);
        return workGroup;
    }
}
