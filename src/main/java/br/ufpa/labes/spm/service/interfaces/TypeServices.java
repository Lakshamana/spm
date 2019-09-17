package org.qrconsult.spm.services.interfaces;

import java.util.List;

import javax.ejb.Remote;

import org.qrconsult.spm.beans.editor.WebAPSEEVO;
import org.qrconsult.spm.dtos.formTypes.TypeDTO;
import org.qrconsult.spm.dtos.formTypes.TypesDTO;

@Remote
public interface TypeServices {
	
	public String[] getRootTypes( String typeClassName );
	
	public String[] getSubTypes( String typeIdent );
	
	public TypesDTO getTypes();
	
	public TypeDTO getType( String typeIdent );
	
	public TypeDTO saveType( TypeDTO typeDTO );
	
	public Boolean removeType( String typeIdent );

	public TypesDTO getTypes(String termoBusca, String domainFilter, boolean orgFilter);
	
	public List<WebAPSEEVO> getTypesVO(String domain);
	
}
