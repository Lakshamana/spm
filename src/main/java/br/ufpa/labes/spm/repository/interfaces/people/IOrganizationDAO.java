package br.ufpa.labes.spm.repository.interfaces.people;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Organization;


@Local
public interface IOrganizationDAO extends IBaseDAO<Organization, String> {
}
