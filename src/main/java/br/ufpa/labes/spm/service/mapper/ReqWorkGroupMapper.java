package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ReqWorkGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReqWorkWorkGroup} and its DTO {@link ReqWorkGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {WorkWorkGroupTypeMapper.class, WorkGroupMapper.class})
public interface ReqWorkWorkGroupMapper extends EntityMapper<ReqWorkWorkGroupDTO, ReqWorkGroup> {

    @Mapping(source = "theWorkWorkGroupType.id", target = "theWorkGroupTypeId")
    @Mapping(source = "theWorkWorkGroup.id", target = "theWorkGroupId")
    ReqWorkWorkGroupDTO toDto(ReqWorkWorkGroup reqWorkGroup);

    @Mapping(source = "theWorkWorkGroupTypeId", target = "theWorkGroupType")
    @Mapping(source = "theWorkWorkGroupId", target = "theWorkGroup")
    ReqWorkWorkGroup toEntity(ReqWorkWorkGroupDTO reqWorkGroupDTO);

    default ReqWorkGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReqWorkWorkGroup reqWorkWorkGroup = new ReqWorkGroup();
        reqWorkGroup.setId(id);
        return reqWorkGroup;
    }
}
