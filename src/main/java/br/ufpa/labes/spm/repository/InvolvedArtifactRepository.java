package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.InvolvedArtifact;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the InvolvedArtifact entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvolvedArtifactRepository extends JpaRepository<InvolvedArtifact, Long> {

}
