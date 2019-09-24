package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WorkGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkWorkGroup} and its DTO {@link WorkGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {WorkGroupTypeMapper.class})
public interface WorkWorkGroupMapper extends EntityMapper<WorkWorkGroupDTO, WorkGroup> {

    @Mapping(source = "theWorkWorkGroupType.id", target = "theWorkGroupTypeId")
    @Mapping(source = "superWorkWorkGroup.id", target = "superWorkGroupId")
    WorkWorkGroupDTO toDto(WorkWorkGroup WorkGroup);

    @Mapping(target = "theReqWorkGroups", ignore = true)
    @Mapping(target = "removeTheReqWorkGroup", ignore = true)
    @Mapping(target = "theWorkGroupMetrics", ignore = true)
    @Mapping(target = "removeTheWorkGroupMetric", ignore = true)
    @Mapping(target = "theWorkGroupEstimations", ignore = true)
    @Mapping(target = "removeTheWorkGroupEstimation", ignore = true)
    @Mapping(source = "theWorkWorkGroupTypeId", target = "theWorkGroupType")
    @Mapping(source = "superWorkWorkGroupId", target = "superWorkGroup")
    @Mapping(target = "subWorkGroups", ignore = true)
    @Mapping(target = "removeSubWorkGroups", ignore = true)
    @Mapping(target = "sugToChosenWorkGroups", ignore = true)
    @Mapping(target = "removeSugToChosenWorkGroup", ignore = true)
    @Mapping(target = "theAgents", ignore = true)
    @Mapping(target = "removeTheAgent", ignore = true)
    @Mapping(target = "theWorkGroupInstSugs", ignore = true)
    @Mapping(target = "removeTheWorkGroupInstSug", ignore = true)
    WorkWorkGroup toEntity(WorkWorkGroupDTO WorkGroupDTO);

    default WorkGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkWorkGroup workWorkGroup = new WorkGroup();
        WorkGroup.setId(id);
        return WorkGroup;
    }
}
