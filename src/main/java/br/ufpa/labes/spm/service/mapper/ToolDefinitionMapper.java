package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ToolDefinitionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ToolDefinition} and its DTO {@link ToolDefinitionDTO}.
 */
@Mapper(componentModel = "spring", uses = {ToolTypeMapper.class, ArtifactTypeMapper.class})
public interface ToolDefinitionMapper extends EntityMapper<ToolDefinitionDTO, ToolDefinition> {

    @Mapping(source = "theToolType.id", target = "theToolTypeId")
    ToolDefinitionDTO toDto(ToolDefinition toolDefinition);

    @Mapping(source = "theToolTypeId", target = "theToolType")
    @Mapping(target = "removeTheArtifactTypes", ignore = true)
    ToolDefinition toEntity(ToolDefinitionDTO toolDefinitionDTO);

    default ToolDefinition fromId(Long id) {
        if (id == null) {
            return null;
        }
        ToolDefinition toolDefinition = new ToolDefinition();
        toolDefinition.setId(id);
        return toolDefinition;
    }
}
