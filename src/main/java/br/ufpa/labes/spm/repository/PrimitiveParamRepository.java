package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.plainActivities.IPrimitiveParamDAO;


import br.ufpa.labes.spm.domain.PrimitiveParam;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PrimitiveParam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrimitiveParamRepository extends IPrimitiveParamDAO, JpaRepository<PrimitiveParam, Long> {

}
