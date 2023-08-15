package ca.mcgill.ecse321.MuseumSoftwareSystem.service;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.DayRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.DayResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.exception.MuseumSoftwareSystemException;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Day;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Museum;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.MuseumSoftwareSystem.repository.DayRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DayService {
	
	@Autowired
	DayRepository dayRepo;
	
	@Autowired
	MuseumService museumService;

	/**
	 * gets a day object given its date
	 * 
	 * @param date
	 * @return Fetched day object
	 * 
	 * @author Mohammed Elsayed
	 */
	@Transactional
	public Day getDayByDate (Date date){
		Day day = dayRepo.findDayByDate(date);

		if (day == null) {
			throw new MuseumSoftwareSystemException(HttpStatus.NOT_FOUND, "Day Not Found.");
		}


		return day;
	}
	
	/**
	 * deletes an existing day object
	 * 
	 * @param date
	 * @return dayRepo that has no day
	 * 
	 * @author Mohammed Elsayed
	 */
	@Transactional
	public Day deleteDay(Date date) {
		Day day = dayRepo.findDayByDate(date);
		if (day != null)
		{
			dayRepo.delete(day);
		}
		return day;
	}
	
	/**
	 * Creates a day object
	 * @param dayRequest
	 * @return saved day object
	 * 
	 * @author Mohammed Elsayed
	 */
	@Transactional 
	public DayResponseDto createDay(DayRequestDto dayRequest) {
		Museum museum  = museumService.getMuseumById(dayRequest.getMuseumId());
		
		Day day = new Day();
		day.setMuseum(museum);
		day.setOpeningHour(dayRequest.getOpeningHour());
		day.setClosingHour(dayRequest.getClosingHour());
		day.setDate(dayRequest.getDate());
		day.setIsHoliday(dayRequest.getIsHoliday());
		
		day = dayRepo.save(day);
		return new DayResponseDto(day);
	}
}
