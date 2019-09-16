package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.EmailConfigurationDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link EmailConfiguration} and its DTO {@link EmailConfigurationDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface EmailConfigurationMapper
    extends EntityMapper<EmailConfigurationDTO, EmailConfiguration> {

  @Mapping(target = "theProcesses", ignore = true)
  @Mapping(target = "removeTheProcesses", ignore = true)
  @Mapping(target = "theManagers", ignore = true)
  @Mapping(target = "removeTheManagers", ignore = true)
  EmailConfiguration toEntity(EmailConfigurationDTO emailConfigurationDTO);

  default EmailConfiguration fromId(Long id) {
    if (id == null) {
      return null;
    }
    EmailConfiguration emailConfiguration = new EmailConfiguration();
    emailConfiguration.setId(id);
    return emailConfiguration;
  }
}
