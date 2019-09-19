package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.processModels.IDescriptionDAO;


import br.ufpa.labes.spm.domain.Description;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Description entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DescriptionRepository extends IDescriptionDAO, JpaRepository<Description, Long> {

}
