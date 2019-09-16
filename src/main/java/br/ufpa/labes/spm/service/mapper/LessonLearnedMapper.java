package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.LessonLearnedDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link LessonLearned} and its DTO {@link LessonLearnedDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {AuthorMapper.class, AssetMapper.class})
public interface LessonLearnedMapper extends EntityMapper<LessonLearnedDTO, LessonLearned> {

  @Mapping(source = "author.id", target = "authorId")
  @Mapping(source = "theAsset.id", target = "theAssetId")
  LessonLearnedDTO toDto(LessonLearned lessonLearned);

  @Mapping(source = "authorId", target = "author")
  @Mapping(source = "theAssetId", target = "theAsset")
  LessonLearned toEntity(LessonLearnedDTO lessonLearnedDTO);

  default LessonLearned fromId(Long id) {
    if (id == null) {
      return null;
    }
    LessonLearned lessonLearned = new LessonLearned();
    lessonLearned.setId(id);
    return lessonLearned;
  }
}
