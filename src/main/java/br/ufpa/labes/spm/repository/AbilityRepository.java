package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.agent.IAbilityDAO;


import br.ufpa.labes.spm.domain.Ability;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Ability entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AbilityRepository extends IAbilityDAO, JpaRepository<Ability, Long> {

}
