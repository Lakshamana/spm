package org.qrconsult.spm.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import org.qrconsult.spm.dataAccess.interfaces.organizationPolicies.ICompanyDAO;
import org.qrconsult.spm.dataAccess.interfaces.organizationPolicies.IProjectDAO;
import org.qrconsult.spm.dataAccess.interfaces.organizationPolicies.ISystemDAO;
import org.qrconsult.spm.dtos.formAgent.AgentDTO;
import org.qrconsult.spm.dtos.formProject.ProjectDTO;
import org.qrconsult.spm.dtos.formSystem.SystemDTO;
import org.qrconsult.spm.dtos.formSystem.SystemsDTO;
import org.qrconsult.spm.model.agent.Agent;
import org.qrconsult.spm.model.agent.AgentAffinityAgent;
import org.qrconsult.spm.model.agent.AgentHasAbility;
import org.qrconsult.spm.model.agent.AgentPlaysRole;
import org.qrconsult.spm.model.agent.Group;
import org.qrconsult.spm.model.organizationPolicies.Company;
import org.qrconsult.spm.model.organizationPolicies.Project;
import org.qrconsult.spm.model.organizationPolicies.System;
import org.qrconsult.spm.services.interfaces.ProjectServices;
import org.qrconsult.spm.services.interfaces.SystemServices;


@Stateless
public class SystemServicesImpl implements SystemServices{

	private static final String SYSTEM_CLASSNAME = System.class.getSimpleName();

	
	@EJB
	IProjectDAO projectDAO;
	
	@EJB
	ISystemDAO systemDAO;
	
	@EJB
	ICompanyDAO comDAO;
	
	Converter converter = new ConverterImpl();
	
	ProjectServices projectServicesImpl =null;
	
	public System getSystemForName(String nome){
		String hql;
		Query query;
		List<System> result = null;
		
		
		hql = "select system from "+System.class.getSimpleName()+" as system where system.name = :sysname";
		query = systemDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("sysname", nome);
		
		result = query.getResultList();
		
		if(!result.isEmpty()){
			System sys = result.get(0);
			return sys;
		}else{
			return null;
		}
	}
	
	private SystemDTO convertSystemToSystemDTO(System system) {
		SystemDTO systemDTO = new SystemDTO();
		try {
			systemDTO = (SystemDTO) converter.getDTO(system, SystemDTO.class);
			return systemDTO;

		} catch (ImplementationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	public SystemDTO getSystem(String nameSystem) {
		String hql;
		Query query;
		List<System> result = null;
		List<Project> abis = new ArrayList<Project>();
		List<ProjectDTO> abisDTO = new ArrayList<ProjectDTO>();
		SystemDTO systemDTO = null;
		ProjectDTO abi = null;
		
		hql = "select role from "+System.class.getSimpleName()+" as role where role.name = :rolname";
		query = systemDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("rolname", nameSystem);
		result = query.getResultList();
		Collection<Project> lis = result.get(0).getTheProject();
		java.lang.System.out.println("project1");
		
		if(!lis.isEmpty()){
			java.lang.System.out.println("project2222");
			for (Project pro : lis) {
				java.lang.System.out.println("project2");
				abis.add(pro);
			}
		}
		
		Converter converter = new ConverterImpl();
		for(int i = 0;i < abis.size();i++){
			try {
				abi = (ProjectDTO) converter.getDTO(abis.get(i), ProjectDTO.class);
				abisDTO.add(abi);
			} catch (ImplementationException e) {
				
				e.printStackTrace();
			}			
		}
		
		try {
			systemDTO = (SystemDTO) converter.getDTO(result.get(0), SystemDTO.class);
			if(abis != null){
				systemDTO.setProjetos(abisDTO);
			}
			
		} catch (ImplementationException e) {
			e.printStackTrace();
		}
		
		return systemDTO;
	}

	@Override
	public SystemDTO saveSystem(SystemDTO systemDTO) {
		Converter converter = new ConverterImpl();
		java.lang.System.out.println("passei aqui antes");
		try {
			String orgSystem = systemDTO.getIdent();
			String systemName =  systemDTO.getName();
			Project abil;
			if (orgSystem != null && !orgSystem.equals("") && systemName != null && !systemName.equals("")){
				Company organize =  comDAO.retrieveBySecondaryKey(orgSystem);
				java.lang.System.out.println(organize.getAddress()+"trse");
				System system = this.getSystemForName(systemName);
				if(organize != null ){
					java.lang.System.out.println("passei aqui 1");
					if(system != null ){
						java.lang.System.out.println("passei aqui 2");
						system = (System) converter.getEntity(systemDTO, system);
						java.lang.System.out.println("Projetos: "  + systemDTO.getProjetos());
						
						for (Project project : system.getTheProject()) { //Quebrar todas as  
							project.setTheSystem(null);
						}											  //anteriormente, pra salvar	
						system.setTheProject(null);
						systemDAO.update(system);
						java.lang.System.out.println("tamanho: " + systemDTO.getProjetos().size());
						
						if(!systemDTO.getProjetos().isEmpty()){
							Collection<Project> lisAbi = new ArrayList<Project>();
							
							for (ProjectDTO pro : systemDTO.getProjetos()) {
								java.lang.System.out.println("Construindo projetos1");
								abil = (Project) converter.getEntity(pro,Project.class);
								abil.setTheSystem(system);
								lisAbi.add(abil);
							}
							system.setTheProject(lisAbi);
						}
						system.setTheOrganization(organize);
						system = systemDAO.update(system);
						java.lang.System.out.println("passei aqui 4");
					}else{
						
						java.lang.System.out.println("passei aqui 5");
						system = (System) converter.getEntity(systemDTO, System.class);
						
						if(systemDTO.getProjetos() != null && !systemDTO.getProjetos().isEmpty() ){
							Collection<Project> lisAbi = new ArrayList<Project>();
							List<Project> ar = this.getProjects();
							for (ProjectDTO pro : systemDTO.getProjetos()) {
								java.lang.System.out.println("Construindo projetos2");
								for(int i = 0;i <ar.size();i++){
									if(pro.getName().equals(ar.get(i).getName())){
										lisAbi.add(ar.get(i));
										break;
									}
								}
							}
							system.setTheProject(lisAbi);
						}
						system.setTheOrganization(organize);
						system =  systemDAO.save(system);
					}
					java.lang.System.out.println("passei aqui 6");
					systemDTO = (SystemDTO) converter.getDTO(system, SystemDTO.class);
				}else{
					java.lang.System.out.println("passei aqui 7");
					return null;
				}
			}else{
				java.lang.System.out.println("passei aqui 8"+orgSystem+":");
				return null;
			}		
			
		} catch (ImplementationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		java.lang.System.out.println("passei aqui 9");
		return systemDTO;
	}

	@Override
	public Boolean removeSystem(SystemDTO systemDTO) {
		String hql;
		Query query;
		java.lang.System.out.println("re1"+systemDTO.getName());
		hql = "select system from "+System.class.getSimpleName()+" as system where system.name = :abiname";
		query = systemDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("abiname", systemDTO.getName());
		List<System> result = query.getResultList();
		java.lang.System.out.println("re1");
		System system = result.get(0);
		if(system != null){
			if(system.getTheProject() != null){
				Collection<Project> re = system.getTheProject();
				for (Project ro : re) {
					//roleNeedsDAO.delete(ro);
					ro.setTheSystem(null);
				}
			}
			java.lang.System.out.println("re2");
			systemDAO.delete(system);
			return true;
		}
		else{
			java.lang.System.out.println("re3");
			return false;
		}
	}

	@Override
	public SystemsDTO getSystems(String term, String domain) {
		String hql;
		Query query;
		List<String> resultList = null;
		
		if(domain != null){
			hql = "select system.name from " + System.class.getSimpleName() + " as system where system.name like :termo and system.ident = :domain";
			query = systemDAO.getPersistenceContext().createQuery(hql);
			query.setParameter("domain", domain);
			query.setParameter("termo", "%"+ term + "%");
			resultList = query.getResultList(); 	
		}else{
			hql = "select system.name from " +System.class.getSimpleName() + " as system where system.name like :termo";
			query = systemDAO.getPersistenceContext().createQuery(hql);
			query.setParameter("termo", "%"+ term + "%");
			resultList = query.getResultList(); 	
		}
		
		SystemsDTO systemsDTO = new SystemsDTO();
		String[] names = new String[resultList.size()];
		resultList.toArray(names);
		systemsDTO.setNameSystemas(names);
		
		
		return systemsDTO;
	}


	public String[] getSystemsCompany(){
		String hql;
		Query query;
		
		hql = "select ident from " +Company.class.getSimpleName();
		query = comDAO.getPersistenceContext().createQuery(hql);
		
		List<String> comList =  new ArrayList<String>();
		comList = query.getResultList();
		String[] list = new String[comList.size()];
		comList.toArray(list);
		return list;
	}
	
	
	@Override
	public SystemsDTO getSystems() {
		String hql;
		Query query;
		hql = "select name from "+System.class.getSimpleName();
		query = systemDAO.getPersistenceContext().createQuery(hql);
		
		List<String> systems = new ArrayList<String>();
		systems = query.getResultList();
		String[] list = new String[systems.size()];
		systems.toArray(list);
		String[] list1 = new String[0];
		list1 = getSystemsCompany();
		SystemsDTO systemsDTO = new SystemsDTO();
		systemsDTO.setNameSystemas(list);
		systemsDTO.setNomeProjetos(list1);
		
		hql = "SELECT system FROM " + SYSTEM_CLASSNAME + " AS system";
		query = systemDAO.getPersistenceContext().createQuery(hql);
		List<System> result = query.getResultList();
		
		java.lang.System.out.println("result: " + result);
		java.lang.System.out.println(converter);
		java.lang.System.out.println(systemsDTO.getSystemsDTO());

		
		for (System system2 : result) {
			systemsDTO.getSystemsDTO().add(convertSystemToSystemDTO(system2));
		}

		return systemsDTO;
	}
	

	@Override
	public Boolean removeProjectToSystem(SystemDTO systemDTO) {
		String hql;
		Query query;
		java.lang.System.out.println("re1"+systemDTO.getName());
		hql = "select system from "+System.class.getSimpleName()+" as system where system.name = :abiname";
		query = systemDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("abiname", systemDTO.getName());
		List<System> result = query.getResultList();
		java.lang.System.out.println("re1");
		System system = result.get(0);
		if(system != null){
			if(system.getTheProject() != null){
				Collection<Project> re = system.getTheProject();
				for (Project ro : re) {
					//roleNeedsDAO.delete(ro);
					ro.setTheSystem(null);
				}
			}
			java.lang.System.out.println("re2");
			system.setTheProject(null);
			systemDAO.update(system);
			return true;
		}
		else{
			java.lang.System.out.println("re3");
			return false;
		}
	}

	@Override
	public List<ProjectDTO> getProjectToSystem() {
		String hql;
		Query query;
		List<Project> result = null;
		List<ProjectDTO> resultDTO = new ArrayList<ProjectDTO>();
		
		hql = "select project from "+Project.class.getSimpleName()+" as project";
		query = projectDAO.getPersistenceContext().createQuery(hql);
		result = query.getResultList();
		Converter converter = new ConverterImpl();
		
		if(!result.isEmpty()){
			ProjectDTO pro = null; 
			for(int i = 0; i < result.size();i++){
				try {
					pro =  (ProjectDTO) converter.getDTO(result.get(i), ProjectDTO.class);
					resultDTO.add(pro);
				} catch (ImplementationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			return resultDTO;
		}else{
			return null;
		}	
	}
	
	
	public List<Project> getProjects(){
		String hql;
		Query query;
		List<Project> result = null;
		//List<ProjectDTO> resultDTO = new ArrayList<ProjectDTO>();
		
		hql = "select project from "+Project.class.getSimpleName()+" as project";
		query = projectDAO.getPersistenceContext().createQuery(hql);
		result = query.getResultList();
		//Converter converter = new ConverterImpl();
		
		if(!result.isEmpty()){
			//ProjectDTO pro = null; 
			//for(int i = 0; i < result.size();i++){
				//try {
					//pro =  (ProjectDTO) converter.getDTO(result.get(i), ProjectDTO.class);
					//resultDTO.add(pro);
				//} catch (ImplementationException e) {
					// TODO Auto-generated catch block
				//	e.printStackTrace();
			
				//}
				
			//}
			return result;
		}else{
			return null;
		}	 
	}

	/*public List<ProjectDTO> getProjectToSystem() {
		this.projectServicesImpl = new ProjectServicesImpl();
		ProjectsDTO prosDTO = this.projectServicesImpl.getProjects(); 
		List<ProjectDTO> list = prosDTO.getProjects();
		if (list.isEmpty()){
			return null;
		}else{
			return list;
		}
	}*/
	
	
}
