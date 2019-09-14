package br.ufpa.labes.spm.repository.impl.people;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.people.IOrganizationDAO;
import br.ufpa.labes.spm.domain.Organization;


@Stateless
public class OrganizationDAO extends BaseDAO<Organization, String> implements IOrganizationDAO {

	public OrganizationDAO() {
		super(Organization.class);
	}

}
