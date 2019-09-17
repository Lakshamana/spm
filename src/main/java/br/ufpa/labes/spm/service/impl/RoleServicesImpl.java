package org.qrconsult.spm.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import org.qrconsult.spm.dataAccess.interfaces.agent.IAbilityDAO;
import org.qrconsult.spm.dataAccess.interfaces.agent.IAgentPlaysRoleDAO;
import org.qrconsult.spm.dataAccess.interfaces.agent.IRoleDAO;
import org.qrconsult.spm.dataAccess.interfaces.agent.IRoleNeedsAbilityDAO;
import org.qrconsult.spm.dataAccess.interfaces.types.IRoleTypeDAO;
import org.qrconsult.spm.dtos.formAbility.AbilityDTO;
import org.qrconsult.spm.dtos.formAgent.AgentDTO;
import org.qrconsult.spm.dtos.formRole.RoleDTO;
import org.qrconsult.spm.dtos.formRole.RolesDTO;
import org.qrconsult.spm.model.agent.Ability;
import org.qrconsult.spm.model.agent.Agent;
import org.qrconsult.spm.model.agent.AgentAffinityAgent;
import org.qrconsult.spm.model.agent.AgentHasAbility;
import org.qrconsult.spm.model.agent.AgentPlaysRole;
import org.qrconsult.spm.model.agent.Group;
import org.qrconsult.spm.model.agent.Role;
import org.qrconsult.spm.model.agent.RoleNeedsAbility;
import org.qrconsult.spm.model.types.RoleType;
import org.qrconsult.spm.services.interfaces.RoleServices;

@Stateless
public class RoleServicesImpl implements RoleServices {

	private static final String AGENT_PLAY_ROLE_CLASSNAME = AgentPlaysRole.class
			.getSimpleName();
	private static final String ROLE_CLASSNAME = Role.class.getSimpleName();
	@EJB
	IAbilityDAO abilityDAO;

	@EJB
	IRoleDAO roleDAO;

	@EJB
	IRoleTypeDAO roleTypeDAO;

	@EJB
	IRoleNeedsAbilityDAO roleNeedsDAO;

	@EJB
	IAgentPlaysRoleDAO agentPlaysRole;

	Converter converter = new ConverterImpl();

	@SuppressWarnings("null")
	public RoleDTO getRole(String nameRole) {
		String hql;
		Query query;
		List<Role> result = null;
		List<Ability> abis = new ArrayList<Ability>();
		List<AbilityDTO> abisDTO = new ArrayList<AbilityDTO>();
		List<Agent> agens = new ArrayList<Agent>();
		List<AgentDTO> agensDTO = new ArrayList<AgentDTO>();
		RoleDTO roleDTO = null;
		AbilityDTO abi = null;
		AgentDTO agen = null;

		hql = "select role from " + Role.class.getSimpleName()
				+ " as role where role.name = :rolname";
		query = roleDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("rolname", nameRole);
		result = query.getResultList();
		Collection<RoleNeedsAbility> lis = result.get(0)
				.getTheRoleNeedsAbility();
		Collection<AgentPlaysRole> lisaux = result.get(0)
				.getTheAgentPlaysRole();

		for (RoleNeedsAbility roleNeedsAbility : lis) {
			abis.add(roleNeedsAbility.getTheAbility());
		}

		Converter converter = new ConverterImpl();
		for (int i = 0; i < abis.size(); i++) {
			try {
				abi = (AbilityDTO) converter.getDTO(abis.get(i),
						AbilityDTO.class);
				abisDTO.add(abi);
			} catch (ImplementationException e) {

				e.printStackTrace();
			}
		}

		for (AgentPlaysRole agentPlaysRole : lisaux) {
			agens.add(agentPlaysRole.getTheAgent());
		}

		for (int i = 0; i < agens.size(); i++) {
			try {
				agen = (AgentDTO) converter
						.getDTO(agens.get(i), AgentDTO.class);
				agensDTO.add(agen);
			} catch (ImplementationException e) {

				e.printStackTrace();
			}
		}

		try {
			roleDTO = (RoleDTO) converter.getDTO(result.get(0), RoleDTO.class);
			if (abis != null) {
				roleDTO.setAbilityToRole(abisDTO);
			}
			if (agens != null) {
				roleDTO.setAgentToRole(agensDTO);
			}

		} catch (ImplementationException e) {
			e.printStackTrace();
		}
		roleDTO.setSuperType(result.get(0).getSubordinate().getName());
		return roleDTO;
	}

	public Role getRoleForName(String nome) {
		String hql;
		Query query;
		List<Role> result = null;

		hql = "select role from " + Role.class.getSimpleName()
				+ " as role where role.name = :rolname";
		query = roleDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("rolname", nome);

		result = query.getResultList();

		if (!result.isEmpty()) {
			Role role = result.get(0);
			return role;
		} else {
			return null;
		}
	}

	@SuppressWarnings("null")
	public List<AbilityDTO> getAbilitysToRole() {
		String hql;
		Query query;
		List<Ability> result = null;
		List<AbilityDTO> resultDTO = new ArrayList<AbilityDTO>();

		hql = "select ability from " + Ability.class.getSimpleName()
				+ " as ability";
		query = roleDAO.getPersistenceContext().createQuery(hql);
		result = query.getResultList();
		Converter converter = new ConverterImpl();

		if (!result.isEmpty()) {
			AbilityDTO abi = null;
			for (int i = 0; i < result.size(); i++) {
				try {
					abi = (AbilityDTO) converter.getDTO(result.get(i),
							AbilityDTO.class);
					resultDTO.add(abi);
				} catch (ImplementationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return resultDTO;
		} else {
			return null;
		}
	}

	public RoleDTO saveRole(RoleDTO roleDTO) {
		Converter converter = new ConverterImpl();
		try {
			String typeRole = roleDTO.getIdent();
			String roleName = roleDTO.getName();
			String roleSubName = roleDTO.getSuperType();
			System.out.println(":" + roleSubName);
			if (typeRole != null && !typeRole.equals("") && roleName != null
					&& !roleName.equals("")) {
				RoleType roleType = roleTypeDAO
						.retrieveBySecondaryKey(typeRole);
				Role role = this.getRoleForName(roleName);
				Role roleSub = this.getRoleForName(roleSubName);
				if (roleType != null) {
					if (role != null) {
						role = (Role) converter.getEntity(roleDTO, role);
						if (roleSub != null) {
							role.setSubordinate(roleSub);
						}
						role.setTheRoleType(roleType);
						role = roleDAO.update(role);
					} else {
						role = (Role) converter.getEntity(roleDTO, Role.class);
						if (roleSub != null) {
							role.setSubordinate(roleSub);
						}
						role.setTheRoleType(roleType);
						role = roleDAO.save(role);
					}
					roleDTO = (RoleDTO) converter.getDTO(role, RoleDTO.class);
				} else {
					return null;
				}
			} else {
				System.out.println("passei aqui 8" + typeRole + ":" + roleName);
				return null;
			}

		} catch (ImplementationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return roleDTO;
	}

	public RoleDTO saveAbilityToRole(RoleDTO roleDTO) {
		Converter converter = new ConverterImpl();
		try {
			// String typeRole = roleDTO.getIdent();
			String roleName = roleDTO.getName();
			// String roleSubName = roleDTO.getSuperType();

			// RoleType roleType = roleTypeDAO.retrieveBySecondaryKey(typeRole);
			Role role = this.getRoleForName(roleName);
			// Role roleSub = this.getRoleForName(roleSubName);

			Ability abil;
			if (!roleDTO.getAbilityToRole().isEmpty()
					&& (roleDTO.getAbilityToRole() != null) && role != null) {

				Collection<RoleNeedsAbility> lisaux = new ArrayList<RoleNeedsAbility>();
				lisaux = role.getTheRoleNeedsAbility();

				if (!lisaux.isEmpty()) {
					for (RoleNeedsAbility roleNeedsAbility : lisaux) {
						roleNeedsDAO.delete(roleNeedsAbility);
					}
				}

				role = (Role) converter.getEntity(roleDTO, role);
				RoleNeedsAbility roleNeeds;
				Collection<RoleNeedsAbility> lisAbi = new ArrayList<RoleNeedsAbility>();
				for (int i = 0; i < roleDTO.getAbilityToRole().size(); i++) {
					roleNeeds = new RoleNeedsAbility();
					abil = (Ability) converter.getEntity(roleDTO
							.getAbilityToRole().get(i), Ability.class);
					roleNeeds.setTheAbility(abil);
					roleNeeds.setTheRole(role);
					roleNeeds.setDegree(roleDTO.getNivelAbility());
					lisAbi.add(roleNeeds);
				}
				role.setTheRoleNeedsAbility(lisAbi);
				role = roleDAO.update(role);
			} else {
				/*
				 * if(roleSub != null){ role.setSubordinate(roleSub);
				 * System.out.println("passei aqui 3/5"); } role = (Role)
				 * converter.getEntity(roleDTO, Role.class);
				 */

			}
			roleDTO = (RoleDTO) converter.getDTO(role, RoleDTO.class);

		} catch (ImplementationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("passei aqui 9");
		return roleDTO;
	}

	@SuppressWarnings("unchecked")
	public Boolean removeRole(RoleDTO roleDTO) {
		String hql;
		Query query;
		System.out.println("re1" + roleDTO.getName());
		hql = "select role from " + Role.class.getSimpleName()
				+ " as role where role.name = :abiname";
		query = roleDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("abiname", roleDTO.getName());
		List<Role> result = query.getResultList();
		System.out.println("re1");
		Role role = result.get(0);
		if (role != null) {
			if (role.getTheRoleNeedsAbility() != null) {
				Collection<RoleNeedsAbility> re = role.getTheRoleNeedsAbility();
				for (RoleNeedsAbility ro : re) {
					roleNeedsDAO.delete(ro);
				}
			}
			System.out.println("re2");
			roleDAO.delete(role);
			return true;
		} else {
			System.out.println("re3");
			return false;
		}
	}

	public RolesDTO getRoles(String term, String domain) {
		String hql;
		Query query;
		List<String> resultList = null;

		if (domain != null) {
			hql = "select role.name from "
					+ Role.class.getSimpleName()
					+ " as role where role.name like :termo and role.ident = :domain";
			query = roleDAO.getPersistenceContext().createQuery(hql);
			query.setParameter("domain", domain);
			query.setParameter("termo", "%" + term + "%");
			resultList = query.getResultList();
		} else {
			hql = "select role.name from " + Role.class.getSimpleName()
					+ " as role where role.name like :termo";
			query = roleDAO.getPersistenceContext().createQuery(hql);
			query.setParameter("termo", "%" + term + "%");
			resultList = query.getResultList();
		}

		RolesDTO rolesDTO = new RolesDTO();
		String[] names = new String[resultList.size()];
		resultList.toArray(names);
		rolesDTO.setNamePapeis(names);

		return rolesDTO;
	}

	public String[] getRoleTypes() {
		String hql;
		Query query;

		hql = "select ident from " + RoleType.class.getSimpleName();
		query = roleTypeDAO.getPersistenceContext().createQuery(hql);

		List<String> abiTypeList = new ArrayList<String>();
		abiTypeList = query.getResultList();
		String[] list = new String[abiTypeList.size()];
		abiTypeList.toArray(list);
		return list;
	}

	public RolesDTO getRoles() {
		String hql;
		Query query;

		hql = "select name from " + Role.class.getSimpleName();
		query = roleDAO.getPersistenceContext().createQuery(hql);

		List<String> roles = new ArrayList<String>();
		roles = query.getResultList();
		String[] list = new String[roles.size()];
		roles.toArray(list);

		String[] list1 = new String[0];
		list1 = getRoleTypes();

		RolesDTO rolesDTO = new RolesDTO();
		rolesDTO.setNamePapeis(list);
		rolesDTO.setTypePapeis(list1);
		rolesDTO.setSuperType(list);

		return rolesDTO;
	}

	@Override
	public List<AgentDTO> getAgentToRole() {
		String hql;
		Query query;
		List<Agent> result = null;
		List<AgentDTO> resultDTO = new ArrayList<AgentDTO>();

		hql = "select agent from " + Agent.class.getSimpleName() + " as agent";
		query = roleDAO.getPersistenceContext().createQuery(hql);
		result = query.getResultList();
		Converter converter = new ConverterImpl();

		if (!result.isEmpty()) {
			AgentDTO abi = null;
			for (int i = 0; i < result.size(); i++) {
				try {
					abi = (AgentDTO) converter.getDTO(result.get(i),
							AgentDTO.class);
					resultDTO.add(abi);
				} catch (ImplementationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			return resultDTO;
		} else {
			return null;
		}
	}

	@Override
	public RoleDTO saveAgentToRole(RoleDTO roleDTO) {
		System.out.println("passei aqui agentRole");
		Converter converter = new ConverterImpl();
		try {

			String roleName = roleDTO.getName();
			Role role = this.getRoleForName(roleName);
			Agent agent;
			System.out.println("passei aqui 1");
			if (!roleDTO.getAgentToRole().isEmpty()
					&& (roleDTO.getAgentToRole() != null) && role != null) {

				Collection<AgentPlaysRole> lisaux = new ArrayList<AgentPlaysRole>();
				lisaux = role.getTheAgentPlaysRole();
				if (!lisaux.isEmpty()) {
					for (AgentPlaysRole agentPlays : lisaux) {
						this.agentPlaysRole.delete(agentPlays);
					}
				}

				role = (Role) converter.getEntity(roleDTO, role);
				AgentPlaysRole roleplay;
				Collection<AgentPlaysRole> lisAgent = new ArrayList<AgentPlaysRole>();
				for (int i = 0; i < roleDTO.getAgentToRole().size(); i++) {
					System.out.println("passei aqui 2");
					roleplay = new AgentPlaysRole();
					agent = (Agent) converter.getEntity(roleDTO
							.getAgentToRole().get(i), Agent.class);
					roleplay.setTheAgent(agent);
					roleplay.setTheRole(role);
					// agentPlaysRole.save(roleplay);
					lisAgent.add(roleplay);
					System.out.println("passei aqui 3");
				}
				System.out.println("passei aqui 3/5");
				role.setTheAgentPlaysRole(lisAgent);
				role = roleDAO.update(role);
				System.out.println("passei aqui 4");
			} else {
				/*
				 * if(roleSub != null){ role.setSubordinate(roleSub);
				 * System.out.println("passei aqui 3/5"); } role = (Role)
				 * converter.getEntity(roleDTO, Role.class);
				 */
				return null;
			}
			System.out.println("passei aqui 6");
			roleDTO = (RoleDTO) converter.getDTO(role, RoleDTO.class);

		} catch (ImplementationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("passei aqui 9");
		return roleDTO;
	}

	@Override
	public Boolean removeAbilityRole(RoleDTO roleDTO) {
		Converter converter = new ConverterImpl();
		try {
			String roleName = roleDTO.getName();

			Role role = this.getRoleForName(roleName);
			System.out.println("passei aqui remove1");

			Ability abil;
			System.out.println("passei aqui remove2");
			if (!roleDTO.getAbilityToRole().isEmpty()
					&& (roleDTO.getAbilityToRole() != null) && role != null) {

				Collection<RoleNeedsAbility> lisaux = new ArrayList<RoleNeedsAbility>();
				lisaux = role.getTheRoleNeedsAbility();
				boolean re = false;
				if (!lisaux.isEmpty()) {
					for (RoleNeedsAbility roleNeedsAbility : lisaux) {
						roleNeedsDAO.delete(roleNeedsAbility);
					}
				}
				role = (Role) converter.getEntity(roleDTO, role);
				RoleNeedsAbility roleNeeds;
				System.out.println("passei aqui remove3");
				Collection<RoleNeedsAbility> lisAbi = new ArrayList<RoleNeedsAbility>();
				for (int i = 0; i < roleDTO.getAbilityToRole().size(); i++) {
					roleNeeds = new RoleNeedsAbility();
					abil = (Ability) converter.getEntity(roleDTO
							.getAbilityToRole().get(i), Ability.class);
					roleNeeds.setTheAbility(abil);
					roleNeeds.setTheRole(role);
					lisAbi.add(roleNeeds);
					System.out.println("passei aqui remove3");
				}
				System.out.println("passei aqui remove4");
				role.setTheRoleNeedsAbility(lisAbi);
				role = roleDAO.update(role);
				System.out.println("passei aqui remove5");
			} else {
				/*
				 * if(roleSub != null){ role.setSubordinate(roleSub);
				 * System.out.println("passei aqui 3/5"); } role = (Role)
				 * converter.getEntity(roleDTO, Role.class);
				 */

			}
			System.out.println("passei aqui remove");
			roleDTO = (RoleDTO) converter.getDTO(role, RoleDTO.class);

		} catch (ImplementationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("passei aqui 9");
		return true;
	}

	@Override
	public Boolean removeAgentRole(RoleDTO roleDTO) {
		Converter converter = new ConverterImpl();
		try {
			String roleName = roleDTO.getName();

			Role role = this.getRoleForName(roleName);
			System.out.println("passei aqui remove1");

			Agent agent;
			System.out.println("passei aqui remove2");
			if (!roleDTO.getAgentToRole().isEmpty()
					&& (roleDTO.getAgentToRole() != null) && role != null) {

				Collection<AgentPlaysRole> lisaux = new ArrayList<AgentPlaysRole>();
				lisaux = role.getTheAgentPlaysRole();
				boolean re = false;
				if (!lisaux.isEmpty()) {
					for (AgentPlaysRole agentPlay : lisaux) {
						agentPlaysRole.delete(agentPlay);
					}
				}
				role = (Role) converter.getEntity(roleDTO, role);
				AgentPlaysRole agentPlay;
				System.out.println("passei aqui remove3");
				Collection<AgentPlaysRole> lisAgent = new ArrayList<AgentPlaysRole>();
				for (int i = 0; i < roleDTO.getAbilityToRole().size(); i++) {
					agentPlay = new AgentPlaysRole();
					agent = (Agent) converter.getEntity(roleDTO
							.getAgentToRole().get(i), Agent.class);
					agentPlay.setTheAgent(agent);
					agentPlay.setTheRole(role);
					lisAgent.add(agentPlay);
					System.out.println("passei aqui remove3");
				}
				System.out.println("passei aqui remove4");
				role.setTheAgentPlaysRole(lisAgent);
				role = roleDAO.update(role);
				System.out.println("passei aqui remove5");
			} else {
				/*
				 * if(roleSub != null){ role.setSubordinate(roleSub);
				 * System.out.println("passei aqui 3/5"); } role = (Role)
				 * converter.getEntity(roleDTO, Role.class);
				 */

			}
			System.out.println("passei aqui remove");
			roleDTO = (RoleDTO) converter.getDTO(role, RoleDTO.class);

		} catch (ImplementationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("passei aqui 9");
		return true;
	}

	public List<RoleDTO> getRolesToTree() {
		String hql;
		Query query;
		List<Role> result = null;
		List<RoleDTO> resultDTO = new ArrayList<RoleDTO>();
		hql = "select role from " + Role.class.getSimpleName() + " as role";
		query = roleDAO.getPersistenceContext().createQuery(hql);
		result = query.getResultList();
		Converter converter = new ConverterImpl();

		if (!result.isEmpty()) {
			RoleDTO role = null;
			for (int i = 0; i < result.size(); i++) {
				try {
					role = (RoleDTO) converter.getDTO(result.get(i),
							RoleDTO.class);
					resultDTO.add(role);
				} catch (ImplementationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			return resultDTO;
		} else {
			return null;
		}
	}

	@Override
	public String getPerfil(RoleDTO agent, int oid) {
		System.out.println("caiu nessa porra!"+oid);
		Query query;
		query = roleDAO
				.getPersistenceContext()
				.createQuery(
						"SELECT r.name FROM "
								+ AGENT_PLAY_ROLE_CLASSNAME
								+ " AS a,"
								+ ROLE_CLASSNAME
								+ " AS r "
								+ "WHERE a.theRole.oid=r.oid and a.theAgent.oid="+ oid);
		String admin = null;
		String gerente = null;
		String retorno = null;

		try {
			System.out.print("Cargos " + query.getResultList());

			for (int i = 0; i < query.getResultList().size(); i++) {
				if (query.getResultList().get(i).equals("Administrador")) {
					System.out.print("caiu no admin");

					admin = "Administrador";

				} else if (query.getResultList().get(i).equals("Gerente")) {
					System.out.print("caiu no gerente");
					gerente = "Gerente";
				}

			}

			if (admin != null) {
				retorno = admin;

			} else {
				if (gerente != null) {
					retorno = gerente;

				} else {

					retorno = "Desenvolvedor";

				}

			}

			return retorno;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private RoleDTO convertRoleToAgentDTO(Role agent) {
		RoleDTO agentDTO = new RoleDTO();
		try {
			agentDTO = (RoleDTO) converter.getDTO(agent, RoleDTO.class);

			return agentDTO;

		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return null;
	}
}
