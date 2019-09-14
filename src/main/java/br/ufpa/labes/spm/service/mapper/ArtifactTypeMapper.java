package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ArtifactTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArtifactType} and its DTO {@link ArtifactTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ArtifactTypeMapper extends EntityMapper<ArtifactTypeDTO, ArtifactType> {


    @Mapping(target = "theTypeSuper", ignore = true)
    @Mapping(target = "theArtifacts", ignore = true)
    @Mapping(target = "removeTheArtifact", ignore = true)
    @Mapping(target = "theArtifactCons", ignore = true)
    @Mapping(target = "removeTheArtifactCon", ignore = true)
    @Mapping(target = "theInvolvedArtifacts", ignore = true)
    @Mapping(target = "removeTheInvolvedArtifact", ignore = true)
    @Mapping(target = "theSubroutines", ignore = true)
    @Mapping(target = "removeTheSubroutine", ignore = true)
    @Mapping(target = "theToolParameters", ignore = true)
    @Mapping(target = "removeTheToolParameter", ignore = true)
    ArtifactType toEntity(ArtifactTypeDTO artifactTypeDTO);

    default ArtifactType fromId(Long id) {
        if (id == null) {
            return null;
        }
        ArtifactType artifactType = new ArtifactType();
        artifactType.setId(id);
        return artifactType;
    }
}
