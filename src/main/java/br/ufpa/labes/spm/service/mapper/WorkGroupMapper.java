package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WorkWorkGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkWorkGroup} and its DTO {@link WorkWorkGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {WorkWorkGroupTypeMapper.class})
public interface WorkWorkGroupMapper extends EntityMapper<WorkWorkGroupDTO, WorkWorkGroup> {

    @Mapping(source = "theWorkWorkGroupType.id", target = "theWorkWorkGroupTypeId")
    @Mapping(source = "superWorkWorkGroup.id", target = "superWorkWorkGroupId")
    WorkWorkGroupDTO toDto(WorkWorkGroup workWorkGroup);

    @Mapping(target = "theReqWorkWorkGroups", ignore = true)
    @Mapping(target = "removeTheReqWorkWorkGroup", ignore = true)
    @Mapping(target = "theWorkWorkGroupMetrics", ignore = true)
    @Mapping(target = "removeTheWorkWorkGroupMetric", ignore = true)
    @Mapping(target = "theWorkWorkGroupEstimations", ignore = true)
    @Mapping(target = "removeTheWorkWorkGroupEstimation", ignore = true)
    @Mapping(source = "theWorkWorkGroupTypeId", target = "theWorkWorkGroupType")
    @Mapping(source = "superWorkWorkGroupId", target = "superWorkWorkGroup")
    @Mapping(target = "subWorkWorkGroups", ignore = true)
    @Mapping(target = "removeSubWorkWorkGroups", ignore = true)
    @Mapping(target = "sugToChosenWorkWorkGroups", ignore = true)
    @Mapping(target = "removeSugToChosenWorkWorkGroup", ignore = true)
    @Mapping(target = "theAgents", ignore = true)
    @Mapping(target = "removeTheAgent", ignore = true)
    @Mapping(target = "theWorkWorkGroupInstSugs", ignore = true)
    @Mapping(target = "removeTheWorkWorkGroupInstSug", ignore = true)
    WorkWorkGroup toEntity(WorkWorkGroupDTO workWorkGroupDTO);

    default WorkWorkGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkWorkGroup workWorkGroup = new WorkWorkGroup();
        workWorkGroup.setId(id);
        return workWorkGroup;
    }
}
