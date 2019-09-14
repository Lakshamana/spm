package br.ufpa.labes.spm.repository.interfaces;

import javax.ejb.Local;

import br.ufpa.labes.spm.domain.User;

@Local
public interface IUserDAO extends IBaseDAO<User, String> {
}
