package br.ufpa.labes.spm.repository.interfaces.plainActivities;


import java.util.Collection;


import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.RequiredPeople;

public interface IRequiredPeopleDAO extends IBaseDAO<RequiredPeople, Integer>{
	public Collection<String> getReqPeopleEmails( String normalIdent );
}
