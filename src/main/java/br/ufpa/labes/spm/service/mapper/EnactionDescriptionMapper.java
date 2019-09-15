package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.EnactionDescriptionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link EnactionDescription} and its DTO {@link EnactionDescriptionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EnactionDescriptionMapper extends EntityMapper<EnactionDescriptionDTO, EnactionDescription> {


    @Mapping(target = "thePlain", ignore = true)
    EnactionDescription toEntity(EnactionDescriptionDTO enactionDescriptionDTO);

    default EnactionDescription fromId(Long id) {
        if (id == null) {
            return null;
        }
        EnactionDescription enactionDescription = new EnactionDescription();
        enactionDescription.setId(id);
        return enactionDescription;
    }
}
