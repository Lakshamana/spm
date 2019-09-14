package br.ufpa.labes.spm.repository.impl;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.interfaces.IUserDAO;
import br.ufpa.labes.spm.domain.User;


@Stateless
public class UserDAO extends BaseDAO<User, String> implements IUserDAO {

	public UserDAO() {
		super(User.class);
	}

}
