package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.OutOfWorkPeriodDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link OutOfWorkPeriod} and its DTO {@link OutOfWorkPeriodDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {AgentMapper.class})
public interface OutOfWorkPeriodMapper extends EntityMapper<OutOfWorkPeriodDTO, OutOfWorkPeriod> {

  @Mapping(source = "theAgent.id", target = "theAgentId")
  OutOfWorkPeriodDTO toDto(OutOfWorkPeriod outOfWorkPeriod);

  @Mapping(source = "theAgentId", target = "theAgent")
  OutOfWorkPeriod toEntity(OutOfWorkPeriodDTO outOfWorkPeriodDTO);

  default OutOfWorkPeriod fromId(Long id) {
    if (id == null) {
      return null;
    }
    OutOfWorkPeriod outOfWorkPeriod = new OutOfWorkPeriod();
    outOfWorkPeriod.setId(id);
    return outOfWorkPeriod;
  }
}
