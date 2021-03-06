package br.ufpa.labes.spm.repository.impl.processModels;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.processModels.ITemplateDAO;
import br.ufpa.labes.spm.domain.Template;

public class TemplateDAO extends BaseDAO<Template, String> implements ITemplateDAO {

  protected TemplateDAO(Class<Template> businessClass) {
    super(businessClass);
  }

  public TemplateDAO() {
    super(Template.class);
  }
}
