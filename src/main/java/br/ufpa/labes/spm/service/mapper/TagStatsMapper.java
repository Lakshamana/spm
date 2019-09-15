package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.TagStatDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TagStats} and its DTO {@link TagStatDTO}.
 */
@Mapper(componentModel = "spring", uses = {TagMapper.class, AssetMapper.class})
public interface TagStatMapper extends EntityMapper<TagStatDTO, TagStats> {

    @Mapping(source = "tag.id", target = "tagId")
    @Mapping(source = "theAsset.id", target = "theAssetId")
    TagStatDTO toDto(TagStats tagStat);

    @Mapping(source = "tagId", target = "tag")
    @Mapping(source = "theAssetId", target = "theAsset")
    TagStats toEntity(TagStatDTO tagStatDTO);

    default TagStats fromId(Long id) {
        if (id == null) {
            return null;
        }
        TagStats tagStat = new TagStats();
        tagStat.setId(id);
        return tagStat;
    }
}
