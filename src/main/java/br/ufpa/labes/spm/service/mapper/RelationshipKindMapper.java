package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.RelationshipKindDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link RelationshipKind} and its DTO {@link RelationshipKindDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface RelationshipKindMapper
    extends EntityMapper<RelationshipKindDTO, RelationshipKind> {

  @Mapping(target = "theAssetRelationships", ignore = true)
  @Mapping(target = "removeTheAssetRelationship", ignore = true)
  RelationshipKind toEntity(RelationshipKindDTO relationshipKindDTO);

  default RelationshipKind fromId(Long id) {
    if (id == null) {
      return null;
    }
    RelationshipKind relationshipKind = new RelationshipKind();
    relationshipKind.setId(id);
    return relationshipKind;
  }
}
