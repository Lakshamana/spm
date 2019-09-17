package org.qrconsult.spm.services.interfaces;

import java.util.List;

import javax.ejb.Remote;

import org.qrconsult.spm.dtos.dashboard.ProjectCost;
import org.qrconsult.spm.dtos.dashboard.ProjectStatistic;
import org.qrconsult.spm.dtos.formAgent.AgentsDTO;
import org.qrconsult.spm.dtos.formArtifacts.ArtifactsDTO;
import org.qrconsult.spm.dtos.formProject.ProjectDTO;
import org.qrconsult.spm.dtos.formProject.ProjectsDTO;
import org.qrconsult.spm.exceptions.DAOException;
import org.qrconsult.spm.exceptions.WebapseeException;
import org.qrconsult.spm.model.organizationPolicies.Project;

@Remote
public interface ProjectServices {
	
	public ProjectDTO getProject(String projectName);
	
	public ProjectDTO saveProject(ProjectDTO projectDTO);
	
	public Boolean removeProject(String projectName);
	
	public ProjectsDTO getProjects();

	public ProjectsDTO getProjects(String termoBusca, Boolean isFinalizado);
	
	public ProjectsDTO getEnabledProjects();
	
	public ProjectsDTO getDisabledProjects(); 
	
	public ProjectDTO disableProject(String projectName);
	
	public ProjectDTO activateProject(String projectName);
	
	public ArtifactsDTO getFinalArtifactsAvailableForProjects();
	
	public Boolean removeFinalArtifact(String projectName, String artifactName);

	public Project getProjectFromName(String projectName);
	
	public Boolean removeManager(ProjectDTO projectName, String manager);
	
	public String getProcessModelXML(String level);
	
	public void importProcess(String processId, String xml);
	
	public String exportProcess(String processId, boolean exportArtifactVersions);

	public ProjectDTO executeProcess(String projectName) throws DAOException, WebapseeException;

	public AgentsDTO getAgentsFromProjects(String theProcess_oid, Integer agent_oid);

	public AgentsDTO getAgentsOnline(Integer agent_oid);
	
	public ProjectDTO getProjectById(Integer oid);
	
	public List<ProjectStatistic> getProjectsForDashboard();

	public ProjectStatistic getProjectForDashboard(Integer oid);

	public ProjectDTO getProjectByIdent(String ident);

	public ProjectCost getProjectCost(Integer projectId);	
}
