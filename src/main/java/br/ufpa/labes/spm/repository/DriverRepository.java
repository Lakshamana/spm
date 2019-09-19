package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.driver.IDriverDAO;


import br.ufpa.labes.spm.domain.Driver;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Driver entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DriverRepository extends IDriverDAO, JpaRepository<Driver, Long> {

}
