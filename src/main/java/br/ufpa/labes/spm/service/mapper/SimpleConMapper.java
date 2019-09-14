package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.SimpleConDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SimpleCon} and its DTO {@link SimpleConDTO}.
 */
@Mapper(componentModel = "spring", uses = {FeedbackMapper.class, SequenceMapper.class, ActivityMapper.class})
public interface SimpleConMapper extends EntityMapper<SimpleConDTO, SimpleCon> {

    @Mapping(source = "theFeedbackSub.id", target = "theFeedbackSubId")
    @Mapping(source = "theSequenceSub.id", target = "theSequenceSubId")
    @Mapping(source = "fromActivity.id", target = "fromActivityId")
    @Mapping(source = "toActivity.id", target = "toActivityId")
    SimpleConDTO toDto(SimpleCon simpleCon);

    @Mapping(source = "theFeedbackSubId", target = "theFeedbackSub")
    @Mapping(source = "theSequenceSubId", target = "theSequenceSub")
    @Mapping(target = "theConnectionSuper", ignore = true)
    @Mapping(source = "fromActivityId", target = "fromActivity")
    @Mapping(source = "toActivityId", target = "toActivity")
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
