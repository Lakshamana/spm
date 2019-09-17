package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.PlainDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Plain} and its DTO {@link PlainDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {EnactionDescriptionMapper.class})
public interface PlainMapper extends EntityMapper<PlainDTO, Plain> {

  @Mapping(source = "theEnactionDescription.id", target = "theEnactionDescriptionId")
  PlainDTO toDto(Plain plain);

  @Mapping(source = "theEnactionDescriptionId", target = "theEnactionDescription")
  @Mapping(target = "theGlobalActivityEvents", ignore = true)
  @Mapping(target = "removeTheGlobalActivityEvent", ignore = true)
  @Mapping(target = "theCatalogEvents", ignore = true)
  @Mapping(target = "removeTheCatalogEvent", ignore = true)
  Plain toEntity(PlainDTO plainDTO);

  default Plain fromId(Long id) {
    if (id == null) {
      return null;
    }
    Plain plain = new Plain();
    plain.setId(id);
    return plain;
  }
}
