package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.Credential;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Credential entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {

    @Query("select credential from Credential credential where credential.user.login = ?#{principal.username}")
    List<Credential> findByUserIsCurrentUser();

}
