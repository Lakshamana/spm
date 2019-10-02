package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.Process;
import br.ufpa.labes.spm.service.dto.ProcessDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Process} and its DTO {@link ProcessDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {ProcessModelMapper.class, ActivityTypeMapper.class, EmailConfigurationMapper.class})
public interface ProcessMapper extends EntityMapper<ProcessDTO, Process> {

  @Mapping(source = "theProcessModel.id", target = "theProcessModelId")
  @Mapping(source = "theActivityType.id", target = "theActivityTypeId")
  @Mapping(source = "theEmailConfiguration.id", target = "theEmailConfigurationId")
  ProcessDTO toDto(Process process);

  @Mapping(source = "theProcessModelId", target = "theProcessModel")
  @Mapping(target = "theProcessAgenda", ignore = true)
  @Mapping(target = "removeTheProcessAgenda", ignore = true)
  @Mapping(source = "theActivityTypeId", target = "theActivityType")
  @Mapping(target = "theLog", ignore = true)
  @Mapping(source = "theEmailConfigurationId", target = "theEmailConfiguration")
  @Mapping(target = "theProcessEvents", ignore = true)
  @Mapping(target = "removeTheProcessEvent", ignore = true)
  @Mapping(target = "theProjects", ignore = true)
  @Mapping(target = "removeTheProject", ignore = true)
  @Mapping(target = "theProcessEstimations", ignore = true)
  @Mapping(target = "removeTheProcessEstimation", ignore = true)
  @Mapping(target = "theProcessMetrics", ignore = true)
  @Mapping(target = "removeTheProcessMetric", ignore = true)
  @Mapping(target = "theAgents", ignore = true)
  @Mapping(target = "removeTheAgent", ignore = true)
  Process toEntity(ProcessDTO processDTO);

  default Process fromId(Long id) {
    if (id == null) {
      return null;
    }
    Process process = new Process();
    process.setId(id);
    return process;
  }
}
