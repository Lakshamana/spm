package br.ufpa.labes.spm.repository.interfaces.types;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.AbilityType;

@Local
public interface IAbilityTypeDAO extends IBaseDAO<AbilityType, String>{

}
