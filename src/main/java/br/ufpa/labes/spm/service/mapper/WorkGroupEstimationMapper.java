package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WorkWorkGroupEstimationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkWorkGroupEstimation} and its DTO {@link WorkWorkGroupEstimationDTO}.
 */
@Mapper(componentModel = "spring", uses = {WorkWorkGroupMapper.class})
public interface WorkWorkGroupEstimationMapper extends EntityMapper<WorkWorkGroupEstimationDTO, WorkWorkGroupEstimation> {

    @Mapping(source = "theWorkWorkGroup.id", target = "theWorkWorkGroupId")
    WorkWorkGroupEstimationDTO toDto(WorkWorkGroupEstimation workWorkGroupEstimation);

    @Mapping(source = "theWorkWorkGroupId", target = "theWorkWorkGroup")
    WorkWorkGroupEstimation toEntity(WorkWorkGroupEstimationDTO workWorkGroupEstimationDTO);

    default WorkWorkGroupEstimation fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkWorkGroupEstimation workWorkGroupEstimation = new WorkWorkGroupEstimation();
        workWorkGroupEstimation.setId(id);
        return workWorkGroupEstimation;
    }
}
