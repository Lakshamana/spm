package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WorkGroupInstSugDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkGroupInstSug} and its DTO {@link WorkGroupInstSugDTO}.
 */
@Mapper(componentModel = "spring", uses = {WorkGroupMapper.class, WorkGroupTypeMapper.class})
public interface WorkGroupInstSugMapper extends EntityMapper<WorkGroupInstSugDTO, WorkGroupInstSug> {

    @Mapping(source = "groupChosen.id", target = "groupChosenId")
    @Mapping(source = "groupTypeRequired.id", target = "groupTypeRequiredId")
    WorkGroupInstSugDTO toDto(WorkGroupInstSug workGroupInstSug);

    @Mapping(source = "groupChosenId", target = "groupChosen")
    @Mapping(source = "groupTypeRequiredId", target = "groupTypeRequired")
    @Mapping(target = "removeGroupSuggested", ignore = true)
    WorkGroupInstSug toEntity(WorkGroupInstSugDTO workGroupInstSugDTO);

    default WorkGroupInstSug fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkGroupInstSug workGroupInstSug = new WorkGroupInstSug();
        workGroupInstSug.setId(id);
        return workGroupInstSug;
    }
}
