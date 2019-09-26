package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.Plugin;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Plugin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PluginRepository extends JpaRepository<Plugin, Long> {

    @Query("select plugin from Plugin plugin where plugin.theCompany.login = ?#{principal.username}")
    List<Plugin> findByTheCompanyIsCurrentUser();

}
