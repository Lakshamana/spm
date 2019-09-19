package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces..ITagDAO;


import br.ufpa.labes.spm.domain.Tag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Tag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagRepository extends ITagDAO, JpaRepository<Tag, Long> {

}
