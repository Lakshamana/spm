package br.ufpa.labes.spm.repository.interfaces.people;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Person;


@Local
public interface IPersonDAO extends IBaseDAO<Person, String> {
}
