package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.SpmConfigurationDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link SpmConfiguration} and its DTO {@link SpmConfigurationDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface SpmConfigurationMapper
    extends EntityMapper<SpmConfigurationDTO, SpmConfiguration> {

  @Mapping(target = "agent", ignore = true)
  SpmConfiguration toEntity(SpmConfigurationDTO spmConfigurationDTO);

  default SpmConfiguration fromId(Long id) {
    if (id == null) {
      return null;
    }
    SpmConfiguration spmConfiguration = new SpmConfiguration();
    spmConfiguration.setId(id);
    return spmConfiguration;
  }
}
