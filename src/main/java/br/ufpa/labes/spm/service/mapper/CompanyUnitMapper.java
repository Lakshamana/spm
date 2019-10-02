package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.CompanyUnitDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link CompanyUnit} and its DTO {@link CompanyUnitDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {CompanyMapper.class, AgentMapper.class})
public interface CompanyUnitMapper extends EntityMapper<CompanyUnitDTO, CompanyUnit> {

  @Mapping(source = "theOrganization.id", target = "theOrganizationId")
  @Mapping(source = "theCommand.id", target = "theCommandId")
  @Mapping(source = "theAgent.id", target = "theAgentId")
  CompanyUnitDTO toDto(CompanyUnit companyUnit);

  @Mapping(source = "theOrganizationId", target = "theOrganization")
  @Mapping(source = "theCommandId", target = "theCommand")
  @Mapping(source = "theAgentId", target = "theAgent")
  @Mapping(target = "theSubordinates", ignore = true)
  @Mapping(target = "removeTheSubordinates", ignore = true)
  @Mapping(target = "theUnitAgents", ignore = true)
  @Mapping(target = "removeTheUnitAgents", ignore = true)
  CompanyUnit toEntity(CompanyUnitDTO companyUnitDTO);

  default CompanyUnit fromId(Long id) {
    if (id == null) {
      return null;
    }
    CompanyUnit companyUnit = new CompanyUnit();
    companyUnit.setId(id);
    return companyUnit;
  }
}
