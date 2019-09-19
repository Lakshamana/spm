package br.ufpa.labes.spm.repository.impl.email;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.email.IEmailDAO;
import br.ufpa.labes.spm.domain.Email;

public class EmailDAO extends BaseDAO<Email, Long> implements IEmailDAO {

  protected EmailDAO(Class<Email> businessClass) {
    super(businessClass);
  }

  public EmailDAO() {
    super(Email.class);
  }
}
