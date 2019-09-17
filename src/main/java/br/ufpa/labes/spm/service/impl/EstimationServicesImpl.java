package org.qrconsult.spm.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import org.qrconsult.spm.dataAccess.interfaces.processKnowledge.IEstimationDAO;
import org.qrconsult.spm.dataAccess.interfaces.processKnowledge.IMetricDefinitionDAO;
import org.qrconsult.spm.dataAccess.interfaces.types.ITypeDAO;
import org.qrconsult.spm.dtos.processKnowledge.EstimationDTO;
import org.qrconsult.spm.dtos.processKnowledge.MetricDTO;
import org.qrconsult.spm.dtos.processKnowledge.MetricDefinitionDTO;
import org.qrconsult.spm.model.agent.Ability;
import org.qrconsult.spm.model.agent.Group;
import org.qrconsult.spm.model.agent.Role;
import org.qrconsult.spm.model.artifacts.Artifact;
import org.qrconsult.spm.model.connections.Connection;
import org.qrconsult.spm.model.log.Event;
import org.qrconsult.spm.model.processKnowledge.Estimation;
import org.qrconsult.spm.model.processKnowledge.Metric;
import org.qrconsult.spm.model.processKnowledge.MetricDefinition;
import org.qrconsult.spm.model.resources.Resource;
import org.qrconsult.spm.model.tools.ToolDefinition;
import org.qrconsult.spm.model.types.MetricType;
import org.qrconsult.spm.model.types.Type;
import org.qrconsult.spm.services.interfaces.EstimationServices;


@Stateless
public class EstimationServicesImpl implements EstimationServices{
	@EJB
	IMetricDefinitionDAO metricDefinitionDAO;
	
	@EJB
	ITypeDAO typeDAO;
	
	@EJB
	IEstimationDAO estimationDAO;
	
	Hashtable<String, Class<?>> typeClasses = new Hashtable<String, Class<?>>();
	Hashtable<String, Class<?>> classes = new Hashtable<String, Class<?>>();
	
	
	@Override
	public EstimationDTO saveEstimation(EstimationDTO estimationDTO) {
		//metricDTO.getMetricDefinition();
				System.out.println("MeAquiEstimation");
				Converter converter = new ConverterImpl();
				try {
					//metricDefinitionDTO.setUnits(null);
					//metricDefinitionDTO.setEstimation(null);
					//metricDefinitionDTO.setMetric(null);
					String typeMetric = estimationDTO.getMetricDefinition();
					//String metricName = metricDTO.getName();
					System.out.println("MeAqui::"+typeMetric);
					System.out.println("MeAqui2");
					if (typeMetric != null && !typeMetric.equals("")){
						//MetricType  abiType = metricTypeDAO.retrieveBySecondaryKey(typeMetric);
						Estimation estimation = null; //= this.getAbilityP(metricName);
						System.out.println("MeAqui3");
						//if(abiType != null ){
							if(estimation != null){
								System.out.println("MeAqui3");
								return null;
							}else{
								System.out.println("MeAqui4");
								estimation = (Estimation) converter.getEntity(estimationDTO, Estimation.class);
								//metricDefinition.setUnits(metricDefinitionDTO.getUnits());
								//metricDefinition.setMetricType( abiType );
								estimation = estimationDAO.save(estimation);
								estimationDTO = (EstimationDTO) converter.getDTO(estimation, EstimationDTO.class);
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
				return estimationDTO;	
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
						li = str.getMetricType().getIdent();
						abi.setMetricType(li);
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
	@Override
	public String[] getWithTypes(String domainFilter, String nameType) {
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
