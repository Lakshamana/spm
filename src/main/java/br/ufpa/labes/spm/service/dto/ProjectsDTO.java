package br.ufpa.labes.spm.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("serial")
public class ProjectsDTO implements Serializable {

	private List<ProjectDTO> projects;

	public ProjectsDTO() {}

	public ProjectsDTO(List<ProjectDTO> projects) {
		this.projects = projects;
	}

	public boolean addProject(ProjectDTO projectDTO) {
		return this.projects.add(projectDTO);
	}

	public boolean addProjectsDTO(ProjectsDTO projectsDTO) {
		return this.projects.addAll(projectsDTO.getProjects());
	}

	public boolean removeProject(ProjectDTO projectDTO) {
		return this.projects.remove(projectDTO);
	}

	public ProjectDTO getProjectDTO(int index) {
		return this.projects.get(index);
	}

	public boolean isEmpty() {
		return this.projects.isEmpty();
	}

	public int size() {
		return this.projects.size();
	}

	public List<String> getProjectNames() {
		List<String> names = new ArrayList<String>();
		for (ProjectDTO projectDTO : this.projects) {
			names.add(projectDTO.getName());
		}

		return names;
	}

	public List<String> getProjectNamesWhereActiveIs(boolean isActive) {
		List<String> projects = new ArrayList<String>();
		for (ProjectDTO projectDTO : this.projects) {
			boolean equalActiveState = projectDTO.isActive();
			if(equalActiveState) {
				projects.add(projectDTO.getName());
			}
		}
		return projects;
	}

	public ProjectsDTO getProjectsWhereActiveIs(boolean isActive) {
		List<ProjectDTO> projects = new ArrayList<ProjectDTO>();
		for (ProjectDTO projectDTO : this.projects) {
			boolean equalActiveState = projectDTO.isActive() == isActive;
			if(equalActiveState) {
				projects.add(projectDTO);
			}
		}

		ProjectsDTO projectsDTO = new ProjectsDTO(projects);
		return projectsDTO;
	}

	public void sortProjects(Comparator<ProjectDTO> comparator) {
		Collections.sort(this.projects, comparator);
	}

	public void sortProjectsByName() {
		Collections.sort(this.projects, new Comparator<ProjectDTO>() {
			@Override
			public int compare(ProjectDTO project1, ProjectDTO project2) {
				return project1.getName().compareTo(project2.getName());
			}
		});
	}

	public List<ProjectDTO> getProjects() {
		return projects;
	}

}
