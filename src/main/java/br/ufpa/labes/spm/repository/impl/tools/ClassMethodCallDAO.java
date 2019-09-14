package br.ufpa.labes.spm.repository.impl.tools;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.tools.IClassMethodCallDAO;
import br.ufpa.labes.spm.domain.ClassMethodCall;

public class ClassMethodCallDAO extends BaseDAO<ClassMethodCall, String> implements IClassMethodCallDAO{

	protected ClassMethodCallDAO(Class<ClassMethodCall> businessClass) {
		super(businessClass);
	}

	public ClassMethodCallDAO() {
		super(ClassMethodCall.class);
	}

}
