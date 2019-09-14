package br.ufpa.labes.spm.repository.interfaces.plannerInfo;


import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.PeopleInstantiationSuggestion;

@Local
public interface IPeopleInstantiationSuggestionDAO  extends IBaseDAO<PeopleInstantiationSuggestion, Integer>{

}
