package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AuthorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Author} and its DTO {@link AuthorDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AuthorMapper extends EntityMapper<AuthorDTO, Author> {

    @Mapping(source = "user.id", target = "userId")
    AuthorDTO toDto(Author author);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "stats", ignore = true)
    @Mapping(target = "removeStats", ignore = true)
    @Mapping(target = "removeAuthorsFollowed", ignore = true)
    @Mapping(target = "assets", ignore = true)
    @Mapping(target = "removeAssets", ignore = true)
    @Mapping(target = "theLessonLearneds", ignore = true)
    @Mapping(target = "removeTheLessonLearned", ignore = true)
    @Mapping(target = "sentMessages", ignore = true)
    @Mapping(target = "removeSentMessages", ignore = true)
    @Mapping(target = "receivedMessages", ignore = true)
    @Mapping(target = "removeReceivedMessages", ignore = true)
    @Mapping(target = "favorites", ignore = true)
    @Mapping(target = "removeFavorites", ignore = true)
    @Mapping(target = "assetsFolloweds", ignore = true)
    @Mapping(target = "removeAssetsFollowed", ignore = true)
    @Mapping(target = "collaborateOnAssets", ignore = true)
    @Mapping(target = "removeCollaborateOnAssets", ignore = true)
    @Mapping(target = "followers", ignore = true)
    @Mapping(target = "removeFollowers", ignore = true)
    Author toEntity(AuthorDTO authorDTO);

    default Author fromId(Long id) {
        if (id == null) {
            return null;
        }
        Author author = new Author();
        author.setId(id);
        return author;
    }
}
