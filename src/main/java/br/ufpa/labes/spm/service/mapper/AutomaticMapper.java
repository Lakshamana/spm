package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AutomaticDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Automatic} and its DTO {@link AutomaticDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {SubroutineMapper.class, ArtifactMapper.class})
public interface AutomaticMapper extends EntityMapper<AutomaticDTO, Automatic> {

  @Mapping(source = "theSubroutine.id", target = "theSubroutineId")
  @Mapping(source = "theArtifact.id", target = "theArtifactId")
  AutomaticDTO toDto(Automatic automatic);

  @Mapping(source = "theSubroutineId", target = "theSubroutine")
  @Mapping(target = "thePlainSuper", ignore = true)
  @Mapping(source = "theArtifactId", target = "theArtifact")
  @Mapping(target = "theParameters", ignore = true)
  @Mapping(target = "removeTheParameters", ignore = true)
  Automatic toEntity(AutomaticDTO automaticDTO);

  default Automatic fromId(Long id) {
    if (id == null) {
      return null;
    }
    Automatic automatic = new Automatic();
    automatic.setId(id);
    return automatic;
  }
}
