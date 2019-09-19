package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WorkWorkGroupMetricDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link WorkWorkGroupMetric} and its DTO {@link WorkWorkGroupMetricDTO}.
 */
@Mapper(componentModel = "spring", uses = {WorkWorkGroupMapper.class})
public interface WorkWorkGroupMetricMapper extends EntityMapper<WorkWorkGroupMetricDTO, WorkWorkGroupMetric> {

    @Mapping(source = "theWorkWorkGroup.id", target = "theWorkWorkGroupId")
    WorkWorkGroupMetricDTO toDto(WorkWorkGroupMetric workWorkGroupMetric);

    @Mapping(source = "theWorkWorkGroupId", target = "theWorkWorkGroup")
    WorkWorkGroupMetric toEntity(WorkWorkGroupMetricDTO workWorkGroupMetricDTO);

    default WorkWorkGroupMetric fromId(Long id) {
        if (id == null) {
            return null;
        }
        WorkWorkGroupMetric workWorkGroupMetric = new WorkWorkGroupMetric();
        workWorkGroupMetric.setId(id);
        return workWorkGroupMetric;
    }
}
