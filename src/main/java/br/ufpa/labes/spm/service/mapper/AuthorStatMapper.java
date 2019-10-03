package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AuthorStatDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AuthorStat} and its DTO {@link AuthorStatDTO}.
 */
@Mapper(componentModel = "spring", uses = {AssetMapper.class, AuthorMapper.class})
public interface AuthorStatMapper extends EntityMapper<AuthorStatDTO, AuthorStat> {

    @Mapping(source = "asset.id", target = "assetId")
    @Mapping(source = "author.id", target = "authorId")
    AuthorStatDTO toDto(AuthorStat authorStat);

    @Mapping(source = "assetId", target = "asset")
    @Mapping(source = "authorId", target = "author")
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
