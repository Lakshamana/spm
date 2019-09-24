package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.DevelopingSystemDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link DevelopingSystem} and its DTO {@link DevelopingSystemDTO}.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface DevelopingSystemMapper extends EntityMapper<DevelopingSystemDTO, DevelopingSystem> {

    @Mapping(source = "theOrganization.id", target = "theOrganizationId")
    DevelopingSystemDTO toDto(DevelopingSystem developingSystem);

    @Mapping(source = "theOrganizationId", target = "theOrganization")
    @Mapping(target = "theProjects", ignore = true)
    @Mapping(target = "removeTheProject", ignore = true)
    DevelopingSystem toEntity(DevelopingSystemDTO developingSystemDTO);

    default DevelopingSystem fromId(Long id) {
        if (id == null) {
            return null;
        }
        DevelopingSystem developingSystem = new DevelopingSystem();
        developingSystem.setId(id);
        return developingSystem;
    }
}
