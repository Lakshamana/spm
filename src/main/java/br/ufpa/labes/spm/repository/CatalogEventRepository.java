package br.ufpa.labes.spm.repository;


import br.ufpa.labes.spm.domain.CatalogEvent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CatalogEvent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatalogEventRepository extends JpaRepository<CatalogEvent, Long> {

}
