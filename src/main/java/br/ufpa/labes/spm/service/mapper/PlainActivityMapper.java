package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.PlainActivityDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlainActivity} and its DTO {@link PlainActivityDTO}.
 */
@Mapper(componentModel = "spring", uses = {EnactionDescriptionMapper.class, AutomaticActivityMapper.class})
public interface PlainActivityMapper extends EntityMapper<PlainActivityDTO, PlainActivity> {

    @Mapping(source = "theEnactionDescription.id", target = "theEnactionDescriptionId")
    @Mapping(source = "theAutomaticActivitySub.id", target = "theAutomaticActivitySubId")
    PlainActivityDTO toDto(PlainActivity plainActivity);

    @Mapping(source = "theEnactionDescriptionId", target = "theEnactionDescription")
    @Mapping(source = "theAutomaticActivitySubId", target = "theAutomaticActivitySub")
    @Mapping(target = "theGlobalActivityEvents", ignore = true)
    @Mapping(target = "removeTheGlobalActivityEvent", ignore = true)
    @Mapping(target = "theActivitySuper", ignore = true)
    @Mapping(target = "theCatalogEvents", ignore = true)
    @Mapping(target = "removeTheCatalogEvent", ignore = true)
    PlainActivity toEntity(PlainActivityDTO plainActivityDTO);

    default PlainActivity fromId(Long id) {
        if (id == null) {
            return null;
        }
        PlainActivity plainActivity = new PlainActivity();
        plainActivity.setId(id);
        return plainActivity;
    }
}
