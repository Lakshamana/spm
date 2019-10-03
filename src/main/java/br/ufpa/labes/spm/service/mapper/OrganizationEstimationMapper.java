package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.OrganizationEstimationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrganizationEstimation} and its DTO {@link OrganizationEstimationDTO}.
 */
@Mapper(componentModel = "spring", uses = {OrganizationMapper.class, CompanyMapper.class})
public interface OrganizationEstimationMapper extends EntityMapper<OrganizationEstimationDTO, OrganizationEstimation> {

    @Mapping(source = "theOrganization.id", target = "theOrganizationId")
    @Mapping(source = "company.id", target = "companyId")
    OrganizationEstimationDTO toDto(OrganizationEstimation organizationEstimation);

    @Mapping(source = "theOrganizationId", target = "theOrganization")
    @Mapping(source = "companyId", target = "company")
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
