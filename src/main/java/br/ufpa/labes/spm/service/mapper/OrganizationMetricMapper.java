package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.OrganizationMetricDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrganizationMetric} and its DTO {@link OrganizationMetricDTO}.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class, CompanyMapper.class})
public interface OrganizationMetricMapper extends EntityMapper<OrganizationMetricDTO, OrganizationMetric> {

    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "company.id", target = "companyId")
    OrganizationMetricDTO toDto(OrganizationMetric organizationMetric);

    @Mapping(source = "organizationId", target = "organization")
    @Mapping(source = "companyId", target = "company")
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
