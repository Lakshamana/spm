package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WorkGroupMetricDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkWorkGroupMetric} and its DTO {@link WorkGroupMetricDTO}.
 */
@Mapper(componentModel = "spring", uses = {WorkGroupMapper.class})
public interface WorkWorkGroupMetricMapper extends EntityMapper<WorkWorkGroupMetricDTO, WorkGroupMetric> {

    @Mapping(source = "theWorkWorkGroup.id", target = "theWorkGroupId")
    WorkWorkGroupMetricDTO toDto(WorkWorkGroupMetric WorkGroupMetric);

    @Mapping(source = "theWorkWorkGroupId", target = "theWorkGroup")
    WorkWorkGroupMetric toEntity(WorkWorkGroupMetricDTO WorkGroupMetricDTO);

    default WorkGroupMetric fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkWorkGroupMetric workWorkGroupMetric = new WorkGroupMetric();
        WorkGroupMetric.setId(id);
        return WorkGroupMetric;
    }
}
