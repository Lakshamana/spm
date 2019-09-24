package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ToolTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ToolType} and its DTO {@link ToolTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ToolTypeMapper extends EntityMapper<ToolTypeDTO, ToolType> {


    @Mapping(target = "theToolDefinitions", ignore = true)
    @Mapping(target = "removeTheToolDefinition", ignore = true)
    ToolType toEntity(ToolTypeDTO toolTypeDTO);

    default ToolType fromId(Long id) {
        if (id == null) {
            return null;
        }
        ToolType toolType = new ToolType();
        toolType.setId(id);
        return toolType;
    }
}
