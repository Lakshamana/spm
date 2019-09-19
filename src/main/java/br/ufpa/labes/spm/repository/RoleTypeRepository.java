package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.types.IRoleTypeDAO;


import br.ufpa.labes.spm.domain.RoleType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RoleType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleTypeRepository extends IRoleTypeDAO, JpaRepository<RoleType, Long> {

}
