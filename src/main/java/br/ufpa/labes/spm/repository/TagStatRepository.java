package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.TagStat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TagStat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagStatRepository extends JpaRepository<TagStat, Long> {

}
