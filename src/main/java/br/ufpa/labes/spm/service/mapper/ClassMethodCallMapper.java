package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ClassMethodCallDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link ClassMethodCall} and its DTO {@link ClassMethodCallDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface ClassMethodCallMapper extends EntityMapper<ClassMethodCallDTO, ClassMethodCall> {

  default ClassMethodCall fromId(Long id) {
    if (id == null) {
      return null;
    }
    ClassMethodCall classMethodCall = new ClassMethodCall();
    classMethodCall.setId(id);
    return classMethodCall;
  }
}
