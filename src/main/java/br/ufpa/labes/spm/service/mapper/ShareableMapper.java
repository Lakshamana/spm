package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ShareableDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Shareable} and its DTO {@link ShareableDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface ShareableMapper extends EntityMapper<ShareableDTO, Shareable> {

  default Shareable fromId(Long id) {
    if (id == null) {
      return null;
    }
    Shareable shareable = new Shareable();
    shareable.setId(id);
    return shareable;
  }
}
