package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ResourceInstSugDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ResourceInstSug} and its DTO {@link ResourceInstSugDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ResourceInstSugMapper extends EntityMapper<ResourceInstSugDTO, ResourceInstSug> {



    default ResourceInstSug fromId(Long id) {
        if (id == null) {
            return null;
        }
        ResourceInstSug resourceInstSug = new ResourceInstSug();
        resourceInstSug.setId(id);
        return resourceInstSug;
    }
}
