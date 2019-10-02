package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.PrimitiveTypeDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link PrimitiveType} and its DTO {@link PrimitiveTypeDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface PrimitiveTypeMapper extends EntityMapper<PrimitiveTypeDTO, PrimitiveType> {

  @Mapping(target = "theToolParameters", ignore = true)
  @Mapping(target = "removeTheToolParameters", ignore = true)
  PrimitiveType toEntity(PrimitiveTypeDTO primitiveTypeDTO);

  default PrimitiveType fromId(Long id) {
    if (id == null) {
      return null;
    }
    PrimitiveType primitiveType = new PrimitiveType();
    primitiveType.setId(id);
    return primitiveType;
  }
}
