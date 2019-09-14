package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ArtifactConDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArtifactCon} and its DTO {@link ArtifactConDTO}.
 */
@Mapper(componentModel = "spring", uses = {ArtifactMapper.class, ArtifactTypeMapper.class, MultipleConMapper.class})
public interface ArtifactConMapper extends EntityMapper<ArtifactConDTO, ArtifactCon> {

    @Mapping(source = "theArtifact.id", target = "theArtifactId")
    @Mapping(source = "theArtifactType.id", target = "theArtifactTypeId")
    ArtifactConDTO toDto(ArtifactCon artifactCon);

    @Mapping(source = "theArtifactId", target = "theArtifact")
    @Mapping(source = "theArtifactTypeId", target = "theArtifactType")
    @Mapping(target = "removeToMultipleCon", ignore = true)
    @Mapping(target = "theConnectionSuper", ignore = true)
    @Mapping(target = "toActivities", ignore = true)
    @Mapping(target = "removeToActivity", ignore = true)
    @Mapping(target = "fromActivities", ignore = true)
    @Mapping(target = "removeFromActivity", ignore = true)
    ArtifactCon toEntity(ArtifactConDTO artifactConDTO);

    default ArtifactCon fromId(Long id) {
        if (id == null) {
            return null;
        }
        ArtifactCon artifactCon = new ArtifactCon();
        artifactCon.setId(id);
        return artifactCon;
    }
}
