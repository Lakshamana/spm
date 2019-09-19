package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.plainActivities.IParameterDAO;


import br.ufpa.labes.spm.domain.Parameter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Parameter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParameterRepository extends IParameterDAO, JpaRepository<Parameter, Long> {

}
