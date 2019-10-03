package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.AssetRelationship;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AssetRelationship entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetRelationshipRepository extends JpaRepository<AssetRelationship, Long> {

}
