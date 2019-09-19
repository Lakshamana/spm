package br.ufpa.labes.spm.repository;


import br.ufpa.labes.spm.domain.ResourceInstSug;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ResourceInstSug entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceInstSugRepository extends JpaRepository<ResourceInstSug, Long> {

}
