package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.TagStatDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TagStat} and its DTO {@link TagStatDTO}.
 */
@Mapper(componentModel = "spring", uses = {TagMapper.class, AssetMapper.class})
public interface TagStatMapper extends EntityMapper<TagStatDTO, TagStat> {

    @Mapping(source = "tag.id", target = "tagId")
    @Mapping(source = "theAsset.id", target = "theAssetId")
    TagStatDTO toDto(TagStat tagStat);

    @Mapping(source = "tagId", target = "tag")
    @Mapping(source = "theAssetId", target = "theAsset")
    TagStat toEntity(TagStatDTO tagStatDTO);

    default TagStat fromId(Long id) {
        if (id == null) {
            return null;
        }
        TagStat tagStat = new TagStat();
        tagStat.setId(id);
        return tagStat;
    }
}
