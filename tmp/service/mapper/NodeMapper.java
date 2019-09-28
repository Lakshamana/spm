package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.NodeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Node} and its DTO {@link NodeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NodeMapper extends EntityMapper<NodeDTO, Node> {

    @Mapping(source = "parentNode.id", target = "parentNodeId")
    NodeDTO toDto(Node node);

    @Mapping(target = "children", ignore = true)
    @Mapping(target = "removeChildren", ignore = true)
    @Mapping(target = "theStructure", ignore = true)
    @Mapping(source = "parentNodeId", target = "parentNode")
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
