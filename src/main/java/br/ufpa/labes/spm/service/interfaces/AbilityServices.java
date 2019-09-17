package org.qrconsult.spm.services.interfaces;

import javax.ejb.Remote;

import org.qrconsult.spm.dtos.formAbility.AbilityDTO;
import org.qrconsult.spm.dtos.formAbility.AbilitysDTO;

@Remote
public interface AbilityServices {
	public String[] getAbilityTypes();
	
	public AbilitysDTO getAbilitys();
	
	public AbilitysDTO getAbilitys(String term, String domain);
	
	public AbilityDTO getAbility( String typeIdent );
	
	public AbilityDTO saveAbility(AbilityDTO typeDTO );
	
	public Boolean removeAbility( String abilityName );

}
