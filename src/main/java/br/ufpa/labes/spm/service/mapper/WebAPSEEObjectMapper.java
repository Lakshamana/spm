package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.WebAPSEEObjectDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link WebAPSEEObject} and its DTO {@link WebAPSEEObjectDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {GraphicCoordinateMapper.class})
public interface WebAPSEEObjectMapper extends EntityMapper<WebAPSEEObjectDTO, WebAPSEEObject> {

  @Mapping(source = "theGraphicCoordinate.id", target = "theGraphicCoordinateId")
  WebAPSEEObjectDTO toDto(WebAPSEEObject webAPSEEObject);

  @Mapping(source = "theGraphicCoordinateId", target = "theGraphicCoordinate")
  WebAPSEEObject toEntity(WebAPSEEObjectDTO webAPSEEObjectDTO);

  default WebAPSEEObject fromId(Long id) {
    if (id == null) {
      return null;
    }
    WebAPSEEObject webAPSEEObject = new WebAPSEEObject();
    webAPSEEObject.setId(id);
    return webAPSEEObject;
  }
}
