package br.ufpa.labes.spm.service.interfaces;

import java.util.Set;

import br.ufpa.labes.spm.service.dto.dashboard.ProjectCost;
import br.ufpa.labes.spm.service.dto.dashboard.ProjectStatistic;
import br.ufpa.labes.spm.service.dto.AgentsDTO;
import br.ufpa.labes.spm.service.dto.ArtifactsDTO;
import br.ufpa.labes.spm.service.dto.ProjectDTO;
import br.ufpa.labes.spm.service.dto.ProjectsDTO;
import br.ufpa.labes.spm.exceptions.DAOException;
import br.ufpa.labes.spm.exceptions.WebapseeException;
import br.ufpa.labes.spm.domain.Project;

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

	public AgentsDTO getAgentsFromProjects(String theProcess_oid, Long agent_oid);

	public AgentsDTO getAgentsOnline(Long agent_oid);

	public ProjectDTO getProjectById(Long oid);

  public Set<ProjectStatistic> getProjectsForDashboard();

	public ProjectStatistic getProjectForDashboard(Long oid);

	public ProjectDTO getProjectByIdent(String ident);

	public ProjectCost getProjectCost(Long projectId);
}
