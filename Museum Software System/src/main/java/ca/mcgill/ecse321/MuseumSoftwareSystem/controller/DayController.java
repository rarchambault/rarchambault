package ca.mcgill.ecse321.MuseumSoftwareSystem.controller;

import java.sql.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.DayRequestDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.DayResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Day;
import ca.mcgill.ecse321.MuseumSoftwareSystem.service.DayService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/day")
public class DayController {

	@Autowired
	DayService dayService;

	/**
	 * Posts a new Day with the input parameters.
	 * 
	 * @param museum      The museum we're talking about
	 * @param openingHour Opening time of the museum on a specified day
	 * @param closingHour Closing time of the museum on a specified day
	 * @param dayDate     Date of the day
	 * @param isHoliday   Whether the day is a holiday or not
	 * @return DayDto of the created Day object
	 * 
	 * @param request
	 * @return DayResponseDto of the created object
	 * @author Mohammed Elsayed
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<DayResponseDto> createDay(@Valid @RequestBody DayRequestDto request) {
		DayResponseDto day = dayService.createDay(request);
		return new ResponseEntity<DayResponseDto>(day, HttpStatus.CREATED);
	}

	/**
	 * Returns the Day object associated with the specified date from the database
	 * 
	 * @param date
	 * @return DayResponseDto
	 * 
	 * @author Mohammed Elsayed
	 */
	@GetMapping("/date/{date}")
	public ResponseEntity<DayResponseDto> getDayByDate(@PathVariable Date date) {
		Day day = dayService.getDayByDate(date);
		DayResponseDto response = new DayResponseDto(day);
		return new ResponseEntity<DayResponseDto>(response, HttpStatus.OK);
	}

	/**
	 * Deletes the existing day object
	 * 
	 * @param date
	 * @return DayResponseDto
	 * 
	 * @author Mohammed Elsayed
	 */
	@DeleteMapping("/date/{date}")
	public ResponseEntity<DayResponseDto> deleteShift(@PathVariable Date date) {
		Day aDay = dayService.deleteDay(date);
		DayResponseDto response = new DayResponseDto(aDay);
		return new ResponseEntity<DayResponseDto>(response, HttpStatus.OK);
	}
}
