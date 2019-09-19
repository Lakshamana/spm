package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.types.IConnectionTypeDAO;


import br.ufpa.labes.spm.domain.ConnectionType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ConnectionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConnectionTypeRepository extends IConnectionTypeDAO, JpaRepository<ConnectionType, Long> {

}
