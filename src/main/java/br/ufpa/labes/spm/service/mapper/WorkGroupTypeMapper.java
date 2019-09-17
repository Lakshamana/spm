package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WorkGroupTypeDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link WorkGroupType} and its DTO {@link WorkGroupTypeDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface WorkGroupTypeMapper extends EntityMapper<WorkGroupTypeDTO, WorkGroupType> {

  @Mapping(target = "theWorkGroups", ignore = true)
  @Mapping(target = "removeTheWorkGroup", ignore = true)
  @Mapping(target = "theReqWorkGroups", ignore = true)
  @Mapping(target = "removeTheReqWorkGroup", ignore = true)
  WorkGroupType toEntity(WorkGroupTypeDTO workGroupTypeDTO);

  default WorkGroupType fromId(Long id) {
    if (id == null) {
      return null;
    }
    WorkGroupType workGroupType = new WorkGroupType();
    workGroupType.setId(id);
    return workGroupType;
  }
}
