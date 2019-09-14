package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WorkGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkGroup} and its DTO {@link WorkGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {WorkGroupTypeMapper.class})
public interface WorkGroupMapper extends EntityMapper<WorkGroupDTO, WorkGroup> {

    @Mapping(source = "theWorkGroupType.id", target = "theWorkGroupTypeId")
    @Mapping(source = "superWorkGroup.id", target = "superWorkGroupId")
    WorkGroupDTO toDto(WorkGroup workGroup);

    @Mapping(target = "theReqWorkGroups", ignore = true)
    @Mapping(target = "removeTheReqWorkGroup", ignore = true)
    @Mapping(target = "theWorkGroupMetrics", ignore = true)
    @Mapping(target = "removeTheWorkGroupMetric", ignore = true)
    @Mapping(target = "theWorkGroupEstimations", ignore = true)
    @Mapping(target = "removeTheWorkGroupEstimation", ignore = true)
    @Mapping(source = "theWorkGroupTypeId", target = "theWorkGroupType")
    @Mapping(source = "superWorkGroupId", target = "superWorkGroup")
    @Mapping(target = "subWorkGroups", ignore = true)
    @Mapping(target = "removeSubWorkGroups", ignore = true)
    @Mapping(target = "sugToChosenWorkGroups", ignore = true)
    @Mapping(target = "removeSugToChosenWorkGroup", ignore = true)
    @Mapping(target = "theAgents", ignore = true)
    @Mapping(target = "removeTheAgent", ignore = true)
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
