package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.DecomposedDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Decomposed} and its DTO {@link DecomposedDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProcessModelMapper.class})
public interface DecomposedMapper extends EntityMapper<DecomposedDTO, Decomposed> {

    @Mapping(source = "theReferedProcessModel.id", target = "theReferedProcessModelId")
    DecomposedDTO toDto(Decomposed decomposed);

    @Mapping(source = "theReferedProcessModelId", target = "theReferedProcessModel")
    Decomposed toEntity(DecomposedDTO decomposedDTO);

    default Decomposed fromId(Long id) {
        if (id == null) {
            return null;
        }
        Decomposed decomposed = new Decomposed();
        decomposed.setId(id);
        return decomposed;
    }
}
