package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WorkWorkGroupInstSugDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkWorkGroupInstSug} and its DTO {@link WorkWorkGroupInstSugDTO}.
 */
@Mapper(componentModel = "spring", uses = {WorkWorkGroupMapper.class, TypeMapper.class})
public interface WorkWorkGroupInstSugMapper extends EntityMapper<WorkWorkGroupInstSugDTO, WorkWorkGroupInstSug> {

    @Mapping(source = "chosenWorkWorkGroup.id", target = "chosenWorkWorkGroupId")
    @Mapping(source = "workWorkGroupTypeRequired.id", target = "workWorkGroupTypeRequiredId")
    WorkWorkGroupInstSugDTO toDto(WorkWorkGroupInstSug workWorkGroupInstSug);

    @Mapping(source = "chosenWorkWorkGroupId", target = "chosenWorkWorkGroup")
    @Mapping(source = "workWorkGroupTypeRequiredId", target = "workWorkGroupTypeRequired")
    @Mapping(target = "removeSugWorkWorkGroup", ignore = true)
    WorkWorkGroupInstSug toEntity(WorkWorkGroupInstSugDTO workWorkGroupInstSugDTO);

    default WorkWorkGroupInstSug fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkWorkGroupInstSug workWorkGroupInstSug = new WorkWorkGroupInstSug();
        workWorkGroupInstSug.setId(id);
        return workWorkGroupInstSug;
    }
}
