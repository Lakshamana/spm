package br.ufpa.labes.spm.repository;


import br.ufpa.labes.spm.domain.ProcessModel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProcessModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcessModelRepository extends JpaRepository<ProcessModel, Long> {

}
