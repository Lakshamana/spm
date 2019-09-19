package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.resources.IConsumableDAO;


import br.ufpa.labes.spm.domain.Consumable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Consumable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsumableRepository extends IConsumableDAO, JpaRepository<Consumable, Long> {

}
