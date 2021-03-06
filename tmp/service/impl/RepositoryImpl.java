package br.ufpa.labes.spm.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import org.qrconsult.spm.artifactManagement.interfaces.ArtifactManagerInterface;
import org.qrconsult.spm.artifactManagement.interfaces.ArtifactVersionControlRemoteInterface;
import org.qrconsult.spm.beans.ArtifactMngContent;
import org.qrconsult.spm.beans.ArtifactMngDownload;
import org.qrconsult.spm.beans.ArtifactMngParamDownload;
import br.ufpa.labes.spm.converter.Converter;
import br.ufpa.labes.spm.converter.ConverterImpl;
import br.ufpa.labes.spm.exceptions.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.artifacts.IArtifactDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.INodeDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.IRepositoryDAO;
import br.ufpa.labes.spm.repository.interfaces.organizationPolicies.IStructureDAO;
import br.ufpa.labes.spm.repository.interfaces.plainActivities.INormalDAO;
import br.ufpa.labes.spm.service.dto.NodeDTO;
import br.ufpa.labes.spm.service.dto.RepositoriesDTO;
import br.ufpa.labes.spm.service.dto.RepositoryDTO;
import br.ufpa.labes.spm.service.dto.StructureDTO;
import br.ufpa.labes.spm.exceptions.WebapseeException;
import br.ufpa.labes.spm.domain.Activity;
import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.domain.ArtifactCon;
import br.ufpa.labes.spm.domain.Node;
import br.ufpa.labes.spm.domain.Project;
import br.ufpa.labes.spm.domain.VCSRepository;
import br.ufpa.labes.spm.domain.Structure;
import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.service.interfaces.RepositoryServices;

public class RepositoryImpl implements RepositoryServices{


	IRepositoryDAO repositoryDAO;

	IStructureDAO structureDAO;

	INodeDAO nodeDAO;

	INormalDAO normalDAO;

	IArtifactDAO artifactDAO;


	ArtifactManagerInterface artifactManager;

	Converter converter = new ConverterImpl();

	@Override
	public RepositoryDTO Salvar(RepositoryDTO repositoryDTO) {
		try {
			VCSRepository repository = (VCSRepository) converter.getEntity(repositoryDTO, VCSRepository.class);

			if (repositoryDAO.retrieveBySecondaryKey(repositoryDTO.getIdent()) == null){

				if (repositoryDTO.getTheStructure() != null){
					repository.setTheStructure(new Structure());

					repository.getTheStructure().setRootElement((Node) converter.getEntity(repositoryDTO.getTheStructure().getRootElement(), Node.class));
					repository.getTheStructure().getRootElement().setChildren(convertNodes(repositoryDTO.getTheStructure().getRootElement().getChildren(), repositoryDTO.getTheStructure().getRootElement()));
				}
				repository = repositoryDAO.daoSave(repository);

			} else {
				if (repositoryDTO.getTheStructure() != null){
					repository.setTheStructure(repositoryDAO.getTheStructure(repository.getIdent()));
					if (repository.getTheStructure() == null)
						repository.setTheStructure(new Structure());

					repository.getTheStructure().setRootElement((Node) converter.getEntity(repositoryDTO.getTheStructure().getRootElement(), Node.class));
					repository.getTheStructure().getRootElement().setChildren(convertNodes(repositoryDTO.getTheStructure().getRootElement().getChildren(), repositoryDTO.getTheStructure().getRootElement()));
				}
				repository = repositoryDAO.update(repository);
			}
			repositoryDTO = (RepositoryDTO) converter.getDTO(repository, RepositoryDTO.class);
		} catch (ImplementationException e) {
			e.printStackTrace();
		}
		return repositoryDTO;
	}

	private Set<Node> convertNodes(List<NodeDTO> children, NodeDTO father) throws ImplementationException {
		Set<Node> nodes = new HashSet<Node>();

		for (NodeDTO nodeDTO : children) {

			Node node = (Node) converter.getEntity(nodeDTO, Node.class);
			if (nodeDTO.getChildren().size()!=0)
				node.getChildren().addAll(convertNodes(nodeDTO.getChildren(), nodeDTO));
			nodes.add(node);
		}

		return nodes;
	}

	private Set<NodeDTO> convertNodesToNodesDTO(Set<Node> children, Node father) throws ImplementationException {
		Set<NodeDTO> nodesDTO = new HashSet<NodeDTO>();
		for (Node node : children) {

			NodeDTO nodeDTO = (NodeDTO) converter.getDTO(node, NodeDTO.class);
			if (node.getChildren().size()!=0)
				nodeDTO.getChildren().addAll(convertNodesToNodesDTO(node.getChildren(), node));
			nodesDTO.add(nodeDTO);
		}

		return nodesDTO;
	}

	@Override
	public Boolean remover(String ident) {
		String hql;
		Query query;

		hql = "select repository from "+VCSRepository.class.getSimpleName()+" as repository where repository.ident = :repname";
		query = repositoryDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("repname", ident);
		List<VCSRepository> result = query.getResultList();
		VCSRepository re = null;
		if(!result.isEmpty()){
			re = result.get(0);
		}

		if(re != null){
			repositoryDAO.daoDelete(re);
			return true;
		}
		else{
			return false;
		}
	}

	public List<RepositoryDTO> getRepositorys(){

		Converter converter = new ConverterImpl();
		String hql;
		Query query;

		hql = "select rep from "+VCSRepository.class.getSimpleName()+" as rep ";
		query = repositoryDAO.getPersistenceContext().createQuery(hql);

		List<VCSRepository> reys = new ArrayList<VCSRepository>();
		reys = query.getResultList();
		if(reys.isEmpty()){
			return null;
		}

		List<RepositoryDTO> rep  = new ArrayList<RepositoryDTO>();
		RepositoryDTO re;
		for (VCSRepository repository : reys) {
			re = new RepositoryDTO();
			try {
				re = (RepositoryDTO) converter.getDTO(repository, RepositoryDTO.class);
				rep.add(re);
			} catch (ImplementationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return rep;
	}

	@Override
	public RepositoriesDTO getRepositories() {
		String hql;
		Query query;

		hql = "select rep from "+VCSRepository.class.getSimpleName()+" as rep ";
		query = repositoryDAO.getPersistenceContext().createQuery(hql);

		List<VCSRepository> reys = new ArrayList<VCSRepository>();
		reys = query.getResultList();
		if(reys.isEmpty()){
			return null;
		}

		String[] list = new String[reys.size()];
		String[] list1 = new String[reys.size()];
		String[] list2 = new String[reys.size()];

		for(int i=0;i <reys.size();i++){
			list[i] = reys.get(i).getIdent();
			list1[i] = reys.get(i).getControlVersionSystem();
			list2[i] = reys.get(i).getServer();
		}


		RepositoriesDTO reDTO = new RepositoriesDTO();
		reDTO.setIdents(list);
		reDTO.setControlVersions(list1);
		reDTO.setServidores(list2);
		return reDTO;
	}

	@Override
	public RepositoryDTO getRepository(String ident) {
		Converter converter = new ConverterImpl();
		RepositoryDTO repositoryDTO = null;
		try {
			VCSRepository repository = repositoryDAO.retrieveBySecondaryKey(ident) ;


			if (repository != null){
				repositoryDTO = (RepositoryDTO) converter.getDTO(repository, RepositoryDTO.class);

				if (repository.getTheStructure() != null){
					repositoryDTO.setTheStructure((StructureDTO) converter.getDTO(repository.getTheStructure(), StructureDTO.class));
					repositoryDTO.getTheStructure().setRootElement((NodeDTO) converter.getDTO(repository.getTheStructure().getRootElement(), NodeDTO.class));

					for (int i = 0; i < repository.getTheStructure().getRootElement().getChildren().size(); i++) {
						repositoryDTO.getTheStructure().getRootElement().setChildren(convertNodesToNodesDTO(repository.getTheStructure().getRootElement().getChildren(), repository.getTheStructure().getRootElement()));
					}

				}

			}else{
				return null;
			}

		} catch (ImplementationException e) {
			e.printStackTrace();
		}
		return repositoryDTO;
	}

	@Override
	public Boolean removerStructure(String ident) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RepositoryDTO getRepos(String ident)  {
		Converter converter = new ConverterImpl();
		VCSRepository rep = repositoryDAO.retrieveBySecondaryKey(ident) ;
		StructureDTO struc = null;
		NodeDTO nodeRoot = null;
		if (rep != null){

			RepositoryDTO repDTO = null;
			try {
				repDTO = (RepositoryDTO) converter.getDTO(rep, RepositoryDTO.class);
				if(rep.getTheStructure() != null){
					struc = (StructureDTO) converter.getDTO(rep.getTheStructure(), StructureDTO.class);
					nodeRoot = (NodeDTO) converter.getDTO(rep.getTheStructure().getRootElement(), NodeDTO.class);
					nodeRoot.setChildren(converterNodesDTO(rep.getTheStructure().getRootElement().getChildren(), struc,nodeRoot));

				}



				if(struc != null){
					repDTO.setTheStructure(struc);
					struc.setRootElement(nodeRoot);
					nodeRoot.setTheStructureDTO(struc);
				}
			} catch (ImplementationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
			return repDTO;
		}else{
			return null;
		}
	}

	private Set<NodeDTO> converterNodesDTO(List<Node> nodes,StructureDTO structure,NodeDTO father) throws ImplementationException{
    Set<NodeDTO> nos = new HashSet<NodeDTO>();
		Converter converter = new ConverterImpl();
		for (Node node : nodes) {
			NodeDTO nodeDTO = (NodeDTO) converter.getDTO(node, NodeDTO.class);
			nodeDTO.setTheStructureDTO(structure);

			if (node.getChildren().size() != 0)
				nodeDTO.setChildren(converterFilhosDTO(node.getChildren(), structure,nodeDTO));
			nos.add(nodeDTO);
		}
		return nos;
	}

	private Set<NodeDTO> converterFilhosDTO(Set<Node> nodes,StructureDTO structure,NodeDTO father) throws ImplementationException {
		Set<NodeDTO> nos = new HashSet<NodeDTO>();
		Converter converter = new ConverterImpl();
		for (Node node : nodes) {
			NodeDTO nodeDTO = (NodeDTO) converter.getDTO(node, NodeDTO.class);
			nodeDTO.setTheStructureDTO(structure);

			nos.add(nodeDTO);
		}
		return nos;
	}

	@Override
	public String performImport(String identAgent, String activityIdent, String artifactIdent, String comment, ArtifactMngContent artifactContent, String repositoryIdent, String path) throws Exception {

		if(repositoryIdent != null || !"".equals(repositoryIdent)){
			try {
				return serviceSVN.performImport(identAgent, activityIdent, artifactIdent, comment, artifactContent, repositoryIdent, path);
			} catch (WebapseeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "";
	}

	@Override
	public ArtifactMngDownload[] performCheckout(String username, String artifactIdent, String arguments) throws WebapseeException {
		ArtifactMngParamDownload[] params = artifactManager.getDownloadParam(username, artifactIdent, arguments);
		ArtifactMngDownload[] downloads = new ArtifactMngDownload[params.length];
		ArtifactMngDownload download = null;

		for (int i = 0; i < params.length; i++) {
			ArtifactMngParamDownload param = params[i];
			VCSRepository repository = artifactManager.getRepositoryByArtifact(param.getArtifactIdent());
			RepositoryDTO repositoryDTO = new RepositoryDTO();
			try {
				repositoryDTO = (RepositoryDTO) converter.getDTO(repository, RepositoryDTO.class);
			} catch (ImplementationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(repository != null){
				download = serviceSVN.performCheckout(username, param.getArtifactName(), param.getArtifactPath(), param.getArguments(), repositoryDTO);
			}

			if(download != null)downloads[i] = download;
			else downloads[i] = null;
		}

		return downloads;
	}

	@Override
	public boolean verifyUploadOk(String artifact_id, String normal_id) {
		//Verify WHEN the upload can be maken
		System.out.println("verify Upload");
		Normal normal = (Normal)normalDAO.retrieveBySecondaryKey( normal_id );
		System.out.println(normal.getTheEnactionDescription());
		if(normal.getTheEnactionDescription().getState().equals(Normal.ACTIVE)){
			//Verify WHO can be the upload
			System.out.println("entrou aqui");
			Artifact artifact = (Artifact) artifactDAO.retrieveBySecondaryKey( artifact_id );
			System.out.println(artifact.getTheArtifactCons());
			Collection artCon = artifact.getTheArtifactCons();
			for(Iterator it = artCon.iterator();it.hasNext();){
				ArtifactCon con = (ArtifactCon) it.next();
				System.out.println(con);
				Collection fromActs = con.getFromActivities();
				System.out.println(fromActs);
				for(Iterator it2 = fromActs.iterator();it2.hasNext();){
					Activity act = (Activity) it2.next();
					if(act.equals(normal)){
						System.out.println("retorna true");
						return true;

					}
				}
			}
		}
		System.out.println("retorna false");
		return false;
	}

	@Override
	public String getRepositoryToImport(String processIdent, String artifactIdent) {
		VCSRepository repository = getRepositoryByArtifact(artifactIdent);
		/*NÃO ENTENDI ISTO, FAZER DEPOIS
		 * if(processIdent.equals(Messages.getString("server.Artifacts.ArtifactTemplates")))
			repository = getDefaultRepository();*/
		if(repository == null)
			repository = getRepositoryByArtifact(artifactIdent);
		if(repository != null){
			return repository.getIdent();
		}else return "";
	}

	public VCSRepository getRepositoryByArtifact(String artifactIdent){
		//Get the repository by the artifact repository
		String hql = "select repository from "
				+ VCSRepository.class.getName()
				+ " as repository, "
				+ Artifact.class.getName()
				+" as artifact "
				+ " where artifact.ident like '"
				+ artifactIdent
				+ "' and artifact.theRepository = repository";

		Query query = repositoryDAO.getPersistenceContext().createQuery( hql );

		List result = query.getResultList();
		if(result != null && result.size() > 0){
			return (VCSRepository) result.get(0);
		}
		return null;
	}

	@Override
	public RepositoryDTO getRepositoryDTOByArtifact(String artifactIdent){
		//Get the repository by the artifact repository
		String hql = "select repository from "
				+ VCSRepository.class.getName()
				+ " as repository, "
				+ Artifact.class.getName()
				+" as artifact "
				+ " where artifact.ident like '"
				+ artifactIdent
				+ "' and artifact.theRepository = repository";

		Query query = repositoryDAO.getPersistenceContext().createQuery( hql );

		List result = query.getResultList();
		if(result != null && result.size() > 0){
			try {
				return (RepositoryDTO) converter.getDTO(result.get(0), RepositoryDTO.class);
			} catch (ImplementationException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public VCSRepository getRepositoryByProject(String projectIdent) {
		//Get the repository by the project repository
		String hql = "select repository from "
				+ VCSRepository.class.getName()
				+ " as repository, "
				+ Project.class.getName()
				+" as project "
				+ " where project.ident like '"
				+ projectIdent
				+ "' and project.theRepository = repository";

		Query query = repositoryDAO.getPersistenceContext().createQuery( hql );

		List result = query.getResultList();
		if(result != null && result.size() > 0){
			return (VCSRepository) result.get(0);
		}
		return null;
	}
}
