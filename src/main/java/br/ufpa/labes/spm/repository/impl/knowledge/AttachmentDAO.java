package br.ufpa.labes.spm.repository.impl.knowledge;


import br.ufpa.labes.spm.repository.impl.BaseDAO;

public class AttachmentDAO extends BaseDAO<Attachment, String> implements IAttachmentDAO{

	protected AttachmentDAO(Class<Attachment> businessClass) {
		super(businessClass);
	}

	public AttachmentDAO() {
		super(Attachment.class);
	}

}
