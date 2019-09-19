package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.CompanyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Company} and its DTO {@link CompanyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompanyMapper extends EntityMapper<CompanyDTO, Company> {


    @Mapping(target = "organizationMetrics", ignore = true)
    @Mapping(target = "removeOrganizationMetric", ignore = true)
    @Mapping(target = "theCompanyEstimations", ignore = true)
    @Mapping(target = "removeTheCompanyEstimation", ignore = true)
    @Mapping(target = "theCompanyUnits", ignore = true)
    @Mapping(target = "removeTheCompanyUnit", ignore = true)
    @Mapping(target = "theDevelopingSystems", ignore = true)
    @Mapping(target = "removeTheDevelopingSystem", ignore = true)
    Company toEntity(CompanyDTO companyDTO);

    default Company fromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
