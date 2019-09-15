package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ActivityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Activity} and its DTO {@link ActivityDTO}.
 */
@Mapper(componentModel = "spring", uses = {PlainMapper.class, DecomposedMapper.class, ActivityTypeMapper.class, JoinConMapper.class, BranchANDConMapper.class, ArtifactConMapper.class, ProcessModelMapper.class})
public interface ActivityMapper extends EntityMapper<ActivityDTO, Activity> {

    @Mapping(source = "thePlainSub.id", target = "thePlainSubId")
    @Mapping(source = "theDecomposedSub.id", target = "theDecomposedSubId")
    @Mapping(source = "theActivityType.id", target = "theActivityTypeId")
    @Mapping(source = "theAncestorActitvity.id", target = "theAncestorActitvityId")
    @Mapping(source = "theProcessModel.id", target = "theProcessModelId")
    ActivityDTO toDto(Activity activity);

    @Mapping(source = "thePlainSubId", target = "thePlainSub")
    @Mapping(source = "theDecomposedSubId", target = "theDecomposedSub")
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
    @Mapping(target = "activityMetrics", ignore = true)
    @Mapping(target = "removeActivityMetric", ignore = true)
    @Mapping(target = "theActivityEstimations", ignore = true)
    @Mapping(target = "removeTheActivityEstimation", ignore = true)
    @Mapping(source = "theActivityTypeId", target = "theActivityType")
    @Mapping(target = "removeToJoinCon", ignore = true)
    @Mapping(target = "removeToBranchANDCon", ignore = true)
    @Mapping(target = "removeFromArtifactCon", ignore = true)
    @Mapping(target = "removeToArtifactCon", ignore = true)
    @Mapping(source = "theAncestorActitvityId", target = "theAncestorActitvity")
    @Mapping(source = "theProcessModelId", target = "theProcessModel")
    @Mapping(target = "theActivityInstantiateds", ignore = true)
    @Mapping(target = "removeTheActivityInstantiated", ignore = true)
    @Mapping(target = "theActivityMetrics", ignore = true)
    @Mapping(target = "removeTheActivityMetric", ignore = true)
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
