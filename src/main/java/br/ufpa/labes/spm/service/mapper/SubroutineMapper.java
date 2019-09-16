package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.SubroutineDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Subroutine} and its DTO {@link SubroutineDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {ClassMethodCallMapper.class, ScriptMapper.class, ArtifactTypeMapper.class})
public interface SubroutineMapper extends EntityMapper<SubroutineDTO, Subroutine> {

  @Mapping(source = "theClassMethodCallSub.id", target = "theClassMethodCallSubId")
  @Mapping(source = "theScriptSub.id", target = "theScriptSubId")
  @Mapping(source = "theArtifactType.id", target = "theArtifactTypeId")
  SubroutineDTO toDto(Subroutine subroutine);

  @Mapping(source = "theClassMethodCallSubId", target = "theClassMethodCallSub")
  @Mapping(source = "theScriptSubId", target = "theScriptSub")
  @Mapping(source = "theArtifactTypeId", target = "theArtifactType")
  @Mapping(target = "theAutomatic", ignore = true)
  @Mapping(target = "theToolParameters", ignore = true)
  @Mapping(target = "removeTheToolParameter", ignore = true)
  Subroutine toEntity(SubroutineDTO subroutineDTO);

  default Subroutine fromId(Long id) {
    if (id == null) {
      return null;
    }
    Subroutine subroutine = new Subroutine();
    subroutine.setId(id);
    return subroutine;
  }
}
