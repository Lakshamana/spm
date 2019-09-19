package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AssetDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Asset} and its DTO {@link AssetDTO}.
 */
@Mapper(componentModel = "spring", uses = {AssetStatMapper.class, AuthorMapper.class})
public interface AssetMapper extends EntityMapper<AssetDTO, Asset> {

    @Mapping(source = "stats.id", target = "statsId")
    @Mapping(source = "owner.id", target = "ownerId")
    AssetDTO toDto(Asset asset);

    @Mapping(source = "statsId", target = "stats")
    @Mapping(target = "authorStats", ignore = true)
    @Mapping(target = "removeAuthorStats", ignore = true)
    @Mapping(target = "tagStats", ignore = true)
    @Mapping(target = "removeTagStats", ignore = true)
    @Mapping(target = "lessonsLearneds", ignore = true)
    @Mapping(target = "removeLessonsLearned", ignore = true)
    @Mapping(target = "relatedAssets", ignore = true)
    @Mapping(target = "removeRelatedAssets", ignore = true)
    @Mapping(target = "relatedByAssets", ignore = true)
    @Mapping(target = "removeRelatedByAssets", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "removeComments", ignore = true)
    @Mapping(source = "ownerId", target = "owner")
    @Mapping(target = "removeFavoritedBy", ignore = true)
    @Mapping(target = "removeFollowers", ignore = true)
    @Mapping(target = "removeCollaborators", ignore = true)
    Asset toEntity(AssetDTO assetDTO);

    default Asset fromId(Long id) {
        if (id == null) {
            return null;
        }
        Asset asset = new Asset();
        asset.setId(id);
        return asset;
    }
}
