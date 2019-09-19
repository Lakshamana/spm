package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces..IAuthorStatDAO;


import br.ufpa.labes.spm.domain.AuthorStat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AuthorStat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorStatRepository extends IAuthorStatDAO, JpaRepository<AuthorStat, Long> {

}
