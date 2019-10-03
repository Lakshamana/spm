package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.OcurrenceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ocurrence} and its DTO {@link OcurrenceDTO}.
 */
@Mapper(componentModel = "spring", uses = {TaskMapper.class})
public interface OcurrenceMapper extends EntityMapper<OcurrenceDTO, Ocurrence> {

    @Mapping(source = "theTask.id", target = "theTaskId")
    OcurrenceDTO toDto(Ocurrence ocurrence);

    @Mapping(source = "theTaskId", target = "theTask")
    Ocurrence toEntity(OcurrenceDTO ocurrenceDTO);

    default Ocurrence fromId(Long id) {
        if (id == null) {
            return null;
        }
        Ocurrence ocurrence = new Ocurrence();
        ocurrence.setId(id);
        return ocurrence;
    }
}
