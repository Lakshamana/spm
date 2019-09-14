package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ActivityTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ActivityType} and its DTO {@link ActivityTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ActivityTypeMapper extends EntityMapper<ActivityTypeDTO, ActivityType> {


    @Mapping(target = "theTypeSuper", ignore = true)
    @Mapping(target = "theActivities", ignore = true)
    @Mapping(target = "removeTheActivity", ignore = true)
    @Mapping(target = "theProcesses", ignore = true)
    @Mapping(target = "removeTheProcess", ignore = true)
    ActivityType toEntity(ActivityTypeDTO activityTypeDTO);

    default ActivityType fromId(Long id) {
        if (id == null) {
            return null;
        }
        ActivityType activityType = new ActivityType();
        activityType.setId(id);
        return activityType;
    }
}
