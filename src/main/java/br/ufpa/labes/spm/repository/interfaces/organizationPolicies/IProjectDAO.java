package br.ufpa.labes.spm.repository.interfaces.organizationPolicies;


import java.util.List;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Project;

@Local
public interface IProjectDAO extends IBaseDAO<Project, String>{
	public List<Project> findAll();
}
