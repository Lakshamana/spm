package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.TypeDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Type} and its DTO {@link TypeDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {
      AbilityTypeMapper.class,
      ActivityTypeMapper.class,
      ArtifactTypeMapper.class,
      ConnectionTypeMapper.class,
      EventTypeMapper.class,
      WorkGroupTypeMapper.class,
      MetricTypeMapper.class,
      ResourceTypeMapper.class,
      RoleTypeMapper.class,
      ToolTypeMapper.class
    })
public interface TypeMapper extends EntityMapper<TypeDTO, Type> {

  @Mapping(source = "theAbilityTypeSub.id", target = "theAbilityTypeSubId")
  @Mapping(source = "theActivityTypeSub.id", target = "theActivityTypeSubId")
  @Mapping(source = "theArtifactTypeSub.id", target = "theArtifactTypeSubId")
  @Mapping(source = "theConnectionTypeSub.id", target = "theConnectionTypeSubId")
  @Mapping(source = "theEventTypeSub.id", target = "theEventTypeSubId")
  @Mapping(source = "theWorkGroupTypeSub.id", target = "theWorkGroupTypeSubId")
  @Mapping(source = "theMetricTypeSub.id", target = "theMetricTypeSubId")
  @Mapping(source = "theResourceTypeSub.id", target = "theResourceTypeSubId")
  @Mapping(source = "theRoleTypeSub.id", target = "theRoleTypeSubId")
  @Mapping(source = "theToolTypeSub.id", target = "theToolTypeSubId")
  @Mapping(source = "superType.id", target = "superTypeId")
  TypeDTO toDto(Type type);

  @Mapping(source = "theAbilityTypeSubId", target = "theAbilityTypeSub")
  @Mapping(source = "theActivityTypeSubId", target = "theActivityTypeSub")
  @Mapping(source = "theArtifactTypeSubId", target = "theArtifactTypeSub")
  @Mapping(source = "theConnectionTypeSubId", target = "theConnectionTypeSub")
  @Mapping(source = "theEventTypeSubId", target = "theEventTypeSub")
  @Mapping(source = "theWorkGroupTypeSubId", target = "theWorkGroupTypeSub")
  @Mapping(source = "theMetricTypeSubId", target = "theMetricTypeSub")
  @Mapping(source = "theResourceTypeSubId", target = "theResourceTypeSub")
  @Mapping(source = "theRoleTypeSubId", target = "theRoleTypeSub")
  @Mapping(source = "theToolTypeSubId", target = "theToolTypeSub")
  @Mapping(source = "superTypeId", target = "superType")
  @Mapping(target = "sugToReqWorkGroups", ignore = true)
  @Mapping(target = "removeSugToReqWorkGroup", ignore = true)
  @Mapping(target = "instSugToTypes", ignore = true)
  @Mapping(target = "removeInstSugToType", ignore = true)
  @Mapping(target = "subTypes", ignore = true)
  @Mapping(target = "removeSubType", ignore = true)
  @Mapping(target = "theToolDefinitionToArtifactTypes", ignore = true)
  @Mapping(target = "removeTheToolDefinitionToArtifactType", ignore = true)
  Type toEntity(TypeDTO typeDTO);

  default Type fromId(Long id) {
    if (id == null) {
      return null;
    }
    Type type = new Type();
    type.setId(id);
    return type;
  }
}
