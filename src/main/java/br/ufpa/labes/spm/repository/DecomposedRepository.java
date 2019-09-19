package br.ufpa.labes.spm.repository;


import br.ufpa.labes.spm.domain.Decomposed;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Decomposed entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DecomposedRepository extends JpaRepository<Decomposed, Long> {

}
