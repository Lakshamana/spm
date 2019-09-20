package br.ufpa.labes.spm.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.ICompanyDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.IProjectDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.IDevelopingSystemDAO;
import br.ufpa.labes.spm.service.dto.AgentDTO;
import br.ufpa.labes.spm.service.dto.ProjectDTO;
import br.ufpa.labes.spm.service.dto.DevelopingSystemDTO;
import br.ufpa.labes.spm.service.dto.SystemsDTO;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.AgentAffinityAgent;
import br.ufpa.labes.spm.domain.AgentHasAbility;
import br.ufpa.labes.spm.domain.AgentPlaysRole;
import br.ufpa.labes.spm.domain.WorkGroup;
import br.ufpa.labes.spm.domain.Company;
import br.ufpa.labes.spm.domain.Project;
import br.ufpa.labes.spm.domain.DevelopingSystem;
import br.ufpa.labes.spm.service.interfaces.ProjectServices;
import br.ufpa.labes.spm.service.interfaces.DevelopingSystemDTO;
import br.ufpa.labes.spm.service.interfaces.SystemServices;

public class SystemServicesImpl implements SystemServices {

  private static final String SYSTEM_CLASSNAME = DevelopingSystem.class.getSimpleName();

  IProjectDAO projectDAO;

  IDevelopingSystemDAO developingSystemDAO;

  ICompanyDAO comDAO;

  Converter converter = new ConverterImpl();

  ProjectServices projectServicesImpl = null;

  public DevelopingSystem getSystemForName(String nome) {
    String hql;
    Query query;
    List<DevelopingSystem> result = null;

    hql = "select system from " + DevelopingSystem.class.getSimpleName() + " as system where system.name = :sysname";
    query = developingSystemDAO.getPersistenceContext().createQuery(hql);
    query.setParameter("sysname", nome);

    result = query.getResultList();

    if (!result.isEmpty()) {
      DevelopingSystem sys = result.get(0);
      return sys;
    } else {
      return null;
    }
  }

  private DevelopingSystemDTO convertSystemToSystemDTO(DevelopingSystem system) {
    DevelopingSystemDTO systemDTO = new DevelopingSystemDTO();
    try {
      systemDTO = (DevelopingSystemDTO) converter.getDTO(system, DevelopingSystemDTO.class);
      return systemDTO;

    } catch (ImplementationException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public DevelopingSystemDTO getSystem(String nameSystem) {
    String hql;
    Query query;
    List<DevelopingSystem> result = null;
    List<Project> abis = new ArrayList<Project>();
    List<ProjectDTO> abisDTO = new ArrayList<ProjectDTO>();
    DevelopingSystemDTO systemDTO = null;
    ProjectDTO abi = null;

    hql = "select role from " + DevelopingSystem.class.getSimpleName() + " as role where role.name = :rolname";
    query = developingSystemDAO.getPersistenceContext().createQuery(hql);
    query.setParameter("rolname", nameSystem);
    result = query.getResultList();
    Collection<Project> lis = result.get(0).getTheProject();
    java.lang.System.out.println("project1");

    if (!lis.isEmpty()) {
      java.lang.System.out.println("project2222");
      for (Project pro : lis) {
        java.lang.System.out.println("project2");
        abis.add(pro);
      }
    }

    Converter converter = new ConverterImpl();
    for (int i = 0; i < abis.size(); i++) {
      try {
        abi = (ProjectDTO) converter.getDTO(abis.get(i), ProjectDTO.class);
        abisDTO.add(abi);
      } catch (ImplementationException e) {

        e.printStackTrace();
      }
    }

    try {
      systemDTO = (DevelopingSystemDTO) converter.getDTO(result.get(0), DevelopingSystemDTO.class);
      if (abis != null) {
        systemDTO.setProjetos(abisDTO);
      }

    } catch (ImplementationException e) {
      e.printStackTrace();
    }

    return systemDTO;
  }

  @Override
  public DevelopingSystemDTO saveSystem(DevelopingSystemDTO systemDTO) {
    Converter converter = new ConverterImpl();
    java.lang.System.out.println("passei aqui antes");
    try {
      String orgSystem = systemDTO.getIdent();
      String systemName = systemDTO.getName();
      Project abil;
      if (orgSystem != null && !orgSystem.equals("") && systemName != null && !systemName.equals("")) {
        Company organize = comDAO.retrieveBySecondaryKey(orgSystem);
        java.lang.System.out.println(organize.getAddress() + "trse");
        DevelopingSystem system = this.getSystemForName(systemName);
        if (organize != null) {
          java.lang.System.out.println("passei aqui 1");
          if (system != null) {
            java.lang.System.out.println("passei aqui 2");
            system = (DevelopingSystem) converter.getEntity(systemDTO, system);
            java.lang.System.out.println("Projetos: " + systemDTO.getProjetos());

            for (Project project : system.getTheProject()) { // Quebrar todas as
              project.setTheSystem(null);
            } // anteriormente, pra salvar
            system.setTheProject(null);
            developingSystemDAO.update(system);
            java.lang.System.out.println("tamanho: " + systemDTO.getProjetos().size());

            if (!systemDTO.getProjetos().isEmpty()) {
              Collection<Project> lisAbi = new ArrayList<Project>();

              for (ProjectDTO pro : systemDTO.getProjetos()) {
                java.lang.System.out.println("Construindo projetos1");
                abil = (Project) converter.getEntity(pro, Project.class);
                abil.setTheSystem(system);
                lisAbi.add(abil);
              }
              system.setTheProject(lisAbi);
            }
            system.setTheOrganization(organize);
            system = developingSystemDAO.update(system);
            java.lang.System.out.println("passei aqui 4");
          } else {

            java.lang.System.out.println("passei aqui 5");
            system = (DevelopingSystem) converter.getEntity(systemDTO, DevelopingSystem.class);

            if (systemDTO.getProjetos() != null && !systemDTO.getProjetos().isEmpty()) {
              Collection<Project> lisAbi = new ArrayList<Project>();
              List<Project> ar = this.getProjects();
              for (ProjectDTO pro : systemDTO.getProjetos()) {
                java.lang.System.out.println("Construindo projetos2");
                for (int i = 0; i < ar.size(); i++) {
                  if (pro.getName().equals(ar.get(i).getName())) {
                    lisAbi.add(ar.get(i));
                    break;
                  }
                }
              }
              system.setTheProject(lisAbi);
            }
            system.setTheOrganization(organize);
            system = developingSystemDAO.daoSave(system);
          }
          java.lang.System.out.println("passei aqui 6");
          systemDTO = (DevelopingSystemDTO) converter.getDTO(system, DevelopingSystemDTO.class);
        } else {
          java.lang.System.out.println("passei aqui 7");
          return null;
        }
      } else {
        java.lang.System.out.println("passei aqui 8" + orgSystem + ":");
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
  public Boolean removeSystem(DevelopingSystemDTO systemDTO) {
    String hql;
    Query query;
    java.lang.System.out.println("re1" + systemDTO.getName());
    hql = "select system from " + DevelopingSystem.class.getSimpleName() + " as system where system.name = :abiname";
    query = developingSystemDAO.getPersistenceContext().createQuery(hql);
    query.setParameter("abiname", systemDTO.getName());
    List<DevelopingSystem> result = query.getResultList();
    java.lang.System.out.println("re1");
    DevelopingSystem system = result.get(0);
    if (system != null) {
      if (system.getTheProject() != null) {
        Collection<Project> re = system.getTheProject();
        for (Project ro : re) {
          // roleNeedsDAO.daoDelete(ro);
          ro.setTheSystem(null);
        }
      }
      java.lang.System.out.println("re2");
      developingSystemDAO.daoDelete(system);
      return true;
    } else {
      java.lang.System.out.println("re3");
      return false;
    }
  }

  @Override
  public SystemsDTO getSystems(String term, String domain) {
    String hql;
    Query query;
    List<String> resultList = null;

    if (domain != null) {
      hql = "select system.name from " + DevelopingSystem.class.getSimpleName()
          + " as system where system.name like :termo and system.ident = :domain";
      query = developingSystemDAO.getPersistenceContext().createQuery(hql);
      query.setParameter("domain", domain);
      query.setParameter("termo", "%" + term + "%");
      resultList = query.getResultList();
    } else {
      hql = "select system.name from " + DevelopingSystem.class.getSimpleName()
          + " as system where system.name like :termo";
      query = developingSystemDAO.getPersistenceContext().createQuery(hql);
      query.setParameter("termo", "%" + term + "%");
      resultList = query.getResultList();
    }

    SystemsDTO systemsDTO = new SystemsDTO();
    String[] names = new String[resultList.size()];
    resultList.toArray(names);
    systemsDTO.setNameSystemas(names);

    return systemsDTO;
  }

  public String[] getSystemsCompany() {
    String hql;
    Query query;

    hql = "select ident from " + Company.class.getSimpleName();
    query = comDAO.getPersistenceContext().createQuery(hql);

    List<String> comList = new ArrayList<String>();
    comList = query.getResultList();
    String[] list = new String[comList.size()];
    comList.toArray(list);
    return list;
  }

  @Override
  public SystemsDTO getSystems() {
    String hql;
    Query query;
    hql = "select name from " + DevelopingSystem.class.getSimpleName();
    query = developingSystemDAO.getPersistenceContext().createQuery(hql);

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
    query = developingSystemDAO.getPersistenceContext().createQuery(hql);
    List<DevelopingSystem> result = query.getResultList();

    java.lang.System.out.println("result: " + result);
    java.lang.System.out.println(converter);
    java.lang.System.out.println(systemsDTO.getSystemsDTO());

    for (DevelopingSystem system2 : result) {
      systemsDTO.getSystemsDTO().add(convertSystemToSystemDTO(system2));
    }

    return systemsDTO;
  }

  @Override
  public Boolean removeProjectToSystem(DevelopingSystemDTO systemDTO) {
    String hql;
    Query query;
    java.lang.System.out.println("re1" + systemDTO.getName());
    hql = "select system from " + DevelopingSystem.class.getSimpleName() + " as system where system.name = :abiname";
    query = developingSystemDAO.getPersistenceContext().createQuery(hql);
    query.setParameter("abiname", systemDTO.getName());
    List<DevelopingSystem> result = query.getResultList();
    java.lang.System.out.println("re1");
    DevelopingSystem system = result.get(0);
    if (system != null) {
      if (system.getTheProject() != null) {
        Collection<Project> re = system.getTheProject();
        for (Project ro : re) {
          // roleNeedsDAO.daoDelete(ro);
          ro.setTheSystem(null);
        }
      }
      java.lang.System.out.println("re2");
      system.setTheProject(null);
      developingSystemDAO.update(system);
      return true;
    } else {
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

    hql = "select project from " + Project.class.getSimpleName() + " as project";
    query = projectDAO.getPersistenceContext().createQuery(hql);
    result = query.getResultList();
    Converter converter = new ConverterImpl();

    if (!result.isEmpty()) {
      ProjectDTO pro = null;
      for (int i = 0; i < result.size(); i++) {
        try {
          pro = (ProjectDTO) converter.getDTO(result.get(i), ProjectDTO.class);
          resultDTO.add(pro);
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

  public List<Project> getProjects() {
    String hql;
    Query query;
    List<Project> result = null;
    // List<ProjectDTO> resultDTO = new ArrayList<ProjectDTO>();

    hql = "select project from " + Project.class.getSimpleName() + " as project";
    query = projectDAO.getPersistenceContext().createQuery(hql);
    result = query.getResultList();
    // Converter converter = new ConverterImpl();

    if (!result.isEmpty()) {
      // ProjectDTO pro = null;
      // for(int i = 0; i < result.size();i++){
      // try {
      // pro = (ProjectDTO) converter.getDTO(result.get(i), ProjectDTO.class);
      // resultDTO.add(pro);
      // } catch (ImplementationException e) {
      // TODO Auto-generated catch block
      // e.printStackTrace();

      // }

      // }
      return result;
    } else {
      return null;
    }
  }

  @Override
  public DevelopingSystemDTO getSystem(String nameSystem) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DevelopingSystemDTO saveSystem(DevelopingSystemDTO systemDTO) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Boolean removeSystem(DevelopingSystemDTO systemDTO) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public br.ufpa.labes.spm.service.interfaces.SystemsDTO getSystems(String termoBusca, String domainFilter) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public br.ufpa.labes.spm.service.interfaces.SystemsDTO getSystems() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Boolean removeProjectToSystem(DevelopingSystemDTO systemDTO) {
    // TODO Auto-generated method stub
    return null;
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
