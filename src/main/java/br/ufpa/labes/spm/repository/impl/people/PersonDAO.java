package br.ufpa.labes.spm.repository.impl.people;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.people.IPersonDAO;
import br.ufpa.labes.spm.domain.Person;

public class PersonDAO extends BaseDAO<Person, String> implements IPersonDAO {

  public PersonDAO() {
    super(Person.class);
  }
}
