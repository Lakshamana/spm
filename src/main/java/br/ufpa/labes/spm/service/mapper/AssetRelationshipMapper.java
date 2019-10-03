package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AssetRelationshipDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetRelationship} and its DTO {@link AssetRelationshipDTO}.
 */
@Mapper(componentModel = "spring", uses = {RelationshipKindMapper.class, AssetMapper.class})
public interface AssetRelationshipMapper extends EntityMapper<AssetRelationshipDTO, AssetRelationship> {

    @Mapping(source = "kind.id", target = "kindId")
    @Mapping(source = "asset.id", target = "assetId")
    @Mapping(source = "relatedAsset.id", target = "relatedAssetId")
    AssetRelationshipDTO toDto(AssetRelationship assetRelationship);

    @Mapping(source = "kindId", target = "kind")
    @Mapping(source = "assetId", target = "asset")
    @Mapping(source = "relatedAssetId", target = "relatedAsset")
    AssetRelationship toEntity(AssetRelationshipDTO assetRelationshipDTO);

    default AssetRelationship fromId(Long id) {
        if (id == null) {
            return null;
        }
        AssetRelationship assetRelationship = new AssetRelationship();
        assetRelationship.setId(id);
        return assetRelationship;
    }
}
