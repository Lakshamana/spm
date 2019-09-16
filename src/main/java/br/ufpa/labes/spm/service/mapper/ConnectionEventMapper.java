package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ConnectionEventDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link ConnectionEvent} and its DTO {@link ConnectionEventDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface ConnectionEventMapper extends EntityMapper<ConnectionEventDTO, ConnectionEvent> {

  @Mapping(target = "theEventSuper", ignore = true)
  @Mapping(target = "theCatalogEventToConnections", ignore = true)
  @Mapping(target = "removeTheCatalogEventToConnection", ignore = true)
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
