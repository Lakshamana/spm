package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ActivityInstantiatedDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ActivityInstantiated} and its DTO {@link ActivityInstantiatedDTO}.
 */
@Mapper(componentModel = "spring", uses = {ActivityMapper.class})
public interface ActivityInstantiatedMapper extends EntityMapper<ActivityInstantiatedDTO, ActivityInstantiated> {

    @Mapping(source = "theActivity.id", target = "theActivityId")
    ActivityInstantiatedDTO toDto(ActivityInstantiated activityInstantiated);

    @Mapping(source = "theActivityId", target = "theActivity")
    @Mapping(target = "theInstSugs", ignore = true)
    @Mapping(target = "removeTheInstSug", ignore = true)
    ActivityInstantiated toEntity(ActivityInstantiatedDTO activityInstantiatedDTO);

    default ActivityInstantiated fromId(Long id) {
        if (id == null) {
            return null;
        }
        ActivityInstantiated activityInstantiated = new ActivityInstantiated();
        activityInstantiated.setId(id);
        return activityInstantiated;
    }
}
