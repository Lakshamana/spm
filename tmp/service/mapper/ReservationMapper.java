package br.ufpa.labes.spm.service.mapper;

import br.ufpa.labes.spm.domain.*;
import br.ufpa.labes.spm.service.dto.ReservationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reservation} and its DTO {@link ReservationDTO}.
 */
@Mapper(componentModel = "spring", uses = {ExclusiveMapper.class, NormalMapper.class})
public interface ReservationMapper extends EntityMapper<ReservationDTO, Reservation> {

    @Mapping(source = "theExclusive.id", target = "theExclusiveId")
    @Mapping(source = "theNormal.id", target = "theNormalId")
    ReservationDTO toDto(Reservation reservation);

    @Mapping(source = "theExclusiveId", target = "theExclusive")
    @Mapping(source = "theNormalId", target = "theNormal")
    Reservation toEntity(ReservationDTO reservationDTO);

    default Reservation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setId(id);
        return reservation;
    }
}
