package ca.mcgill.ecse321.MuseumSoftwareSystem.repository;

import java.util.Date;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Day;

public interface DayRepository extends CrudRepository<Day, Date> {
	public Day findDayByDate(Date date);
	
}