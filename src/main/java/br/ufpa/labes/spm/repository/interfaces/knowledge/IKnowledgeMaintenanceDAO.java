package br.ufpa.labes.spm.repository.interfaces.knowledge;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.KnowledgeMaintenance;

@Local
public interface IKnowledgeMaintenanceDAO extends IBaseDAO<KnowledgeMaintenance, Integer>{

}
