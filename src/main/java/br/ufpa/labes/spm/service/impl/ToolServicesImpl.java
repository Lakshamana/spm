package br.ufpa.labes.spm.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.tools.IToolDefinitionDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IArtifactTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IToolTypeDAO;
import br.ufpa.labes.spm.service.dto.ToolDTO;
import br.ufpa.labes.spm.service.dto.ToolsDTO;
import br.ufpa.labes.spm.service.dto.TypesDTO;
import br.ufpa.labes.spm.domain.ToolDefinition;
import br.ufpa.labes.spm.domain.ArtifactType;
import br.ufpa.labes.spm.domain.ToolType;
import br.ufpa.labes.spm.domain.Type;
import br.ufpa.labes.spm.service.interfaces.ToolServices;

public class ToolServicesImpl implements ToolServices {

	private final String TOOL_CLASSNAME = ToolDefinition.class.getSimpleName();

	IToolDefinitionDAO toolDAO;

	IToolTypeDAO toolTypeDAO;

	IArtifactTypeDAO artifactTypeDAO;

	Converter converter = new ConverterImpl();

	private Query query;

	@Override
	@SuppressWarnings("unchecked")
	public TypesDTO getToolTypes() {
		String hql;
		List<Type> typesLists = new ArrayList<Type>();

		hql = "from " + ToolType.class.getSimpleName();
		query = toolTypeDAO.getPersistenceContext().createQuery(hql);
		typesLists = query.getResultList();

		TypesDTO typesDTO = new TypesDTO(typesLists.size());
		int j = 0;
		for (Type type : typesLists) {
			String typeIdent = type.getIdent();
			String superTypeIdent = (type.getSuperType() != null ? type
					.getSuperType().getIdent() : "");
			String rootType = ArtifactType.class.getSimpleName();
			typesDTO.addType(typeIdent, superTypeIdent, rootType, j);
			j++;
		}
		return typesDTO;
	}

	@Override
	public ToolDTO saveTool(ToolDTO toolDTO) {
		ToolDefinition tool = new ToolDefinition();
		tool = toolDAO.retrieveBySecondaryKey(toolDTO.getIdent());
		ToolType toolType = toolTypeDAO.retrieveBySecondaryKey(toolDTO
				.getTheToolType());
		Collection<String> artifactTypeNames = toolDTO.getTheArtifactType();

		if (tool != null) {
			updateTool(tool, toolDTO);
		} else {
			tool = this.convertToolDTOToTool(toolDTO);
			toolDAO.daoSave(tool);

			String newIdent = toolDAO.generateIdent(tool.getName(), tool);
			tool.setIdent(newIdent);
//			toolDTO.setIdent(newIdent);
		}

		tool.setTheToolType(toolType);
		this.updateDependencies(tool, toolDTO);

		toolDAO.update(tool);

		toolDTO = this.convertToolToToolDTO(tool);
		toolDTO.setTheArtifactType(artifactTypeNames);

		// System.out.println("----> Tool Oid: " + toolDTO.getOid() + "; Name: "
		// + toolDTO.getName() + "; Artifacts: " +
		// toolDTO.getTheArtifactType().size());

		return toolDTO;
	}

	private void updateTool(ToolDefinition tool, ToolDTO toolDTO) {
		tool.setName(toolDTO.getName());
		tool.setDescription(toolDTO.getDescription());
	}

	private void updateDependencies(ToolDefinition tool, ToolDTO toolDTO) {
		List<ArtifactType> artifactTypes = new ArrayList<ArtifactType>();

		for (String artifactType : toolDTO.getTheArtifactType()) {
			ArtifactType artifact = artifactTypeDAO
					.retrieveBySecondaryKey(artifactType);
			artifactTypes.add(artifact);
			artifact = null;
		}

		tool.setTheArtifactType(artifactTypes);
	}

	@Override
	public ToolDTO getTool(String toolIdent) {
		ToolDTO toolDTO = new ToolDTO();
		ToolDefinition tool = toolDAO.retrieveBySecondaryKey(toolIdent);
		if (tool != null) {
			toolDTO = this.convertToolToToolDTO(tool);
		}
		return toolDTO;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ToolsDTO getTools() {
		String hql = "SELECT tool FROM " + TOOL_CLASSNAME + " AS tool";
		query = toolDAO.getPersistenceContext().createQuery(hql);

		ToolsDTO toolsDTO = new ToolsDTO();
		List<ToolDefinition> result = query.getResultList();

		if (!result.isEmpty()) {
			ToolDTO toolDTO = null;
			for (ToolDefinition tool : result) {

				toolDTO = this.convertToolToToolDTO(tool);
				toolsDTO.addTool(toolDTO);

				// System.out.println("Tool, name: " + toolDTO.getName() +
				// "; Artifacts: " + toolDTO.getTheArtifactType());

				toolDTO = null;
			}
		}

		return toolsDTO;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ToolsDTO getTools(String toolName, String toolType, String artifact,
			Boolean isActive) {
		System.out.println("Par�metros - name: " + toolName + "; type: "
				+ toolType + "; artifact: " + artifact);

		ToolsDTO toolsDTO = new ToolsDTO();

		String hql = "SELECT tool FROM " + TOOL_CLASSNAME
				+ " AS tool WHERE tool.name like :name ";
		String typeFilter = (toolType.equals("")) ? ""
				: " AND tool.theToolType.ident = :toolType";

		query = toolDAO.getPersistenceContext().createQuery(hql + typeFilter);
		query.setParameter("name", "%" + toolName + "%");

		if (!toolType.equals("")) {
			query.setParameter("toolType", toolType);
		}

		List<ToolDefinition> result = query.getResultList();
		for (ToolDefinition toolDefinition : result) {
			ToolDTO tool = this.convertToolToToolDTO(toolDefinition);
			toolsDTO.addTool(tool);
		}

		if (!artifact.equals("")) {
			this.filterArtifacts(artifact, toolsDTO);
		}

		return toolsDTO;
	}

	@Override
	public Boolean removeTool(String toolIdent) {
		ToolDefinition tool = toolDAO.retrieveBySecondaryKey(toolIdent);
		if (tool != null) {
			for (ArtifactType artifactType : tool.getTheArtifactType()) {
				artifactType.getTheToolDefinition().remove(tool);
			}
//			tool.setTheArtifactType(new HashSet<ArtifactType>());
			tool.getTheArtifactType().clear();

			toolDAO.update(tool);

			toolDAO.daoDelete(tool);
			return true;
		}
		return false;
	}

	private ToolsDTO filterArtifacts(String artifact, ToolsDTO result) {
		for (int i = 0; i < result.size(); i++) {
			ToolDTO tool = result.getTool(i);
			if (!tool.getTheArtifactType().contains(artifact)) {
				result.removeTool(tool);
			}
		}

		return result;
	}

	@Override
	public Boolean removeArtifactFromTool(String artifactName, ToolDTO tool) {
		ArtifactType artifactType = artifactTypeDAO
				.retrieveBySecondaryKey(artifactName);
		ToolDefinition toolDefinition = toolDAO.retrieveBySecondaryKey(tool.getIdent());

		if ((artifactType != null) && (toolDefinition != null)) {
			artifactType.removeFromTheToolDefinition(toolDefinition);
			toolDAO.update(toolDefinition);

			return true;
		}

		return false;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private ToolDefinition getToolFromName(String toolName) {
		String hql = "SELECT tool FROM " + TOOL_CLASSNAME
				+ " as tool where tool.name = :name";
		query = toolDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("name", toolName);

		List<ToolDefinition> result = null;

		result = query.getResultList();

		if (!result.isEmpty()) {
			ToolDefinition tool = result.get(0);

			return tool;
		}

		return null;
	}

	private ToolDTO convertToolToToolDTO(ToolDefinition tool) {
		try {
			ToolDTO toolDTO = new ToolDTO();
			toolDTO = (ToolDTO) converter.getDTO(tool, ToolDTO.class);
			toolDTO.setTheToolType(tool.getTheToolType().getIdent());

			// System.out.println("Tool: " + tool.getName() +
			// " ---------> Size: " + tool.getTheArtifactType().size());

			List<String> artifactType = new ArrayList<String>();
			for (ArtifactType artifact : tool.getTheArtifactType()) {
				artifactType.add(artifact.getIdent());
			}

			toolDTO.setTheArtifactType(artifactType);

			return toolDTO;
		} catch (ImplementationException e) {
			e.printStackTrace();
		}
		return new ToolDTO();
	}

	private ToolDefinition convertToolDTOToTool(ToolDTO toolDTO) {
		try {
			ToolDefinition tool = new ToolDefinition();
			tool = (ToolDefinition) converter.getEntity(toolDTO,
					ToolDefinition.class);

			return tool;
		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return new ToolDefinition();
	}

	@SuppressWarnings("unused")
	private ToolDefinition convertToolDTOToTool(ToolDTO toolDTO,
			ToolDefinition tool) {
		try {
			tool = (ToolDefinition) converter.getEntity(toolDTO, tool);

			return tool;
		} catch (ImplementationException e) {
			e.printStackTrace();
		}

		return new ToolDefinition();
	}

}
