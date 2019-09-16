package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ScriptDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Script} and its DTO {@link ScriptDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface ScriptMapper extends EntityMapper<ScriptDTO, Script> {

  @Mapping(target = "theSubroutineSuper", ignore = true)
  Script toEntity(ScriptDTO scriptDTO);

  default Script fromId(Long id) {
    if (id == null) {
      return null;
    }
    Script script = new Script();
    script.setId(id);
    return script;
  }
}
