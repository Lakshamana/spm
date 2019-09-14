package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WorkGroupMetricDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkGroupMetric} and its DTO {@link WorkGroupMetricDTO}.
 */
@Mapper(componentModel = "spring", uses = {WorkGroupMapper.class})
public interface WorkGroupMetricMapper extends EntityMapper<WorkGroupMetricDTO, WorkGroupMetric> {

    @Mapping(source = "theWorkGroup.id", target = "theWorkGroupId")
    WorkGroupMetricDTO toDto(WorkGroupMetric workGroupMetric);

    @Mapping(target = "theMetricSuper", ignore = true)
    @Mapping(source = "theWorkGroupId", target = "theWorkGroup")
    WorkGroupMetric toEntity(WorkGroupMetricDTO workGroupMetricDTO);

    default WorkGroupMetric fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkGroupMetric workGroupMetric = new WorkGroupMetric();
        workGroupMetric.setId(id);
        return workGroupMetric;
    }
}
