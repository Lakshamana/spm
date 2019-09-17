package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ParameterDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Parameter} and its DTO {@link ParameterDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {AutomaticMapper.class})
public interface ParameterMapper extends EntityMapper<ParameterDTO, Parameter> {

  @Mapping(source = "theAutomatic.id", target = "theAutomaticId")
  ParameterDTO toDto(Parameter parameter);

  @Mapping(source = "theAutomaticId", target = "theAutomatic")
  Parameter toEntity(ParameterDTO parameterDTO);

  default Parameter fromId(Long id) {
    if (id == null) {
      return null;
    }
    Parameter parameter = new Parameter();
    parameter.setId(id);
    return parameter;
  }
}
