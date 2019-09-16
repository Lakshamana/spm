package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ConnectionDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Connection} and its DTO {@link ConnectionDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {
      MultipleConMapper.class,
      SimpleConMapper.class,
      ArtifactConMapper.class,
      ProcessModelMapper.class,
      ConnectionTypeMapper.class
    })
public interface ConnectionMapper extends EntityMapper<ConnectionDTO, Connection> {

  @Mapping(source = "theMultipleConSub.id", target = "theMultipleConSubId")
  @Mapping(source = "theSimpleConSub.id", target = "theSimpleConSubId")
  @Mapping(source = "theArtifactConSub.id", target = "theArtifactConSubId")
  @Mapping(source = "theProcessModel.id", target = "theProcessModelId")
  @Mapping(source = "theConnectionType.id", target = "theConnectionTypeId")
  ConnectionDTO toDto(Connection connection);

  @Mapping(source = "theMultipleConSubId", target = "theMultipleConSub")
  @Mapping(source = "theSimpleConSubId", target = "theSimpleConSub")
  @Mapping(source = "theArtifactConSubId", target = "theArtifactConSub")
  @Mapping(source = "theProcessModelId", target = "theProcessModel")
  @Mapping(source = "theConnectionTypeId", target = "theConnectionType")
  Connection toEntity(ConnectionDTO connectionDTO);

  default Connection fromId(Long id) {
    if (id == null) {
      return null;
    }
    Connection connection = new Connection();
    connection.setId(id);
    return connection;
  }
}
