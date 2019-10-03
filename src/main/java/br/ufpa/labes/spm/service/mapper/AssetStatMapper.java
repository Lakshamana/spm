package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AssetStatDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetStat} and its DTO {@link AssetStatDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AssetStatMapper extends EntityMapper<AssetStatDTO, AssetStat> {


    @Mapping(target = "theAsset", ignore = true)
    AssetStat toEntity(AssetStatDTO assetStatDTO);

    default AssetStat fromId(Long id) {
        if (id == null) {
            return null;
        }
        AssetStat assetStat = new AssetStat();
        assetStat.setId(id);
        return assetStat;
    }
}
