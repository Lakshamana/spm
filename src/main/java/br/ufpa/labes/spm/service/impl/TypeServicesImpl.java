package br.ufpa.labes.spm.service.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.qrconsult.spm.beans.editor.WebAPSEEVO;
import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.types.IAbilityTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IActivityTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IArtifactTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IConnectionTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IEventTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IGroupTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IKnowledgeTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IMetricTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IPolicyTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IResourceTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IRoleTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.IToolTypeDAO;
import br.ufpa.labes.spm.repository.interfaces.types.ITypeDAO;
import org.qrconsult.spm.dtos.formTypes.TypeDTO;
import org.qrconsult.spm.dtos.formTypes.TypesDTO;
import br.ufpa.labes.spm.domain.AbilityType;
import br.ufpa.labes.spm.domain.ActivityType;
import br.ufpa.labes.spm.domain.ArtifactType;
import br.ufpa.labes.spm.domain.ConnectionType;
import br.ufpa.labes.spm.domain.EventType;
import br.ufpa.labes.spm.domain.GroupType;
import br.ufpa.labes.spm.domain.KnowledgeType;
import br.ufpa.labes.spm.domain.MetricType;
import br.ufpa.labes.spm.domain.PolicyType;
import br.ufpa.labes.spm.domain.ResourceType;
import br.ufpa.labes.spm.domain.RoleType;
import br.ufpa.labes.spm.domain.ToolType;
import br.ufpa.labes.spm.domain.Type;
import br.ufpa.labes.spm.service.interfaces.TypeServices;
import org.qrconsult.spm.util.Translator;

@Stateless
public class TypeServicesImpl implements TypeServices{

	ITypeDAO typeDAO;
	IArtifactTypeDAO artTypeDAO;
	IActivityTypeDAO actTypeDAO;
	IToolTypeDAO toolTypeDAO;
	IGroupTypeDAO grpTypeDAO;
	IMetricTypeDAO metTypeDAO;
	IAbilityTypeDAO abiTypeDAO;
	IRoleTypeDAO roleTypeDAO;
	IResourceTypeDAO resTypeDAO;
	IConnectionTypeDAO conTypeDAO;
	IKnowledgeTypeDAO knowTypeDAO;
	IEventTypeDAO eveTypeDAO;
	IPolicyTypeDAO polTypeDAO;

	Hashtable<String, Class<?>> typeClasses = new Hashtable<String, Class<?>>();

	public TypeServicesImpl(){
		typeClasses.put("AbilityType", AbilityType.class);
		typeClasses.put("ActivityType", ActivityType.class);
		typeClasses.put("ArtifactType", ArtifactType.class);
		typeClasses.put("ConnectionType", ConnectionType.class);
		typeClasses.put("EventType", EventType.class);
		typeClasses.put("GroupType", GroupType.class);
		typeClasses.put("PolicyType", KnowledgeType.class);
		typeClasses.put("MetricType", MetricType.class);
		typeClasses.put("PolicyType", PolicyType.class);
		typeClasses.put("ResourceType", ResourceType.class);
		typeClasses.put("RoleType", RoleType.class);
		typeClasses.put("ToolType", ToolType.class);
	}

	@Override
	public String[] getRootTypes(String typeClassName) {

		String internalPackageName = Translator.getInternalPackageName(typeClassName);
		String hql = "select type.ident from " + internalPackageName + " as type where type.superType is null";

		Query query = typeDAO.getPersistenceContext().createQuery(hql);

		List<String> list = query.getResultList();
		String[] ret = new String[list.size()];
		list.toArray(ret);

		return ret;
	}

	@Override
	public String[] getSubTypes(String typeIdent) {

		String hql = "select type.ident from " + Type.class.getName() + " as type where type.superType is not null and type.superType.ident=:ident ";

		Query query = typeDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("ident", typeIdent);

		List<String> list = query.getResultList();
		String[] ret = new String[list.size()];
		list.toArray(ret);

		return ret;
	}

	@Override
	public TypesDTO getTypes() {

		Class<?>[] typeClasses = { AbilityType.class, ActivityType.class,
				ArtifactType.class, ConnectionType.class, EventType.class,
				GroupType.class, KnowledgeType.class, MetricType.class,
				PolicyType.class, ResourceType.class, RoleType.class,
				ToolType.class };

		String hql;
		Query query;
		List<List<Type>> typesLists = new ArrayList<List<Type>>();
		int sizeList = 0;
		for (int i = 0; i < typeClasses.length; i++) {
			Class<?> class_ = typeClasses[i];
			hql = "from " + class_.getName();
			query = typeDAO.getPersistenceContext().createQuery(hql);
			typesLists.add(query.getResultList());
			sizeList += typesLists.get(i).size();
		}

		TypesDTO typesDTO = new TypesDTO(sizeList);
		int j = 0;
		for (int i = 0; i < typesLists.size(); i++) {
			List<Type> list = typesLists.get(i);

			for (Type type : list) {
				String typeIdent = type.getIdent();
				String superTypeIdent = (type.getSuperType()!=null ? type.getSuperType().getIdent() : "");
				String rootType = typeClasses[i].getSimpleName();
				typesDTO.addType(typeIdent, superTypeIdent, rootType, j);
				j++;
			}
		}

		return typesDTO;
	}

	@Override
	public TypeDTO getType(String typeIdent) {
		try{
			Type type = typeDAO.retrieveBySecondaryKey(typeIdent);
			if(type != null) {
				Converter converter = new ConverterImpl();
				TypeDTO typeDTO = (TypeDTO) converter.getDTO(type, TypeDTO.class);
				typeDTO.setSubtypesNumber(type.getSubType().size());
				typeDTO.setSuperTypeIdent(type.getSuperType()!=null ? type.getSuperType().getIdent() : "");
				typeDTO.setRootType(getInstanceOfType(type));
				return typeDTO;
			}
		}catch(ImplementationException e){
			e.printStackTrace();
		}
		return null;
	}

	private String getInstanceOfType(Type type) {
		if (type instanceof ArtifactType)
			return ArtifactType.class.getSimpleName();
		else if (type instanceof ActivityType)
			return ActivityType.class.getSimpleName();
		else if (type instanceof AbilityType)
			return AbilityType.class.getSimpleName();
		else if (type instanceof ToolType)
			return ToolType.class.getSimpleName();
		else if (type instanceof MetricType)
			return MetricType.class.getSimpleName();
		else if (type instanceof RoleType)
			return RoleType.class.getSimpleName();
		else if (type instanceof GroupType)
			return GroupType.class.getSimpleName();
		else
			return "";
	}

	@Override
	public TypeDTO saveType(TypeDTO typeDTO) {
		try {
			String typeIdent = typeDTO.getIdent();
			String typeClass = typeDTO.getRootType();
			if (typeIdent != null && !typeIdent.equals("") && typeClass != null	&& !typeClass.equals("")) {

				if (typeClass.equals(ActivityType.class.getSimpleName())) {

					ActivityType type = actTypeDAO.retrieveBySecondaryKey(typeIdent);

					Converter converter = new ConverterImpl();
					if (type != null) {
						type = (ActivityType) converter.getEntity(typeDTO, type);
						typeDTO.setSubtypesNumber(type.getSubType().size());
					} else {
						type = (ActivityType) converter.getEntity(typeDTO, ActivityType.class);
						type = actTypeDAO.save(type);
					}

					String superTypeIdent = typeDTO.getSuperTypeIdent();
					if (superTypeIdent != null && !superTypeIdent.equals("")) {
						ActivityType superType = actTypeDAO.retrieveBySecondaryKey(superTypeIdent);
						type.setSuperType(superType);
					}

					actTypeDAO.update(type);

					typeDTO = (TypeDTO) converter.getDTO(type, TypeDTO.class);

				} else if (typeClass.equals(ArtifactType.class.getSimpleName())) {

						ArtifactType type = artTypeDAO.retrieveBySecondaryKey(typeIdent);

						Converter converter = new ConverterImpl();
						if (type != null) {
							type = (ArtifactType) converter.getEntity(typeDTO, type);
							typeDTO.setSubtypesNumber(type.getSubType().size());
						} else {
							type = (ArtifactType) converter.getEntity(typeDTO, ArtifactType.class);
							type = artTypeDAO.save(type);
						}

						String superTypeIdent = typeDTO.getSuperTypeIdent();
						if (superTypeIdent != null && !superTypeIdent.equals("")) {
							ArtifactType superType = artTypeDAO.retrieveBySecondaryKey(superTypeIdent);
							type.setSuperType(superType);
						}

						artTypeDAO.update(type);

						typeDTO = (TypeDTO) converter.getDTO(type, TypeDTO.class);

				} else if (typeClass.equals(AbilityType.class.getSimpleName())) {

					AbilityType type = abiTypeDAO.retrieveBySecondaryKey(typeIdent);

					Converter converter = new ConverterImpl();
					if (type != null) {
						type = (AbilityType) converter.getEntity(typeDTO, type);
						typeDTO.setSubtypesNumber(type.getSubType().size());
					} else {
						type = (AbilityType) converter.getEntity(typeDTO, AbilityType.class);
						type = abiTypeDAO.save(type);
					}

					String superTypeIdent = typeDTO.getSuperTypeIdent();
					if (superTypeIdent != null && !superTypeIdent.equals("")) {
						AbilityType superType = abiTypeDAO.retrieveBySecondaryKey(superTypeIdent);
						type.setSuperType(superType);
					}

					abiTypeDAO.update(type);

					typeDTO = (TypeDTO) converter.getDTO(type, TypeDTO.class);

				} else if (typeClass.equals(GroupType.class.getSimpleName())) {

					GroupType type = grpTypeDAO.retrieveBySecondaryKey(typeIdent);

					Converter converter = new ConverterImpl();
					if (type != null) {
						type = (GroupType) converter.getEntity(typeDTO, type);
						typeDTO.setSubtypesNumber(type.getSubType().size());
					} else {
						type = (GroupType) converter.getEntity(typeDTO, GroupType.class);
						type = grpTypeDAO.save(type);
					}

					String superTypeIdent = typeDTO.getSuperTypeIdent();
					if (superTypeIdent != null && !superTypeIdent.equals("")) {
						GroupType superType = grpTypeDAO.retrieveBySecondaryKey(superTypeIdent);
						type.setSuperType(superType);
					}

					grpTypeDAO.update(type);

					typeDTO = (TypeDTO) converter.getDTO(type, TypeDTO.class);

				} else if (typeClass.equals(RoleType.class.getSimpleName())) {

					RoleType type = roleTypeDAO.retrieveBySecondaryKey(typeIdent);

					Converter converter = new ConverterImpl();
					if (type != null) {
						type = (RoleType) converter.getEntity(typeDTO, type);
						typeDTO.setSubtypesNumber(type.getSubType().size());
					} else {
						type = (RoleType) converter.getEntity(typeDTO, RoleType.class);
						type = roleTypeDAO.save(type);
					}

					String superTypeIdent = typeDTO.getSuperTypeIdent();
					if (superTypeIdent != null && !superTypeIdent.equals("")) {
						RoleType superType = roleTypeDAO.retrieveBySecondaryKey(superTypeIdent);
						type.setSuperType(superType);
					}

					roleTypeDAO.update(type);

					typeDTO = (TypeDTO) converter.getDTO(type, TypeDTO.class);

				} else if (typeClass.equals(ResourceType.class.getSimpleName())) {

					ResourceType type = resTypeDAO.retrieveBySecondaryKey(typeIdent);

					Converter converter = new ConverterImpl();
					if (type != null) {
						type = (ResourceType) converter.getEntity(typeDTO, type);
						typeDTO.setSubtypesNumber(type.getSubType().size());
					} else {
						type = (ResourceType) converter.getEntity(typeDTO, ResourceType.class);
						type = resTypeDAO.save(type);
					}

					String superTypeIdent = typeDTO.getSuperTypeIdent();
					if (superTypeIdent != null && !superTypeIdent.equals("")) {
						ResourceType superType = resTypeDAO.retrieveBySecondaryKey(superTypeIdent);
						type.setSuperType(superType);
					}

					resTypeDAO.update(type);

					typeDTO = (TypeDTO) converter.getDTO(type, TypeDTO.class);

				} else if (typeClass.equals(ToolType.class.getSimpleName())) {

					ToolType type = toolTypeDAO.retrieveBySecondaryKey(typeIdent);

					Converter converter = new ConverterImpl();
					if (type != null) {
						type = (ToolType) converter.getEntity(typeDTO, type);
						typeDTO.setSubtypesNumber(type.getSubType().size());
					} else {
						type = (ToolType) converter.getEntity(typeDTO, ToolType.class);
						type = toolTypeDAO.save(type);
					}

					String superTypeIdent = typeDTO.getSuperTypeIdent();
					if (superTypeIdent != null && !superTypeIdent.equals("")) {
						ToolType superType = toolTypeDAO.retrieveBySecondaryKey(superTypeIdent);
						type.setSuperType(superType);
					}

					toolTypeDAO.update(type);

					typeDTO = (TypeDTO) converter.getDTO(type, TypeDTO.class);

				} else if (typeClass.equals(MetricType.class.getSimpleName())) {

					MetricType type = metTypeDAO.retrieveBySecondaryKey(typeIdent);

					Converter converter = new ConverterImpl();
					if (type != null) {
						type = (MetricType) converter.getEntity(typeDTO, type);
						typeDTO.setSubtypesNumber(type.getSubType().size());
					} else {
						type = (MetricType) converter.getEntity(typeDTO, MetricType.class);
						type = metTypeDAO.save(type);
					}

					String superTypeIdent = typeDTO.getSuperTypeIdent();
					if (superTypeIdent != null && !superTypeIdent.equals("")) {
						MetricType superType = metTypeDAO.retrieveBySecondaryKey(superTypeIdent);
						type.setSuperType(superType);
					}

					metTypeDAO.update(type);

					typeDTO = (TypeDTO) converter.getDTO(type, TypeDTO.class);

				}else return null;

				return typeDTO;
			}
		} catch (ImplementationException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Boolean removeType(String typeIdent) {
		Type type = typeDAO.retrieveBySecondaryKey(typeIdent);
		if(type != null) {
			typeDAO.delete(type);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public TypesDTO getTypes(String termoBusca, String domainFilter, boolean orgFilter) {
		System.out.println("Param�tros: " + termoBusca + ", " + domainFilter + ", " + orgFilter);

		String hql;
		Query query;
		List<List<Type>> typesLists = new ArrayList<List<Type>>();
		List<Class<?>> returnedClasses = new ArrayList<Class<?>>();
		int sizeLists = 0;

		if(domainFilter!=null){
			Class<?> currentClass = typeClasses.get(domainFilter);
			hql = "select type from " + currentClass.getName() + " as type where type.ident like :termo and type.userDefined is not :orgFilter";
			query = typeDAO.getPersistenceContext().createQuery(hql);
			query.setParameter("orgFilter", orgFilter);
			query.setParameter("termo", "%"+ termoBusca + "%");
			List<Type> resultList = query.getResultList();
			typesLists.add(resultList);
			returnedClasses.add(currentClass);
			sizeLists = resultList.size();
		}else{
			Iterator<Class<?>> classIterator = typeClasses.values().iterator();
			while (classIterator.hasNext()) {
				Class<?> class_ = classIterator.next();
				hql = "select type from " + class_.getName() + " as type where type.ident like :termo and type.userDefined is not :orgFilter";
				query = typeDAO.getPersistenceContext().createQuery(hql);
				query.setParameter("orgFilter", orgFilter);
				query.setParameter("termo", "%"+ termoBusca + "%");
				List<Type> resultList = query.getResultList();
				typesLists.add(resultList);
				returnedClasses.add(class_);
				sizeLists += resultList.size();
			}
		}

		TypesDTO typesDTO = new TypesDTO(sizeLists);

		for (int i = 0; i < typesLists.size(); i++) {
			List<Type> list = typesLists.get(i);

			int j = 0;
			for (Iterator<Type> iterator = list.iterator(); iterator.hasNext();) {
				Type type = (Type) iterator.next();
				String typeIdent = type.getIdent();
				System.out.println("Tipo Selecionado: " + typeIdent);
				String superTypeIdent = (type.getSuperType()!=null ? type.getSuperType().getIdent() : "");
				String rootType = returnedClasses.get(i).getSimpleName();
				typesDTO.addType(typeIdent, superTypeIdent, rootType, j);
				j++;
			}

		}

		return typesDTO;
	}

	public static void main(String[] args){
		TypeServicesImpl impl = new TypeServicesImpl();
		TypesDTO types = impl.getTypes("", null, false);
		for (int i = 0; i < types.getTypeIdents().length; i++) {
			System.out.println("Tipo Selecionado: " + types.getTypeIdents()[i]);
		}
	}

	@Override
	public List<WebAPSEEVO> getTypesVO(String domain) {

		String internalPackageName = Translator.getInternalPackageName(domain);
		String hql = "select type.ident from " + internalPackageName + " as type order by type.ident";

		Query query = typeDAO.getPersistenceContext().createQuery(hql);

		List<String> list = query.getResultList();
		List<WebAPSEEVO> typesVO = new ArrayList<WebAPSEEVO>();
		for(String ident : list){
			WebAPSEEVO webapseeVO = new WebAPSEEVO();
			webapseeVO.setIdent(ident);
			typesVO.add(webapseeVO);
		}
		System.out.println(" # ArtifactTypes: " + typesVO.size());
		return typesVO;
	}

}
