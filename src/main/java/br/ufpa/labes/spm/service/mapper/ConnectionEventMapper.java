package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ConnectionEventDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link ConnectionEvent} and its DTO {@link ConnectionEventDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {CatalogEventMapper.class})
public interface ConnectionEventMapper extends EntityMapper<ConnectionEventDTO, ConnectionEvent> {

  @Mapping(source = "theCatalogEvent.id", target = "theCatalogEventId")
  ConnectionEventDTO toDto(ConnectionEvent connectionEvent);

  @Mapping(source = "theCatalogEventId", target = "theCatalogEvent")
  ConnectionEvent toEntity(ConnectionEventDTO connectionEventDTO);

  default ConnectionEvent fromId(Long id) {
    if (id == null) {
      return null;
    }
    ConnectionEvent connectionEvent = new ConnectionEvent();
    connectionEvent.setId(id);
    return connectionEvent;
  }
}
