package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ConnectionTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConnectionType} and its DTO {@link ConnectionTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ConnectionTypeMapper extends EntityMapper<ConnectionTypeDTO, ConnectionType> {


    @Mapping(target = "theTypeSuper", ignore = true)
    @Mapping(target = "theConnections", ignore = true)
    @Mapping(target = "removeTheConnection", ignore = true)
    ConnectionType toEntity(ConnectionTypeDTO connectionTypeDTO);

    default ConnectionType fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConnectionType connectionType = new ConnectionType();
        connectionType.setId(id);
        return connectionType;
    }
}
