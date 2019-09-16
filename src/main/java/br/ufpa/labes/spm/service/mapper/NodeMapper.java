package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.NodeDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Node} and its DTO {@link NodeDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface NodeMapper extends EntityMapper<NodeDTO, Node> {

  @Mapping(source = "theNode.id", target = "theNodeId")
  NodeDTO toDto(Node node);

  @Mapping(target = "children", ignore = true)
  @Mapping(target = "removeChildren", ignore = true)
  @Mapping(source = "theNodeId", target = "theNode")
  @Mapping(target = "theStructures", ignore = true)
  @Mapping(target = "removeTheStructure", ignore = true)
  Node toEntity(NodeDTO nodeDTO);

  default Node fromId(Long id) {
    if (id == null) {
      return null;
    }
    Node node = new Node();
    node.setId(id);
    return node;
  }
}
