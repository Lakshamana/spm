package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.domain.PlainActivity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PlainActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlainActivityRepository extends JpaRepository<PlainActivity, Long> {

}
