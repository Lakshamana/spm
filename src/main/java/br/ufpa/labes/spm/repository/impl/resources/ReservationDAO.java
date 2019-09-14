package br.ufpa.labes.spm.repository.impl.resources;


import br.ufpa.labes.spm.repository.impl.BaseDAO;
import br.ufpa.labes.spm.repository.interfaces.resources.IReservationDAO;
import br.ufpa.labes.spm.domain.Reservation;

public class ReservationDAO extends BaseDAO<Reservation, Integer> implements IReservationDAO{

	protected ReservationDAO(Class<Reservation> businessClass) {
		super(businessClass);
	}

	public ReservationDAO() {
		super(Reservation.class);
	}


}
