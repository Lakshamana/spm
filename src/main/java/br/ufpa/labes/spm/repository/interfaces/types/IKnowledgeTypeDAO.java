package br.ufpa.labes.spm.repository.interfaces.types;

import javax.ejb.Local;
import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.KnowledgeType;

@Local
public interface IKnowledgeTypeDAO extends IBaseDAO<KnowledgeType, String>{

}
