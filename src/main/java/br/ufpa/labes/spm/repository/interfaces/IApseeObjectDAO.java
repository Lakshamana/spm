package br.ufpa.labes.spm.repository.interfaces;

import javax.ejb.Local;

@Local
public interface IApseeObjectDAO extends IBaseDAO<Object, Integer> {

	public <T> T retrieve(Class<T> classe, String key);
}
