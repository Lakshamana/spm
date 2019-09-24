package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.DriverDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Driver} and its DTO {@link DriverDTO}.
 */
@Mapper(componentModel = "spring", uses = {PluginMapper.class})
public interface DriverMapper extends EntityMapper<DriverDTO, Driver> {

    @Mapping(source = "thePlugin.id", target = "thePluginId")
    DriverDTO toDto(Driver driver);

    @Mapping(source = "thePluginId", target = "thePlugin")
    Driver toEntity(DriverDTO driverDTO);

    default Driver fromId(Long id) {
        if (id == null) {
            return null;
        }
        Driver driver = new Driver();
        driver.setId(id);
        return driver;
    }
}
