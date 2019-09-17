package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.RoleTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link RoleType} and its DTO {@link RoleTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface RoleTypeMapper extends EntityMapper<RoleTypeDTO, RoleType> {


    @Mapping(target = "theRoles", ignore = true)
    @Mapping(target = "removeTheRole", ignore = true)
    RoleType toEntity(RoleTypeDTO roleTypeDTO);

    default RoleType fromId(Long id) {
        if (id == null) {
            return null;
        }
        RoleType roleType = new RoleType();
        roleType.setId(id);
        return roleType;
    }
}
