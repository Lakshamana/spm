package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.ICompanyUnitDAO;


import br.ufpa.labes.spm.domain.CompanyUnit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CompanyUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyUnitRepository extends ICompanyUnitDAO, JpaRepository<CompanyUnit, Long> {

}
