package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.SubroutineDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Subroutine} and its DTO {@link SubroutineDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {ArtifactTypeMapper.class})
public interface SubroutineMapper extends EntityMapper<SubroutineDTO, Subroutine> {

  @Mapping(source = "theArtifactType.id", target = "theArtifactTypeId")
  SubroutineDTO toDto(Subroutine subroutine);

  @Mapping(source = "theArtifactTypeId", target = "theArtifactType")
  @Mapping(target = "theAutomatic", ignore = true)
  @Mapping(target = "theToolParameters", ignore = true)
  @Mapping(target = "removeTheToolParameters", ignore = true)
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
