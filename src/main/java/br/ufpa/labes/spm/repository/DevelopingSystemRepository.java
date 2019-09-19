package br.ufpa.labes.spm.repository;


import br.ufpa.labes.spm.domain.DevelopingSystem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DevelopingSystem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DevelopingSystemRepository extends JpaRepository<DevelopingSystem, Long> {

}
