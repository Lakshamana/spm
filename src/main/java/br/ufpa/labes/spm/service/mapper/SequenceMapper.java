package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.SequenceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sequence} and its DTO {@link SequenceDTO}.
 */
@Mapper(componentModel = "spring", uses = {DependencyMapper.class})
public interface SequenceMapper extends EntityMapper<SequenceDTO, Sequence> {

    @Mapping(source = "theDependencyToMultipleSequences.id", target = "theDependencyToMultipleSequencesId")
    SequenceDTO toDto(Sequence sequence);

    @Mapping(source = "theDependencyToMultipleSequencesId", target = "theDependencyToMultipleSequences")
    @Mapping(target = "theDependency", ignore = true)
    Sequence toEntity(SequenceDTO sequenceDTO);

    default Sequence fromId(Long id) {
        if (id == null) {
            return null;
        }
        Sequence sequence = new Sequence();
        sequence.setId(id);
        return sequence;
    }
}
