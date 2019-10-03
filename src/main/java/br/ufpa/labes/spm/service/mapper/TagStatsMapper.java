package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.TagStatsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link TagStats} and its DTO {@link TagStatsDTO}.
 */
@Mapper(componentModel = "spring", uses = {TagMapper.class, AssetMapper.class})
public interface TagStatsMapper extends EntityMapper<TagStatsDTO, TagStats> {

    @Mapping(source = "tag.id", target = "tagId")
    @Mapping(source = "asset.id", target = "assetId")
    TagStatsDTO toDto(TagStats tagStats);

    @Mapping(source = "tagId", target = "tag")
    @Mapping(source = "assetId", target = "asset")
    TagStats toEntity(TagStatsDTO tagStatsDTO);

    default TagStats fromId(Long id) {
        if (id == null) {
            return null;
        }
        TagStats tagStats = new TagStats();
        tagStats.setId(id);
        return tagStats;
    }
}
