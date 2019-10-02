package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.JoinConDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link JoinCon} and its DTO {@link JoinConDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {MultipleConMapper.class, ActivityMapper.class})
public interface JoinConMapper extends EntityMapper<JoinConDTO, JoinCon> {

  @Mapping(source = "toMultipleCon.id", target = "toMultipleConId")
  @Mapping(source = "toActivity.id", target = "toActivityId")
  JoinConDTO toDto(JoinCon joinCon);

  @Mapping(source = "toMultipleConId", target = "toMultipleCon")
  @Mapping(target = "removeFromMultipleCon", ignore = true)
  @Mapping(source = "toActivityId", target = "toActivity")
  @Mapping(target = "fromActivities", ignore = true)
  @Mapping(target = "removeFromActivity", ignore = true)
  JoinCon toEntity(JoinConDTO joinConDTO);

  default JoinCon fromId(Long id) {
    if (id == null) {
      return null;
    }
    JoinCon joinCon = new JoinCon();
    joinCon.setId(id);
    return joinCon;
  }
}
