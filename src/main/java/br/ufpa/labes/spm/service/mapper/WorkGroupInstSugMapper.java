package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WorkGroupInstSugDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link WorkGroupInstSug} and its DTO {@link WorkGroupInstSugDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {WorkGroupMapper.class, TypeMapper.class})
public interface WorkGroupInstSugMapper
    extends EntityMapper<WorkGroupInstSugDTO, WorkGroupInstSug> {

  @Mapping(source = "chosenWorkGroup.id", target = "chosenWorkGroupId")
  @Mapping(source = "workGroupTypeRequired.id", target = "workGroupTypeRequiredId")
  WorkGroupInstSugDTO toDto(WorkGroupInstSug workGroupInstSug);

  @Mapping(source = "chosenWorkGroupId", target = "chosenWorkGroup")
  @Mapping(source = "workGroupTypeRequiredId", target = "workGroupTypeRequired")
  @Mapping(target = "removeSugWorkGroup", ignore = true)
  @Mapping(target = "thePeopleInstSugSuper", ignore = true)
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
