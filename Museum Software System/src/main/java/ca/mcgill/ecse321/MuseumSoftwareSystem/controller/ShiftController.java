package ca.mcgill.ecse321.MuseumSoftwareSystem.controller;

import java.sql.Date;
import java.util.ArrayList;

import javax.validation.Valid;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.ShiftRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.mcgill.ecse321.MuseumSoftwareSystem.dto.ShiftResponseDto;
import ca.mcgill.ecse321.MuseumSoftwareSystem.model.Shift;
import ca.mcgill.ecse321.MuseumSoftwareSystem.service.ShiftService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/shift")
public class ShiftController {

	@Autowired
	ShiftService shiftService;

	/**
	 * Gets all shift objects from the database
	 * @return Iterable object of all shifts from the database
	 *
	 * @author Roxanne Archambault
	 */
	@GetMapping()
	public ResponseEntity<Iterable<ShiftResponseDto>> getAllShifts() {
		Iterable<Shift> shifts = shiftService.getAllShifts();

		ArrayList<ShiftResponseDto> shiftResponses = new ArrayList<ShiftResponseDto>();

		for (var shift : shifts) {
			shiftResponses.add(new ShiftResponseDto(shift));
		}

		return new ResponseEntity<Iterable<ShiftResponseDto>>(shiftResponses, HttpStatus.OK);
	}

	/**
	 * Posts a new Shift with the input parameters.
	 *
	 * @param request ShiftRequestDto
	 * @return ShiftDto of the created Shift object
	 *
	 * @author Mohammed Elsayed
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<ShiftResponseDto> createShift(@Valid @RequestBody ShiftRequestDto request) {
		ShiftResponseDto shift = shiftService.createShift(request);
		return new ResponseEntity<ShiftResponseDto>(shift, HttpStatus.CREATED);
	}

	/**
	 * Returns the Shift object associated with the specified id from the database
	 *
	 * @param id
	 * @return ShiftResponseDto
	 *
	 * @author Mohammed Elsayed
	 */
	@GetMapping("/id/{id}")
	public ResponseEntity<ShiftResponseDto> getShiftById(@PathVariable int id) {
		Shift shift = shiftService.getShiftById(id);
		ShiftResponseDto response = new ShiftResponseDto(shift);
		return new ResponseEntity<ShiftResponseDto>(response, HttpStatus.OK);
	}

	/**
	 * Returns the Shift object associated with the specified employee email address
	 * from the database
	 *
	 * @param employeeEmailAddress
	 * @return ShiftResponseDto
	 *
	 * @author Mohammed Elsayed
	 */
	@GetMapping("/employeeEmailAddress/{employeeEmailAddress}")
	public ResponseEntity<Iterable<ShiftResponseDto>> getShiftsByEmployeeEmailAddress(
			@PathVariable String employeeEmailAddress) {
		Iterable<Shift> shifts = shiftService.getShiftsByEmployeeEmailAddress(employeeEmailAddress);
		ArrayList<ShiftResponseDto> shiftResponses = new ArrayList<ShiftResponseDto>();

		for (var shift : shifts) {
			shiftResponses.add(new ShiftResponseDto(shift));
		}

		return new ResponseEntity<Iterable<ShiftResponseDto>>(shiftResponses, HttpStatus.OK);
	}

	/**
	 * Returns the Shift object associated with the specified date from the database
	 *
	 * @param date
	 * @return ShiftResponseDto
	 *
	 * @author Mohammed Elsayed
	 */
	@GetMapping("/dayDate/{date}")
	public ResponseEntity<ShiftResponseDto> getShiftByDayDate(@PathVariable Date date) {
		Shift shift = shiftService.getShiftByDayDate(date);
		ShiftResponseDto response = new ShiftResponseDto(shift);
		return new ResponseEntity<ShiftResponseDto>(response, HttpStatus.OK);
	}

	/**
	 * Deletes the existing shift object
	 *
	 * @param id
	 * @return ShiftResponseDto
	 *
	 * @author Mohammed Elsayed
	 */
	@DeleteMapping("/id/{id}")
	public ResponseEntity<ShiftResponseDto> deleteShift(@PathVariable int id) {
		Shift shift = shiftService.deleteShift(id);
		ShiftResponseDto response = new ShiftResponseDto(shift);
		return new ResponseEntity<ShiftResponseDto>(response, HttpStatus.OK);

	}

}
