package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.OrganizationDTO;

import org.mapstruct.*;

/** Mapper for the entity {@link Organization} and its DTO {@link OrganizationDTO}. */
@Mapper(
    componentModel = "spring",
    uses = {})
public interface OrganizationMapper extends EntityMapper<OrganizationDTO, Organization> {

  @Mapping(target = "organizationMetrics", ignore = true)
  @Mapping(target = "removeOrganizationMetric", ignore = true)
  @Mapping(target = "organizationEstimations", ignore = true)
  @Mapping(target = "removeOrganizationEstimation", ignore = true)
  Organization toEntity(OrganizationDTO organizationDTO);

  default Organization fromId(Long id) {
    if (id == null) {
      return null;
    }
    Organization organization = new Organization();
    organization.setId(id);
    return organization;
  }
}
