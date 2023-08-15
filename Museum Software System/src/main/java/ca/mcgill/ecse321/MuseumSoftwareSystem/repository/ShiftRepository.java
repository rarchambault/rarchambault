package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Shift;

public interface ShiftRepository extends CrudRepository<Shift, Integer> {
	public Shift findShiftById(int id);
	
	public Shift findShiftByDayDate(Date date);

	public List<Shift> findShiftsByEmployeeEmailAddress(String employeeEmailAddress);
}
