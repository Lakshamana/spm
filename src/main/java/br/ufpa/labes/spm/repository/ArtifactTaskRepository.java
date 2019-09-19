package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.artifacts.IArtifactTaskDAO;


import br.ufpa.labes.spm.domain.ArtifactTask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ArtifactTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtifactTaskRepository extends IArtifactTaskDAO, JpaRepository<ArtifactTask, Long> {

}
