package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ToolParameterDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ToolParameter} and its DTO {@link ToolParameterDTO}.
 */
@Mapper(componentModel = "spring", uses = {ArtifactTypeMapper.class, SubroutineMapper.class, PrimitiveTypeMapper.class})
public interface ToolParameterMapper extends EntityMapper<ToolParameterDTO, ToolParameter> {

    @Mapping(source = "theArtifactType.id", target = "theArtifactTypeId")
    @Mapping(source = "theSubroutine.id", target = "theSubroutineId")
    @Mapping(source = "thePrimitiveType.id", target = "thePrimitiveTypeId")
    ToolParameterDTO toDto(ToolParameter toolParameter);

    @Mapping(source = "theArtifactTypeId", target = "theArtifactType")
    @Mapping(source = "theSubroutineId", target = "theSubroutine")
    @Mapping(source = "thePrimitiveTypeId", target = "thePrimitiveType")
    ToolParameter toEntity(ToolParameterDTO toolParameterDTO);

    default ToolParameter fromId(Long id) {
        if (id == null) {
            return null;
        }
        ToolParameter toolParameter = new ToolParameter();
        toolParameter.setId(id);
        return toolParameter;
    }
}
