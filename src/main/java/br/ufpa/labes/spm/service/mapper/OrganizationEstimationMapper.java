package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.OrganizationEstimationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrganizationEstimation} and its DTO {@link
 * OrganizationEstimationDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {OrganizationMapper.class, CompanyMapper.class})
public interface OrganizationEstimationMapper
    extends EntityMapper<OrganizationEstimationDTO, OrganizationEstimation> {

  @Mapping(source = "theOrganization.id", target = "theOrganizationId")
  @Mapping(source = "theCompany.id", target = "theCompanyId")
  OrganizationEstimationDTO toDto(OrganizationEstimation organizationEstimation);

  @Mapping(source = "theOrganizationId", target = "theOrganization")
  @Mapping(target = "theEstimationSuper", ignore = true)
  @Mapping(source = "theCompanyId", target = "theCompany")
  OrganizationEstimation toEntity(OrganizationEstimationDTO organizationEstimationDTO);

  default OrganizationEstimation fromId(Long id) {
    if (id == null) {
      return null;
    }
    OrganizationEstimation organizationEstimation = new OrganizationEstimation();
    organizationEstimation.setId(id);
    return organizationEstimation;
  }
}
