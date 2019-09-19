package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.PeopleInstSug;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PeopleInstSug entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeopleInstSugRepository extends JpaRepository<PeopleInstSug, Long> {

}
