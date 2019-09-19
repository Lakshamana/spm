package br.ufpa.labes.spm.repository;

import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.IProjectDAO;


import br.ufpa.labes.spm.domain.Project;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Project entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectRepository extends IProjectDAO, JpaRepository<Project, Long> {

}
