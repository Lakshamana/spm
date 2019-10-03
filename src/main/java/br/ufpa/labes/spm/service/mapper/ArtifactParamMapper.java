package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ArtifactParamDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArtifactParam} and its DTO {@link ArtifactParamDTO}.
 */
@Mapper(componentModel = "spring", uses = {ArtifactMapper.class})
public interface ArtifactParamMapper extends EntityMapper<ArtifactParamDTO, ArtifactParam> {

    @Mapping(source = "theArtifact.id", target = "theArtifactId")
    ArtifactParamDTO toDto(ArtifactParam artifactParam);

    @Mapping(source = "theArtifactId", target = "theArtifact")
    ArtifactParam toEntity(ArtifactParamDTO artifactParamDTO);

    default ArtifactParam fromId(Long id) {
        if (id == null) {
            return null;
        }
        ArtifactParam artifactParam = new ArtifactParam();
        artifactParam.setId(id);
        return artifactParam;
    }
}
