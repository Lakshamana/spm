package br.ufpa.labes.spm.repository.interfaces.people;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Author;


@Local
public interface IAuthorDAO extends IBaseDAO<Author, String>{
}
