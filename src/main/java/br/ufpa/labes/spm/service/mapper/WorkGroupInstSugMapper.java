package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WorkGroupInstSugDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkWorkGroupInstSug} and its DTO {@link WorkGroupInstSugDTO}.
 */
@Mapper(componentModel = "spring", uses = {WorkGroupMapper.class, TypeMapper.class})
public interface WorkWorkGroupInstSugMapper extends EntityMapper<WorkWorkGroupInstSugDTO, WorkGroupInstSug> {

    @Mapping(source = "chosenWorkWorkGroup.id", target = "chosenWorkGroupId")
    @Mapping(source = "workWorkGroupTypeRequired.id", target = "WorkGroupTypeRequiredId")
    WorkWorkGroupInstSugDTO toDto(WorkWorkGroupInstSug WorkGroupInstSug);

    @Mapping(source = "chosenWorkWorkGroupId", target = "chosenWorkGroup")
    @Mapping(source = "workWorkGroupTypeRequiredId", target = "WorkGroupTypeRequired")
    @Mapping(target = "removeSugWorkGroup", ignore = true)
    WorkWorkGroupInstSug toEntity(WorkWorkGroupInstSugDTO WorkGroupInstSugDTO);

    default WorkGroupInstSug fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkWorkGroupInstSug workWorkGroupInstSug = new WorkGroupInstSug();
        WorkGroupInstSug.setId(id);
        return WorkGroupInstSug;
    }
}
