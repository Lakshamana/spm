package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.SpmLogDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SpmLog} and its DTO {@link SpmLogDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProcessMapper.class})
public interface SpmLogMapper extends EntityMapper<SpmLogDTO, SpmLog> {

    @Mapping(source = "theProcess.id", target = "theProcessId")
    SpmLogDTO toDto(SpmLog spmLog);

    @Mapping(source = "theProcessId", target = "theProcess")
    @Mapping(target = "theEvents", ignore = true)
    @Mapping(target = "removeTheEvent", ignore = true)
    SpmLog toEntity(SpmLogDTO spmLogDTO);

    default SpmLog fromId(Long id) {
        if (id == null) {
            return null;
        }
        SpmLog spmLog = new SpmLog();
        spmLog.setId(id);
        return spmLog;
    }
}
