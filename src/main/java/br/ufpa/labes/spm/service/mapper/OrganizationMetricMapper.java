package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.OrganizationMetricDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrganizationMetric} and its DTO {@link OrganizationMetricDTO}.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class, CompanyMapper.class})
public interface OrganizationMetricMapper extends EntityMapper<OrganizationMetricDTO, OrganizationMetric> {

    @Mapping(source = "theOrganization.id", target = "theOrganizationId")
    @Mapping(source = "theCompany.id", target = "theCompanyId")
    OrganizationMetricDTO toDto(OrganizationMetric organizationMetric);

    @Mapping(source = "theOrganizationId", target = "theOrganization")
    @Mapping(target = "theMetricSuper", ignore = true)
    @Mapping(source = "theCompanyId", target = "theCompany")
    OrganizationMetric toEntity(OrganizationMetricDTO organizationMetricDTO);

    default OrganizationMetric fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrganizationMetric organizationMetric = new OrganizationMetric();
        organizationMetric.setId(id);
        return organizationMetric;
    }
}
