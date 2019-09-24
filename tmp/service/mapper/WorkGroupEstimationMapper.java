package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WorkGroupEstimationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkWorkGroupEstimation} and its DTO {@link WorkGroupEstimationDTO}.
 */
@Mapper(componentModel = "spring", uses = {WorkGroupMapper.class})
public interface WorkWorkGroupEstimationMapper extends EntityMapper<WorkWorkGroupEstimationDTO, WorkGroupEstimation> {

    @Mapping(source = "theWorkWorkGroup.id", target = "theWorkGroupId")
    WorkWorkGroupEstimationDTO toDto(WorkWorkGroupEstimation WorkGroupEstimation);

    @Mapping(source = "theWorkWorkGroupId", target = "theWorkGroup")
    WorkWorkGroupEstimation toEntity(WorkWorkGroupEstimationDTO WorkGroupEstimationDTO);

    default WorkGroupEstimation fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkWorkGroupEstimation workWorkGroupEstimation = new WorkGroupEstimation();
        WorkGroupEstimation.setId(id);
        return WorkGroupEstimation;
    }
}
