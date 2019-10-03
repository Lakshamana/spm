package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.StructureDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Structure} and its DTO {@link StructureDTO}.
 */
@Mapper(componentModel = "spring", uses = {NodeMapper.class})
public interface StructureMapper extends EntityMapper<StructureDTO, Structure> {

    @Mapping(source = "rootElement.id", target = "rootElementId")
    StructureDTO toDto(Structure structure);

    @Mapping(source = "rootElementId", target = "rootElement")
    @Mapping(target = "theRepositories", ignore = true)
    @Mapping(target = "removeTheRepository", ignore = true)
    Structure toEntity(StructureDTO structureDTO);

    default Structure fromId(Long id) {
        if (id == null) {
            return null;
        }
        Structure structure = new Structure();
        structure.setId(id);
        return structure;
    }
}
