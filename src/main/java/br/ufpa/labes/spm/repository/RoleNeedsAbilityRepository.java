package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.agent.IRoleNeedsAbilityDAO;


import br.ufpa.labes.spm.domain.RoleNeedsAbility;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RoleNeedsAbility entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleNeedsAbilityRepository extends IRoleNeedsAbilityDAO, JpaRepository<RoleNeedsAbility, Long> {

}
