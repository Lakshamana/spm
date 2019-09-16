package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AuthorDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Author} and its DTO {@link AuthorDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {UserMapper.class, OrganizationMapper.class, PersonMapper.class})
public interface AuthorMapper extends EntityMapper<AuthorDTO, Author> {

  @Mapping(source = "user.id", target = "userId")
  @Mapping(source = "theOrganizationSub.id", target = "theOrganizationSubId")
  @Mapping(source = "thePersonSub.id", target = "thePersonSubId")
  AuthorDTO toDto(Author author);

  @Mapping(source = "userId", target = "user")
  @Mapping(source = "theOrganizationSubId", target = "theOrganizationSub")
  @Mapping(source = "thePersonSubId", target = "thePersonSub")
  @Mapping(target = "removeAuthorsFollowed", ignore = true)
  @Mapping(target = "theAssets", ignore = true)
  @Mapping(target = "removeTheAssets", ignore = true)
  @Mapping(target = "theAuthorStats", ignore = true)
  @Mapping(target = "removeTheAuthorStats", ignore = true)
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
  @Mapping(target = "theAuthors", ignore = true)
  @Mapping(target = "removeTheAuthor", ignore = true)
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
