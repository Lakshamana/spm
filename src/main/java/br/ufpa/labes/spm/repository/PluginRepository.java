package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces..IPluginDAO;


import br.ufpa.labes.spm.domain.Plugin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Plugin entity.
 */
@Repository
public interface PluginRepository extends IPluginDAO, JpaRepository<Plugin, Long> {

    @Query(value = "select distinct plugin from Plugin plugin left join fetch plugin.users",
        countQuery = "select count(distinct plugin) from Plugin plugin")
    Page<Plugin> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct plugin from Plugin plugin left join fetch plugin.users")
    List<Plugin> findAllWithEagerRelationships();

    @Query("select plugin from Plugin plugin left join fetch plugin.users where plugin.id =:id")
    Optional<Plugin> findOneWithEagerRelationships(@Param("id") Long id);

}
