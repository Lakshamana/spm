package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.OrganizationEstimation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the OrganizationEstimation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrganizationEstimationRepository extends JpaRepository<OrganizationEstimation, Long> {

}
