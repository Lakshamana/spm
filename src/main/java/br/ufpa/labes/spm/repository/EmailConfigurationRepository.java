package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.EmailConfiguration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EmailConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailConfigurationRepository extends JpaRepository<EmailConfiguration, Long> {

}
