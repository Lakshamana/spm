package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.types.IActivityTypeDAO;


import br.ufpa.labes.spm.domain.ActivityType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ActivityType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityTypeRepository extends IActivityTypeDAO, JpaRepository<ActivityType, Long> {

}
