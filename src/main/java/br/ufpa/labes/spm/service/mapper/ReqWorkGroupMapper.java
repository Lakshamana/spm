package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ReqWorkWorkGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReqWorkWorkGroup} and its DTO {@link ReqWorkWorkGroupDTO}.
 */
@Mapper(componentModel = "spring", uses = {WorkWorkGroupTypeMapper.class, WorkWorkGroupMapper.class})
public interface ReqWorkWorkGroupMapper extends EntityMapper<ReqWorkWorkGroupDTO, ReqWorkWorkGroup> {

    @Mapping(source = "theWorkWorkGroupType.id", target = "theWorkWorkGroupTypeId")
    @Mapping(source = "theWorkWorkGroup.id", target = "theWorkWorkGroupId")
    ReqWorkWorkGroupDTO toDto(ReqWorkWorkGroup reqWorkWorkGroup);

    @Mapping(source = "theWorkWorkGroupTypeId", target = "theWorkWorkGroupType")
    @Mapping(source = "theWorkWorkGroupId", target = "theWorkWorkGroup")
    ReqWorkWorkGroup toEntity(ReqWorkWorkGroupDTO reqWorkWorkGroupDTO);

    default ReqWorkWorkGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        ReqWorkWorkGroup reqWorkWorkGroup = new ReqWorkWorkGroup();
        reqWorkWorkGroup.setId(id);
        return reqWorkWorkGroup;
    }
}
