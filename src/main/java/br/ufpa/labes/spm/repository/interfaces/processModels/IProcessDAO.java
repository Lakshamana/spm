package br.ufpa.labes.spm.repository.interfaces.processModels;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Process;
import org.qrconsult.spm.services.interfaces.commonData.SimpleActivityQueryResult;

@Local
public interface IProcessDAO extends IBaseDAO<Process, String>{

	public SimpleActivityQueryResult[] getAllNormalActivitiesFromProcess(String processIdent);
	public SimpleActivityQueryResult[] getAllActivitiesFromProcess(String processIdent);
}
