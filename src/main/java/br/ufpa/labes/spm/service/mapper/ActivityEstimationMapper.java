package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ActivityEstimationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ActivityEstimation} and its DTO {@link ActivityEstimationDTO}.
 */
@Mapper(componentModel = "spring", uses = {ActivityMapper.class})
public interface ActivityEstimationMapper extends EntityMapper<ActivityEstimationDTO, ActivityEstimation> {

    @Mapping(source = "activity.id", target = "activityId")
    ActivityEstimationDTO toDto(ActivityEstimation activityEstimation);

    @Mapping(source = "activityId", target = "activity")
    ActivityEstimation toEntity(ActivityEstimationDTO activityEstimationDTO);

    default ActivityEstimation fromId(Long id) {
        if (id == null) {
            return null;
        }
        ActivityEstimation activityEstimation = new ActivityEstimation();
        activityEstimation.setId(id);
        return activityEstimation;
    }
}
