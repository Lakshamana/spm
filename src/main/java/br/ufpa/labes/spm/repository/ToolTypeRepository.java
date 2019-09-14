package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.ToolType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ToolType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToolTypeRepository extends JpaRepository<ToolType, Long> {

}
