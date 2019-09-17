package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.EstimationDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Estimation} and its DTO {@link EstimationDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {MetricDefinitionMapper.class})
public interface EstimationMapper extends EntityMapper<EstimationDTO, Estimation> {

  @Mapping(source = "theMetricDefinition.id", target = "theMetricDefinitionId")
  EstimationDTO toDto(Estimation estimation);

  @Mapping(source = "theMetricDefinitionId", target = "theMetricDefinition")
  Estimation toEntity(EstimationDTO estimationDTO);

  default Estimation fromId(Long id) {
    if (id == null) {
      return null;
    }
    Estimation estimation = new Estimation();
    estimation.setId(id);
    return estimation;
  }
}
