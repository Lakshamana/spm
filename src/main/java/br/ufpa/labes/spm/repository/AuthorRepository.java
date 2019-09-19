package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.people.IAuthorDAO;


import br.ufpa.labes.spm.domain.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Author entity.
 */
@Repository
public interface AuthorRepository extends IAuthorDAO, JpaRepository<Author, Long> {

    @Query(value = "select distinct author from Author author left join fetch author.authorsFolloweds",
        countQuery = "select count(distinct author) from Author author")
    Page<Author> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct author from Author author left join fetch author.authorsFolloweds")
    List<Author> findAllWithEagerRelationships();

    @Query("select author from Author author left join fetch author.authorsFolloweds where author.id =:id")
    Optional<Author> findOneWithEagerRelationships(@Param("id") Long id);

}
