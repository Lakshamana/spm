package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WorkGroupEstimationDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link WorkGroupEstimation} and its DTO {@link WorkGroupEstimationDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {WorkGroupMapper.class})
public interface WorkGroupEstimationMapper
    extends EntityMapper<WorkGroupEstimationDTO, WorkGroupEstimation> {

  @Mapping(source = "workGroup.id", target = "workGroupId")
  WorkGroupEstimationDTO toDto(WorkGroupEstimation workGroupEstimation);

  @Mapping(source = "workGroupId", target = "workGroup")
  WorkGroupEstimation toEntity(WorkGroupEstimationDTO workGroupEstimationDTO);

  default WorkGroupEstimation fromId(Long id) {
    if (id == null) {
      return null;
    }
    WorkGroupEstimation workGroupEstimation = new WorkGroupEstimation();
    workGroupEstimation.setId(id);
    return workGroupEstimation;
  }
}
