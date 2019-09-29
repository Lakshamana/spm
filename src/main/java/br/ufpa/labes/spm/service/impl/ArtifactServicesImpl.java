package br.ufpa.labes.spm.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import br.ufpa.labes.spm.converter.Converter;
import br.ufpa.labes.spm.converter.ConverterImpl;
import br.ufpa.labes.spm.exceptions.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.artifacts.IArtifactDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.IProjectDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IArtifactTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.ITypeDAO;
import br.ufpa.labes.spm.service.dto.SimpleArtifactDescriptorDTO;
import br.ufpa.labes.spm.service.dto.ArtifactDTO;
import br.ufpa.labes.spm.service.dto.ArtifactsDTO;
import br.ufpa.labes.spm.service.dto.TypesDTO;
import br.ufpa.labes.spm.exceptions.DAOException;
import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.domain.Project;
import br.ufpa.labes.spm.domain.ArtifactType;
import br.ufpa.labes.spm.domain.Type;
import br.ufpa.labes.spm.service.interfaces.ArtifactServices;
import br.ufpa.labes.spm.util.ident.ConversorDeIdent;
import br.ufpa.labes.spm.util.ident.SemCaracteresEspeciais;
import br.ufpa.labes.spm.util.ident.TrocaEspacoPorPonto;


public class ArtifactServicesImpl implements ArtifactServices {

	private static final int SINGLE_RESULT = 1;

	private static final String ARTIFACT_CLASS_NAME = Artifact.class.getSimpleName();

	IArtifactDAO artifactDAO;

	IArtifactTypeDAO artifactTypeDAO;

	IProjectDAO projectDAO;

	ITypeDAO typeDAO;

	Converter converter = new ConverterImpl();

	private Query query;

	@Override
	@SuppressWarnings("unchecked")
	public TypesDTO getArtifactTypes() {

		String hql;
		List<Type> typesLists = new ArrayList<Type>();

		hql = "from " + ArtifactType.class.getSimpleName();
		query = typeDAO.getPersistenceContext().createQuery(hql);
		typesLists = query.getResultList();

		TypesDTO typesDTO = new TypesDTO(typesLists.size());
		int j = 0;
		for (Type type : typesLists) {
			String typeIdent = type.getIdent();
			String superTypeIdent = (type.getSuperType()!=null ? type.getSuperType().getIdent() : "");
			String rootType = ArtifactType.class.getSimpleName();
			typesDTO.addType(typeIdent, superTypeIdent, rootType, j);
			j++;
		}
		return typesDTO;
	}

	@Override
	public ArtifactDTO getArtifact(String artifactIdent) {
			try {
				Artifact artifact = artifactDAO.retrieveBySecondaryKey(artifactIdent);
				if(artifact != null) {
					ArtifactDTO artifactDTO = (ArtifactDTO) converter.getDTO(artifact, ArtifactDTO.class);
					ArtifactType theArtifactType = artifact.getTheArtifactType();

					if(theArtifactType != null) {
						System.out.println(theArtifactType + " - " + theArtifactType.getIdent());
						artifactDTO.setTheArtifactType(theArtifactType.getIdent());
					}
					return artifactDTO;
				}
			} catch (ImplementationException e) {
				e.printStackTrace();
			}
		return null;
	}

	@Override
	public ArtifactDTO alreadyExist(String artifactIdent) throws DAOException {
		Artifact artifact = artifactDAO.retrieveBySecondaryKey(artifactIdent);
		ArtifactDTO artifactDTO;
		if(artifact == null || artifact.getId().equals(null)) {
			throw new DAOException("Não Existe");
		} else {
			artifactDTO = convertArtifactToArtifactDTO(artifact);
		}
		return artifactDTO;
	}

	@Override
	public ArtifactDTO saveArtifact(ArtifactDTO artifactDTO) {
		try {
				for (String derivedTo : artifactDTO.getDerivedTo()) {
					System.out.println(derivedTo);
				}
				ArtifactType artifactType = artifactTypeDAO.retrieveBySecondaryKey(artifactDTO.getTheArtifactType());

				List<Artifact> possess = this.getArtifactsFromNames(artifactDTO.getPossess());
				List<Artifact> derivedTo = this.getArtifactsFromNames(artifactDTO.getDerivedTo());
				Artifact artifact = null;

				this.getArtifacts(artifactDTO.getName(), null, null, null);

				artifact = artifactDAO.retrieveBySecondaryKey(artifactDTO.getIdent());

				if(artifact == null) {
					artifact = (Artifact) converter.getEntity(artifactDTO, Artifact.class);
					artifact.setTheArtifactType(artifactType);
					artifactDAO.daoSave(artifact);

					String newIdent = artifactDAO.generateIdent(artifact.getName(), artifact);
					System.out.println("new ident: " + newIdent);
					artifact.setIdent(newIdent);
					artifactDTO.setIdent(newIdent);
					artifactDAO.update(artifact);

				} else {
					artifact.setName(artifactDTO.getName());
					artifact.setDescription(artifactDTO.getDescription());
					artifact.setTheArtifactType(artifactType);
					artifact.setPathName(artifactDTO.getPathName());
					artifact.setIsTemplate(artifactDTO.isIsTemplate());
					artifact.setIsActive(artifactDTO.isIsActive());
				}

				for (Artifact derived : artifact.getDerivedTo()) { //Quebrar todas as
					derived.setDerivedFrom(null);				   //rela��es que estavam
				}										   //anteriormente, pra salvar
				artifact.setDerivedTo(null);					   //somente as novas

				for (Artifact poss : artifact.getPossess()) { //Quebrar todas as
					poss.setBelongsTo(null);				  //rela��es que estavam
				}											  //anteriormente, pra salvar
				artifact.setPossess(null);					  //somente as novas

//				artifactDAO.update(artifact);

				artifact.setPossess(possess);
				artifact.setDerivedTo(derivedTo);

				this.setPossessBelongsTo(artifact, possess);
				this.setDerivedToDerivedFrom(artifact, derivedTo);
				this.updateArtifacts(possess);
				this.updateArtifacts(derivedTo);

				artifactDAO.update(artifact);

			} catch (ImplementationException e) {
				e.printStackTrace();
			}

		return artifactDTO;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ArtifactsDTO getArtifacts() {
		String hql = "select artifact from " + ARTIFACT_CLASS_NAME + " as artifact";
		query = artifactDAO.getPersistenceContext().createQuery(hql);
		List<Artifact> artifacts = query.getResultList();

		return this.convertArtifactsToArtifactsDTO(artifacts);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ArtifactsDTO getArtifactsWithoutRelationship(boolean composicao) {
		String hql;
		if(composicao) {
			hql = "select artifact from " + ARTIFACT_CLASS_NAME + " as artifact where artifact.belongsTo is null";
		} else {
			hql = "select artifact from " + ARTIFACT_CLASS_NAME + " as artifact where artifact.derivedFrom is null";
		}
		query = artifactDAO.getPersistenceContext().createQuery(hql);
		List<Artifact> artifacts = query.getResultList();
		return this.convertArtifactsToArtifactsDTO(artifacts);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ArtifactsDTO getArtifacts(String termoBusca, String domainFilter, String projectFilter, Boolean orgFilter) {
//		System.out.println("Param�tros: " + termoBusca + ", " + domainFilter + ", " + orgFilter);

		String activeFilter = (orgFilter == null) ? "" : " and art.isActive is :orgFilter" ;

		String hql;
		if(domainFilter != null) {
			hql = "select art from " + ARTIFACT_CLASS_NAME + " as art where art.name like :termo and art.ident = :domain" + activeFilter;
			query = artifactDAO.getPersistenceContext().createQuery(hql);
			query.setParameter("termo", "%"+ termoBusca + "%");
			query.setParameter("domain", domainFilter);
		} else {
			hql = "select art from " + ARTIFACT_CLASS_NAME + " as art where art.name like :termo" + activeFilter;
			query = artifactDAO.getPersistenceContext().createQuery(hql);
			query.setParameter("termo", "%"+ termoBusca + "%");
		}
		if(!activeFilter.isEmpty()) {
			query.setParameter("orgFilter", orgFilter);
		}

		List<Artifact> resultado = query.getResultList();

		if(projectFilter != null) {
			List<Artifact> artifactsFromProject = usingProjectFilter(projectFilter);
			List<Artifact> artifactsToRemove = new ArrayList<Artifact>();
			for (Artifact artifact : artifactsFromProject) {
				if(!resultado.contains(artifact)) {
					artifactsToRemove.add(artifact);
				}
			}
			artifactsFromProject.removeAll(artifactsToRemove);
			resultado.clear();
			resultado.addAll(artifactsFromProject);
		}
		ArtifactsDTO artifactsDTO = new ArtifactsDTO(new ArrayList<ArtifactDTO>());
		artifactsDTO = convertArtifactsToArtifactsDTO(resultado);

		return artifactsDTO;
	}

	private List<Artifact> usingProjectFilter(String projectFilter) {
		List<Artifact> artifactsFromProject = new ArrayList<Artifact>();

		String hql = "SELECT project FROM " + Project.class.getSimpleName() + " AS project WHERE project.name = :name";
		query = projectDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("name", projectFilter);

		if(!query.getResultList().isEmpty()) {
			Project project = (Project) query.getResultList().get(0);
			artifactsFromProject.addAll(project.getFinalArtifacts());
		}

		return artifactsFromProject;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ArtifactsDTO getArtifactsThatBelongsTo(String artifactName) {
		String hql = "select artifact from " + ARTIFACT_CLASS_NAME + " as artifact where artifact.belongsTo.name = '" + artifactName + "'";
		query = artifactDAO.getPersistenceContext().createQuery(hql);

		List<Artifact> resultado = query.getResultList();
		ArtifactsDTO artifactsDTO = new ArtifactsDTO(new ArrayList<ArtifactDTO>());

		if(!resultado.isEmpty())
			artifactsDTO = convertArtifactsToArtifactsDTO(resultado);

		return artifactsDTO;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ArtifactsDTO getArtifactsDerivedFrom(String artifactName) {
		String hql = "select artifact from " + ARTIFACT_CLASS_NAME + " as artifact where artifact.derivedFrom.name = '" + artifactName + "'";
		query = artifactDAO.getPersistenceContext().createQuery(hql);

		List<Artifact> resultado = query.getResultList();
		ArtifactsDTO artifactsDTO = new ArtifactsDTO(new ArrayList<ArtifactDTO>());

		if(!resultado.isEmpty())
			artifactsDTO = convertArtifactsToArtifactsDTO(resultado);

		return artifactsDTO;
	}

	@Override
	public ArtifactDTO updateBelongsTo(String artifactName) {
		ArtifactDTO artifactDTO = new ArtifactDTO();

		String hql = "select artifact from " + ARTIFACT_CLASS_NAME + " as artifact where artifact.name = '" + artifactName + "'";
		query = artifactDAO.getPersistenceContext().createQuery(hql);
		if(!query.getResultList().isEmpty()) {
			Artifact artifact = (Artifact) query.getResultList().get(0);
			artifact.setBelongsTo(null);
			artifactDTO = this.convertArtifactToArtifactDTO(artifact);
		}

		return artifactDTO;
	}

	@Override
	public ArtifactDTO updateDerivedFrom(String artifactName) {
		ArtifactDTO artifactDTO = new ArtifactDTO();
		String hql = "select artifact from " + ARTIFACT_CLASS_NAME + " as artifact where artifact.name = '" + artifactName + "'";
		query = artifactDAO.getPersistenceContext().createQuery(hql);
		if(!query.getResultList().isEmpty()) {
			Artifact artifact = (Artifact) query.getResultList().get(0);
			artifact.setDerivedFrom(null);
			artifactDTO = this.convertArtifactToArtifactDTO(artifact);
		}

		return artifactDTO;
	}

	@Override
	public Boolean removeArtifact(ArtifactDTO artifactDTO) {
		String hql = "select artifact from " + ARTIFACT_CLASS_NAME + " as artifact where artifact.name = '" + artifactDTO.getName() + "'";
		query = artifactDAO.getPersistenceContext().createQuery(hql);
//		Artifact artifact = (Artifact) query.getSingleResult();
		Artifact artifact = (Artifact) query.getResultList().get(0);

		Collection<Artifact> possess = this.getArtifactsFromNames(artifactDTO.getPossess());
		List<Artifact> derivedTo = this.getArtifactsFromNames(artifactDTO.getDerivedTo());

		if(artifact != null) {
			this.setPossessBelongsTo(null, possess);
			this.setDerivedToDerivedFrom(null, derivedTo);
			this.updateArtifacts(possess);
			this.updateArtifacts(derivedTo);

			artifactDAO.getPersistenceContext().remove(artifact);
			return true;
		}

		return false;
	}

	private void updateArtifacts(Collection<Artifact> possess) {
		for (Artifact artifact : possess) {
			artifactDAO.update(artifact);
		}
	}

	private void setPossessBelongsTo(Artifact artifact, Collection<Artifact> possess) {
		for (Artifact component : possess) {
			component.setBelongsTo(artifact);
		}
	}

	private void setDerivedToDerivedFrom(Artifact artifact, Collection<Artifact> derivedTo) {
		for (Artifact derived : derivedTo) {
			derived.setDerivedFrom(artifact);
		}
	}

	private ArtifactDTO convertArtifactToArtifactDTO(Artifact artifact) {
		ArtifactDTO artifactDTO = new ArtifactDTO();
		try {

			artifactDTO = (ArtifactDTO) converter.getDTO(artifact, ArtifactDTO.class);
			if(artifact.getTheArtifactType() != null)
				artifactDTO.setTheArtifactType(artifact.getTheArtifactType().getIdent());
		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return artifactDTO;
	}

	private ArtifactsDTO convertArtifactsToArtifactsDTO(List<Artifact> artifactsList) {
		ArtifactsDTO artifactsDTOList = new ArtifactsDTO(new ArrayList<ArtifactDTO>());

		for(Artifact artifact : artifactsList) {
			artifactsDTOList.addArtifactDTO(this.convertArtifactToArtifactDTO(artifact));
		}

		return artifactsDTOList;
	}

	private List<Artifact> getArtifactsFromNames(Collection<String> collection) {
		List<Artifact> artifacts = new ArrayList<Artifact>();
		for(String name : collection) {
			artifacts.add(this.getArtifactFromName(name));
		}
//		System.out.println("aqui");
//		for (Artifact artifact : artifacts) {
//			System.out.println(artifact.getName());
//		}

		return artifacts;
	}

	private Artifact getArtifactFromName(String ident) {
//		String hql = "select o from " + ARTIFACT_CLASS_NAME + " o where o.name = :name";
//		query = artifactDAO.getPersistenceContext().createQuery(hql);
//		query.setParameter("name", name);
		System.out.println("Ident: " + ident);
		Artifact artifact = artifactDAO.retrieveBySecondaryKey(ident);
		System.out.println(artifact);

		return artifact;
	}

	@Override
	public Map<String, SimpleArtifactDescriptorDTO[]> getArtifactsForSelectedActivity(String identActivity) {
		SimpleArtifactDescriptorDTO[] input = artifactDAO.getInputArtifactsForNormal(identActivity);
		SimpleArtifactDescriptorDTO[] output = artifactDAO.getOutputArtifactsForNormal(identActivity);

		Map<String, SimpleArtifactDescriptorDTO[]> result = new HashMap<String, SimpleArtifactDescriptorDTO[]>();
		result.put("input", input);
		result.put("output", output);

		return result;
	}

}
