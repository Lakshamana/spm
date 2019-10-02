package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.FeedbackDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Feedback} and its DTO {@link FeedbackDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface FeedbackMapper extends EntityMapper<FeedbackDTO, Feedback> {

  default Feedback fromId(Long id) {
    if (id == null) {
      return null;
    }
    Feedback feedback = new Feedback();
    feedback.setId(id);
    return feedback;
  }
}
