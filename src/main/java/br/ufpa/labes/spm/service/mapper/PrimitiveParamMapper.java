package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.PrimitiveParamDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PrimitiveParam} and its DTO {@link PrimitiveParamDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PrimitiveParamMapper extends EntityMapper<PrimitiveParamDTO, PrimitiveParam> {


    @Mapping(target = "theParameterSuper", ignore = true)
    PrimitiveParam toEntity(PrimitiveParamDTO primitiveParamDTO);

    default PrimitiveParam fromId(Long id) {
        if (id == null) {
            return null;
        }
        PrimitiveParam primitiveParam = new PrimitiveParam();
        primitiveParam.setId(id);
        return primitiveParam;
    }
}
