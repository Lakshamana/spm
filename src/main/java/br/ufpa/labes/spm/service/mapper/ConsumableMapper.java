package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ConsumableDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Consumable} and its DTO {@link ConsumableDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface ConsumableMapper extends EntityMapper<ConsumableDTO, Consumable> {

  default Consumable fromId(Long id) {
    if (id == null) {
      return null;
    }
    Consumable consumable = new Consumable();
    consumable.setId(id);
    return consumable;
  }
}
