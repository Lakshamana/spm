package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.DependencyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dependency} and its DTO {@link DependencyDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DependencyMapper extends EntityMapper<DependencyDTO, Dependency> {


    @Mapping(target = "theMultipleCons", ignore = true)
    @Mapping(target = "removeTheMultipleCon", ignore = true)
    @Mapping(target = "theSequences", ignore = true)
    @Mapping(target = "removeTheSequence", ignore = true)
    Dependency toEntity(DependencyDTO dependencyDTO);

    default Dependency fromId(Long id) {
        if (id == null) {
            return null;
        }
        Dependency dependency = new Dependency();
        dependency.setId(id);
        return dependency;
    }
}
