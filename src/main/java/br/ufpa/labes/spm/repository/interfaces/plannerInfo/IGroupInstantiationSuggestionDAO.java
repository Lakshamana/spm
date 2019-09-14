package br.ufpa.labes.spm.repository.interfaces.plannerInfo;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.GroupInstantiationSuggestion;

@Local
public interface IGroupInstantiationSuggestionDAO  extends IBaseDAO<GroupInstantiationSuggestion, Integer>{

}
