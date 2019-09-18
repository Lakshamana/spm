package br.ufpa.labes.spm.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Query;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import br.ufpa.labes.spm.repository.interfaces.agent.IAbilityDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IMetricDAO;
import br.ufpa.labes.spm.repository.interfaces.processKnowledge.IMetricDefinitionDAO;
import br.ufpa.labes.spm.repository.interfaces.types.ITypeDAO;
import br.ufpa.labes.spm.service.dto.AbilityDTO;
import br.ufpa.labes.spm.service.dto.MetricDTO;
import br.ufpa.labes.spm.service.dto.MetricDefinitionDTO;
import br.ufpa.labes.spm.domain.Activity;
import br.ufpa.labes.spm.domain.Ability;
import br.ufpa.labes.spm.domain.Group;
import br.ufpa.labes.spm.domain.Role;
import br.ufpa.labes.spm.domain.Artifact;
import br.ufpa.labes.spm.domain.Connection;
import br.ufpa.labes.spm.domain.KnowledgeAttribute;
import br.ufpa.labes.spm.domain.Event;
import br.ufpa.labes.spm.domain.Metric;
import br.ufpa.labes.spm.domain.MetricDefinition;
import br.ufpa.labes.spm.domain.Resource;
import br.ufpa.labes.spm.domain.ToolDefinition;
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
import br.ufpa.labes.spm.service.interfaces.MetricServices;


public class MetricServicesImpl implements MetricServices{

	IMetricDefinitionDAO metricDefinitionDAO;

	ITypeDAO typeDAO;

	IMetricDAO metricDAO;


	Hashtable<String, Class<?>> typeClasses = new Hashtable<String, Class<?>>();
	Hashtable<String, Class<?>> classes = new Hashtable<String, Class<?>>();

	public MetricServicesImpl() {
		// TODO Auto-generated constructor stub
		typeClasses.put("AbilityType", AbilityType.class);
		typeClasses.put("ActivityType", ActivityType.class);
		typeClasses.put("ArtifactType", ArtifactType.class);
		typeClasses.put("ConnectionType", ConnectionType.class);
		typeClasses.put("EventType", EventType.class);
		typeClasses.put("GroupType", GroupType.class);
		//typeClasses.put("PolicyType", KnowledgeType.class);
		typeClasses.put("MetricType", MetricType.class);
		//typeClasses.put("PolicyType", PolicyType.class);
		typeClasses.put("ResourceType", ResourceType.class);
		typeClasses.put("RoleType", RoleType.class);
		typeClasses.put("ToolType", ToolType.class);

		classes.put("AbilityType", Ability.class);
		classes.put("ActivityType", Activity.class);
		classes.put("ArtifactType", Artifact.class);
		classes.put("ConnectionType", Connection.class);
		classes.put("EventType", Event.class);
		classes.put("GroupType", Group.class);
		//classes.put("Policy", KnowledgeAttribute.class);
		classes.put("MetricType", Metric.class);
		//classes.put("Policy", Policy.class);
		classes.put("Resource", Resource.class);
		classes.put("RoleType", Role.class);
		classes.put("ToolType", ToolDefinition.class);

	}

	@Override
	public MetricDTO saveMetric(MetricDTO metricDTO) {
		//metricDTO.getMetricDefinition();
		System.out.println("MeAquiMetric");
		Converter converter = new ConverterImpl();
		try {
			//metricDefinitionDTO.setUnits(null);
			//metricDefinitionDTO.setEstimation(null);
			//metricDefinitionDTO.setMetric(null);
			String typeMetric = metricDTO.getMetricDefinition();
			//String metricName = metricDTO.getName();
			System.out.println("MeAqui::"+typeMetric);
			System.out.println("MeAqui2");
			if (typeMetric != null && !typeMetric.equals("")){
				//MetricType  abiType = metricTypeDAO.retrieveBySecondaryKey(typeMetric);
				Metric metric = null; //= this.getAbilityP(metricName);
				System.out.println("MeAqui3");
				//if(abiType != null ){
					if(metric != null){
						System.out.println("MeAqui3");
						return null;
					}else{
						System.out.println("MeAqui4");
						metric = (Metric) converter.getEntity(metricDTO, Metric.class);
						//metricDefinition.setUnits(metricDefinitionDTO.getUnits());
						//metricDefinition.setMetricType( abiType );
						metric = metricDAO.save(metric);
						metricDTO = (MetricDTO) converter.getDTO(metric, MetricDTO.class);
						System.out.println("MeAqui5");
					}
				//}else{
					System.out.println("MeAqui6");
					//return null;
				//}
			}else{
				return null;
			}

		} catch (ImplementationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("MeAqui7");
		return metricDTO;
	}

	@Override
	public List<MetricDefinitionDTO> getMetricsDefinitions() {
		String hql;
		Query query;
		List<MetricDefinition> result = null;
		List<MetricDefinitionDTO> resultDTO = new ArrayList<MetricDefinitionDTO>();

		hql = "select metricDefinition from "+MetricDefinition.class.getSimpleName()+" as metricDefinition";
		query = metricDefinitionDAO.getPersistenceContext().createQuery(hql);
		result = query.getResultList();
		Converter converter = new ConverterImpl();
		System.out.println("Size:"+result.size());
		if(!result.isEmpty()){
			/*for(int i = 0; i < result.size();i++){
				try {
					MetricDefinitionDTO abi = new MetricDefinitionDTO();
					abi =  (MetricDefinitionDTO) converter.getDTO(result.get(i), MetricDefinitionDTO.class);

					String[] strings= new String[result.get(i).getUnits().size()];
					Collection<String> unidades = new ArrayList<String>();
					unidades = result.get(i).getUnits();
					//abi.setUnits(unidades);
			/		abi.setMetricType(result.get(i).getMetricType().getIdent());
					strings = unidades.toArray(strings);
					System.out.println("ppp:"+strings[0]);
					//resultDTO.add(abi);




				} catch (ImplementationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				}*/
			  int j=0;

				for (MetricDefinition str : result) {

					MetricDefinitionDTO abi = new MetricDefinitionDTO();
					List<String> lis = new ArrayList<String>();
					String li = "";
					try {
						abi =  (MetricDefinitionDTO) converter.getDTO(str, MetricDefinitionDTO.class);
						String[] strings= new String[str.getUnits().size()];
						Collection<String> unidades = new ArrayList<String>();
						unidades = str.getUnits();
						for (String string : unidades) {
							lis.add(string);
						}
						abi.setUnits(lis);
						if(str.getMetricType() != null){
							System.out.println("MetricServiceImpl");
							li = str.getMetricType().getIdent();
						}
						if(li != null){
							abi.setMetricType(li);
						}
						strings = unidades.toArray(strings);
						j++;
						System.out.println("ppp:"+strings[0]+":"+j);
						resultDTO.add(abi);

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

	@Override
	public String[] getTypes(String domainFilter) {
		String hql;
		Query query;
		List<List<Type>> typesLists = new ArrayList<List<Type>>();
		List<Class<?>> returnedClasses = new ArrayList<Class<?>>();
		int sizeLists = 0;

		if(domainFilter!=null){
			Class<?> currentClass = typeClasses.get(domainFilter);
			hql = "select type from " + currentClass.getName()  + " as type";
			query = typeDAO.getPersistenceContext().createQuery(hql);
			//query.setParameter("orgFilter", orgFilter);
			//query.setParameter("termo", "%"+ termoBusca + "%");
			List<Type> resultList = query.getResultList();
			typesLists.add(resultList);
			returnedClasses.add(currentClass);
			sizeLists = resultList.size();
		}else{
			/*Iterator<Class<?>> classIterator = typeClasses.values().iterator();
			while (classIterator.hasNext()) {
				Class<?> class_ = classIterator.next();
				hql = "select type from " + class_.getName() + " as type where type.ident like :termo and type.userDefined is not :orgFilter";
				query = typeDAO.getPersistenceContext().createQuery(hql);
				query.setParameter("orgFilter", orgFilter);
				query.setParameter("termo", "%"+ termoBusca + "%");
				List<Type> resultList = query.getResultList();
				typesLists.add(resultList);
				returnedClasses.add(class_);
				sizeLists += resultList.size();*/
			}



		List<String> strinList = new ArrayList<String>();
		for (int i = 0; i < typesLists.size(); i++) {
			List<Type> list = typesLists.get(i);

			for (Iterator<Type> iterator = list.iterator(); iterator.hasNext();) {
				Type type = (Type) iterator.next();
				String typeIdent = type.getIdent();
				strinList.add(typeIdent);
			}

		}
		String[] strings= new String[strinList.size()];
		strings = strinList.toArray(strings);

		for(int j= 0;j < strings.length;j++){
			System.out.println(strings[j]+":");
		}

		return strings;
	}

	@SuppressWarnings("null")
	@Override
	public String[] getWithTypes(String domainFilter,String nameType) {
		// TODO Auto-generated method stub

		String hql;
		Query query = null;
		List<List> typesLists = new ArrayList<List>();
		List<Class<?>> returnedClasses = new ArrayList<Class<?>>();
		int sizeLists = 0;
		System.out.println("NameType:"+nameType);
		if(domainFilter!=null){
			Class<?> currentClass = typeClasses.get(domainFilter);
			Class<?> obClasses = classes.get(domainFilter);
			hql = "select o from " + obClasses.getSimpleName()  + " as o where o.the"+currentClass.getSimpleName()+".ident = '"+nameType+"'";
			System.out.println("hql:"+hql);
			//query.setParameter("type", nameType);
			query = typeDAO.getPersistenceContext().createQuery(hql);
			List resultList = query.getResultList();
			typesLists.add(resultList);
			returnedClasses.add(currentClass);
			sizeLists = resultList.size();
		}



		List<String> strinList = new ArrayList<String>();
		for (int i = 0; i < typesLists.size(); i++) {
			List list = typesLists.get(i);

			/*for (Iterator<Type> iterator = list.iterator(); iterator.hasNext();) {
				Type type = (Type) iterator.next();
				String typeIdent = type.getIdent();
				strinList.add(typeIdent);
			}*/

			for (Object object : list) {

				if (object instanceof Ability){
					Ability abi = (Ability) object;
					String typeIdent = abi.getName();
					strinList.add(typeIdent);
				}
				else if (object instanceof Connection){
					Connection abi = (Connection) object;
					String typeIdent = abi.getIdent();
					strinList.add(typeIdent);
				}else if (object instanceof Artifact){
					Artifact abi = (Artifact) object;
					String typeIdent = abi.getName();
					strinList.add(typeIdent);
				}else if (object instanceof Event){
					//Event abi = (Event) object;
					//String typeIdent = abi.getIdent();
					//strinList.add(typeIdent);
				}else if (object instanceof Group){
					Group abi = (Group) object;
					String typeIdent = abi.getName();
					strinList.add(typeIdent);
				}else if (object instanceof Resource){
					Resource abi = (Resource) object;
					String typeIdent = abi.getName();
					strinList.add(typeIdent);
				}else if (object instanceof Role){
					Role abi = (Role) object;
					String typeIdent = abi.getName();
					strinList.add(typeIdent);
				}else if (object instanceof ToolDefinition){
					ToolDefinition abi = (ToolDefinition) object;
					String typeIdent = abi.getName();
					strinList.add(typeIdent);
				}
			}

		}
		String[] strings= new String[strinList.size()];
		strings = strinList.toArray(strings);

		for(int j= 0;j < strings.length;j++){
			System.out.println(strings[j]+":");
		}

		return strings;
	}
}
