package br.ufpa.labes.spm.repository.impl.people;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.people.IAuthorDAO;
import br.ufpa.labes.spm.domain.Author;


@Stateless
public class AuthorDAO extends BaseDAO<Author, String> implements IAuthorDAO {

	public AuthorDAO() {
		super(Author.class);
	}

}
