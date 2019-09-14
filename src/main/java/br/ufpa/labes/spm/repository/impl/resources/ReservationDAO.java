package br.ufpa.labes.spm.repository.impl.resources;

import javax.ejb.Stateless;

import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.resources.IReservationDAO;
import br.ufpa.labes.spm.domain.Reservation;

@Stateless
public class ReservationDAO extends BaseDAO<Reservation, Integer> implements IReservationDAO{

	protected ReservationDAO(Class<Reservation> businessClass) {
		super(businessClass);
	}

	public ReservationDAO() {
		super(Reservation.class);
	}


}
