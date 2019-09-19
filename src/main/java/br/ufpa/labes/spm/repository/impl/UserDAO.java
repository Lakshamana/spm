package br.ufpa.labes.spm.repository.impl;

import br.ufpa.labes.spm.repository.interfaces.IUserDAO;
import br.ufpa.labes.spm.domain.User;

public class UserDAO extends BaseDAO<User, Long> implements IUserDAO {

  public UserDAO() {
    super(User.class);
  }
}
