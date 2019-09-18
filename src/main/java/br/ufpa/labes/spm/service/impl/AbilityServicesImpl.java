package br.ufpa.labes.spm.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.agent.IAbilityDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IRoleNeedsAbilityDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IAbilityTypeDAO;
import br.ufpa.labes.spm.service.dto.AbilityDTO;
import br.ufpa.labes.spm.service.dto.AbilitysDTO;
import br.ufpa.labes.spm.domain.Ability;
import br.ufpa.labes.spm.domain.RoleNeedsAbility;
import br.ufpa.labes.spm.domain.AbilityType;
import br.ufpa.labes.spm.service.interfaces.AbilityServices;


public class AbilityServicesImpl  implements AbilityServices{

	IAbilityDAO abilityDAO;

	IAbilityTypeDAO abiTypeDAO;

	IRoleNeedsAbilityDAO roleNeedsDAO;

	@Override
	public AbilityDTO getAbility(String ident) {
		AbilityDTO abilityDTO = null;

//		hql = "select ability from "+Ability.class.getSimpleName()+" as ability where ability.name = :abiname";
//		query = abilityDAO.getPersistenceContext().createQuery(hql);
//		query.setParameter("abiname", ident);
//		result = query.getResultList();
		Ability ability = abilityDAO.retrieveBySecondaryKey(ident);

		Converter converter = new ConverterImpl();
		try {
			String abilityType = "";
			abilityDTO = (AbilityDTO) converter.getDTO(ability, AbilityDTO.class);
			if(ability.getTheAbilityType() != null) {
				abilityType = ability.getTheAbilityType().getIdent();
			}
			abilityDTO.setAbilityType(abilityType);
		} catch (ImplementationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return abilityDTO;
	}

	public Ability getAbilityP(String name) {
		String hql;
		Query query;
		List<Ability> result = null;


		hql = "select ability from "+Ability.class.getSimpleName()+" as ability where ability.name = :abiname";
		query = abilityDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("abiname", name);

		result = query.getResultList();

		if(!result.isEmpty()){
			Ability ability = result.get(0);
			return ability;
		}else{
			return null;
		}

	}

	@Override
	public AbilityDTO saveAbility(AbilityDTO abilityDTO){

		Converter converter = new ConverterImpl();
		try {
			String abilityIdent = abilityDTO.getIdent();
			String abilityType = abilityDTO.getAbilityType();
			Ability ability = abilityDAO.retrieveBySecondaryKey(abilityIdent);
			AbilityType abiType = abiTypeDAO.retrieveBySecondaryKey(abilityType);
			if(ability == null) {
				ability = (Ability) converter.getEntity(abilityDTO, Ability.class);
				ability.setTheAbilityType(abiType);

				abilityDAO.save(ability);

				String newIdent = abilityDAO.generateIdent(ability.getName(), ability);
				ability.setIdent(newIdent);
				abilityDTO.setIdent(newIdent);
				abilityDAO.update(ability);
			} else {
				ability.setName(abilityDTO.getName());
				ability.setDescription(abilityDTO.getDescription());
				ability.setTheAbilityType(abiType);

				abilityDAO.update(ability);
			}
		} catch (ImplementationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return abilityDTO;
	}

	@Override
	public Boolean removeAbility( String ident ){
		Ability ability = abilityDAO.retrieveBySecondaryKey(ident);
		if(ability != null){
			if(!ability.getTheRoleNeedsAbility().isEmpty() ){
				for (RoleNeedsAbility role : ability.getTheRoleNeedsAbility()) {
					roleNeedsDAO.delete(role);
				}
			}
			abilityDAO.delete(ability);
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public String[] getAbilityTypes() {
		String hql;
		Query query;

		hql = "select ident from " +AbilityType.class.getSimpleName();
		query = abiTypeDAO.getPersistenceContext().createQuery(hql);

		List<String> abiTypeList =  new ArrayList<String>();
		abiTypeList = query.getResultList();
		String[] list = new String[abiTypeList.size()];
		abiTypeList.toArray(list);
		return list;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public AbilitysDTO getAbilitys() {
		String hql;
		Query query;

		hql = "select ability from "+Ability.class.getSimpleName() + " as ability";
		query = abilityDAO.getPersistenceContext().createQuery(hql);


		List<Ability> abilitys = new ArrayList<Ability>();
		abilitys = query.getResultList();
		String[] idents = new String[abilitys.size()];
		String[] names = new String[abilitys.size()];
		String[] types = new String[abilitys.size()];

		for (int i = 0; i < abilitys.size(); i++) {
			Ability ability = abilitys.get(i);
			idents[i] = ability.getIdent();
			names[i] = ability.getName();
			types[i] = ability.getTheAbilityType().toString();
		}

		AbilitysDTO abilitysDTO = new AbilitysDTO();
		abilitysDTO.setIdentAbilitys(idents);
		abilitysDTO.setNameAbilitys(names);
		abilitysDTO.setTypeIdents(types);

		return abilitysDTO;
	}



	@SuppressWarnings("unchecked")
	@Override
	public AbilitysDTO getAbilitys(String term, String domain) {
		String hql;
		Query query;
		List<String> resultList = null;

		if(domain != null){
			hql = "select ability.name from " + Ability.class.getSimpleName() + " as ability where ability.name like :termo and ability.ident = :domain";
			query = abilityDAO.getPersistenceContext().createQuery(hql);
			query.setParameter("domain", domain);
			query.setParameter("termo", "%"+ term + "%");
			resultList = query.getResultList();
		}else{
			hql = "select ability.name from " + Ability.class.getSimpleName() + " as ability where ability.name like :termo";
			query = abilityDAO.getPersistenceContext().createQuery(hql);
			query.setParameter("termo", "%"+ term + "%");
			resultList = query.getResultList();
		}

		AbilitysDTO abilitysDTO = new AbilitysDTO();
		String[] names = new String[resultList.size()];
		resultList.toArray(names);
		abilitysDTO.setNameAbilitys(names);

		return abilitysDTO;
	}

}
