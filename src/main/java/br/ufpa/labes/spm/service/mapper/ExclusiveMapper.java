package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ExclusiveDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Exclusive} and its DTO {@link ExclusiveDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface ExclusiveMapper extends EntityMapper<ExclusiveDTO, Exclusive> {

  @Mapping(target = "theReservations", ignore = true)
  @Mapping(target = "removeTheReservation", ignore = true)
  Exclusive toEntity(ExclusiveDTO exclusiveDTO);

  default Exclusive fromId(Long id) {
    if (id == null) {
      return null;
    }
    Exclusive exclusive = new Exclusive();
    exclusive.setId(id);
    return exclusive;
  }
}
