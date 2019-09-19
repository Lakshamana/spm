package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.types.IResourceTypeDAO;


import br.ufpa.labes.spm.domain.ResourceType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ResourceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResourceTypeRepository extends IResourceTypeDAO, JpaRepository<ResourceType, Long> {

}
