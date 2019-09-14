package br.ufpa.labes.spm.repository.interfaces.resources;

import javax.ejb.Local;

import br.ufpa.labes.spm.repository.interfaces.IBaseDAO;
import br.ufpa.labes.spm.domain.Reservation;

@Local
public interface IReservationDAO  extends IBaseDAO<Reservation, Integer>{

}
