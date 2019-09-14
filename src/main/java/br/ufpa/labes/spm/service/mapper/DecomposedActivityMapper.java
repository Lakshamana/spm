package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.DecomposedActivityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link DecomposedActivity} and its DTO {@link DecomposedActivityDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DecomposedActivityMapper extends EntityMapper<DecomposedActivityDTO, DecomposedActivity> {


    @Mapping(target = "theActivitySuper", ignore = true)
    DecomposedActivity toEntity(DecomposedActivityDTO decomposedActivityDTO);

    default DecomposedActivity fromId(Long id) {
        if (id == null) {
            return null;
        }
        DecomposedActivity decomposedActivity = new DecomposedActivity();
        decomposedActivity.setId(id);
        return decomposedActivity;
    }
}
