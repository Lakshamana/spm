package br.ufpa.labes.spm.repository.interfaces.organizationPolicies;


import java.util.List;


import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Project;

public interface IProjectDAO extends IBaseDAO<Project, String>{
	public List<Project> findAll();
}
