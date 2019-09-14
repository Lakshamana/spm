package br.ufpa.labes.spm.repository.interfaces.plainActivities;


import java.util.Collection;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.RequiredPeople;

@Local
public interface IRequiredPeopleDAO extends IBaseDAO<RequiredPeople, Integer>{
	public Collection<String> getReqPeopleEmails( String normalIdent );
}
