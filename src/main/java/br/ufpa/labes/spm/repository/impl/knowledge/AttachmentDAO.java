package br.ufpa.labes.spm.repository.impl.knowledge;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.knowledge.IAttachmentDAO;
import br.ufpa.labes.spm.domain.Attachment;

@Stateless
public class AttachmentDAO extends BaseDAO<Attachment, String> implements IAttachmentDAO{

	protected AttachmentDAO(Class<Attachment> businessClass) {
		super(businessClass);
	}

	public AttachmentDAO() {
		super(Attachment.class);
	}

}
