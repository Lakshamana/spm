package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.AutomaticActivityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AutomaticActivity} and its DTO {@link AutomaticActivityDTO}.
 */
@Mapper(componentModel = "spring", uses = {SubroutineMapper.class, ArtifactMapper.class})
public interface AutomaticActivityMapper extends EntityMapper<AutomaticActivityDTO, AutomaticActivity> {

    @Mapping(source = "theSubroutine.id", target = "theSubroutineId")
    @Mapping(source = "theArtifact.id", target = "theArtifactId")
    AutomaticActivityDTO toDto(AutomaticActivity automaticActivity);

    @Mapping(source = "theSubroutineId", target = "theSubroutine")
    @Mapping(target = "theAutomatic", ignore = true)
    @Mapping(source = "theArtifactId", target = "theArtifact")
    @Mapping(target = "theParameters", ignore = true)
    @Mapping(target = "removeTheParameters", ignore = true)
    AutomaticActivity toEntity(AutomaticActivityDTO automaticActivityDTO);

    default AutomaticActivity fromId(Long id) {
        if (id == null) {
            return null;
        }
        AutomaticActivity automaticActivity = new AutomaticActivity();
        automaticActivity.setId(id);
        return automaticActivity;
    }
}
