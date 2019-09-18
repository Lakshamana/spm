package br.ufpa.labes.spm.service.interfaces;


import br.ufpa.labes.spm.service.dto.AbilityDTO;
import br.ufpa.labes.spm.service.dto.AbilitysDTO;

@Remote
public interface AbilityServices {
	public String[] getAbilityTypes();

	public AbilitysDTO getAbilitys();

	public AbilitysDTO getAbilitys(String term, String domain);

	public AbilityDTO getAbility( String typeIdent );

	public AbilityDTO saveAbility(AbilityDTO typeDTO );

	public Boolean removeAbility( String abilityName );

}
