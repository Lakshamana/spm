package br.ufpa.labes.spm.repository;


import br.ufpa.labes.spm.domain.ArtifactParam;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ArtifactParam entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtifactParamRepository extends JpaRepository<ArtifactParam, Long> {

}
