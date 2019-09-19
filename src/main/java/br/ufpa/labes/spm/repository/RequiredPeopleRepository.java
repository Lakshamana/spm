package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.plainActivities.IRequiredPeopleDAO;


import br.ufpa.labes.spm.domain.RequiredPeople;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RequiredPeople entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequiredPeopleRepository extends IRequiredPeopleDAO, JpaRepository<RequiredPeople, Long> {

}
