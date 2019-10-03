package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.LessonLearned;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the LessonLearned entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LessonLearnedRepository extends JpaRepository<LessonLearned, Long> {

}
