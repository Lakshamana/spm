package br.ufpa.labes.spm.repository.interfaces.processModels;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Process;
import org.qrconsult.spm.services.interfaces.commonData.SimpleActivityQueryResult;

public interface IProcessDAO extends IBaseDAO<Process, String>{

	public SimpleActivityQueryResult[] getAllNormalActivitiesFromProcess(String processIdent);
	public SimpleActivityQueryResult[] getAllActivitiesFromProcess(String processIdent);
}
