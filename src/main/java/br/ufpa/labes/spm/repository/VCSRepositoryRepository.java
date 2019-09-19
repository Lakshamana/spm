package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces..IVCSRepositoryDAO;


import br.ufpa.labes.spm.domain.VCSRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the VCSRepository entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VCSRepositoryRepository extends IVCSRepositoryDAO, JpaRepository<VCSRepository, Long> {

}
