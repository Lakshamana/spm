package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.NormalDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Normal} and its DTO {@link NormalDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface NormalMapper extends EntityMapper<NormalDTO, Normal> {

  @Mapping(target = "theTasks", ignore = true)
  @Mapping(target = "removeTheTasks", ignore = true)
  @Mapping(target = "theReservations", ignore = true)
  @Mapping(target = "removeTheReservation", ignore = true)
  @Mapping(target = "theRequiredResources", ignore = true)
  @Mapping(target = "removeTheRequiredResource", ignore = true)
  @Mapping(target = "theRequiredPeople", ignore = true)
  @Mapping(target = "removeTheRequiredPeople", ignore = true)
  @Mapping(target = "theResourceEvent", ignore = true)
  @Mapping(target = "theAgendaEvents", ignore = true)
  @Mapping(target = "removeTheAgendaEvent", ignore = true)
  @Mapping(target = "involvedArtifactToNormals", ignore = true)
  @Mapping(target = "removeInvolvedArtifactToNormal", ignore = true)
  @Mapping(target = "involvedArtifactFromNormals", ignore = true)
  @Mapping(target = "removeInvolvedArtifactFromNormal", ignore = true)
  Normal toEntity(NormalDTO normalDTO);

  default Normal fromId(Long id) {
    if (id == null) {
      return null;
    }
    Normal normal = new Normal();
    normal.setId(id);
    return normal;
  }
}
