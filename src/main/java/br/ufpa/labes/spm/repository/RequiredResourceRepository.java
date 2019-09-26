package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.RequiredResource;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RequiredResource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequiredResourceRepository extends JpaRepository<RequiredResource, Long> {

}
