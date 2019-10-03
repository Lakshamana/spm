package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.GraphicCoordinateDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link GraphicCoordinate} and its DTO {@link GraphicCoordinateDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GraphicCoordinateMapper extends EntityMapper<GraphicCoordinateDTO, GraphicCoordinate> {


    @Mapping(target = "theObjectReference", ignore = true)
    GraphicCoordinate toEntity(GraphicCoordinateDTO graphicCoordinateDTO);

    default GraphicCoordinate fromId(Long id) {
        if (id == null) {
            return null;
        }
        GraphicCoordinate graphicCoordinate = new GraphicCoordinate();
        graphicCoordinate.setId(id);
        return graphicCoordinate;
    }
}
