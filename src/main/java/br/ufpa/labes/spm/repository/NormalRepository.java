package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.plainActivities.INormalDAO;


import br.ufpa.labes.spm.domain.Normal;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Normal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NormalRepository extends INormalDAO, JpaRepository<Normal, Long> {

}
