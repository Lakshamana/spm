package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.plainActivities.IReqAgentRequiresAbilityDAO;


import br.ufpa.labes.spm.domain.ReqAgentRequiresAbility;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ReqAgentRequiresAbility entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReqAgentRequiresAbilityRepository extends IReqAgentRequiresAbilityDAO, JpaRepository<ReqAgentRequiresAbility, Long> {

}
