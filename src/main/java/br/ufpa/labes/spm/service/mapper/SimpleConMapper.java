package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.SimpleConDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SimpleCon} and its DTO {@link SimpleConDTO}.
 */
@Mapper(componentModel = "spring", uses = {ActivityMapper.class})
public interface SimpleConMapper extends EntityMapper<SimpleConDTO, SimpleCon> {

    @Mapping(source = "toActivity.id", target = "toActivityId")
    @Mapping(source = "fromActivity.id", target = "fromActivityId")
    SimpleConDTO toDto(SimpleCon simpleCon);

    @Mapping(source = "toActivityId", target = "toActivity")
    @Mapping(source = "fromActivityId", target = "fromActivity")
    SimpleCon toEntity(SimpleConDTO simpleConDTO);

    default SimpleCon fromId(Long id) {
        if (id == null) {
            return null;
        }
        SimpleCon simpleCon = new SimpleCon();
        simpleCon.setId(id);
        return simpleCon;
    }
}
