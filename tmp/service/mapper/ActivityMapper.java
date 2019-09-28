package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ActivityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Activity} and its DTO {@link ActivityDTO}.
 */
@Mapper(componentModel = "spring", uses = {ActivityTypeMapper.class, JoinConMapper.class, BranchANDConMapper.class, ArtifactConMapper.class, ProcessModelMapper.class})
public interface ActivityMapper extends EntityMapper<ActivityDTO, Activity> {

    @Mapping(source = "theActivityType.id", target = "theActivityTypeId")
    @Mapping(source = "isVersionOf.id", target = "isVersionOfId")
    @Mapping(source = "theProcessModel.id", target = "theProcessModelId")
    ActivityDTO toDto(Activity activity);

    @Mapping(target = "theModelingActivityEvents", ignore = true)
    @Mapping(target = "removeTheModelingActivityEvent", ignore = true)
    @Mapping(target = "hasVersions", ignore = true)
    @Mapping(target = "removeHasVersions", ignore = true)
    @Mapping(target = "fromSimpleCons", ignore = true)
    @Mapping(target = "removeFromSimpleCon", ignore = true)
    @Mapping(target = "toSimpleCons", ignore = true)
    @Mapping(target = "removeToSimpleCon", ignore = true)
    @Mapping(target = "fromJoinCons", ignore = true)
    @Mapping(target = "removeFromJoinCon", ignore = true)
    @Mapping(target = "toBranchCons", ignore = true)
    @Mapping(target = "removeToBranchCon", ignore = true)
    @Mapping(target = "activityEstimations", ignore = true)
    @Mapping(target = "removeActivityEstimation", ignore = true)
    @Mapping(source = "theActivityTypeId", target = "theActivityType")
    @Mapping(target = "removeToJoinCon", ignore = true)
    @Mapping(target = "removeFromBranchANDCon", ignore = true)
    @Mapping(target = "removeFromArtifactCon", ignore = true)
    @Mapping(target = "removeToArtifactCon", ignore = true)
    @Mapping(source = "isVersionOfId", target = "isVersionOf")
    @Mapping(source = "theProcessModelId", target = "theProcessModel")
    @Mapping(target = "theBranchConCondToActivities", ignore = true)
    @Mapping(target = "removeTheBranchConCondToActivity", ignore = true)
    @Mapping(target = "theActivityInstantiateds", ignore = true)
    @Mapping(target = "removeTheActivityInstantiated", ignore = true)
    @Mapping(target = "activityMetrics", ignore = true)
    @Mapping(target = "removeActivityMetric", ignore = true)
    Activity toEntity(ActivityDTO activityDTO);

    default Activity fromId(Long id) {
        if (id == null) {
            return null;
        }
        Activity activity = new Activity();
        activity.setId(id);
        return activity;
    }
}
