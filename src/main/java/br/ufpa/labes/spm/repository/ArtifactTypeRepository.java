package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.ArtifactType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/** Spring Data repository for the ArtifactType entity. */
@SuppressWarnings("unused")
@Repository
public interface ArtifactTypeRepository extends JpaRepository<ArtifactType, Long> {}
