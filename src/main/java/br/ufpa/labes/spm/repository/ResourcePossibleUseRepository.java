package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.plannerInfo.IResourcePossibleUseDAO;


import br.ufpa.labes.spm.domain.ResourcePossibleUse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ResourcePossibleUse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourcePossibleUseRepository extends IResourcePossibleUseDAO, JpaRepository<ResourcePossibleUse, Long> {

}
