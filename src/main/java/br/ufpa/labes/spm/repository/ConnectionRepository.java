package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.connections.IConnectionDAO;


import br.ufpa.labes.spm.domain.Connection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Connection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConnectionRepository extends IConnectionDAO, JpaRepository<Connection, Long> {

}
