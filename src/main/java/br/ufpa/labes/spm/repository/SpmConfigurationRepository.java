package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.SpmConfiguration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SpmConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpmConfigurationRepository extends JpaRepository<SpmConfiguration, Long> {

}
