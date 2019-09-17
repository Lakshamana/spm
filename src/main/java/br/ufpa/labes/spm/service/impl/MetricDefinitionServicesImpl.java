package org.qrconsult.spm.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.qrconsult.spm.converter.core.Converter;
import org.qrconsult.spm.converter.core.ConverterImpl;
import org.qrconsult.spm.converter.exception.ImplementationException;
import org.qrconsult.spm.dataAccess.interfaces.processKnowledge.IMetricDefinitionDAO;
import org.qrconsult.spm.dataAccess.interfaces.types.IAbilityTypeDAO;
import org.qrconsult.spm.dataAccess.interfaces.types.IMetricTypeDAO;
import org.qrconsult.spm.dtos.formAbility.AbilityDTO;
import org.qrconsult.spm.dtos.formAgent.AgentDTO;
import org.qrconsult.spm.dtos.formRole.RoleDTO;
import org.qrconsult.spm.dtos.formRole.RolesDTO;
import org.qrconsult.spm.dtos.formTypes.TypesDTO;
import org.qrconsult.spm.dtos.processKnowledge.MetricDefinitionDTO;
import org.qrconsult.spm.dtos.processKnowledge.MetricDefinitionsDTO;
import org.qrconsult.spm.model.agent.Ability;
import org.qrconsult.spm.model.agent.Agent;
import org.qrconsult.spm.model.agent.AgentPlaysRole;
import org.qrconsult.spm.model.agent.Role;
import org.qrconsult.spm.model.agent.RoleNeedsAbility;
import org.qrconsult.spm.model.processKnowledge.MetricDefinition;
import org.qrconsult.spm.model.types.AbilityType;
import org.qrconsult.spm.model.types.MetricType;
import org.qrconsult.spm.model.types.RoleType;
import org.qrconsult.spm.model.types.Type;
import org.qrconsult.spm.services.interfaces.MetricDefinitionServices;

@Stateless
public class MetricDefinitionServicesImpl implements MetricDefinitionServices{
	@EJB
	IMetricTypeDAO metricTypeDAO;
	
	@EJB
	IMetricDefinitionDAO metricDefinitionDAO;
	
	
	public MetricDefinition getMetricDefinitionP(String name) {
		String hql;
		Query query;
		List<MetricDefinition> result = null;
		
		
		hql = "select ability from "+MetricDefinition.class.getSimpleName()+" as ability where ability.name = :abiname";
		query = metricDefinitionDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("abiname", name);
		
		result = query.getResultList();
		
		if(!result.isEmpty()){
			MetricDefinition ability = result.get(0);
			return ability;
		}else{
			return null;
		}
		
	}
	
	
	@Override
	public MetricDefinitionDTO saveMetricDefinition(MetricDefinitionDTO metricDefinitionDTO) {
		
		System.out.println("MeAqui");
		Converter converter = new ConverterImpl();
		try {
			//metricDefinitionDTO.setUnits(null);
			//metricDefinitionDTO.setEstimation(null);
			//metricDefinitionDTO.setMetric(null);
			String typeMetric = metricDefinitionDTO.getMetricType();
			String metricName = metricDefinitionDTO.getName();
			System.out.println("MeAquitypeMetric::"+typeMetric);
			System.out.println("MeAqui2");
			if (typeMetric != null && !typeMetric.equals("")){
				MetricType  abiType = metricTypeDAO.retrieveBySecondaryKey(typeMetric);
				MetricDefinition metricDefinition = this.getMetricDefinitionP(metricName);
				System.out.println("MeAqui3");
				if(abiType != null ){
					if(metricDefinition != null){
						System.out.println("MeAqui3");
						return null;
					}else{
						System.out.println( "MeAqui4units:"+metricDefinitionDTO.getUnits().size() );
						metricDefinition = (MetricDefinition) converter.getEntity(metricDefinitionDTO, MetricDefinition.class);
						metricDefinition.setUnits(metricDefinitionDTO.getUnits());
						metricDefinition.setMetricType( abiType );
						metricDefinition = metricDefinitionDAO.save(metricDefinition);
						metricDefinitionDTO = (MetricDefinitionDTO) converter.getDTO(metricDefinition, MetricDefinitionDTO.class);
						metricDefinitionDTO.setMetricType(abiType.getIdent());
						System.out.println("MeAqui5:"+metricDefinitionDTO.getMetricType());
					}		
				}else{
					System.out.println("MeAqui6");
					return null;
				}
			}else{
				return null;
			}		
			
		} catch (ImplementationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("MeAqui7");
		return metricDefinitionDTO;
	}

	@Override
	public String[] getMetricTypes() {
		System.out.println("MeAqui6");
		String hql;
		Query query;
		
		hql = "select ident from " +MetricType.class.getSimpleName();
		query = metricTypeDAO.getPersistenceContext().createQuery(hql);
		
		List<String> abiTypeList =  new ArrayList<String>();
		abiTypeList = query.getResultList();
		String[] list = new String[abiTypeList.size()];
		abiTypeList.toArray(list);
		System.out.println("MeAqui8");
		return list;
	}

	
	public String[] getMetricDefinitionsTypes(){
		String hql;
		Query query;
		
		hql = "select ident from " +MetricType.class.getSimpleName();
		query = metricTypeDAO.getPersistenceContext().createQuery(hql);
		
		List<String> abiTypeList =  new ArrayList<String>();
		abiTypeList = query.getResultList();
		String[] list = new String[abiTypeList.size()];
		abiTypeList.toArray(list);
		
		for (String string : list) {
			System.out.println(list);
		}
		
		return list;
	}
	
	@Override
	public MetricDefinitionsDTO getMetricDefinitions() {
		String hql;
		Query query;
		
		hql = "select name from "+MetricDefinition.class.getSimpleName();
		query = metricDefinitionDAO.getPersistenceContext().createQuery(hql);
		
		
		List<String> roles = new ArrayList<String>();
		roles = query.getResultList();
		String[] list = new String[roles.size()];
		roles.toArray(list);
		
		String[] list1 = new String[0];
		list1 = getMetricDefinitionsTypes();
		
		MetricDefinitionsDTO rolesDTO = new MetricDefinitionsDTO();
		rolesDTO.setNameMetricsDefinitions(list);
		rolesDTO.setTypeMetrics(list1);
		
		/*for (String string : list) {
			System.out.println("Linda:"+list);
		}
		
		for (String string : list1) {
			System.out.println("Linda:"+list1);
		}*/
		
		return rolesDTO;
	}

	@Override
	public MetricDefinitionsDTO getMetricDefinitions(String term, String domain, boolean orgFilter) {
		String hql;
		Query query;
		List<String> resultList = null;
		
		if(domain != null){
			hql = "select role.name from " + MetricDefinition.class.getSimpleName() + " as role where role.name like :termo and role.ident = :domain and";
			query = metricDefinitionDAO.getPersistenceContext().createQuery(hql);
			query.setParameter("domain", domain);
			query.setParameter("termo", "%"+ term + "%");
			resultList = query.getResultList(); 	
		}else{
			hql = "select role.name from " +MetricDefinition.class.getSimpleName() + " as role where role.name like :termo";
			query = metricDefinitionDAO.getPersistenceContext().createQuery(hql);
			query.setParameter("termo", "%"+ term + "%");
			resultList = query.getResultList(); 	
		}
		
		MetricDefinitionsDTO rolesDTO = new MetricDefinitionsDTO();
		String[] names = new String[resultList.size()];
		resultList.toArray(names);
		rolesDTO.setNameMetricsDefinitions(names);
		
		
		
    /*System.out.println("Param�tros: " + termoBusca + ", " + domainFilter + ", " + orgFilter);
		
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

		return typesDTO;*/
		
		return rolesDTO;
	}

	@Override
	public MetricDefinitionDTO getMetricDefinitionDTO(String nameMetricDefinition) {
		String hql;
		Query query;
		List<MetricDefinition> result = null;
		MetricDefinition resuk;
		//List<Ability> abis = new ArrayList<Ability>();
		//List<AbilityDTO> abisDTO = new ArrayList<AbilityDTO>();
		//List<Agent> agens = new ArrayList<Agent>();
		//ist<AgentDTO> agensDTO = new ArrayList<AgentDTO>();
		MetricDefinitionDTO metricDefinitionDTO = null;
		//AbilityDTO abi = null;
		//AgentDTO agen = null;
		
		hql = "select role from "+MetricDefinition.class.getSimpleName()+" as role where role.name = :rolname";
		query = metricDefinitionDAO.getPersistenceContext().createQuery(hql);
		query.setParameter("rolname", nameMetricDefinition);
		result = query.getResultList();
		//Collection<RoleNeedsAbility> lis = result.get(0).getTheRoleNeedsAbility();
		//Collection<AgentPlaysRole> lisaux = result.get(0).getTheAgentPlaysRole();
		
		/*for (RoleNeedsAbility roleNeedsAbility : lis) {
			abis.add(roleNeedsAbility.getTheAbility());
		}*/
		
		Converter converter = new ConverterImpl();
		/*for(int i = 0;i < abis.size();i++){
			try {
				abi = (AbilityDTO) converter.getDTO(abis.get(i), AbilityDTO.class);
				abisDTO.add(abi);
			} catch (Implementation
		
		for (AgentPlaysRole agentPlaysRole : lisaux) {
			agens.add(agentPlaysRole.getTheAgent());
		}
		
		for(int i = 0;i < agens.size();i++){
			try {
				agen = (AgentDTO) converter.getDTO(agens.get(i), AgentDTO.class);
				agensDTO.add(agen);
			} catch (ImplementationException e) {
				
				e.printStackTrace();
			}			
		}
		
		*/
		try {
			
			Collection<String> abis ;
			abis = result.get(0).getUnits();
			//metricDefinitionDTO =  new MetricDefinitionDTO();
			//if( result.get(0).getUnits() != null){
			//	metricDefinitionDTO.setUnits(abis);
			//}
			resuk = result.get(0);
			metricDefinitionDTO = (MetricDefinitionDTO) converter.getDTO( resuk, MetricDefinitionDTO.class);
			//System.out.println("units+"+metricDefinitionDTO.getUnits());
			for ( String string : resuk.getUnits() ) {
				//System.out.println(string);
			}
			
			
			/*System.out.println("Abis:::"+abis);
			if( abis  != null ){
				System.out.println( "Abis:::"+abis.size() );
				metricDefinitionDTO.setUnits( abis );
			}*/
			/*if(abis != null){
				roleDTO.setAbilityToRole(abisDTO);
			}
			if(agens != null){
				roleDTO.setAgentToRole(agensDTO);
			}*/
			
		} catch (ImplementationException e) {
			e.printStackTrace();
		}
		//roleDTO.setSuperType(result.get(0).getName());
		//System.out.println("MetricDefinitoin:"+result.get(0).getMetricType().getIdent());
		/*Collection<E>
		for (int i = 0; i < result.get(0).u; i++) {
			array_type array_element = array[i];
			
		}*/
		return metricDefinitionDTO;
	}

}
