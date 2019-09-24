package br.ufpa.labes.spm.service.impl;

import java.util.Collection;

import javax.persistence.Query;

import org.qrconsult.spm.beans.editor.WebAPSEEVO;
import br.ufpa.labes.spm.repository.interfaces.agent.IAgentDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IWorkGroupDAO;
import br.ufpa.labes.spm.repository.interfaces.agent.IRoleDAO;
import br.ufpa.labes.spm.repository.interfaces.artifacts.IArtifactDAO;
import br.ufpa.labes.spm.repository.interfaces.resources.IResourceDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IArtifactTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IWorkGroupTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IResourceTypeDAO;
import br.ufpa.labes.spm.domain.Agent;
import br.ufpa.labes.spm.domain.AgentPlaysRole;
import br.ufpa.labes.spm.domain.WorkGroup;
import br.ufpa.labes.spm.domain.Role;
import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.domain.Resource;
import br.ufpa.labes.spm.domain.ArtifactType;
import br.ufpa.labes.spm.domain.WorkGroupType;
import br.ufpa.labes.spm.domain.ResourceType;
import br.ufpa.labes.spm.service.interfaces.WebAPSEEVOServices;


public class WebAPSEEVOServicesImpl implements WebAPSEEVOServices {

	private static final String ARTIFACT_CLASS_NAME = Artifact.class.getSimpleName();
	private static final String AGENT_CLASS_NAME = Agent.class.getSimpleName();
	private static final String WorkGroup_CLASS_NAME = WorkGroup.class.getSimpleName();
	private static final String RESOURCE_CLASS_NAME = Resource.class.getSimpleName();

	private static final String ARTIFACTTYPE_CLASS_NAME = ArtifactType.class.getSimpleName();
	private static final String ROLE_CLASS_NAME = Role.class.getSimpleName();
	private static final String WorkGroupTYPE_CLASS_NAME = WorkGroupType.class.getSimpleName();
	private static final String RESOURCETYPE_CLASS_NAME = ResourceType.class.getSimpleName();

	IArtifactDAO artifactDAO;
	IAgentDAO agentDAO;
	IWorkGroupDAO WorkGroupDAO;
	IResourceDAO resourceDAO;

	IArtifactTypeDAO artifactTypeDAO;
	IRoleDAO roleDAO;
	IWorkGroupTypeDAO WorkGroupTypeDAO;
	IResourceTypeDAO resourceTypeDAO;

	private Query query;

	@Override
	public String getWebAPSEEVOList(String className) {
		if (className.equals("Artifact"))
			return getArtifactVOList();
		else if (className.equals("Agent"))
			return getAgentVOList();
		else if (className.equals("WorkGroup"))
			return getWorkGroupVOList();
		else if (className.equals("Resource"))
			return getResourceVOList();
		else
			return null;
	}

	private String getArtifactVOList(){
		String hql = "select artifact from " + ARTIFACT_CLASS_NAME + " as artifact order by artifact.ident";
		query = artifactDAO.getPersistenceContext().createQuery(hql);
		Collection<Artifact> artifacts = query.getResultList();

		WebAPSEEVO webapseeVO;
		StringBuffer processXML = new StringBuffer();
		processXML.append("<ARTIFACTSVO>\n");
		for(Artifact artifact : artifacts){
			webapseeVO = new WebAPSEEVO();
			webapseeVO.setIdent(artifact.getIdent());
			webapseeVO.setName(artifact.getName());
			if(artifact.getTheArtifactType() != null) webapseeVO.setType(artifact.getTheArtifactType().getIdent());
			processXML.append(webapseeVO.getXMLTag());
		}

		processXML.append("</ARTIFACTSVO>\n");

		return processXML.toString();
	}

	private String getAgentVOList(){
		String hql = "select distinct agent from " + AGENT_CLASS_NAME + " as agent LEFT JOINCon agent.theAgentPlaysRole roles order by agent.ident";
		query = agentDAO.getPersistenceContext().createQuery(hql);
		Collection<Agent> agents = query.getResultList();

		WebAPSEEVO webapseeVO;
		StringBuffer processXML = new StringBuffer();
		processXML.append("<AGENTSVO>\n");
		for(Agent agent : agents){
			webapseeVO = new WebAPSEEVO();
			webapseeVO.setIdent(agent.getIdent());
			webapseeVO.setName(agent.getName());
			//Load roles played by agent
			Collection<AgentPlaysRole> aprs = agent.getTheAgentPlaysRoles();
			String[] roles = new String[aprs.size()];
			int i=0;
			for (AgentPlaysRole agentPlaysRole : aprs) {
				roles[i++] = agentPlaysRole.getTheRole().getIdent();
			}
			webapseeVO.setRoles(roles);

			processXML.append(webapseeVO.getXMLTag());
		}

		processXML.append("</AGENTSVO>\n");

		System.out.println(processXML.toString());
		return processXML.toString();
	}

	private String getWorkGroupVOList(){
		String hql = "select WorkGroup from " + WorkGroup_CLASS_NAME + " as WorkGroup order by WorkGroup.ident";
		query = WorkGroupDAO.getPersistenceContext().createQuery(hql);
		Collection<WorkGroup> WorkGroups = query.getResultList();

		WebAPSEEVO webapseeVO;
		StringBuffer processXML = new StringBuffer();
		processXML.append("<WorkGroupSVO>\n");
		for(WorkGroup WorkGroup : WorkGroups){
			webapseeVO = new WebAPSEEVO();
			webapseeVO.setIdent(WorkGroup.getIdent());
			webapseeVO.setName(WorkGroup.getName());
			webapseeVO.setType(WorkGroup.getTheWorkGroupType().getIdent());
			processXML.append(webapseeVO.getXMLTag());
		}

		processXML.append("</WorkGroupSVO>\n");
		return processXML.toString();
	}

	private String getResourceVOList(){
		String hql = "select resource from " + RESOURCE_CLASS_NAME + " as resource order by resource.ident";
		query = resourceDAO.getPersistenceContext().createQuery(hql);
		Collection<Resource> resources = query.getResultList();

		WebAPSEEVO webapseeVO;
		StringBuffer processXML = new StringBuffer();
		processXML.append("<RESOURCESVO>\n");
		for(Resource resource : resources){
			webapseeVO = new WebAPSEEVO();
			webapseeVO.setIdent(resource.getIdent());
			webapseeVO.setName(resource.getName());
			webapseeVO.setType(resource.getTheResourceType().getIdent());
			processXML.append(webapseeVO.getXMLTag());
		}

		processXML.append("</RESOURCESVO>\n");
		return processXML.toString();
	}

	@Override
	public String getTypeVOList(String className) {
		if (className.equals("Artifact"))
			return getArtifactTypeVOList();
		else if (className.equals("Agent"))
			return getRoleVOList();
		else if (className.equals("WorkGroup"))
			return getWorkGroupTypeVOList();
		else if (className.equals("Resource"))
			return getResourceTypeVOList();
		else
			return null;
	}

	private String getArtifactTypeVOList(){
		String hql = "select artifacttype from " + ARTIFACTTYPE_CLASS_NAME + " as artifacttype order by artifacttype.ident";
		query = artifactTypeDAO.getPersistenceContext().createQuery(hql);
		Collection<ArtifactType> artifactTypes = query.getResultList();

		WebAPSEEVO webapseeVO;
		StringBuffer processXML = new StringBuffer();
		processXML.append("<ARTIFACTTYPESVO>\n");
		for(ArtifactType artifactType : artifactTypes){
			webapseeVO = new WebAPSEEVO();
			webapseeVO.setIdent(artifactType.getIdent());
			processXML.append(webapseeVO.getXMLTag());
		}

		processXML.append("</ARTIFACTTYPESVO>\n");
		return processXML.toString();
	}

	private String getRoleVOList(){
		String hql = "select role from " + ROLE_CLASS_NAME + " as role order by role.ident";
		query = roleDAO.getPersistenceContext().createQuery(hql);
		Collection<Role> roles = query.getResultList();

		WebAPSEEVO webapseeVO;
		StringBuffer processXML = new StringBuffer();
		processXML.append("<ROLESVO>\n");
		for(Role role : roles){
			webapseeVO = new WebAPSEEVO();
			webapseeVO.setIdent(role.getIdent());
			processXML.append(webapseeVO.getXMLTag());
		}

		processXML.append("</ROLESVO>\n");
		return processXML.toString();
	}

	private String getWorkGroupTypeVOList(){
		String hql = "select WorkGrouptype from " + WorkGroupTYPE_CLASS_NAME + " as WorkGrouptype order by WorkGrouptype.ident";
		query = WorkGroupTypeDAO.getPersistenceContext().createQuery(hql);
		Collection<WorkGroupType> WorkGroupTypes = query.getResultList();

		WebAPSEEVO webapseeVO;
		StringBuffer processXML = new StringBuffer();
		processXML.append("<WorkGroupTYPESVO>\n");
		for(WorkGroupType WorkGroupType : WorkGroupTypes){
			webapseeVO = new WebAPSEEVO();
			webapseeVO.setIdent(WorkGroupType.getIdent());
			processXML.append(webapseeVO.getXMLTag());
		}

		processXML.append("</WorkGroupTYPESVO>\n");
		return processXML.toString();
	}

	private String getResourceTypeVOList(){
		String hql = "select resourcetype from " + RESOURCETYPE_CLASS_NAME + " as resourcetype order by resourcetype.ident";
		query = resourceTypeDAO.getPersistenceContext().createQuery(hql);
		Collection<ResourceType> resourceTypes = query.getResultList();

		WebAPSEEVO webapseeVO;
		StringBuffer processXML = new StringBuffer();
		processXML.append("<RESOURCETYPESVO>\n");
		for(ResourceType resourceType : resourceTypes){
			webapseeVO = new WebAPSEEVO();
			webapseeVO.setIdent(resourceType.getIdent());
			processXML.append(webapseeVO.getXMLTag());
		}

		processXML.append("</RESOURCETYPESVO>\n");
		return processXML.toString();
	}

}
