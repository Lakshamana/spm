package br.ufpa.labes.spm.repository.interfaces.email;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Email;

@Local
public interface IEmailDAO extends IBaseDAO<Email, String>{

}
