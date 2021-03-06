package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ActivityMetricDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ActivityMetric} and its DTO {@link ActivityMetricDTO}.
 */
@Mapper(componentModel = "spring", uses = {ActivityMapper.class})
public interface ActivityMetricMapper extends EntityMapper<ActivityMetricDTO, ActivityMetric> {

    @Mapping(source = "activity.id", target = "activityId")
    ActivityMetricDTO toDto(ActivityMetric activityMetric);

    @Mapping(source = "activityId", target = "activity")
    ActivityMetric toEntity(ActivityMetricDTO activityMetricDTO);

    default ActivityMetric fromId(Long id) {
        if (id == null) {
            return null;
        }
        ActivityMetric activityMetric = new ActivityMetric();
        activityMetric.setId(id);
        return activityMetric;
    }
}
