package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AuthorStatDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AuthorStat} and its DTO {@link AuthorStatDTO}.
 */
@Mapper(componentModel = "spring", uses = {AuthorMapper.class, AssetMapper.class})
public interface AuthorStatMapper extends EntityMapper<AuthorStatDTO, AuthorStat> {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "theAsset.id", target = "theAssetId")
    AuthorStatDTO toDto(AuthorStat authorStat);

    @Mapping(source = "authorId", target = "author")
    @Mapping(source = "theAssetId", target = "theAsset")
    AuthorStat toEntity(AuthorStatDTO authorStatDTO);

    default AuthorStat fromId(Long id) {
        if (id == null) {
            return null;
        }
        AuthorStat authorStat = new AuthorStat();
        authorStat.setId(id);
        return authorStat;
    }
}
