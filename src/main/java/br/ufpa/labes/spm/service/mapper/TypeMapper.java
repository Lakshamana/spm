package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.TypeDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Type} and its DTO {@link TypeDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface TypeMapper extends EntityMapper<TypeDTO, Type> {

  @Mapping(source = "superType.id", target = "superTypeId")
  TypeDTO toDto(Type type);

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
