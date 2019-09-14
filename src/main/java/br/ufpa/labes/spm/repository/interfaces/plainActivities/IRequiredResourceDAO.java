package br.ufpa.labes.spm.repository.interfaces.plainActivities;



import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.RequiredResource;

public interface IRequiredResourceDAO extends IBaseDAO<RequiredResource, Integer>{

	public RequiredResource findRequiredResourceFromProcessModel(String resourceIdent, String resourceTypeIdent, String normalIdent);
}
