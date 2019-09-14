package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ReqWorkGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReqWorkGroup} and its DTO {@link ReqWorkGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {WorkGroupTypeMapper.class, WorkGroupMapper.class})
public interface ReqWorkGroupMapper extends EntityMapper<ReqWorkGroupDTO, ReqWorkGroup> {

    @Mapping(source = "theWorkGroupType.id", target = "theWorkGroupTypeId")
    @Mapping(source = "theWorkGroup.id", target = "theWorkGroupId")
    ReqWorkGroupDTO toDto(ReqWorkGroup reqWorkGroup);

    @Mapping(source = "theWorkGroupTypeId", target = "theWorkGroupType")
    @Mapping(target = "theRequiredPeopleSuper", ignore = true)
    @Mapping(source = "theWorkGroupId", target = "theWorkGroup")
    ReqWorkGroup toEntity(ReqWorkGroupDTO reqWorkGroupDTO);

    default ReqWorkGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReqWorkGroup reqWorkGroup = new ReqWorkGroup();
        reqWorkGroup.setId(id);
        return reqWorkGroup;
    }
}
